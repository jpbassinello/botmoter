package br.com.botmoter.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@SpringBootApplication(scanBasePackages = "br.com.botmoter")
public class BotmoterApplication {

	public static void main(String[] args) {
		SpringApplication.run(BotmoterApplication.class, args);
	}
}