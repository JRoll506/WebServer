package com.rbruno.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class WebClient implements Runnable {

	private Socket clientSocket;
	private BufferedReader in;
	private PrintWriter out;

	/**
	 * Creates a new WebClient instance. This class once ran will read the
	 * request by the client, pass it into a Request object then sends it to
	 * Server.process().
	 * 
	 * @param clientSocket
	 * @throws IOException
	 */
	public WebClient(Socket clientSocket) throws IOException {
		this.clientSocket = clientSocket;
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		out = new PrintWriter(clientSocket.getOutputStream(), true);
	}

	/**
	 * Reads the request by the client, passes it into a Request object then
	 * sends it to Server.process().
	 */
	@Override
	public void run() {
		ArrayList<String> message = new ArrayList<String>();
		String line;
		String post = "";
		try {
			while ((line = in.readLine()) != null) {
				if (line.length() == 0) {
					for (String current : message) {
						if (current.startsWith("Content-Length")) {
							if (current.split(": ").length >= 2) {
								char[] buffer = new char[Integer.parseInt(current.split(": ")[1])];
								in.read(buffer, 0, Integer.parseInt(current.split(": ")[1]));
								post = new String(buffer);
							}
						}
					}
					break;
				}
				message.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Request request;
		try {
			request = new Request(message, post);
		} catch (Exception e1) {
			return;
		}
		if (message != null) {
			if (message.size() > 0) try {
				Server.getServer().process(clientSocket, request);
			} catch (IOException e) {

			}
		}

		try {
			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
