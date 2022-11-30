package com.max.restaurant.utils;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class UtilsEmailNotificator {
    public static final String mimeTypePdf = "application/pdf";

    public static void send(Collection<String> recipients, String subject, String text,
                            List<InputStream> attachments, List<String> fileNames, List<String> mimeTypes)
            throws EmailException, IOException {

        // check for null references
        Objects.requireNonNull(recipients);

        // load email configuration from properties file
        Properties properties = new Properties();
        properties.load(UtilsEmailNotificator.class.getResourceAsStream("/mail.properties"));
        String host = properties.getProperty("mail.smtp.host");
        String port = properties.getProperty("mail.smtp.port");
        String ssl = properties.getProperty("mail.smtp.ssl.enable");
        String username = properties.getProperty("mail.smtp.username");
        String password = properties.getProperty("mail.smtp.password");

        // create an email message with html support
        HtmlEmail email = new HtmlEmail();
        email.setCharset(StandardCharsets.UTF_8.name());

        // configure SMTP connection
        email.setHostName(host);
        email.setSmtpPort(Integer.parseInt(port));
        email.setAuthentication(username, password);
        email.setSSLOnConnect(Boolean.parseBoolean(ssl));

        // set its properties accordingly
        email.setFrom(username);
        recipients = List.of(username); //remove to send somewhere!!!
        email.addTo(recipients.toArray(new String[]{}));
        email.setSubject(subject);
        email.setHtmlMsg(text);

        // add the attachment
        if (attachments != null) {
            for (int i = 0; i < attachments.size(); i++) {
                // create a data source to wrap the attachment and its mime type
                ByteArrayDataSource dataSource = new ByteArrayDataSource(attachments.get(i), mimeTypes.get(i));
                email.attach(dataSource, fileNames.get(i), "attachment");
            }
        }
        email.send();
    }
}
