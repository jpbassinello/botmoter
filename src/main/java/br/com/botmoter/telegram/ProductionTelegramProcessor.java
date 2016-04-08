package br.com.botmoter.telegram;

import br.com.botmoter.model.*;
import br.com.botmoter.service.FoursquareApiService;
import br.com.botmoter.service.GooglePlacesApiService;
import br.com.botmoter.telegram.model.Location;
import br.com.botmoter.telegram.model.Message;
import br.com.botmoter.telegram.model.Update;
import br.com.botmoter.util.DownloadImageFromWeb;
import br.com.botmoter.web.controller.TelegramRestController;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Multimaps;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@Component
@Profile("production")
public class ProductionTelegramProcessor implements TelegramProcessor {

	private static final Logger LOGGER = Logger
			.getLogger(ProductionTelegramProcessor.class.getName());
	private static final String APP_BASE_URL = "https://botmoter.appspot.com";
	private static final int MAX_TIPS = 3;
	private static final String TIP = "/dica";
	private static final Map<Integer, ChatDataWrapper> DATA_BY_CHAT = new HashMap<>();
	private static final String LINE_BREAK = "\n";
	private final GooglePlacesApiService googlePlacesApiService = new GooglePlacesApiService();
	@Autowired
	private TelegramService telegramService;

	private static String removeAccents(String original) {
		if (original == null) {
			return null;
		}
		original = Normalizer.normalize(original, Normalizer.Form.NFD);
		original = original.replaceAll("[^\\p{ASCII}]", "");
		return original;
	}

	public static void main(String[] args) {
		Random random = new Random();
		System.out.println(random.nextInt(2));
		System.out.println(random.nextInt(2));
		System.out.println(random.nextInt(2));
		System.out.println(random.nextInt(2));
	}

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

	public void processUpdate(Update update) {
		boolean msgSent = false;
		try {
			LOGGER.info("Start Processing update " + update);
			msgSent = processIt(update);
			LOGGER.info("End Processing update " + update);
		} catch (Exception e) {
			msgSent = false;
			LOGGER.log(Level.SEVERE, "Unexpected error while processing update " + update, e);
		}
		if (!msgSent) {
			final Message message = update.getMessage();
			final int chatId = message.getChat().getId();
			String name = message.getFrom().getFirstName();
			String msg = "Me desculpe " + name + ", mas eu ainda não sei processar sua mensagem." +
					" " +
					"Talvez você queira me enviar a sua localização ou começar novamente com " +
					"/start";
			try {
				telegramService.sendResponse(chatId, msg);
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Unexpected error while processing update " + update, e);
			}
		}
	}

	private boolean processIt(Update update) throws IOException {
		final Message message = update.getMessage();
		final int chatId = message.getChat().getId();
		final Location location = message.getLocation();
		if (location != null) {
			sendMessageToChatAfterLocation(chatId, location);
			return true;
		}
		final String text = message.getText();
		if ("/start".equals(text)) {
			sendStartMessageToChat(chatId);
			return true;
		}
		if (text.startsWith(TIP)) {
			sentTipMessageToChat(chatId, text);
			return true;
		}
		return false;
	}

