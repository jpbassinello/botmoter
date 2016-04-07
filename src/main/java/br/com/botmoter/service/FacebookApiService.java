package br.com.botmoter.service;

import br.com.botmoter.model.GooglePlace;
import com.google.common.base.Strings;
import com.restfb.*;
import org.joda.time.LocalDateTime;

import java.util.List;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class FacebookApiService {

	private static final Version VERSION = Version.VERSION_2_5;
	private static final String CLIENT_ID = "223366094687572";
	private static final String APP_SECRET = "8209ca0ac39bc38181eef9aadba57f5a";
	private static final int MINUTES_TO_CONSIDER_FACEBOOK_ACCESS_TOKEN = 1;
	private static volatile FacebookClient.AccessToken accessTokenForWeb;

	public List<GooglePlace> searchPlaces(double latitude, double longitude) {
		FacebookClient publicOnlyFacebookClient = getNewFacebookClient();

		Parameter[] parameters = new Parameter[5];
		parameters[0] = Parameter.with("type", "place");
		parameters[1] = Parameter.with("limit", "30");
		parameters[2] = Parameter
				.with("center", String.valueOf(latitude) + "," + String.valueOf(longitude));
		parameters[3] = Parameter.with("distance", "5000");
		parameters[4] = Parameter.with("fields", "id, name, location");
		Connection<GooglePlace> search = publicOnlyFacebookClient
				.fetchConnection("/search", GooglePlace.class, parameters);


		return search.getData();
	}

	private synchronized FacebookClient getNewFacebookClient() {
		FacebookClient facebookClient = new DefaultFacebookClient(VERSION);
		if (accessTokenForWeb == null) {
			accessTokenForWeb = facebookClient.obtainAppAccessToken(CLIENT_ID, APP_SECRET);
		} else {
			if (accessTokenForWeb.getExpires() != null && LocalDateTime.now()
					.plusMinutes(MINUTES_TO_CONSIDER_FACEBOOK_ACCESS_TOKEN)
					.isAfter(LocalDateTime.fromDateFields(accessTokenForWeb.getExpires()))) {
				accessTokenForWeb = facebookClient.obtainExtendedAccessToken(CLIENT_ID, APP_SECRET,
						accessTokenForWeb.getAccessToken());
			}
		}
		if (accessTokenForWeb == null || Strings
				.isNullOrEmpty(accessTokenForWeb.getAccessToken())) {
			throw new IllegalStateException("Invalid Access Token to facebook services.");
		}
		return new DefaultFacebookClient(accessTokenForWeb.getAccessToken(), VERSION);
	}
}