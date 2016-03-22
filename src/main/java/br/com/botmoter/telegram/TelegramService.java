package br.com.botmoter.telegram;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class TelegramService {

	public void sendPhoto(long chatId,
			String photoUrl) throws UnsupportedEncodingException {
		TelegramClient client = new TelegramClient().withEndpoint
				("/sendPhoto").withGetParameters("?chat_id=" + chatId +
				"&photo=" + URLEncoder.encode(photoUrl, "UTF-8"));

		client.call();
	}

	public void sendLocation(long chatId, double latitude, double longitude) {
		TelegramClient client = new TelegramClient().withEndpoint
				("/sendLocation").withGetParameters("?chat_id=" + chatId +
				"&latitude=" + latitude + "&longitude=" + longitude);

		client.call();
	}

	public void sendResponse(long chatId, String text) throws IOException {
		TelegramClient client = new TelegramClient().withEndpoint
				("/sendmessage").withGetParameters("?chat_id=" + chatId +
				"&text=" + (text == null ? "null" : URLEncoder.encode(text,
				"UTF-8")));

		client.call();
	}

	public String getUpdates(Integer offset) {
		TelegramClient client = new TelegramClient().withEndpoint
				("/getupdates");
		if (offset != null) {
			client = client.withGetParameters("?offset=" + offset);
		}

		String response = client.getSingleResult(String.class);
		return response;
	}

}