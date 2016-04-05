package br.com.botmoter.web.controller;

import br.com.botmoter.telegram.TelegramProcessor;
import br.com.botmoter.telegram.model.Update;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@RestController
public class TelegramRestController {

	public static final String UPDATES_REST_PATH = "/aokkow23SSwqQLLzqW/updates";
	private static final Logger LOGGER = LoggerFactory.getLogger(TelegramRestController.class);

	@Autowired
	private TelegramProcessor telegramProcessor;

	@RequestMapping(path = UPDATES_REST_PATH, method = RequestMethod.POST)
	public Update updates(@RequestBody String messageJson) {
		LOGGER.info("Receiving a update from Telegram {}" + messageJson);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			final Update update = objectMapper.readValue(messageJson, Update.class);
			telegramProcessor.processUpdate(update);
			// even the Telegram bot doesn´t need it, return the object
			// to be validated in Integration Test
			return update;
		} catch (IOException e) {
			LOGGER.warn("Error while reading messsage from Telegram", e);
			throw new IllegalStateException("Error while reading messsage from Telegram", e);
		}
	}

}
