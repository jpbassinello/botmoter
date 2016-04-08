package br.com.botmoter.model;

import com.google.common.collect.Range;
import org.joda.time.LocalTime;
import se.walkercrou.places.Types;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static br.com.botmoter.util.Constants.*;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public enum LocationType {

	RESTAURANTS(Types.TYPE_RESTAURANT, "food",
			Arrays.asList(Range.closedOpen(TIME_11_00, TIME_15_00),
					Range.closedOpen(TIME_18_00, TIME_23_00))),
	COFFEE(Types.TYPE_CAFE, "coffee", Arrays.asList(Range.closedOpen(TIME_5_00, TIME_11_00),
			Range.closedOpen(TIME_15_00, TIME_18_00))),
	BARS(Types.TYPE_BAR, "drinks", Arrays.asList(Range.closedOpen(TIME_21_00, TIME_23_59),
			Range.closedOpen(TIME_00_00, TIME_5_00)));

	private final String googleMapsApiType;
	private final String foursquareSection;
	private final List<Range<LocalTime>> rangesOfTimes;

	LocationType(String googleMapsApiType, String foursquareSection,
			List<Range<LocalTime>> rangesOfTimes) {
		this.googleMapsApiType = googleMapsApiType;
		this.foursquareSection = foursquareSection;
		this.rangesOfTimes = rangesOfTimes;
	}

	public static Set<LocationType> getTypesByTimeInADay(LocalTime localTime) {
		final Set<LocationType> locationTypes = new HashSet<>();
		for (LocationType locationType : values()) {
			for (Range<LocalTime> ranges : locationType.rangesOfTimes) {
				if (ranges.contains(localTime)) {
					locationTypes.add(locationType);
				}
			}
		}
		return locationTypes;
	}

	public String getGoogleMapsApiType() {
		return googleMapsApiType;
	}

	public String getFoursquareSection() {
		return foursquareSection;
	}
}
