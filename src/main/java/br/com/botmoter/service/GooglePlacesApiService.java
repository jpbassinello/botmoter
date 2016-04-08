package br.com.botmoter.service;

import br.com.botmoter.model.GooglePlace;
import br.com.botmoter.model.LocationType;
import br.com.botmoter.util.ApiClient;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import org.apache.http.client.methods.HttpPost;
import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Param;
import se.walkercrou.places.Place;
import se.walkercrou.places.RequestHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class GooglePlacesApiService {

	private static final String GOOGLE_KEY = "AIzaSyBU9anoKp46N76GcBh9tzN_y5u0eYABuFo";
	private static final double DEFAULT_RADIUS = 5000D;
	private static final int DEFAULT_NUMBER_OF_RESULTS = 60;

	public List<GooglePlace> getPlaces(final double latitude, final double longitude,
			LocationType locationType) {
		GooglePlaces client = createClient();

		final Param[] params = {new Param("rankBy").value("distance"), new Param("types").value(
				locationType.getGoogleMapsApiType())};
		final List<Place> nearbyPlaces = client
				.getNearbyPlaces(latitude, longitude, DEFAULT_RADIUS, DEFAULT_NUMBER_OF_RESULTS,
						params);
		final ImmutableList<GooglePlace> places = FluentIterable.from(nearbyPlaces)
				.transform(new Function<Place, GooglePlace>() {
					@Override
					public GooglePlace apply(Place input) {
						return new GooglePlace(input, latitude, longitude, GOOGLE_KEY);
					}
				}).toSortedList(Ordering.natural());
		return places;
	}

	public List<GooglePlace> getPlaces(String address, LocationType locationType) {
		GooglePlaces client = createClient();
		final List<Place> placesByQuery = client
				.getPlacesByQuery(address, DEFAULT_NUMBER_OF_RESULTS);
		if (placesByQuery == null || placesByQuery.isEmpty()) {
			return Collections.emptyList();
		}
		// by the default, the address is a single place
		Place place = placesByQuery.get(0);
		return getPlaces(place.getLatitude(), place.getLongitude(), locationType);
	}

	private GooglePlaces createClient() {
		return new GooglePlaces(GOOGLE_KEY, new CustomRequestHandler());
	}

	/**
	 * Needs to reimplement the handler to work if Google App Engine
	 */
	public final class CustomRequestHandler implements RequestHandler {

		public static final String CHARSET = "UTF-8";

		@Override
		public String getCharacterEncoding() {
			return CHARSET;
		}

		@Override
		public void setCharacterEncoding(String s) {
			throw new UnsupportedOperationException(
					"setCharacterEncoding not supported on this CustomRequestHandler");
		}

		@Override
		public InputStream getInputStream(String s) throws IOException {
			throw new UnsupportedOperationException(
					"getInputStream not supported on this CustomRequestHandler");
		}

		@Override
		public String get(String s) throws IOException {
			return ApiClient.build(s).get().call();
		}

		@Override
		public String post(HttpPost httpPost) throws IOException {
			throw new UnsupportedOperationException(
					"post not supported on this CustomRequestHandler");
		}
	}

}