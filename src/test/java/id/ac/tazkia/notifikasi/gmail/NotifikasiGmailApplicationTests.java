package id.ac.tazkia.notifikasi.gmail;

import id.ac.tazkia.notifikasi.gmail.service.GmailApiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class NotifikasiGmailApplicationTests {

	@Value("classpath:templates/welcome.html")
	private Resource mailTemplate;

	@Autowired
	private GmailApiService gmailApiService;

	@Test
	public void testSendGmail() throws Exception {
        String emailTemplate = StreamUtils
                .copyToString(mailTemplate.getInputStream(),
                Charset.forName("UTF-8"));
        gmailApiService.send("Test Notifikasi","endy@tazkia.ac.id",
                "Selamat Datang di Aplikasi Notifikasi "+ LocalDateTime.now().toString(),
                emailTemplate);
    }

}
