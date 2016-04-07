package br.com.botmoter.service;

import br.com.botmoter.model.FoursquarePlace;
import br.com.botmoter.model.LocationType;
import br.com.botmoter.util.ApiClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

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
	private static final String LIMIT = "50";

	public List<FoursquarePlace> searchPlaces(double latitude, double longitude,
			LocationType locationType) {

		final Map<String, String> params = buildDefaultParamsMap();
		params.put("ll", String.valueOf(latitude) + "," + String.valueOf(longitude));
		params.put("radius", String.valueOf(DEFAULT_RADIUS));
		params.put("categoryId", locationType.getFoursquareCategoryIds());

		final String response = ApiClient.build("https://api.foursquare.com/v2/venues/search")
				.withParameters(params).get().call();

		try {
			JSONObject jsonObject = new JSONObject(response);
			final String venues = jsonObject.getJSONObject("response").getJSONArray("venues")
					.toString();
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(venues, new TypeReference<List<FoursquarePlace>>() {
			});
		} catch (Exception e) {
			throw new IllegalStateException("Unexpected JSON return", e);
		}
	}

	private Map<String, String> buildDefaultParamsMap() {
		final Map<String, String> params = new HashMap<>();
		params.put("client_id", CLIENT_ID);
		params.put("client_secret", CLIENT_SECRET);
		params.put("v", VERSION);
		params.put("limit", LIMIT);
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