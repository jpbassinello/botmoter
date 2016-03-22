package br.com.botmoter.hackathon.bot;

import br.com.botmoter.hackathon.bot.model.Message;
import br.com.botmoter.hackathon.bot.model.MessageLocation;
import br.com.botmoter.hackathon.bot.model.MessageText;
import br.com.botmoter.hackathon.bot.model.Response;
import br.com.botmoter.service.UrlShortnerService;
import br.com.botmoter.telegram.TelegramService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

	private static final Set<Long> IDS = new HashSet<>();
	private static MainBot BOT = new MainBot();
	private static TelegramService telegramService = new TelegramService();
	// TODO: alterar antes da apresentação
	private static int LAST_MESSAGE = 1358;

	public static void main(String[] args) throws Exception {
		while (true) {
			String updates = telegramService.getUpdates(LAST_MESSAGE);
			JsonObject obj = parse(updates);
			final JsonArray array = obj.get("result").getAsJsonArray();
			for (JsonElement e : array) {
				JsonObject message = e.getAsJsonObject().get("message")
						.getAsJsonObject();
				Long id = message.get("message_id").getAsLong();
				if (id < LAST_MESSAGE || IDS.contains(id)) {
					continue;
				}
				IDS.add(id);

				Message m = extractMessage(message);

				if (m == null) {
					// não sabemos que tipo de mensagem é. Continuando.
					continue;
				}
				List<Response> rs;
				rs = BOT.process(m);
				if (rs == null) {
					continue;
				}
				for (Response r : rs) {
					switch (r.getResponseType()) {
						case TEXT:
							telegramService.sendResponse(m.getUserId(), r
									.getTexto());
							break;
						case LOCATION:
							telegramService.sendLocation(m.getUserId(), r
									.getLatitude(), r.getLongitude());
							break;
						case PHOTO:
							telegramService.sendResponse(m.getUserId(), new
									UrlShortnerService().shortUrl(r.getPhoto
									()));
							break;
					}
				}
			}

			Thread.sleep(1000);
		}

	}

	private static Message extractMessage(JsonObject message) {
		Message m = null;
		long userId = message.get("chat").getAsJsonObject().get("id")
				.getAsLong();
		String text = message.get("text") == null ? null : message.get("text")
				.getAsString();
		if (text != null) {
			m = new MessageText(text, userId);
		} else {
			final JsonObject location = message.getAsJsonObject("location");
			if (location != null) {
				final double latitude = location.get("latitude").getAsDouble();
				final double longitude = location.get("longitude")
						.getAsDouble();
				m = new MessageLocation(latitude, longitude, userId);
			}
		}
		return m;
	}

	private static JsonObject parse(String result) {
		return (JsonObject) new JsonParser().parse(result);
	}

}