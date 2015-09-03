package com.rbruno.webserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;

import com.rbruno.webserver.command.Command;
import com.rbruno.webserver.config.Config;
import com.rbruno.webserver.logger.WebLogger;
import com.rbruno.webserver.page.Page;

public class Server implements Runnable {

	private static Server server;
	private ServerSocket socket;
	private Thread run;

	private Config config;
	private boolean running = true;

	public Server(String config) throws Exception {
		try {
			this.config = new Config(config);
		} catch (Exception e) { 
			WebLogger.log("An error has occured while reading the config", Level.SEVERE);
			throw e;
		}
		Page.loadPages();

		socket = new ServerSocket(this.config.getPort());
		WebLogger.log("Started Server on port: " + this.config.getPort());

		run = new Thread(this, "WebServer");
		run.start();

	}

	public void run() {
		while (running) {
			try {
				Socket clientSocket = socket.accept();
				Thread client = new Thread(new WebClient(clientSocket), "WebClient");
				client.start();
			} catch (Exception e) {
				if (!running)
					return;
				e.printStackTrace();
			}
		}
	}

	public void process(Socket clientSocket, Request request) throws IOException {

		Response response = new Response(clientSocket);

		for (Page page : Page.pages) {
			if (page.getName().equals(request.getPage())) {
				page.called(request, response);
				return;
			}
		}

		if (request.getPage().equals("/")) {
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

	public static void main(String[] args) {
		try {
			server = new Server("config.txt");
			Scanner scanner = new Scanner(System.in);
			while (true) {
				String input = scanner.next();
				if (input.equals("quit")) {
					server.stop();
					scanner.close();
					return;
				}
				for (Command command : Command.commands) {
					if (command.getName().equalsIgnoreCase(input)) {
						command.called();
						continue;
					}
					WebLogger.log("Unknown command.");
				}
			}
		} catch (Exception e) {
			WebLogger.log(e.getMessage(), Level.SEVERE);
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

	public void reloadPages() {
		Page.loadPages();
	}

}
