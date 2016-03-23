package br.com.botmoter.web.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@Component
public class Properties {

	@Value("${app.env}")
	private String appEnv;

	public boolean isProduction() {
		return "Production".equals(appEnv);
	}

	public String getAppEnv() {
		return appEnv;
	}
}
