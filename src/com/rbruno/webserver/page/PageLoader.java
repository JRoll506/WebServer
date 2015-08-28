package com.rbruno.webserver.page;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import com.rbruno.webserver.logger.Logger;

public class PageLoader {

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

}
