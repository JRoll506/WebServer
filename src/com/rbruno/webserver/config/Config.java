package com.rbruno.webserver.config;

import java.io.File;
import java.io.FileReader;

import org.json.JSONObject;

import com.rbruno.webserver.logger.Logger;

public class Config {

	File file;

	int port;

	public Config(String path) throws Exception {
		file = new File(path);
		read();
	}

	public Config(File file) throws Exception {
		this.file = file;
		read();
	}

	public void read() throws Exception {
		FileReader reader = new FileReader(file);
		char[] block = new char[(int) file.length()];
		reader.read(block, 0, (int) file.length());
		reader.close();
		JSONObject json = new JSONObject(new String(block));
		
		port = checkPort(json);
		Logger.log("Loaded config successfully.");
	}
	
	public int checkPort(JSONObject json) throws ConfigException {
		int port = json.getInt("port");
		if (port > 1 && port < 65535) {
			throw new ConfigException("Port in config out side of range.");
		}
		return port;
	}

	public int getPort() {
		return port;
	}

}
