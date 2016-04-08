package br.com.botmoter.model;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public final class PlaceTip {
	private final GooglePlace googlePlace;
	private final FoursquarePlace foursquarePlace;

	public PlaceTip(GooglePlace googlePlace, FoursquarePlace foursquarePlace) {
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
