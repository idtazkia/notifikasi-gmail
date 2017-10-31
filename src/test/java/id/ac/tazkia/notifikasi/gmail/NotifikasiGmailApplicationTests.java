package id.ac.tazkia.notifikasi.gmail;

import id.ac.tazkia.notifikasi.gmail.service.GmailApiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;

import java.nio.charset.Charset;

@RunWith(SpringRunner.class)
@SpringBootTest
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
        gmailApiService.send("endy@tazkia.ac.id",
                "Selamat Datang di Aplikasi Notifikasi",
                emailTemplate);
    }

}
