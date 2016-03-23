package br.com.botmoter.telegram;

import br.com.botmoter.telegram.model.Update;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class TelegramService {

	private final String botToken;

	public TelegramService(String botToken) {
		this.botToken = botToken;
	}

	public static void main(String[] args) {
		String s = "{\"update_id\":344901419, \"message\":{\"message_id\":7," +
				"\"from\":{\"id\":192468144,\"first_name\":\"Lucas\"," +
				"\"last_name\":\"Laurindo dos Santos\"}," +
				"\"chat\":{\"id\":192468144," +
				"\"first_name\":\"Lucas\",\"last_name\":\"Laurindo dos " +
				"Santos\",\"type\":\"private\"}," +
				"\"date\":1458403367,\"location\":{\"longitude\":-47.047745," +
				"\"latitude\":-22.893990}}}";

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			final Update update = objectMapper.readValue(s, Update.class);
			System.out.println(update);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendPhoto(long chatId,
			String photoUrl) throws UnsupportedEncodingException {
		TelegramClient client = new TelegramClient(botToken).withEndpoint
				("/sendPhoto").withGetParameters("?chat_id=" + chatId +
				"&photo=" + URLEncoder.encode(photoUrl, "UTF-8"));

		client.call();
	}

	public void sendLocation(long chatId, double latitude, double longitude) {
		TelegramClient client = new TelegramClient(botToken).withEndpoint
				("/sendLocation").withGetParameters("?chat_id=" + chatId +
				"&latitude=" + latitude + "&longitude=" + longitude);

		client.call();
	}

	public void sendResponse(long chatId, String text) throws IOException {
		TelegramClient client = new TelegramClient(botToken).withEndpoint
				("/sendmessage").withGetParameters("?chat_id=" + chatId +
				"&text=" + (text == null ? "null" : URLEncoder.encode(text,
				"UTF-8")));

		client.call();
	}

	public String getUpdates(Integer offset) {
		TelegramClient client = new TelegramClient(botToken).withEndpoint
				("/getupdates");
		if (offset != null) {
			client = client.withGetParameters("?offset=" + offset);
		}

		String response = client.getSingleResult(String.class);
		return response;
	}

}