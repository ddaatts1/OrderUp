package com.FoodOrdering.OrderUp.Service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class EmailService {
	private static final Logger log = LogManager.getLogger(EmailService.class);



	@Value("${dailyReport.name}")
	private String DAILY_REPORT_NAME;

	@Value("${dailyReport.path}")
	private String DAILY_REPORT_PATH;


	@Value("${spring.mail.username}")
	private String EMAIL_SENDER;

	@Value("${dailyReport.email.receiverList}")
	private String DAILY_REPORT_EMAIL_RECEIVER_LIST;

	@Value("${dailyReport.email.developerList}")
	private String DAILY_REPORT_EMAIL_DEVELOPER_LIST;

	@Value("${dailyReport.email.subject}")
	private String DAILY_REPORT_EMAIL_SUBJECT;

	@Value("${dailyReport.email.content}")
	private String DAILY_REPORT_EMAIL_CONTENT;

	@Autowired
	private JavaMailSender javaMailSender;

	public void sendEmail(String logCategory, String sender, String receiverList, String subject, String content,
			String fileName, String filePath) throws Exception {
		log.info(
				"{} | Send email start | sender={} | receiverList={} | subject={} | text={} | fileName={} | filePath={}",
				logCategory, sender, receiverList, subject, content, fileName, filePath);

		MimeMessage msg = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg, true);

		// Init email
		helper.setFrom(sender);
		helper.setTo(InternetAddress.parse(receiverList));

		helper.setSubject(subject);
		helper.setText(content, true);

		// Attachment
		if (fileName != null
				&& fileName.trim().isEmpty() == false
				&& filePath != null
				&& filePath.trim().isEmpty() == false) {
			FileSystemResource file = new FileSystemResource(new File(filePath.trim()));
			helper.addAttachment(fileName.trim(), file);
		} else {
			log.info("{} | Email has no attachment", logCategory);
		}

		// Send email
		javaMailSender.send(msg);

		log.info("{} | Send email done", logCategory);
	}

	public void sendDailyReportEmail(String logCategory, boolean sendToReceiverList, String email) throws Exception {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(new Date());

		String sender = EMAIL_SENDER;
//		String receiverList = DAILY_REPORT_EMAIL_DEVELOPER_LIST;
//		if (sendToReceiverList == true) {
//			receiverList = DAILY_REPORT_EMAIL_RECEIVER_LIST;
//		}
		String receiverList = email;

		String subject = DAILY_REPORT_EMAIL_SUBJECT.replaceAll("%date", date);
		String content = DAILY_REPORT_EMAIL_CONTENT.replaceAll("%date", date);
		String fileName = DAILY_REPORT_NAME.replaceAll("%date", date);
		String filePath = DAILY_REPORT_PATH + fileName;

		sendEmail(logCategory, sender, receiverList, subject, content, fileName, filePath);
	}



}
