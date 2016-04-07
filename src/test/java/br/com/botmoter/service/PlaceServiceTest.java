package br.com.botmoter.service;

import br.com.botmoter.model.LocationType;
import br.com.botmoter.model.GooglePlace;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class PlaceServiceTest {

	private static final Logger LOGGER = Logger.getLogger(PlaceServiceTest.class.getName());

	@Test
	@Ignore
	public void testGetPlaces() {
		final GooglePlacesApiService placeService = new GooglePlacesApiService();

		// teste1: bares by lat long
		final double latitude = -22.893990D;
		final double longitude = -47.047745D;

		LOGGER.info("================= teste1: bares by lat long");
		printInSysOut(placeService.getPlaces(latitude, longitude, LocationType.BARS));

		// teste2: restaurants by address
		String address = "Rua Adelino Martins, 500, Campinas - SP";
		LOGGER.info("\n\n================= teste2: restaurants by " + "address");
		printInSysOut(placeService.getPlaces(address, LocationType.RESTAURANTS));
	}

	private void printInSysOut(List<GooglePlace> places) {
		LOGGER.info(Joiner.on("\n\n")
				.join(FluentIterable.from(places).transform(new Function<GooglePlace, String>() {
					@Override
					public String apply(GooglePlace input) {
						return input.toString();
					}
				})));
	}

}