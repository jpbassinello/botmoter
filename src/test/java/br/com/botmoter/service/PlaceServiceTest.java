package br.com.botmoter.service;

import br.com.botmoter.model.LocationType;
import br.com.botmoter.model.Place;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class PlaceServiceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(PlaceServiceTest.class);

	@Test
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

	private void printInSysOut(List<Place> places) {
		LOGGER.info(places.stream().map(Place::toString).collect(Collectors.joining("\n\n")));
	}

}