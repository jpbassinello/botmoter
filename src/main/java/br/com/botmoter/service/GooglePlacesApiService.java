package br.com.botmoter.service;

import br.com.botmoter.model.LocationType;
import br.com.botmoter.model.Place;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Param;

import java.util.Collections;
import java.util.List;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class GooglePlacesApiService {

	private static final String GOOGLE_KEY = "AIzaSyBU9anoKp46N76GcBh9tzN_y5u0eYABuFo";
	private static final double DEFAULT_RADIUS = 5000D;
	private static final int DEFAULT_NUMBER_OF_RESULTS = 10;

	public List<Place> getPlaces(final double latitude, final double longitude,
			LocationType locationType) {
		GooglePlaces client = new GooglePlaces(GOOGLE_KEY);

		final Param[] params = {new Param("rankBy").value("distance"), new Param("types").value(
				locationType.getGoogleMapsApiType())};
		final List<se.walkercrou.places.Place> nearbyPlaces = client
				.getNearbyPlaces(latitude, longitude, DEFAULT_RADIUS, DEFAULT_NUMBER_OF_RESULTS,
						params);
		final ImmutableList<Place> places = FluentIterable.from(nearbyPlaces)
				.transform(new Function<se.walkercrou.places.Place, Place>() {
					@Override
					public Place apply(se.walkercrou.places.Place input) {
						return new Place(input, latitude, longitude, GOOGLE_KEY);
					}
				}).toSortedList(Ordering.natural());
		return places;
	}

	public List<Place> getPlaces(String address, LocationType locationType) {
		GooglePlaces client = new GooglePlaces(GOOGLE_KEY);
		final List<se.walkercrou.places.Place> placesByQuery = client
				.getPlacesByQuery(address, DEFAULT_NUMBER_OF_RESULTS);
		if (placesByQuery == null || placesByQuery.isEmpty()) {
			return Collections.emptyList();
		}
		// by the default, the address is a single place
		se.walkercrou.places.Place place = placesByQuery.get(0);
		return getPlaces(place.getLatitude(), place.getLongitude(), locationType);
	}

}