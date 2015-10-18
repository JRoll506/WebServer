package com.rbruno.webserver.page;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import com.rbruno.webserver.Request;
import com.rbruno.webserver.Response;
import com.rbruno.webserver.Server;
import com.rbruno.webserver.logger.WebLogger;

public class Page {

	private String name;

	public static ArrayList<Page> pages = new ArrayList<Page>();

	/**
	 * Pages can be built into the jar by adding them to the pages ArrayList
	 * here.
	 */
	public static void loadPages() {
		pages.add(new Bootstrap());
	}

	/**
	 * Creates a new Page instance.
	 * 
	 * @param name The URL the page can be found at. Should start with a /.
	 */
	public Page(String name) {
		this.name = name;
	}

	/**
	 * This method is called every time the page is called by a client.
	 * 
	 * @param request The clients request.
	 * @param response An response to be filled out by this method.
	 * @throws IOException
	 */
	public void called(Request request, Response response) throws IOException {
	}

	/**
	 * Reads a file and returns it's contents as a string.
	 * 
	 * @param fileName The files path.
	 * @return The files contents.
	 * @throws IOException
	 */
	public String read(String fileName) throws IOException {
		File file = new File(fileName);
		FileReader reader = new FileReader(file);
		char[] text = new char[(int) file.length()];
		reader.read(text, 0, (int) file.length());
		reader.close();
		return new String(text);
	}

	/**
	 * Loads all the classes in a directory that extend
	 * com.rbruno.webserver.Page then returns them in a List.
	 * 
	 * @param directory The directory in which to look for classes.
	 * @return A list of all the classes that extend com.rbruno.webserver.Page
	 *         in the specified directory.
	 */
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
					WebLogger.log("Not a page: " + clazz.getSimpleName());
					continue;
				}
				Page page = (Page) object;
				pages.add(page);
				WebLogger.log("Loaded page: " + page.getClass().getSimpleName() + " \"" + page.getName() + "\"");
			} catch (Exception ex) {
				WebLogger.log("Error loading '" + name + "' page!");
				ex.printStackTrace();
			} catch (Error ex) {
				WebLogger.log("Error loading '" + name + "' page!");
				ex.printStackTrace();
			}
		}
		try {
			loader.close();
		} catch (IOException e) {
			WebLogger.log("Error closing class loader.");
		}
		return pages;
	}

	/**
	 * Loads a the class that extends com.rbruno.webserver.Page at the location
	 * specified.
	 * 
	 * @param file The location where the Page is.
	 * @return An instance of the Page.
	 */
	public static Page load(File file) {
		Page page = null;
		if (!file.exists()) {
			return null;
		}

		URLClassLoader loader;
		try {
			File folder = new File(file.getPath().substring(0, file.getPath().lastIndexOf("\\")));
			loader = new URLClassLoader(new URL[] { folder.toURI().toURL() }, Page.class.getClassLoader());
		} catch (MalformedURLException ex) {
			return null;
		}
		try {
			String name = file.getName().substring(0, file.getName().lastIndexOf("."));
			Class<?> clazz = loader.loadClass(name);
			Object object = clazz.newInstance();

			if (!(object instanceof Page)) {
				WebLogger.log("Not a page: " + clazz.getSimpleName());
				try {
					loader.close();
				} catch (IOException e) {
					WebLogger.log("Error closing class loader.");
				}
				return null;
			}
			page = (Page) object;
		} catch (Exception e) {
			WebLogger.log("Error loading '" + file.getName() + "'!");
			e.printStackTrace();
		}
		try {
			loader.close();
		} catch (IOException e) {
			WebLogger.log("Error closing class loader.");
		}
		return page;
	}

	/**
	 * Returns an instance of the Server.
	 * 
	 * @return An instance of the Server.
	 */
	public Server getServer() {
		return Server.getServer();
	}

	/**
	 * Returns the name of the page.
	 * 
	 * @return The name of the page.
	 */
	public String getName() {
		return name;
	}

}
