Library Project (JSP/Servlet) - MySQL Database

1) Import
   - phpMyAdmin: Import -> choose `library.sql`
   - MySQL CLI:
       mysql -u root -p < library.sql

2) Default database
   - Database name: `library`

3) Demo accounts (plain passwords for college project demo)
   - Admin:     admin@library.com / admin123
   - Librarian: librarian@library.com / librarian123
   - User:      user@library.com / user12345

4) Rules in code
   - Loan period: 14 days
   - Fine: 5.00 per day after due date
   - Borrow limit: max 3 issued books per user

5) Online Payment (Razorpay)
   - This project supports paying fines online using Razorpay Orders + Checkout + signature verification.
   - Configure your Razorpay keys in `web/WEB-INF/web.xml` as context params:
       razorpayKeyId, razorpayKeySecret
   - Optional: configure `upiVpa` and `upiName` to show a UPI QR code on the payment page.
