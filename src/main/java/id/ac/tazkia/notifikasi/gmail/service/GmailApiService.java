package id.ac.tazkia.notifikasi.gmail.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import com.google.common.io.BaseEncoding;

import jakarta.annotation.PostConstruct;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class GmailApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GmailApiService.class);

    private static final List<String> SCOPES =
            Arrays.asList(GmailScopes.GMAIL_SEND);

    @Value("${gmail.credential}")
    private String credentialFile;

    @Value("${gmail.folder}")
    private String dataStoreFolder;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${gmail.account.username}")
    private String senderGmailAccount;

    @Value("${sender.displayname}")
    private String senderDisplayName;

    private Gmail gmail;

    @PostConstruct
    public void initGmail() throws Exception {
        JsonFactory jsonFactory =
                GsonFactory.getDefaultInstance();

        FileDataStoreFactory fileDataStoreFactory =
                new FileDataStoreFactory(new File(dataStoreFolder));

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(jsonFactory,
                        new InputStreamReader(new FileInputStream(credentialFile)));

        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        httpTransport, jsonFactory, clientSecrets, SCOPES)
                        .setDataStoreFactory(fileDataStoreFactory)
                        .setAccessType("offline")
                        .build();

        Credential gmailCredential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");

        gmail = new Gmail.Builder(httpTransport, jsonFactory, gmailCredential)
                .setApplicationName(applicationName)
                .build();

    }

    public void send(String sender, String to, String subject, String body)  {

        if(!StringUtils.hasText(sender)){
            sender = senderDisplayName;
        }

        try {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);

            InternetAddress from = new InternetAddress(senderGmailAccount, sender);
            InternetAddress destination = new InternetAddress(to);
            MimeMessage email = new MimeMessage(session);
            email.setFrom(from);
            email.addRecipient(jakarta.mail.Message.RecipientType.TO, destination);
            email.setSubject(subject);
            email.setContent(body, "text/html; charset=utf-8");

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            email.writeTo(buffer);
            byte[] bytes = buffer.toByteArray();
            String encodedEmail = BaseEncoding.base64().encode(bytes);
            Message message = new Message();
            message.setRaw(encodedEmail);

            message = gmail.users().messages().send("me", message).execute();
            LOGGER.info("Email {} from {} to {} with subject {}", message.getId(), from, destination, subject);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}