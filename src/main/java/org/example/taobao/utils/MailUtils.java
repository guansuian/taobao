package org.example.taobao.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class MailUtils {
    private static MimeMessage message;
    //用于判定验证码是否匹配
    public static String JudgeCode;

    public static String sendEmail(String to) throws MessagingException {
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        // QQ邮箱服务器
        String smtpHost = "smtp.qq.com";
        // 邮箱用户名，即QQ账号（自定义）
        final String username = "438739692";
        // 邮箱授权码（自定义）
        final String password = "cgwcetpxbkombibh";
        // 自己的邮箱（自定义）
        final String from = "438739692@qq.com";
        // 要发送的邮箱地址（自定义）
        String toAddress = to;

        Transport transport;
        Properties props = new Properties();
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.setProperty("mail.smtp.auth", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.username", username);
        props.put("mail.smtp.password", password);
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        InternetAddress[] addresses = {new InternetAddress(toAddress)};
        message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, addresses);
        message.setSubject("验证码");// 发送标题（自定义）
        message.setSentDate(new Date());
        String Code = generateVerificationCode(5);
        message.setText(Code);// 发送内容（自定义）
        transport = session.getTransport("smtp");
        transport.connect(smtpHost, username, password);
        transport.send(message);
        System.out.println("Email has been sent");
        transport.close();
        return Code;
    }

    private static String generateVerificationCode(int length) {
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder verificationCode = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            verificationCode.append(charSet.charAt(random.nextInt(charSet.length())));
        }
        System.out.println(verificationCode.toString());
        return verificationCode.toString();
    }

    public static void main(String[] args) throws MessagingException {
        try {
            sendEmail("2829649106@qq.com");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}