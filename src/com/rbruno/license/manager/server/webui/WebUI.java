package com.rbruno.license.manager.server.webui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.rbruno.license.manager.server.logger.Logger;

public class WebUI implements Runnable {

	private int port;
	private ServerSocket socket;
	private Thread run;

	public WebUI(int port) throws IOException {
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

				Logger.log("Web Socket created " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
				Thread client = new Thread(new WebClient(clientSocket, this), "WebClient");
				client.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void process(Socket clientSocket, Request request) throws IOException {
		
		Response response = new Response(clientSocket);
		
		if (request.getPage().equals("/")){
			try {
				response.setContentType("text/html");
				response.sendFile(new File("www/index.html"));
				return;
			} catch (FileNotFoundException e) {
				response.addToHeader("HTTP/1.1 404 UNFOUND");
				response.addToBody("<head>\n<title>404 NOT FOUND</title>\n</head>");
				response.send();
				return;
			} catch (IOException e) {
				response.addToHeader("HTTP/1.1 404 UNFOUND");
				response.addToBody("<head>\n<title>404 NOT FOUND</title>\n</head>");
				response.send();
				return;
			}
		}
		try {
			response.setContentType(getContentType(request.getPage()));
			response.sendFile(new File("www/" + request.getPage()));
			return;
		} catch (FileNotFoundException e) {
			response.addToHeader("HTTP/1.1 404 UNFOUND");
			response.addToBody("<head>\n<title>404 NOT FOUND</title>\n</head>");
			response.send();
			return;
		} catch (IOException e) {
			response.addToHeader("HTTP/1.1 404 UNFOUND");
			response.addToBody("<head>\n<title>404 NOT FOUND</title>\n</head>");
			response.send();
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
			new WebUI(1309);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
