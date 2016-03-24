package br.com.botmoter.telegram.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.math.BigDecimal;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {

	@JsonProperty("latitude")
	private BigDecimal latitude;
	@JsonProperty("longitude")
	private BigDecimal longitude;

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "Location{" +
				"latitude=" + latitude +
				", longitude=" + longitude +
				'}';
	}
}
