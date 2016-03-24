package br.com.botmoter.web.controller;

import br.com.botmoter.telegram.AsyncProcessorService;
import br.com.botmoter.telegram.TelegramService;
import br.com.botmoter.telegram.model.Update;
import br.com.botmoter.web.bean.Properties;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@RestController
public class TelegramRestController {

	public static final String UPDATES_REST_PATH = "/aokkow23SSwqQLLzqW/updates";
	private static final Logger LOGGER = LoggerFactory.getLogger(TelegramRestController.class);
	@Autowired
	private Properties properties;
	@Autowired
	private AsyncProcessorService asyncProcessorService;

	@PostConstruct
	public void init() {
		if (properties.isProduction()) {
			new TelegramService().setWebhook(properties.getAppUrl() + UPDATES_REST_PATH);
		}
	}

	@RequestMapping(path = UPDATES_REST_PATH, method = RequestMethod.POST)
	public Update updates(@RequestBody String messageJson) {
		LOGGER.info("Receiving a update from Telegram {}" + messageJson);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			final Update update = objectMapper.readValue(messageJson, Update.class);
			if (properties.isProduction()) {
				asyncProcessorService.processUpdate(update);
			}
			// even the Telegram bot doesn´t need it, return the object
			// to be validated in Integration Test
			return update;
		} catch (IOException e) {
			LOGGER.warn("Error while reading messsage from Telegram", e);
			throw new IllegalStateException("Error while reading messsage from Telegram", e);
		}
	}

}
