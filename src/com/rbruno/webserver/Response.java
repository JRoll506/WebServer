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

	/**
	 * Creates a new Response instance.
	 * 
	 * @param socket The socket that the response is going to.
	 */
	public Response(Socket socket) {
		this.socket = socket;
	}

	/**
	 * Sends the file to the client ignoring ant data in the body field.
	 * 
	 * @param file The file you wish to send.
	 * @throws IOException
	 */
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
		out.flush();
		Files.copy(file.toPath(), outputStream);
		outputStream.flush();
		outputStream.close();
	}

	/**
	 * Sends the data inputed to this class to the client.
	 * 
	 * @throws IOException
	 */
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

	private String getContentType(String page) {
		String extention = page;
		if (page.contains(".")) {
			extention = page.split("\\.")[1];
		}
		switch (extention) {
		// Images
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
			// video
		case "mp4":
			return "video/mp4";
			// Text
		case "html":
			return "text/html";
		case "txt":
			return "text/plain";
		default:
			return "text/html";
		}
	}

	/**
	 * Adds a line to the header.
	 * 
	 * @param line Line to add to the header.
	 */
	public void addToHeader(String line) {
		header.add(line);
	}

	/**
	 * Adds a string to the body. Does not add a new line with it.
	 * 
	 * @param string String to add to body.
	 */
	public void addToBody(String string) {
		body = body + string;
	}

	/**
	 * Sets content type.
	 * 
	 * @param contentType The content type of the file being sent.
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * Returns the response code default is HTTP/1.1 200 OK.
	 * 
	 * @return The response code.
	 */
	public String getResponse() {
		return response;
	}

	/**
	 * Sets the response code default is HTTP/1.1 200 OK.
	 * @param response The response code.
	 */
	public void setResponse(String response) {
		this.response = response;
	}

	/**
	 * Returns the socket in which the request will be sent to.
	 * 
	 * @return The socket in which the request will be sent to.
	 */
	public Socket getSocket() {
		return socket;
	}
}
