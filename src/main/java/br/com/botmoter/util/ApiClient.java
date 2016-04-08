package br.com.botmoter.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public final class ApiClient {

	private static final String CHARSET = "UTF-8";
	private static final String LINE_FEED = "\r\n";
	private static final int TIMEOUT = 10000;
	private static final String POST = "POST";
	private static final String GET = "GET";
	private final String url;
	private final Map<String, String> parameters = new HashMap<>();
	private final Map<String, FormFile> files = new HashMap<>();
	private String httpMethod;
	private boolean multipartFormData = false;

	private ApiClient(String url) {
		this.url = url;
	}

	public static ApiClient build(String url) {
		return new ApiClient(url);
	}

	public ApiClient withParameters(Map<String, String> parameters) {
		this.parameters.putAll(parameters);
		return this;
	}

	public ApiClient withFiles(Map<String, FormFile> files) {
		this.files.putAll(files);
		return this;
	}

	public ApiClient get() {
		this.httpMethod = GET;
		return this;
	}

	public ApiClient post() {
		this.httpMethod = POST;
		return this;
	}

	public ApiClient postMultipartFormData() {
		this.multipartFormData = true;
		return post();
	}

	public String call() {
		try {
			return doCall();
		} catch (Exception e) {
			throw new IllegalStateException("Error while calling API", e);
		}
	}

	private String doCall() throws IOException {

		String finalUrl = url;
		if (GET.equals(httpMethod)) {
			finalUrl += addGetQueryString();
		}

		final HttpURLConnection connection = buildConnection(finalUrl);

		final String boundary = "===" + System.currentTimeMillis() + "===";
		if (multipartFormData) {
			connection.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + boundary);
		}

		if (POST.equals(httpMethod)) {
			final OutputStream outputStream = connection.getOutputStream();
			final PrintWriter writer = new PrintWriter(
					new OutputStreamWriter(outputStream, CHARSET), true);
			addPostFormFields(writer, boundary);
			addPostFormFiles(writer, outputStream, boundary);
			writer.append(LINE_FEED);
			writer.flush();
			writer.append("--" + boundary + "--");
			writer.append(LINE_FEED);
			writer.close();
		}

		return readResponse(connection);
	}

	private HttpURLConnection buildConnection(final String finalUrl) throws IOException {
		final HttpURLConnection connection = (HttpURLConnection) new URL(finalUrl)
				.openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod(httpMethod);
		connection.setRequestProperty("Connection", "Keep-Alive");
		connection.setRequestProperty("Cache-Control", "no-cache");
		connection.setConnectTimeout(TIMEOUT);
		connection.setReadTimeout(TIMEOUT);
		connection.setRequestProperty("Accept-Charset", CHARSET);
		return connection;
	}

	private String readResponse(HttpURLConnection connection) throws IOException {
		final StringBuilder response = new StringBuilder();
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(connection.getInputStream(), CHARSET));
		String line = null;
		while ((line = reader.readLine()) != null) {
			response.append(line);
		}
		reader.close();
		connection.disconnect();

		int status = connection.getResponseCode();
		if (status == HttpURLConnection.HTTP_OK) {
			return response.toString();
		} else {
			throw new IOException(
					"Unexpected return status (" + status + ") from server. Response: " +
							response);
		}
	}

	private void addPostFormFields(final PrintWriter writer, final String boundary) {
		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			writer.append("--" + boundary);
			writer.append(LINE_FEED);
			writer.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"");
			writer.append(LINE_FEED);
			writer.append("Content-Type: text/plain; charset=" + CHARSET);
			writer.append(LINE_FEED);
			writer.append(LINE_FEED);
			writer.append(entry.getValue());
			writer.append(LINE_FEED);
			writer.flush();
		}
	}

	private void addPostFormFiles(final PrintWriter writer, final OutputStream outputStream,
			final String boundary) throws IOException {
		for (Map.Entry<String, FormFile> entry : files.entrySet()) {
			String paramName = entry.getKey();
			final FormFile formFile = entry.getValue();
			writer.append("--" + boundary);
			writer.append(LINE_FEED);
			writer.append("Content-Disposition: form-data; name=\"" + paramName + "\"; " +
					"filename=\"" +
					formFile.getFileName() + "\"");
			writer.append(LINE_FEED);
//			writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName))
//					.append(LINE_FEED);
			writer.append("Content-Transfer-Encoding: binary");
			writer.append(LINE_FEED);
			writer.append(LINE_FEED);
			writer.flush();
			outputStream.write(formFile.getBytes());
			outputStream.flush();
			writer.append(LINE_FEED);
			writer.flush();
		}
	}

	private String addGetQueryString() throws UnsupportedEncodingException {
		boolean first = true;
		StringBuilder queryString = new StringBuilder();
		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			String sep = first ? "?" : "&";
			queryString.append(sep);
			queryString.append(entry.getKey());
			queryString.append("=");
			queryString.append(URLEncoder.encode(entry.getValue(), CHARSET));

			first = false;
		}
		return queryString.toString();
	}

}