	private void sentTipMessageToChat(int chatId, String text) throws IOException {
		int index = Integer.valueOf(text.replace(TIP, "")) - 1;

		ChatDataWrapper chatDataWrapper = DATA_BY_CHAT.get(chatId);
		final PlaceTip placeTip = chatDataWrapper.lastTipsSent.get(index);

		final GooglePlace googlePlace = placeTip.getGooglePlace();
		// precisa recarregar o objeto completo no Foursquare
		final FoursquareApiService foursquareApiService = new FoursquareApiService();
		final FoursquarePlace foursquarePlace = foursquareApiService
				.searchFullPlace(placeTip.getFoursquarePlace().getId());
		String msg = "<strong>" + googlePlace.getName() + "</strong>";
		final List<FoursquareCategory> categories = foursquarePlace.getCategories();
		if (categories != null && !categories.isEmpty()) {
			String cats = Joiner.on(", ").join(FluentIterable.from(categories)
					.transform(new Function<FoursquareCategory, String>() {
						@Override
						public String apply(FoursquareCategory input) {
							return input.getName();
						}
					}));
			msg += LINE_BREAK;
			msg += cats;
		}
		if (foursquarePlace.getUrl() != null) {
			msg += LINE_BREAK;
			msg += "<strong>Website: </strong>" + foursquarePlace.getUrl();
		}
		if (googlePlace.getRating() != null) {
			msg += LINE_BREAK;
			msg += "<strong>Avaliação do Google Maps: </strong>" + googlePlace.getRating()
					.multiply(BigDecimal.valueOf(2)).toString();
		}
		if (foursquarePlace.getRating() != null) {
			msg += LINE_BREAK;
			msg += "<strong>Avaliação do Foursquare: </strong>" + foursquarePlace.getRating()
					.toString();
		}
		if (googlePlace.getGoogleUrl() != null) {
			msg += LINE_BREAK;
			msg += "<strong>Visualizar no Google: </strong>" + googlePlace.getGoogleUrl();
		}
		if (foursquarePlace.getShortUrl() != null) {
			msg += LINE_BREAK;
			msg += "<strong>Visualizar no Foursquare: </strong>" + foursquarePlace.getShortUrl();
		}

		telegramService.sendResponse(chatId, msg);

		final FoursquareLocation location = foursquarePlace.getLocation();
		if (location != null && location.getFormattedAddress() != null && !location
				.getFormattedAddress().isEmpty()) {
			String address = Joiner.on(LINE_BREAK).join(location.getFormattedAddress());
			telegramService
					.sendResponse(chatId, "<strong>Endereço: </strong>" + LINE_BREAK + address);
		}
		telegramService.sendLocation(chatId, googlePlace.getLatitude().doubleValue(),
				googlePlace.getLongitude().doubleValue());
	}

	private void sendStartMessageToChat(int chatId) throws IOException {
		String msg = "Eu posso sugerir alguns lugares para você. Basta você me  enviar a sua " +
				"localização a qualquer momento.";
		telegramService.sendResponse(chatId, msg);
		DATA_BY_CHAT.put(chatId, new ChatDataWrapper());
	}

	private void sendMessageToChatAfterLocation(int chatId, Location location) throws IOException {
		final List<PlaceTip> sentTips = findAndShuffleTips(location);

		if (sentTips.isEmpty()) {
			String msg = "Desculpe, mas não encontrei nenhuma sugestão de local próximo a você. "
					+ "Por favor, tente novamente depois com uma nova localização.";
			telegramService.sendResponse(chatId, msg);
			return;
		}

		String msg = "<strong>Veja alguns lugares próximos a você</strong>";
		telegramService.sendResponse(chatId, msg);

		for (int i = 0; i < sentTips.size(); i++) {
			PlaceTip placeTip = sentTips.get(i);
			final GooglePlace googlePlace = placeTip.getGooglePlace();

			String tipMsg = googlePlace.getName() + LINE_BREAK;

			int distance = googlePlace.getDistanceBetweenOrigin()
					.divide(BigDecimal.valueOf(1000), 0, BigDecimal.ROUND_HALF_UP).intValue();

			distance = distance < 1 ? 1 : distance;
			String km = distance == 1 ? "km" : "kms";

			tipMsg += "Distância aproximada do seu local atual: " + distance + km + LINE_BREAK;
			tipMsg += "Saiba mais " + TIP + (i + 1);

			final String imgUrl = googlePlace.getImgUrl();
			if (imgUrl != null) {
				byte[] bytes = DownloadImageFromWeb.downloadImage(imgUrl);
				telegramService.sendPhoto(chatId, bytes, tipMsg);
			} else {
				telegramService.sendResponse(chatId, tipMsg);
			}
		}

		ChatDataWrapper chatDataWrapper = DATA_BY_CHAT.get(chatId);
		if (chatDataWrapper == null) {
			chatDataWrapper = new ChatDataWrapper();
		}
		chatDataWrapper.chatStage = ChatStage.LOCATION_SEND;
		chatDataWrapper.lastTipsSent = sentTips;
		DATA_BY_CHAT.put(chatId, chatDataWrapper);
	}

