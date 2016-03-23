package br.com.botmoter.web.controller;

import br.com.botmoter.web.bean.Properties;
import org.hibernate.validator.cfg.defs.AssertTrueDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.AssertTrue;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@RestController
public class TelegramRestController {

	public static final String UPDATES_REST_PATH = "/aokkow23SSwqQLLzqW/updates";
	private static final String BOT_TOKEN =
			"bot157015122:AAELqgI6HOOzmKQxuyZYu6cDX3hFnKnUmNc";

	@Autowired
	private Properties properties;

	@RequestMapping(path = UPDATES_REST_PATH, method = RequestMethod.GET)
	public Message updates() {
		System.out.println("=================================================");
		System.out.println("=================================================");
		System.out.println("=================================================");
		System.out.println(properties.getAppEnv());
		System.out.println(properties.isProduction());
		Assert.isTrue(!properties.isProduction());
		System.out.println("=================================================");
		System.out.println("=================================================");
		System.out.println("=================================================");
		return new Message("hello");
	}

	class Message {
		private String message;

		public Message(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}
}
