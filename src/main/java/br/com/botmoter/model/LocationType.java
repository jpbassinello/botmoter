package br.com.botmoter.model;

import se.walkercrou.places.Types;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public enum LocationType {

	RESTAURANTS(Types.TYPE_RESTAURANT),
	CAFES(Types.TYPE_CAFE),
	BARS(Types.TYPE_BAR);

	private final String googleMapsApiType;

	LocationType(String googleMapsApiType) {
		this.googleMapsApiType = googleMapsApiType;
	}

	public String getGoogleMapsApiType() {
		return googleMapsApiType;
	}
}
