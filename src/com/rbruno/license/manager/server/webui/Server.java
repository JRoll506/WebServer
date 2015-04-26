package com.rbruno.license.manager.server.webui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.rbruno.license.manager.server.logger.Logger;
import com.rbruno.license.manager.server.webui.page.Page;
import com.rbruno.license.manager.server.webui.page.PageManager;

public class Server implements Runnable {

	private int port;
	private ServerSocket socket;
	private Thread run;
	
	private ArrayList<Page> pages = new ArrayList<Page>();

	public Server(int port) throws IOException {
		this.port = port;

		socket = new ServerSocket(port);
		Logger.log("Started webUI on port: " + port);
		
		run = new Thread(this, "WebServer");
		run.start();
	}

	public void run() {
		while (true) {
			try {
				Socket clientSocket = socket.accept();

				//Logger.log("Web Socket created " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
				Thread client = new Thread(new WebClient(clientSocket, this), "WebClient");
				client.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void process(Socket clientSocket, Request request) throws IOException {
		
		PageManager pageManager = new PageManager(this);
		
		Response response = new Response(clientSocket);
		
		for (Page page : pageManager.pages) {
			if (page.getName().equals(request.getPage())) {
				page.called(request, response);
				return;
			}
		}
		
		if (request.getPage().equals("/")){
			try {
				response.setContentType("text/html");
				response.sendFile(new File("www/index.html"));
				return;
			} catch (FileNotFoundException e) {
				response.setResponse("HTTP/1.1 404 UNFOUND");
				response.sendFile(new File("www/404.html"));
				return;
			} catch (IOException e) {
				response.setResponse("HTTP/1.1 404 UNFOUND");
				response.sendFile(new File("www/404.html"));
				return;
			}
		}
		
		try {
			response.setContentType(getContentType(request.getPage()));
			response.sendFile(new File("www/" + request.getPage()));
			return;
		} catch (FileNotFoundException e) {
			response.setResponse("HTTP/1.1 404 UNFOUND");
			response.sendFile(new File("www/404.html"));
			return;
		} catch (IOException e) {
			response.setResponse("HTTP/1.1 404 UNFOUND");
			response.sendFile(new File("www/404.html"));
			return;
		}		
	}

	private String getContentType(String page) {
		String extention = page;
		if (page.contains(".")){
			extention = page.split("\\.")[1];
		}
		switch (extention) {
		//Images
		case "jpg":
			return "image/jpeg";
		case "jpeg":
			return "image/jpeg";
		case "png":
			return "image/png";
		case "gif":
			return "image/gif";
		case "bmp":
			return "image/bmp";
		//video
		case "mp4":
			return "video/mp4";		
		//Text
		case "html":
			return "text/html";
		case "txt":
			return "text/plain";
		default:
			return "text/html";
		}
	}

	public int getPort() {
		return port;
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
	
	
	public static void main(String[] args){
		try {
			new Server(1309);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/*public void addPage(Page page){
		pages.add(page);
	}*/

	public ArrayList<Page> getPages() {
		return pages;
	}
}
