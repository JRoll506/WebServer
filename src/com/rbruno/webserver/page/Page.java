package com.rbruno.webserver.page;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.rbruno.webserver.Request;
import com.rbruno.webserver.Response;
import com.rbruno.webserver.Server;
import com.rbruno.webserver.logger.Logger;

public class Page {

	private String name;
	private String file;
	
	public static ArrayList<Page> pages = new ArrayList<Page>();

	public static void loadPages() {		
		for (Page page : load("www/")){
			pages.clear();
			pages.add(page);
		}
	}

	public Page(String name) {
		this.name = name;
	}

	public void called(Request request, Response response) throws IOException {
	}

	public String read(String fileName) throws IOException {
		File file = new File(fileName);
		FileReader reader = new FileReader(file);
		char[] text = new char[(int) file.length()];
		reader.read(text, 0, (int) file.length());
		reader.close();
		return new String(text);
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
	
	public static List<Page> load(String directory) {
		List<Page> pages = new ArrayList<Page>();
		File dir = new File(directory);
		if (!dir.exists()) {
			return pages;
		}

		URLClassLoader loader;

		try {
			loader = new URLClassLoader(new URL[] { dir.toURI().toURL() }, Page.class.getClassLoader());
		} catch (MalformedURLException ex) {
			return pages;
		}
		for (File file : dir.listFiles()) {
			if (!file.getName().endsWith(".class")) {
				continue;
			}
			String name = file.getName().substring(0, file.getName().lastIndexOf("."));

			try {
				Class<?> clazz = loader.loadClass(name);
				Object object = clazz.newInstance();
				if (!(object instanceof Page)) {
					Logger.log("Not a page: " + clazz.getSimpleName());
					continue;
				}
				Page page = (Page) object;
				pages.add(page);
				Logger.log("Loaded page: " + page.getClass().getSimpleName() + " \"" + page.getName() + "\"");
			} catch (Exception ex) {
				Logger.log("Error loading '" + name + "' page!");
				ex.printStackTrace();
			} catch (Error ex) {
				Logger.log("Error loading '" + name + "' page!");
				ex.printStackTrace();
			}
		}
		try {
			loader.close();
		} catch (IOException e) {
			Logger.log("Error closing class loader.");
		}
		return pages;
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
