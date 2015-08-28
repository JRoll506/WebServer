package com.rbruno.webserver.page;

import java.util.ArrayList;

import com.rbruno.webserver.page.pages.ExamplePage;

public class PageManager {

	public ArrayList<Page> pages = new ArrayList<Page>();

	public PageManager() {
		this.pages.add(new ExamplePage());
		
		for (Page page : PageLoader.load("www/")){
			this.pages.add(page);
		}
	}
}
