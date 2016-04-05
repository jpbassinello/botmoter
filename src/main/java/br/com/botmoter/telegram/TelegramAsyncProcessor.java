package br.com.botmoter.telegram;

import br.com.botmoter.model.LocationType;
import br.com.botmoter.model.Place;
import br.com.botmoter.service.GooglePlacesApiService;
import br.com.botmoter.telegram.model.Location;
import br.com.botmoter.telegram.model.Update;
import br.com.botmoter.web.bean.Properties;
import br.com.botmoter.web.controller.TelegramRestController;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@Component
public class TelegramAsyncProcessor {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TelegramAsyncProcessor.class);
	private final GooglePlacesApiService googlePlacesApiService = new GooglePlacesApiService();

	@Autowired
	private TelegramService telegramService;

	@Autowired
	private Properties properties;

	@PostConstruct
	public void init() {
		if (properties.isProduction()) {
			new TelegramService()
					.setWebhook(properties.getAppUrl() + TelegramRestController.UPDATES_REST_PATH);
		}
	}

	@Async
	public void processUpdate(Update update) {
		try {
			LOGGER.info("Start Processing update {}", update);
			processIt(update);
			LOGGER.info("End Processing update {}", update);
		} catch (Exception e) {
			LOGGER.warn("Unexpected error while processing update {}", update, e);
		}
	}

	private void processIt(Update update) throws IOException {
		final int chatId = update.getMessage().getChat().getId();
		final Location location = update.getMessage().getLocation();
		if (location != null) {
			final List<Place> places = googlePlacesApiService
					.getPlaces(location.getLatitude().doubleValue(),
							location.getLongitude().doubleValue(), LocationType.BARS);
			String msg = "Achei alguns lugares: \n";
			msg += Joiner.on(", ")
					.join(FluentIterable.from(places).transform(new Function<Place, String>() {
						@Override
						public String apply(Place input) {
							return input.getName();
						}
					}));
			telegramService.sendResponse(chatId, msg);
		}
		String name = update.getMessage().getFrom().getFirstName();
		String msg = "Olá " + name + "! Eu ainda estou em fase de teste e não sei processar sua " +
				"mensagem. Talvez você queira me enviar a sua localização para eu sugerir alguns" +
				" " +
				"lugares.";

		telegramService.sendResponse(chatId, msg);
	}
}