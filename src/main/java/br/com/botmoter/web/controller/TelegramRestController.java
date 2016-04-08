package br.com.botmoter.web.controller;

import br.com.botmoter.model.GooglePlace;
import br.com.botmoter.model.LocationType;
import br.com.botmoter.service.GooglePlacesApiService;
import br.com.botmoter.telegram.TelegramProcessor;
import br.com.botmoter.telegram.TelegramService;
import br.com.botmoter.telegram.model.Update;
import br.com.botmoter.util.DownloadImageFromWeb;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@RestController
public class TelegramRestController {

	public static final String UPDATES_REST_PATH = "/aokkow23SSwqQLLzqW/updates";
	private static final Logger LOGGER = Logger.getLogger(TelegramRestController.class.getName());

	@Autowired
	private TelegramProcessor telegramProcessor;

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String abc() {
		return "abc";
	}

	@RequestMapping(path = "/test", method = RequestMethod.GET)
	public Object test() throws IOException {
//		double latitude = -22.90556D;
//		double longitude = -47.06083D;

		return "sucesso";
	}

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
			LOGGER.log(Level.WARNING, "Error while reading messsage from Telegram", e);
			throw new IllegalStateException("Error while reading messsage from Telegram", e);
		}
	}

}
