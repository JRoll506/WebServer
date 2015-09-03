package com.rbruno.webserver.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

import com.rbruno.webserver.logger.WebLogger;

public class Config {

	private File file;
	private int port;
	private Properties properties;

	public Config(String path) throws Exception {
		file = new File(path);
		read();
		save();
	}

	public Config(File file) throws Exception {
		this.file = file;
		read();
		save();
	}

	public void read() throws Exception {
		Properties properties = new Properties();
		properties.load(new FileReader(file));
		this.properties = properties;
		
		port = checkPort(properties.getProperty("port"));
		WebLogger.log("Loaded config successfully.");
	}
	

	private void save() throws Exception {
		properties.store(new FileWriter(file), "WebServer config file");
	}
	
	public int checkPort(String string) throws Exception {
		int port = Integer.parseInt(string);
		if (port < 1 && port > 65535) {
			throw new ConfigException("Port in config out side of range.");
		}
		return port;
	}

	public int getPort() {
		return port;
	}

}
