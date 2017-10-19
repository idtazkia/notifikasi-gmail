package id.ac.tazkia.notifikasi.gmail;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import id.ac.tazkia.notifikasi.gmail.service.GmailApiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;

import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NotifikasiGmailApplicationTests {

	@Value("classpath:templates/welcome.html")
	private Resource mailTemplate;

	@Autowired
	private GmailApiService gmailApiService;

	@Test
	public void testSendGmail() throws Exception {
        String emailTemplate = StreamUtils.copyToString(mailTemplate.getInputStream(),
                Charset.forName("UTF-8"));

        Map<String, String> data = new HashMap<>();
        data.put("customerName", "Endy Muhardin");

        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache m = mf.compile("templates/welcome.html");
        StringWriter emailContent = new StringWriter();
        m.execute(emailContent, data);
        gmailApiService.send("endy@tazkia.ac.id", "Selamat Datang di Aplikasi Notifikasi", emailContent.toString());
    }

}
