package com.rbruno.webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import com.rbruno.webserver.config.Config;
import com.rbruno.webserver.logger.Logger;
import com.rbruno.webserver.page.Page;
import com.rbruno.webserver.page.PageManager;

public class Server implements Runnable {

	private static Server server;
	private int port;
	private ServerSocket socket;
	private Thread run;
	private PageManager pageManager;
	
	private Config config;
	private boolean running = true;
	
	public Server(String config) throws Exception {
		this.config = new Config(config);
		this.port = this.config.getPort();
		socket = new ServerSocket(port);
		this.pageManager = new PageManager();
		Logger.log("Started Server on port: " + port);
		
		run = new Thread(this, "WebServer");
		run.start();
		
	}

	public void run() {
		while (running ) {
			try {
				Socket clientSocket = socket.accept();
				//Logger.log("Web Socket created " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
				Thread client = new Thread(new WebClient(clientSocket), "WebClient");
				client.start();
			} catch (Exception e) {
				if (e.getMessage().contains("closed")) return;
				e.printStackTrace();
			} 
		}
	}

	public void process(Socket clientSocket, Request request) throws IOException {
		
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
				response.sendFile(new File("404.html"));
				return;
			} catch (IOException e) {
				response.setResponse("HTTP/1.1 404 UNFOUND");
				response.sendFile(new File("404.html"));
				return;
			}
		}
		
		try {
			response.sendFile(new File("www/" + request.getPage()));
			return;
		} catch (FileNotFoundException e) {
			response.setResponse("HTTP/1.1 404 UNFOUND");
			response.sendFile(new File("404.html"));
			return;
		} catch (IOException e) {
			response.setResponse("HTTP/1.1 404 UNFOUND");
			response.sendFile(new File("404.html"));
			return;
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
			server = new Server("config.txt");
			Scanner scanner = new Scanner(System.in);
			while (true) {
				String input = scanner.next();
				if (input.equals("reload")) {
					Logger.log("Reloading pages!");
					server.reloadPages();
				} if (input.equals("quit")){
					server.stop();
					scanner.close();
					return;
				} else {
					Logger.log("Unknown command!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void stop() throws Exception {
		running = false;
		socket.close();
		run.join();
		
	}

	public static Server getServer() {
		return server;
	}
	
	public PageManager getPageManager() {
		return pageManager;
	}
	
	public void reloadPages() {
		pageManager = new PageManager();
	}
			
	
}
