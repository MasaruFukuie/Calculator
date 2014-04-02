package main.java;

import java.io.IOException;
import java.util.Properties;

public class Calcurator {

	private Properties conf = null;

	public Calcurator() throws IOException {

		conf = new Properties();
		conf.load(this.getClass().getResourceAsStream("/main/resources/calculator.properties"));
	}
	
	public String getValue(String key){
		return conf.getProperty(key);
	}

}
