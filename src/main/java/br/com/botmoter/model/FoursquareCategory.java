package br.com.botmoter.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FoursquareCategory {
	private String id;
	private String name;

	public FoursquareCategory() {
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
}
