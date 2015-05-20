package com.rbruno.webserver.page;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.rbruno.webserver.Request;
import com.rbruno.webserver.Response;
import com.rbruno.webserver.Server;

public class Page {

	private String name;
	private String file;

	public Page(String name) {
		this.name = name;
	}

	public void called(Request request, Response response) throws IOException {
	}

	public String read(String fileName) throws FileNotFoundException {
		BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
		String html = "";
		try {
			while (reader.ready()) {
				String line = reader.readLine();
				html = html + line;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return html;
	}

	public HashMap<String, String> phraseGet(String args) {
		HashMap<String, String> map = new HashMap<String, String>();
		for (String string : args.split("\\&")) {
			if (string.split("\\=").length >=2){
				map.put(string.split("\\=")[0], string.split("\\=")[1]);
			}
		}
		return map;
	}

	public Server getServer() {
		return Server.getServer();
	}

	public String getName() {
		return name;
	}

	public String getFile() {
		return file;
	}

}
