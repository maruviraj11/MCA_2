package com.library.controller;

import com.library.dao.PaymentDAO;
import com.library.dao.RequestDAO;
import com.library.model.BookRequest;
import com.library.model.Payment;
import com.library.model.User;
import com.library.util.RazorpayUtil;
import com.library.util.Validation;
import com.library.util.WebUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.sql.Timestamp;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PaymentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "start";

        if ("start".equals(action)) {
            startPayment(request, response);
            return;
        }

        response.sendRedirect("user_requests.jsp?error=" + WebUtil.enc("Invalid payment action."));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "";

        if ("verify".equals(action)) {
            verifyPayment(request, response);
            return;
        }

        response.sendRedirect("user_requests.jsp?error=" + WebUtil.enc("Invalid payment action."));
    }

    private void startPayment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");
        if (user == null || !"user".equals(user.getRole())) {
            response.sendRedirect("index.jsp");
            return;
        }

        Integer requestId = Validation.parsePositiveInt(request.getParameter("requestId"));
        if (requestId == null) {
            response.sendRedirect("user_requests.jsp?error=" + WebUtil.enc("Invalid request."));
            return;
        }

        RequestDAO requestDAO = new RequestDAO();
        BookRequest br = requestDAO.getRequestById(requestId.intValue());
        if (br == null || br.getUserId() != user.getId()) {
            response.sendRedirect("user_requests.jsp?error=" + WebUtil.enc("Request not found."));
            return;
        }

        String status = br.getStatus();
        if (!"returned".equals(status) && !"issued".equals(status)) {
            response.sendRedirect("user_requests.jsp?error=" + WebUtil.enc("No payable fine for this request."));
            return;
        }

        BigDecimal fine = br.getFineAmount() == null ? BigDecimal.ZERO : br.getFineAmount();
        if ("issued".equals(status)) {
            // Ensure fine is estimated till now for overdue only (RequestDAO sets it for issued)
            fine = fine == null ? BigDecimal.ZERO : fine;
        }

        PaymentDAO paymentDAO = new PaymentDAO();
        int paidPaise = paymentDAO.sumPaidPaiseByRequest(br.getId());
        BigDecimal paid = BigDecimal.valueOf(paidPaise).divide(new BigDecimal("100"));
        BigDecimal outstanding = fine.subtract(paid);
        if (outstanding.compareTo(BigDecimal.ZERO) <= 0) {
            response.sendRedirect("user_requests.jsp?msg=" + WebUtil.enc("No pending fine amount."));
            return;
        }

        String keyId = getServletContext().getInitParameter("razorpayKeyId");
        String keySecret = getServletContext().getInitParameter("razorpayKeySecret");
        if (Validation.isBlank(keyId) || Validation.isBlank(keySecret) || isPlaceholderKey(keyId) || isPlaceholderKey(keySecret)) {
            response.sendRedirect("user_requests.jsp?error=" + WebUtil.enc("Payment is not configured (missing Razorpay keys)."));
            return;
        }

        int amountPaise = outstanding
                .movePointRight(2)
                .setScale(0, RoundingMode.HALF_UP)
                .intValue();
        if (amountPaise <= 0) {
            response.sendRedirect("user_requests.jsp?msg=" + WebUtil.enc("No pending fine amount."));
            return;
        }
        String receipt = "fine_req_" + br.getId() + "_" + System.currentTimeMillis();

        try {
            String orderId = RazorpayUtil.createOrder(keyId.trim(), keySecret.trim(), amountPaise, "INR", receipt);

            Payment p = paymentDAO.createPayment(br.getId(), user.getId(), amountPaise, "INR", "razorpay", orderId);
            if (p == null) {
                response.sendRedirect("user_requests.jsp?error=" + WebUtil.enc("Failed to create payment record."));
                return;
            }

            renderCheckout(response, keyId.trim(), orderId, amountPaise, user.getEmail(), br.getId(), status);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("user_requests.jsp?error=" + WebUtil.enc("Failed to start payment: " + e.getMessage()));
        }
    }

    private void verifyPayment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");
        if (user == null || !"user".equals(user.getRole())) {
            response.sendRedirect("index.jsp");
            return;
        }

        String razorpayPaymentId = request.getParameter("razorpay_payment_id");
        String razorpayOrderId = request.getParameter("razorpay_order_id");
        String razorpaySignature = request.getParameter("razorpay_signature");

        if (Validation.isBlank(razorpayPaymentId) || Validation.isBlank(razorpayOrderId) || Validation.isBlank(razorpaySignature)) {
            response.sendRedirect("user_requests.jsp?error=" + WebUtil.enc("Missing payment details."));
            return;
        }

        PaymentDAO paymentDAO = new PaymentDAO();
        Payment p = paymentDAO.getByOrderId(razorpayOrderId.trim());
        if (p == null || p.getUserId() != user.getId()) {
            response.sendRedirect("user_requests.jsp?error=" + WebUtil.enc("Payment not found."));
            return;
        }

        if (!"created".equals(p.getStatus())) {
            response.sendRedirect("user_requests.jsp?msg=" + WebUtil.enc("Payment already processed."));
            return;
        }

        String keySecret = getServletContext().getInitParameter("razorpayKeySecret");
        if (Validation.isBlank(keySecret) || isPlaceholderKey(keySecret)) {
            response.sendRedirect("user_requests.jsp?error=" + WebUtil.enc("Payment is not configured (missing Razorpay secret)."));
            return;
        }

        try {
            boolean ok = RazorpayUtil.verifySignature(p.getOrderId(), razorpayPaymentId.trim(), razorpaySignature.trim(), keySecret.trim());
            if (!ok) {
                paymentDAO.markFailed(p.getId());
                response.sendRedirect("user_requests.jsp?error=" + WebUtil.enc("Payment verification failed."));
                return;
            }

            boolean marked = paymentDAO.markPaid(p.getId(), razorpayPaymentId.trim(), razorpaySignature.trim());
            if (!marked) {
                response.sendRedirect("user_requests.jsp?error=" + WebUtil.enc("Failed to update payment status."));
                return;
            }

            RequestDAO requestDAO = new RequestDAO();
            BookRequest br = requestDAO.getRequestById(p.getRequestId());
            if (br != null && "returned".equals(br.getStatus())) {
                int paidPaise = paymentDAO.sumPaidPaiseByRequest(br.getId());
                BigDecimal paid = BigDecimal.valueOf(paidPaise).divide(new BigDecimal("100"));
                BigDecimal fine = br.getFineAmount() == null ? BigDecimal.ZERO : br.getFineAmount();
                if (paid.compareTo(fine) >= 0) {
                    requestDAO.markFinePaid(br.getId());
                }
                response.sendRedirect("user_requests.jsp?msg=" + WebUtil.enc("Payment successful. Fine updated."));
            } else {
                response.sendRedirect("user_requests.jsp?msg=" + WebUtil.enc("Payment successful. Return book for final fine settlement."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            paymentDAO.markFailed(p.getId());
            response.sendRedirect("user_requests.jsp?error=" + WebUtil.enc("Payment verification error: " + e.getMessage()));
        }
    }

    private void renderCheckout(HttpServletResponse response, String keyId, String orderId, int amountPaise, String userEmail, int requestId, String requestStatus) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String safeEmail = userEmail == null ? "" : userEmail.replace("\"", "");
        String qrBlock = buildUpiQrBlock(amountPaise, requestId);
        String note = "issued".equals(requestStatus)
                ? "<p style='color:rgba(248,250,252,0.72)'>Note: this is overdue fine till now. Return the book to finalize the total fine.</p>"
                : "";

        String html =
                "<!DOCTYPE html>" +
                "<html><head>" +
                "<meta charset='UTF-8'/>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1'/>" +
                "<title>Pay Fine</title>" +
                "<script src='https://checkout.razorpay.com/v1/checkout.js'></script>" +
                "<style>body{font-family:Arial,sans-serif;padding:18px;background:#0b1020;color:#f8fafc}a{color:#7c83ff}</style>" +
                "</head><body>" +
                "<h2>Pay Fine Online</h2>" +
                qrBlock +
                note +
                "<p>Order: <b>" + escapeHtml(orderId) + "</b></p>" +
                "<p>Amount: <b>₹" + (amountPaise / 100.0) + "</b></p>" +
                "<form id='verifyForm' method='POST' action='PaymentServlet'>" +
                "<input type='hidden' name='action' value='verify'/>" +
                "<input type='hidden' name='razorpay_payment_id' id='rp_payment_id'/>" +
                "<input type='hidden' name='razorpay_order_id' id='rp_order_id'/>" +
                "<input type='hidden' name='razorpay_signature' id='rp_signature'/>" +
                "</form>" +
                "<button id='payBtn' style='padding:10px 14px;border:0;border-radius:10px;background:#6366f1;color:white;cursor:pointer'>Pay Now</button> " +
                "<a href='user_requests.jsp' style='margin-left:12px;'>Cancel</a>" +
                "<script>" +
                "var options = {" +
                "  key: " + jsString(keyId) + "," +
                "  amount: " + amountPaise + "," +
                "  currency: 'INR'," +
                "  name: 'Smart Library'," +
                "  description: 'Fine payment (Request #" + requestId + ")'," +
                "  order_id: " + jsString(orderId) + "," +
                "  prefill: { email: " + jsString(safeEmail) + " }," +
                "  handler: function (resp) {" +
                "    document.getElementById('rp_payment_id').value = resp.razorpay_payment_id || '';" +
                "    document.getElementById('rp_order_id').value = resp.razorpay_order_id || '';" +
                "    document.getElementById('rp_signature').value = resp.razorpay_signature || '';" +
                "    document.getElementById('verifyForm').submit();" +
                "  }" +
                "};" +
                "var rzp = new Razorpay(options);" +
                "document.getElementById('payBtn').onclick = function(e){ e.preventDefault(); rzp.open(); };" +
                "</script>" +
                "</body></html>";

        response.getWriter().write(html);
    }

    private boolean isPlaceholderKey(String value) {
        if (value == null) return true;
        String v = value.trim();
        if (v.isEmpty()) return true;
        String upper = v.toUpperCase();
        return "YOUR_KEY_ID".equals(upper)
                || "YOUR_KEY_SECRET".equals(upper)
                || upper.startsWith("YOUR_")
                || upper.contains("CHANGE_ME");
    }

    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    private String jsString(String s) {
        if (s == null) return "''";
        String v = s.replace("\\", "\\\\").replace("'", "\\'");
        return "'" + v + "'";
    }

    private String buildUpiQrBlock(int amountPaise, int requestId) {
        String upiVpa = getServletContext().getInitParameter("upiVpa");
        String upiName = getServletContext().getInitParameter("upiName");
        if (Validation.isBlank(upiVpa) || Validation.isBlank(upiName)) return "";

        String amount = String.format(java.util.Locale.US, "%.2f", amountPaise / 100.0);
        String note = "Library fine (Request #" + requestId + ")";

        String upiUri = "upi://pay?pa=" + upiVpa.trim()
                + "&pn=" + upiName.trim()
                + "&am=" + amount
                + "&cu=INR"
                + "&tn=" + note;

        try {
            String encoded = URLEncoder.encode(upiUri, "UTF-8");
            String img = "https://chart.googleapis.com/chart?chs=220x220&cht=qr&chl=" + encoded;
            return "<div style='margin:14px 0;padding:12px;border-radius:12px;background:rgba(255,255,255,0.06)'>"
                    + "<div style='font-weight:bold;margin-bottom:6px;'>Scan QR (UPI)</div>"
                    + "<img alt='UPI QR' src='" + img + "' width='220' height='220' style='border-radius:12px;background:white;padding:8px;'/>"
                    + "<div style='margin-top:8px;color:rgba(248,250,252,0.72)'>UPI ID: <b>" + escapeHtml(upiVpa.trim()) + "</b></div>"
                    + "</div>";
        } catch (Exception e) {
            return "";
        }
    }
}
