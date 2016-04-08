package br.com.botmoter.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FoursquarePlace {

	private String id;
	private String name;
	private String url;
	private BigDecimal rating;
	private String shortUrl;
	private FoursquareLocation location;
	private List<FoursquareCategory> categories;

	public FoursquarePlace() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public BigDecimal getRating() {
		return rating;
	}

	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public FoursquareLocation getLocation() {
		return location;
	}

	public void setLocation(FoursquareLocation location) {
		this.location = location;
	}

	public List<FoursquareCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<FoursquareCategory> categories) {
		this.categories = categories;
	}
}
