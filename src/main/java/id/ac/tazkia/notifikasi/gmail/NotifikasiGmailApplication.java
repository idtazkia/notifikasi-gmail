package id.ac.tazkia.notifikasi.gmail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication @EnableKafka
public class NotifikasiGmailApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(NotifikasiGmailApplication.class, args);
	}

	@Value("${gmail.folder}") private String gmailFolder;

	@Override
	public void run(String... args) throws Exception {
		Files.createDirectories(Paths.get(gmailFolder));
	}
}
