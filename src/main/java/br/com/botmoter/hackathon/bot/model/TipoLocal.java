package br.com.botmoter.hackathon.bot.model;

import se.walkercrou.places.Types;

/**
 * Created by luan on 3/19/16.
 */
public enum TipoLocal {
	RESTAURANTES(Types.TYPE_RESTAURANT),
	CAFES(Types.TYPE_CAFE),
	BARES(Types.TYPE_BAR);

	private final String apiType;

	TipoLocal(String apiType) {
		this.apiType = apiType;
	}

	public String getApiType() {
		return apiType;
	}
}
