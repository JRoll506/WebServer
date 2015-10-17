package com.rbruno.webserver;

import java.util.ArrayList;
import java.util.HashMap;

public class Request {

	private String header;
	private String page;
	private HashMap<String, String> get;

	private ArrayList<String> message;
	private HashMap<String, String> post;

	/**
	 * Creates a new Request instance. This will phrase the clients request into
	 * easy fields.
	 * 
	 * @param message A list of the HTTP header split by lines.
	 * @param post The post data.
	 * @throws Exception
	 */
	public Request(ArrayList<String> message, String post) throws Exception {
		this.message = message;
		this.post = phrase(post);

		this.header = message.get(0);
		this.page = header.split(" ")[1];
		if (page.contains("?")) {
			this.get = phrase(page.split("\\?")[1]);
			this.page = page.split("\\?")[0];

		}
	}

	private HashMap<String, String> phrase(String args) {
		HashMap<String, String> map = new HashMap<String, String>();
		for (String string : args.split("&")) {
			if (string.split("=").length >= 2) {
				map.put(string.split("=")[0], string.split("=")[1]);
			}
		}
		return map;
	}

	/**
	 * Return the post data.
	 * 
	 * @return The post data.
	 */
	public HashMap<String, String> getPost() {
		return post;
	}

	/**
	 * Return the list of the HTTP header split by lines.
	 * 
	 * @return The list of the HTTP header split by lines.
	 */
	public ArrayList<String> getMessage() {
		return message;
	}

	/**
	 * Returns the header.
	 * 
	 * @return The first line of the request usually starting with GET or POST.
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * Returns the page the user is requesting.
	 * 
	 * @return The page the user is requesting.
	 */
	public String getPage() {
		return page;
	}

	/**
	 * Return the get data.
	 * 
	 * @return The get data.
	 */
	public HashMap<String, String> getGet() {
		return get;
	}
}
