package com.rbruno.webserver.page;

import java.util.ArrayList;

public class PageManager {

	public ArrayList<Page> pages = new ArrayList<Page>();

	public PageManager() {		
		for (Page page : PageLoader.load("www/")){
			this.pages.add(page);
		}
	}
}
