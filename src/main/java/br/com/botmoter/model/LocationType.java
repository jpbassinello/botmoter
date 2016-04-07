package br.com.botmoter.model;

import se.walkercrou.places.Types;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public enum LocationType {

	RESTAURANTS(Types.TYPE_RESTAURANT, "4d4b7105d754a06374d81259"),
	CAFES(Types.TYPE_CAFE,
			"4bf58dd8d48988d128941735,4bf58dd8d48988d1e0931735,4bf58dd8d48988d16d941735"),
	BARS(Types.TYPE_BAR, "4d4b7105d754a06376d81259");

	private final String googleMapsApiType;
	// https://developer.foursquare.com/categorytree
	private final String foursquareCategoryIds;

	LocationType(String googleMapsApiType, String foursquareCategoryIds) {
		this.googleMapsApiType = googleMapsApiType;
		this.foursquareCategoryIds = foursquareCategoryIds;
	}

	public String getGoogleMapsApiType() {
		return googleMapsApiType;
	}

	public String getFoursquareCategoryIds() {
		return foursquareCategoryIds;
	}
}
