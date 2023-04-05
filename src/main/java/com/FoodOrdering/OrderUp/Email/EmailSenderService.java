package com.FoodOrdering.OrderUp.Email;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;


    public void sendSimpleEmail(String toEmail,
                                String OTP
    ) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String htmlContent = "<div style=\"font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2\">" +
                "<div style=\"margin:50px auto;width:70%;padding:20px 0\">" +
                "<div style=\"border-bottom:1px solid #eee\">" +
                "<a href=\"\" style=\"font-size:1.4em;color: #00466a;text-decoration:none;font-weight:600\">OrderUp</a>" +
                "</div>" +
                "<p style=\"font-size:1.1em\">Hi,</p>" +
                "<p>Thank you for choosing OrderUp. Use the following OTP to complete your Sign Up procedures. OTP is valid for 5 minutes</p>" +
                "<h2 style=\"background: #00466a;margin: 0 auto;width: max-content;padding: 0 10px;color: #fff;border-radius: 4px;\">"+OTP+"</h2>" +
                "<p style=\"font-size:0.9em;\">Regards,<br />OrderUp</p>" +
                "<hr style=\"border:none;border-top:1px solid #eee\" />" +
                "<div style=\"float:right;padding:8px 0;color:#aaa;font-size:0.8em;line-height:1;font-weight:300\">" +
                "<p>OrderUp</p>" +
                "<p>Phạm Hùng, Từ Liêm, Hà Nội, Việt Nam</p>" +
                "<p>Ha Noi</p>" +
                "</div>" +
                "</div>" +
                "</div>";

        helper.setTo(toEmail);
        helper.setSubject("Confirm mail");
        helper.setText(" ", htmlContent);

        mailSender.send(message);
        System.out.println("Mail Send...");
    }



    public boolean resendEmail(String email){
        String otp = new String(CreateOTP.OTP(6));
        try{
            sendSimpleEmail(email,otp);
        }catch (MessagingException e){
            return false;
        }
        return true;

    }


}