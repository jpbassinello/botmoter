package br.com.botmoter.telegram;

import br.com.botmoter.util.ApiClient;
import br.com.botmoter.util.FormFile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@Component
public class TelegramService {

	private static final Logger LOGGER = Logger.getLogger(TelegramService.class.getName());

	private static final String BASE_URL = "https://api.telegram.org/";
	// WhereabOt: 208812869:AAEVUVet1bhappvadvUa6JliK61RAum6Gcw
	private static final String BOT_TOKEN = "bot157015122:AAELqgI6HOOzmKQxuyZYu6cDX3hFnKnUmNc";

	public void sendPhoto(long chatId, byte[] photoBytes, String caption) {
		LOGGER.info("Calling Telegram sendPhoto");

		final Map<String, String> parameters = new HashMap<>();
		parameters.put("chat_id", String.valueOf(chatId));
		parameters.put("caption", caption);

		final Map<String, FormFile> files = new HashMap<>();
		files.put("photo", new FormFile(System.currentTimeMillis() + ".png", photoBytes));

		String response = ApiClient.build(buildTelegramUrl("/sendPhoto")).withParameters
				(parameters)
				.withFiles(files).postMultipartFormData().call();
		LOGGER.info("Success Calling Telegram sendPhoto with response: " + response);
	}

	public void sendLocation(long chatId, double latitude, double longitude) {
		LOGGER.info("Calling Telegram sendLocation");
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("chat_id", String.valueOf(chatId));
		parameters.put("latitude", String.valueOf(latitude));
		parameters.put("longitude", String.valueOf(longitude));
		String response = ApiClient.build(buildTelegramUrl("/sendLocation"))
				.withParameters(parameters).get().call();
		LOGGER.info("Success Calling Telegram sendLocation");
	}

	public void sendResponse(long chatId, String text) throws IOException {
		LOGGER.info("Calling Telegram sendmessage");
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("chat_id", String.valueOf(chatId));
		parameters.put("text", text);
		parameters.put("parse_mode", "HTML");
		String response = ApiClient.build(buildTelegramUrl("/sendmessage"))
				.withParameters(parameters).get().call();
		LOGGER.info("Success Calling Telegram sendmessage");
	}

	public String getUpdates(Integer offset) {
//		TelegramClient client = new TelegramClient(BOT_TOKEN).withEndpoint("/getupdates");
//		if (offset != null) {
//			client = client.withGetParameters("?offset=" + offset);
//		}
//
//		String response = client.getSingleResult(String.class);
//		return response;
		return null;
	}

	public void setWebhook(String url) {
		LOGGER.info("Calling Telegram setWebhook with url: " + url);
		String response = ApiClient.build(buildTelegramUrl("/setWebhook"))
				.withParameters(Collections.singletonMap("url", url)).get().call();
		LOGGER.info("Success Calling Telegram setWebhook with response: " + response);
	}

	private String buildTelegramUrl(String endpoint) {
		return BASE_URL + BOT_TOKEN + endpoint;
	}
}