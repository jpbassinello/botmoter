package br.com.botmoter.telegram;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.Encoded;
import javax.ws.rs.HttpMethod;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@Encoded
public final class TelegramClient {

	private static final String BASE_URL = "https://api.telegram.org/";
	private static final int TIMEOUT = 10000;
	private final String botToken;
	private String endpoint;
	private String HTTP_METHOD = HttpMethod.GET;
	private String getParameters;

	TelegramClient(String botToken) {
		this.botToken = botToken;
	}

	TelegramClient withEndpoint(String endpoint) {
		this.endpoint = endpoint;
		return this;
	}

	TelegramClient withGetParameters(String parameters) {
		this.getParameters = parameters;
		return this;
	}

	<T> T getSingleResult(Class<T> returnType) {
		ClientResponse response = makeRequest();
		if (returnType == null) {
			return null;
		}
		T readEntity = response.getEntity(returnType);
		reset();
		return readEntity;
	}

	void call() {
		getSingleResult(Object.class);
	}

	private void reset() {
		this.endpoint = null;
		this.HTTP_METHOD = null;
	}

	private ClientResponse makeRequest() {
		String url = BASE_URL + botToken + endpoint;
		if (getParameters != null) {
			url += getParameters;
		}
		WebResource webResource = buildClient().resource(url);
		return webResource.method(HTTP_METHOD, ClientResponse.class);
	}

	private Client buildClient() {
		final DefaultClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getClasses().add(JacksonJsonProvider.class);
		Client client = Client.create(clientConfig);
		client.setConnectTimeout(TIMEOUT);
		client.setReadTimeout(TIMEOUT);

		return client;
	}

}