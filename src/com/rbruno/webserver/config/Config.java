package com.rbruno.webserver.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class Config {

	File file;

	int port;

	public Config(String path) throws IOException, JSONException {
		file = new File(path);
		read();
	}

	public Config(File file) throws IOException, JSONException {
		this.file = file;
		read();
	}

	public void read() throws IOException, JSONException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String contents = "";
		String line;
		while ((line = reader.readLine()) != null) {
			contents += line;
		}
		JSONObject json = new JSONObject(contents);

		port = json.getInt("port");

		reader.close();
	}

	public int getPort() {
		return port;
	}

}
