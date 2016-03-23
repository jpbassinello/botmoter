package br.com.botmoter.web.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@RestController
public class TelegramRestController {

	private static final String BOT_TOKEN =
			"bot157015122:AAELqgI6HOOzmKQxuyZYu6cDX3hFnKnUmNc";

	@RequestMapping(path = "/echo/{message}", method = RequestMethod.GET)
	public Message echo(@PathVariable("message") String message) {
		return new Message(message);
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
