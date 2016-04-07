package br.com.botmoter.telegram;

import br.com.botmoter.util.ApiClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
	private static final String BOT_TOKEN = "bot157015122:AAELqgI6HOOzmKQxuyZYu6cDX3hFnKnUmNc";

	public void sendPhoto(long chatId, String photoUrl) throws UnsupportedEncodingException {
//		TelegramClient client = new TelegramClient(BOT_TOKEN).withEndpoint("/sendPhoto")
//				.withGetParameters("?chat_id=" +
//						chatId +
//						"&photo=" + URLEncoder.encode(photoUrl, "UTF-8"));
//
//		client.call();
	}

	public void sendLocation(long chatId, double latitude, double longitude) {
//		TelegramClient client = new TelegramClient(BOT_TOKEN).withEndpoint("/sendLocation")
//				.withGetParameters("?chat_id=" +
//						chatId +
//						"&latitude=" + latitude + "&longitude=" + longitude);
//
//		client.call();
	}

	public void sendResponse(long chatId, String text) throws IOException {
//		TelegramClient client = new TelegramClient(BOT_TOKEN).withEndpoint("/sendmessage")
//				.withGetParameters("?chat_id=" +
//						chatId +
//						"&text=" + URLEncoder.encode(text, "UTF-8"));
//
//		client.call();
		LOGGER.info("Calling Telegram sendmessage");
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("chat_id", String.valueOf(chatId));
		parameters.put("text", text);
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