package br.com.botmoter.service;

import br.com.botmoter.hackathon.bot.model.TipoLocal;
import br.com.botmoter.model.Place;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class PlaceServiceTest {

	@Test
	public void testGetPlaces() {
		final PlaceService placeService = new PlaceService();

		// teste1: bares by lat long
		final double latitude = -22.893990D;
		final double longitude = -47.047745D;

		System.out.println("================= teste1: bares by lat long");
		printInSysOut(placeService.getPlaces(latitude, longitude, TipoLocal
				.BARES));

		// teste2: restaurants by address
		String address = "Rua Adelino Martins, 500, Campinas - SP";
		System.out.println("\n\n================= teste2: restaurants by " +
				"address");
		printInSysOut(placeService.getPlaces(address, TipoLocal.RESTAURANTES));
	}

	private void printInSysOut(List<Place> places) {
		System.out.print(places.stream().map(Place::toString).collect
				(Collectors.joining("\n\n")));
	}

}