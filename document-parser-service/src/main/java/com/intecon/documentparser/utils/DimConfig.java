package com.intecon.documentparser.utils;

import java.io.IOException;
import java.util.Properties;


public class DimConfig {

	private Properties props = new Properties();

	private static DimConfig instance = null;

	public static DimConfig getInstance() {
		if (instance == null)
			DimConfig.instance = new DimConfig();
		return instance;
	}

	private DimConfig() {
		// if (Global.GLOBALCONFPATH != null) {
		try {
			props.load(getClass().getResourceAsStream("../../../dim.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// }
	}

	public String getPropertyValue(String key) {
		return props.getProperty(key);
	}

}
