package br.com.botmoter.web.controller;

import br.com.botmoter.model.FoursquarePlace;
import br.com.botmoter.model.GooglePlace;
import br.com.botmoter.model.LocationType;
import br.com.botmoter.service.FoursquareApiService;
import br.com.botmoter.service.GooglePlacesApiService;
import br.com.botmoter.telegram.TelegramProcessor;
import br.com.botmoter.telegram.model.Update;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.sun.org.apache.xpath.internal.operations.Mult;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@RestController
public class TelegramRestController {

	public static final String UPDATES_REST_PATH = "/aokkow23SSwqQLLzqW/updates";
	private static final Logger LOGGER = Logger.getLogger(TelegramRestController.class.getName());

	@Autowired
	private TelegramProcessor telegramProcessor;

	public static String removeAccents(String original) {
		if (original == null) {
			return null;
		}
		original = Normalizer.normalize(original, Normalizer.Form.NFD);
		original = original.replaceAll("[^\\p{ASCII}]", "");
		return original;
	}

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String abc() {
		return "abc";
	}

	@RequestMapping(path = "/testPlaces", method = RequestMethod.GET)
	public Object testPlaces() {
		final double latitude = -22.893990D;
		final double longitude = -47.047745D;

		final List<GooglePlace> googlePlaces = new GooglePlacesApiService()
				.getPlaces(latitude, longitude, LocationType.BARS);

		final Map<String, Collection<GooglePlace>> googlePlaceByName = Multimaps
				.index(googlePlaces, new Function<GooglePlace, String>() {
					@Override
					public String apply(GooglePlace googlePlace) {
						return removeAccents(googlePlace.getName()).toUpperCase();
					}
				}).asMap();

		final List<FoursquarePlace> foursquarePlaces = new FoursquareApiService()
				.searchPlaces(latitude, longitude, LocationType.BARS);

		final Map<String, Collection<FoursquarePlace>> foursquarePlaceByName = Multimaps
				.index(foursquarePlaces, new Function<FoursquarePlace, String>() {
					@Override
					public String apply(FoursquarePlace foursquarePlace) {
						return removeAccents(foursquarePlace.getName()).toUpperCase();
					}
				}).asMap();

		final List<PlaceMatch> matches = new ArrayList<>();
		for (Map.Entry<String, Collection<GooglePlace>> entry : googlePlaceByName.entrySet()) {
			Collection<FoursquarePlace> fsqPlaces = foursquarePlaceByName.get(entry.getKey());
			if (fsqPlaces != null) {
				final GooglePlace googlePlace = FluentIterable.from(entry.getValue()).first()
						.get();
				final FoursquarePlace foursquarePlace = FluentIterable.from(fsqPlaces).first()
						.get();
				matches.add(new PlaceMatch(googlePlace, foursquarePlace));
			}
		}
		return matches;
	}

	@RequestMapping(path = UPDATES_REST_PATH, method = RequestMethod.POST)
	public Update updates(@RequestBody String messageJson) {
		LOGGER.info("Receiving a update from Telegram {}" + messageJson);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			final Update update = objectMapper.readValue(messageJson, Update.class);
			telegramProcessor.processUpdate(update);
			// even the Telegram bot doesn´t need it, return the object
			// to be validated in Integration Test
			return update;
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Error while reading messsage from Telegram", e);
			throw new IllegalStateException("Error while reading messsage from Telegram", e);
		}
	}

	public final class PlaceMatch {
		private final GooglePlace googlePlace;
		private final FoursquarePlace foursquarePlace;

		public PlaceMatch(GooglePlace googlePlace, FoursquarePlace foursquarePlace) {
			this.googlePlace = googlePlace;
			this.foursquarePlace = foursquarePlace;
		}

		public GooglePlace getGooglePlace() {
			return googlePlace;
		}

		public FoursquarePlace getFoursquarePlace() {
			return foursquarePlace;
		}
	}

}
