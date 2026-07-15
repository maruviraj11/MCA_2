package com.cmp.transport;

import com.cmp.config.AppConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailTransport {
    
    public static Map sendMail(String to, String subject, String temppass,String name,String role) 
    {
        Map result = new HashMap();
        result.put("success", Boolean.FALSE);
        result.put("message", "Mail not attempted.");

        if (!AppConfig.MAIL_ENABLED) {
            result.put("message", "Mail is disabled in AppConfig.");
            return result;
        }

        Properties props = new Properties();

        props.put("mail.smtp.host", AppConfig.SMTP_HOST);
        props.put("mail.smtp.port", AppConfig.SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        if (AppConfig.SMTP_TRUST_ALL_CERTS) {
            props.put("mail.smtp.ssl.trust", AppConfig.SMTP_HOST);
        }

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                AppConfig.SMTP_USERNAME,
                                AppConfig.SMTP_PASSWORD
                        );
                    }
                });

        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(AppConfig.MAIL_FROM));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );

            message.setSubject(subject);

            String htmlMessage = buildHtml(name, to, temppass, role);
            
            message.setContent(htmlMessage, "text/html");

            Transport.send(message);

            result.put("success", Boolean.TRUE);
            result.put("message", "Mail sent successfully to " + to + ".");
            System.out.println("Mail Sent Successfully to " + to);

        } catch (Exception e) {
            String errorMessage = e.getMessage() == null ? "Unknown mail error." : e.getMessage();
            result.put("message", "Mail failed for " + to + ": " + errorMessage);
            e.printStackTrace();
        }
        
        return result;
    }
    
    private static String buildHtml(String receiverName, String receiverEmail, String password, String role) {
        return "<div style='margin:0;padding:32px;background:#f4f6fb;font-family:Segoe UI,Tahoma,sans-serif;color:#1f2937;'>"
                + "<div style='max-width:640px;margin:0 auto;background:#ffffff;border:1px solid #dbe3ef;border-radius:22px;overflow:hidden;box-shadow:0 20px 42px rgba(15,23,42,0.08);'>"
                + "<div style='padding:28px 30px;background:linear-gradient(135deg,#0f766e,#155e75 55%,#2563eb);color:#ffffff;'>"
                + "<div style='font-size:13px;letter-spacing:.08em;text-transform:uppercase;opacity:.82;'>Complaint Management Portal</div>"
                + "<h2 style='margin:10px 0 0;font-size:28px;line-height:1.2;'>Your " + role + " Account Is Ready</h2>"
                + "<p style='margin:10px 0 0;font-size:14px;line-height:1.7;color:rgba(255,255,255,0.88);'>Use the credentials below for your first login.</p>"
                + "</div>"
                + "<div style='padding:28px 30px;'>"
                + "<p style='margin:0 0 16px;'>Hello " + receiverName + ",</p>"
                + "<p style='margin:0 0 18px;line-height:1.7;color:#4b5563;'>Your account has been created successfully. Please sign in with the details below and change your password immediately after login.</p>"
                + "<div style='border:1px solid #e5e7eb;border-radius:18px;overflow:hidden;background:#fbfdff;'>"
                + "<div style='display:flex;border-bottom:1px solid #e5e7eb;'><div style='width:40%;padding:14px 16px;font-weight:700;background:#f8fafc;'>Role</div><div style='width:60%;padding:14px 16px;'>" + role + "</div></div>"
                + "<div style='display:flex;border-bottom:1px solid #e5e7eb;'><div style='width:40%;padding:14px 16px;font-weight:700;background:#f8fafc;'>Email</div><div style='width:60%;padding:14px 16px;'>" + receiverEmail + "</div></div>"
                + "<div style='display:flex;'><div style='width:40%;padding:14px 16px;font-weight:700;background:#f8fafc;'>Temporary Password</div><div style='width:60%;padding:14px 16px;font-size:16px;font-weight:700;color:#0f766e;'>" + password + "</div></div>"
                + "</div>"
                + "<div style='margin-top:20px;padding:16px 18px;border-radius:16px;background:#fff7ed;border:1px solid #fed7aa;color:#9a3412;'>"
                + "<strong>Important:</strong> This temporary password should be changed after your first login."
                + "</div>"
                + "<p style='margin:22px 0 0;color:#6b7280;line-height:1.7;'>Regards,<br><strong style='color:#111827;'>Complaint Management Portal</strong></p>"
                + "</div>"
                + "</div>"
                + "</div>";
    }

    
    
}
