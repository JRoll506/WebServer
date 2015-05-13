package com.rbruno.license.manager.server.webui.page;

import java.util.ArrayList;

import com.rbruno.license.manager.server.webui.page.pages.ExamplePage;

public class PageManager {

	public ArrayList<Page> pages = new ArrayList<Page>();

	public PageManager() {
		this.pages.add(new ExamplePage());
	}
}
