package br.com.botmoter.telegram;

import br.com.botmoter.hackathon.bot.model.TipoLocal;
import br.com.botmoter.model.Place;
import br.com.botmoter.service.PlaceService;
import br.com.botmoter.telegram.model.Location;
import br.com.botmoter.telegram.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@Service
public class AsyncProcessorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncProcessorService.class);
	private final PlaceService placeService = new PlaceService();

	@Autowired
	private TelegramService telegramService;

	@Async
	public void proccessUpdate(Update update) {
		try {
			proccessIt(update);
		} catch (Exception e) {
			LOGGER.warn("Unexpected error while processing update {}", update, e);
		}
	}

	private void proccessIt(Update update) throws IOException {
		final int chatId = update.getMessage().getChat().getId();
		final Location location = update.getMessage().getLocation();
		if (location != null) {
			final List<Place> places = placeService.getPlaces(location.getLatitude().doubleValue()
					, location.getLongitude().doubleValue(), TipoLocal.BARES);
			String msg = "Achei alguns lugares: \n";
			msg += places.stream().map(Place::getName).collect(Collectors.joining(", "));
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
