package com.rbruno.webserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;

public class Response {

	private Socket socket;

	private String response = "HTTP/1.1 200 OK";
	private ArrayList<String> header = new ArrayList<String>();
	private String body = "";
	private String contentType = "text/html";
	private String charset = "utf-8"; 

	public Response(Socket socket) {
		this.socket = socket;
	}

	public void sendFile(File file) throws IOException {
		contentType = getContentType(file.getName());
		if (!file.exists()) throw new FileNotFoundException();
		OutputStream outputStream = socket.getOutputStream();
		PrintWriter out = new PrintWriter(outputStream);
		out.println(response);
		for (String line : header) {
			out.println(line);
		}
		out.println("Content-type: " + contentType);
		out.println("Content-length: " + file.length());
		out.println();
		Files.copy(file.toPath(), outputStream);
		outputStream.flush();
		outputStream.close();
	}

	public void send() throws IOException {
		PrintWriter out = new PrintWriter(socket.getOutputStream());
		out.println(response);
		for (String line : header) {
			out.println(line);
		}
		out.println("Content-type: " + contentType + ";charset=" + charset);
		out.println("Content-length: " + body.length());
		out.println();
		out.print(body);
		out.flush();
		out.close();
	}
	
	public String getContentType(String page) {
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

	public void addToHeader(String string) {
		header.add(string);
	}

	public void addToBody(String string) {
		body = body + string;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Socket getSocket() {
		return socket;
	}
}
