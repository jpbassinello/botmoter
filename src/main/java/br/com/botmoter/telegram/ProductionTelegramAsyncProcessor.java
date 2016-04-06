package br.com.botmoter.telegram;

import br.com.botmoter.model.LocationType;
import br.com.botmoter.model.Place;
import br.com.botmoter.service.GooglePlacesApiService;
import br.com.botmoter.telegram.model.Location;
import br.com.botmoter.telegram.model.Update;
import br.com.botmoter.web.controller.TelegramRestController;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@Component
@Profile("production")
public class ProductionTelegramAsyncProcessor implements TelegramProcessor {

	private static final Logger LOGGER = Logger
			.getLogger(ProductionTelegramAsyncProcessor.class.getName());
	private static final String APP_BASE_URL = "https://botmoter.appspot.com";
	private final GooglePlacesApiService googlePlacesApiService = new GooglePlacesApiService();

	@Autowired
	private TelegramService telegramService;

	@PostConstruct
	public void init() {
		LOGGER.info("=====================================================================");
		LOGGER.info("=====================================================================");
		LOGGER.info("Iniciando chamada para registrar o Webhook no Telegram");
		new TelegramService().setWebhook(APP_BASE_URL + TelegramRestController.UPDATES_REST_PATH);
		LOGGER.info("Fim da chamada para registrar o Webhook no Telegram");
		LOGGER.info("=====================================================================");
		LOGGER.info("=====================================================================");
	}

	@Async
	public void processUpdate(Update update) {
		try {
			LOGGER.info("Start Processing update " + update);
			processIt(update);
			LOGGER.info("End Processing update " + update);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Unexpected error while processing update " + update, e);
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
				" lugares.";

		telegramService.sendResponse(chatId, msg);
	}
}
