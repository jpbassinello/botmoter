package br.com.botmoter.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FoursquareLocation {
	private String address;
	private String crossStreet;
	private BigDecimal lat;
	private BigDecimal lng;
	private String cc;
	private String city;
	private String state;
	private String country;
	private List<String> formattedAddress;

	public FoursquareLocation() {
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCrossStreet() {
		return crossStreet;
	}

	public void setCrossStreet(String crossStreet) {
		this.crossStreet = crossStreet;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public BigDecimal getLng() {
		return lng;
	}

	public void setLng(BigDecimal lng) {
		this.lng = lng;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<String> getFormattedAddress() {
		return formattedAddress;
	}

	public void setFormattedAddress(List<String> formattedAddress) {
		this.formattedAddress = formattedAddress;
	}
}
