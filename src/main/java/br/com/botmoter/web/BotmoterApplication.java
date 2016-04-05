package br.com.botmoter.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@SpringBootApplication(scanBasePackages = "br.com.botmoter")
@EnableAsync
public class BotmoterApplication {

	public static void main(String[] args) {
		SpringApplication.run(BotmoterApplication.class, args);
	}
}