	private List<PlaceTip> findAndShuffleTips(Location location) {
		final List<PlaceTip> tips = findTips(location.getLatitude().doubleValue(),
				location.getLongitude().doubleValue());
		final List<PlaceTip> sentTips = new ArrayList<>();
		if (tips.size() <= MAX_TIPS) {
			sentTips.addAll(tips);
		} else {
			Collections.shuffle(tips);
			sentTips.addAll(tips.subList(0, MAX_TIPS));
		}
		return sentTips;
	}

	private List<PlaceTip> findTips(double latitude, double longitude) {
		final LocalTime now = LocalTime.now(DateTimeZone.forID("America/Sao_Paulo"));
		final Set<LocationType> locationTypesByThisTime = LocationType.getTypesByTimeInADay(now);

		final List<PlaceTip> tips = new ArrayList<>();
		for (LocationType locationType : locationTypesByThisTime) {
			final List<PlaceTip> tipsByLocationType = findTipsByLocationType(latitude, longitude,
					locationType);
			tips.addAll(tipsByLocationType);
		}
		return tips;
	}

	private List<PlaceTip> findTipsByLocationType(double latitude, double longitude,
			LocationType locationType) {
		final List<GooglePlace> googlePlaces = new GooglePlacesApiService()
				.getPlaces(latitude, longitude, locationType);

		final Map<String, Collection<GooglePlace>> googlePlaceByName = Multimaps
				.index(googlePlaces, new Function<GooglePlace, String>() {
					@Override
					public String apply(GooglePlace googlePlace) {
						return removeAccents(googlePlace.getName()).toUpperCase();
					}
				}).asMap();

		final List<FoursquarePlace> foursquarePlaces = new ArrayList<>();
		final FoursquareApiService foursquareApiService = new FoursquareApiService();
		// two pages, for best results
		foursquarePlaces
				.addAll(foursquareApiService.searchPlaces(latitude, longitude, locationType, 0));
		foursquarePlaces
				.addAll(foursquareApiService.searchPlaces(latitude, longitude, locationType, 1));

		final Map<String, Collection<FoursquarePlace>> foursquarePlaceByName = Multimaps
				.index(foursquarePlaces, new Function<FoursquarePlace, String>() {
					@Override
					public String apply(FoursquarePlace foursquarePlace) {
						return removeAccents(foursquarePlace.getName()).toUpperCase();
					}
				}).asMap();

		final List<PlaceTip> tips = new ArrayList<>();
		for (Map.Entry<String, Collection<GooglePlace>> entry : googlePlaceByName.entrySet()) {
			Collection<FoursquarePlace> fsqPlaces = foursquarePlaceByName.get(entry.getKey());
			if (fsqPlaces != null) {
				// TODO: no futuro, mudar o algoritmo para considerar também a lat,long
				final GooglePlace googlePlace = FluentIterable.from(entry.getValue()).first()
						.get();
				final FoursquarePlace foursquarePlace = FluentIterable.from(fsqPlaces).first()
						.get();
				tips.add(new PlaceTip(googlePlace, foursquarePlace));
			}
		}
		return tips;
	}

	private enum ChatStage {
		START,
		LOCATION_SEND;
	}

	private final class ChatDataWrapper {
		private ChatStage chatStage = ChatStage.START;
		private List<PlaceTip> lastTipsSent;
	}

}