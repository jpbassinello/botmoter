package br.com.botmoter.service;

import br.com.botmoter.model.FoursquarePlace;
import br.com.botmoter.model.LocationType;
import br.com.botmoter.util.ApiClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class FoursquareApiService {
	private static final String CLIENT_ID = "BQFNRJBNZM0X3M4E3R1QXADISDUTUILEMGDC4YJ4ONLVZRSW";
	private static final String CLIENT_SECRET = "TYA1BG4AKRN30SNQMBTUGPRXWJNGFOAUF1AGEINJ5XCD5LQG";
	private static final double DEFAULT_RADIUS = 5000D;
	private static final String VERSION = "20140806";
	private static final int LIMIT = 50;
	private static final String LOCALE = "pt";

	public List<FoursquarePlace> searchPlaces(double latitude, double longitude,
			LocationType locationType, int page) {

		final Map<String, String> params = buildDefaultParamsMap();
		params.put("ll", String.valueOf(latitude) + "," + String.valueOf(longitude));
		params.put("radius", String.valueOf(DEFAULT_RADIUS));
		params.put("section", locationType.getFoursquareSection());
		params.put("limit", String.valueOf(LIMIT));
		params.put("offset", String.valueOf(page * LIMIT));

		final String response = ApiClient.build("https://api.foursquare.com/v2/venues/explore")
				.withParameters(params).get().call();

		try {
			JSONObject jsonObject = new JSONObject(response);
			final JSONArray groups = jsonObject.getJSONObject("response").getJSONArray("groups");

			final ObjectMapper objectMapper = new ObjectMapper();
			final List<FoursquarePlace> places = new ArrayList<>();
			for (int i = 0; i < groups.length(); i++) {
				final JSONObject group = groups.getJSONObject(i);
				final JSONArray items = group.getJSONArray("items");
				for (int j = 0; j < items.length(); j++) {
					final String venue = items.getJSONObject(j).getJSONObject("venue").toString();
					places.add(objectMapper.readValue(venue, FoursquarePlace.class));
				}
			}
			return places;
		} catch (Exception e) {
			throw new IllegalStateException("Unexpected JSON return", e);
		}
	}

	private Map<String, String> buildDefaultParamsMap() {
		final Map<String, String> params = new HashMap<>();
		params.put("client_id", CLIENT_ID);
		params.put("client_secret", CLIENT_SECRET);
		params.put("v", VERSION);
		params.put("locale", LOCALE);
		return params;
	}

	public FoursquarePlace searchFullPlace(String id) {

		final Map<String, String> params = buildDefaultParamsMap();

		final String response = ApiClient.build("https://api.foursquare.com/v2/venues/" + id)
				.withParameters(params).get().call();

		try {
			JSONObject jsonObject = new JSONObject(response);
			final String venue = jsonObject.getJSONObject("response").getJSONObject("venue")
					.toString();
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(venue, new TypeReference<FoursquarePlace>() {
			});
		} catch (Exception e) {
			throw new IllegalStateException("Unexpected JSON return", e);
		}
	}

}