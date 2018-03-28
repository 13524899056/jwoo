package com.jwaoo.account.sevice;

import com.jwaoo.account.config.Constants;
import com.jwaoo.account.model.Account;
import com.jwaoo.common.core.config.Global;
import com.jwaoo.common.core.utils.LogUtils;
import com.jwaoo.common.core.zk.ZKApi;
import com.sun.net.ssl.internal.ssl.Provider;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.security.Security;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);
    
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final String PROP_TLS = "mail.smtp.ssl.enable";
//    private static final String PROP_HOST = "mail.host";
//    private static final String PROP_PORT = "mail.port";
//    private static final String PROP_USER = "mail.user";
//    private static final String PROP_PASSWORD = "mail.password";
//    private static final String PROP_PROTO = "mail.protocol";
//    private static final String PROP_AUTH = "mail.auth";
    private static final String PROP_SMTP_AUTH = "mail.smtp.auth";
    private static final String PROP_STARTTLS = "mail.smtp.starttls.enable";
    private static final String PROP_TRANSPORT_PROTO = "mail.transport.protocol";
//    private static final String EMAIL_TEMPLATE_PROTO = "mail.resetPwd.template";
//    private static final String EMAIL_SUBJECT = "mail.resetPwd.subject";
    private static final String AWS_USERNAME = "account@senselovers.com";
    private static final String AWS_PASSWORD = "Jwaoo2020";
    private static final String AWS_SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    private static final String LOCALHOST_USERNAME = "apple02@jwaoo.com";
    private static final String LOCALHOST_PASSWORD = "Jwaoo135";
    private static final String LOCALHOST_PROTOCOL = "smtp";
    private static final String LOCALHOST_HOST = "smtp.qiye.163.com";
    private static final int LOCALHOST_PORT = 25;

    private static final MailService instance = new MailService();
    private static JavaMailSenderImpl javaMailSender;

    private static String SERVER_HOST;


    private MailService(){
        SERVER_HOST = Global.getConfigCfg("cfg:mail.host", "https://127.0.0.1/oauth/v1/registration/verification/email");
        if (Constants.isTestMode())
        {
            javaMailSender = init();
        }else
        {
            javaMailSender = initGmail();
        }
    }
    
    public static MailService getInstance(){
    	return instance;
    }
    
    /**
     * System default email address that sends the e-mails.
     */
    private String from;
    private String cc;
    private String bcc;

	private JavaMailSenderImpl initGmail(){
		this.from = AWS_USERNAME;
        this.cc = "";//settings.get("mail.cc");
        this.bcc = "";//settings.get("mail.bcc");
        
        log.debug("Configuring gmail server");
		Provider p = new Provider();
		Security.addProvider(p);
    	//String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		// Get a Properties object
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.host","smtp.gmail.com");
		props.setProperty("mail.smtp.socketFactory.class", AWS_SSL_FACTORY);
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		props.setProperty("mail.smtp.user", AWS_USERNAME);
		props.setProperty("mail.smtp.password", AWS_PASSWORD);
		props.setProperty("mail.smtp.auth", "true");

		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		  Session session = Session.getDefaultInstance(props, new Authenticator(){
		      protected PasswordAuthentication getPasswordAuthentication() {
		          return new PasswordAuthentication(AWS_USERNAME, AWS_PASSWORD);
		      }});
		sender.setSession(session);
		sender.setUsername(AWS_USERNAME);
		sender.setPassword(AWS_PASSWORD);
		sender.setJavaMailProperties(props);
		return sender;
    }
    
    
    private JavaMailSenderImpl init() {
        this.from = LOCALHOST_USERNAME;
        this.cc = "";
        this.bcc = "";
        log.debug("Configuring mail server");
        Boolean tls = false;
        Boolean auth = true;

        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        if (LOCALHOST_HOST != null && !LOCALHOST_HOST.isEmpty()) {
            sender.setHost(LOCALHOST_HOST);
        } else {
            log.warn("Warning! Your SMTP server is not configured. We will try to use one on localhost.");
            log.debug("Did you configure your SMTP settings in your application.yml?");
            sender.setHost(DEFAULT_HOST);
        }
        sender.setPort(LOCALHOST_PORT);
        sender.setUsername(LOCALHOST_USERNAME);
        sender.setPassword(LOCALHOST_PASSWORD);

        Properties sendProperties = new Properties();
        sendProperties.setProperty(PROP_SMTP_AUTH, auth.toString());
        sendProperties.setProperty(PROP_TLS, tls.toString());
        sendProperties.setProperty(PROP_TRANSPORT_PROTO, LOCALHOST_PROTOCOL);
        sendProperties.setProperty(PROP_STARTTLS, tls.toString());
        sender.setJavaMailProperties(sendProperties);
        return sender;
    }

    public boolean sendEmail(String to, String content, String subject) {

        log.debug("Send e-mail to '{}' with subject '{}' and content={}", to, subject, content);
        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(from);
            if (StringUtils.isNotBlank(cc)) {
                message.setCc(cc.split(","));
            }
            if (StringUtils.isNotBlank(bcc)) {
                message.setBcc(bcc.split(","));
            }

            message.setSubject(subject);
            message.setText(content, true);
            javaMailSender.send(mimeMessage);
            log.info("Sent e-mail to User '{}'", to);
            return true;
        } catch (Exception e) {
            log.error("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
            return false;
        }
    }

    /**
     *
     * @param loginName -- 用户登录名
     * @param email -- 邮件地址
     * @param code -- 校验码
     * @return
     */
    @Async
    public Future<Boolean> sendVerificationCode(String email) {
        String subject = "Email verification";
        String content = "Just a Test Email!!!!!!!";
            
        try {
            sendEmail(email, content, subject);
            return new AsyncResult<>(true);
        }catch (Exception e){
            log.error("exception: " + e.getMessage());
        }
        return new AsyncResult<>(false);
    }
    
    
    /**
    * 忘记密码给出一个随机密码发送到用户邮箱
    * @param loginName -- 用户登录名
    * @param email -- 邮件地址
    * @param pwdOrg -- 密码
    * @return
    */
   public Boolean sendForgotPassword(Account user, String email, String sysPassword)
   {

       // 获取用户显示名称
       String showName = user.getNickname();
       
	   // 获取发送邮件标题
	   String subject = "SenseNow Reset Password";
       String HefURL2 = "<div style='font-size:14px;font-family:arial,verdana,sans-serif ;line-height:1.666;padding:0;margin:0;overflow:auto;white-space:normal;word-wrap:break-word;min-height:100px' >"
               +"<div ><div class='edm_name'>Dear <a href='mailto:'>" + showName + "</a>,</div></div>"
               +"<br><div class='edm_content'>You're receiving this email because you requested a password reset for your SenseNow account.</div><br>"
               +"<div class='edm_content'>Our system automatically generated a new password for you: [<span style='color: Red'>" + sysPassword + "</span>], please login with this password within two hours after receiving this email. You can then choose your own password once logged in.</div><br>"
               +"<div class='edm_content'>This is an automatically generated email, please do not reply.</div>"
               +"<hr style='height: 1px'></div>";

       String content = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=gb2312'/></head><body>" + HefURL2 + "<br></body></html>";
       try {
           CompletableFuture<Boolean> res = CompletableFuture.supplyAsync(() -> sendEmail(email, content, subject));
           return res.get(3, TimeUnit.SECONDS);
//               return new AsyncResult<>(true);
       } catch (Exception e){
           LogUtils.log4Error("send mail error ", e);
           return false;
       }
   }
     
	public Boolean sendVerificationCodeToUserEmail(String email, String code) {
           // 获取发送邮件标题
           String url3 = SERVER_HOST + "?code="+code;
           //final String subject = "Register Email verification code to " + loginName;
           String subject = "Please verify your email address";

           String HefURL = "<div style='padding: 35px 18px 20px 18px; font-size: 12px; font-family: arial, verdana, sans-serif;'><p style='font-weight: 700; padding-left: 0; font-size: 14px;'>Hey there,</p>"
                +"<p style='padding-left: 2em;font-size: 14px;'>Before we can give you access to all of SenseNow features, please confirm your account within 72 hours by clicking the button below.</p>"
                +"<p style='padding-left: 2em;'><a style='margin-left: 2em; padding: 11px 0; width: 152px; text-align: center; background-color: #54a3ee; color: #fff; font-size: 14px; margin: 30px 0;"
                +"display: block; text-decoration: none; -moz-border-radius: 4px; -webkit-border-radius: 4px; border-radius: 4px;' href='" + url3 + "'>Validate email</a></p>"
                +"<p style='padding-left: 2em;font-size: 14px;'>Or click here:</p>"
                +"<p style='line-height: 20px;padding-left: 2em;'><a href='" + url3 + "'>" + url3 + "</a></p>  <p style='padding-left: 2em;font-size: 14px;'>This is an automated email, please do not reply.</p>"
                +"<hr style='height: 1px'></div>";

           String content = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'/></head><body>" + HefURL + "<br></body></html>";

           try {
               CompletableFuture<Boolean> res = CompletableFuture.supplyAsync(() -> sendEmail(email, content, subject));
               return res.get(3, TimeUnit.SECONDS);
//	               return new AsyncResult<>(true);
           }catch (Exception e){
               LogUtils.log4Error("send mail error: ", e);
               return false;
           }
//	        return new AsyncResult<>(false);
	}
    
}
