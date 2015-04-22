package com.rbruno.license.manager.server.webui;

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
	private String contentType;

	public Response(Socket socket) {
		this.socket = socket;
	}

	public void sendFile(File file) throws IOException {
		if (!file.exists()) throw new FileNotFoundException();
		OutputStream outputStream = socket.getOutputStream();
		PrintWriter out = new PrintWriter(outputStream);
		out.println(response);
		for (String line : header) {
			out.println(line);
		}
		out.println("Content-type: " + contentType);
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
		out.println("Content-type: " + contentType);
		out.println();
		out.print(body);
		out.flush();
		out.close();
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
