package com.rbruno.webserver.bootstrap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;

public class Start {

	//private static final String DEFAULT_CONFIG_FILE = "F:\\Files\\workspace\\WebServer\\config.txt";
	private static final String DEFAULT_PORT_STRING = "80";
	private Properties properties;

	public Start() throws FileNotFoundException, IOException {
		this.properties = getPropertys(getBootstrapConfFile());
	}

	private static void printUsage() {
		System.out.println("Usage:");
		System.out.println("");
		System.out.println("java WebServer.jar <command>");
		System.out.println("");
		System.out.println("Commands:");
		System.out.println("");
		System.out.println("Start: ");
		System.out.println("Stop: ");
		System.out.println("Restart: ");
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			printUsage();
			return;
		}

		final String command = args[0];

		switch (command.toLowerCase()) {
		case "start":
		case "stop":
		case "restart":
			break;
		default:
			printUsage();
			return;
		}

		final Start bootstrap = new Start();

		switch (command.toLowerCase()) {
		case "start":
			bootstrap.start();
			break;
		case "stop":
			bootstrap.stop();
			break;
		case "restart":
			bootstrap.stop();
			bootstrap.start();
			break;
		}

	}

	private void start() throws Exception {
		//TODO Fix
		Runtime.getRuntime().exec("java -jar WebServer.jar");
	}

	private void stop() throws Exception {
		Socket socket = new Socket("127.0.0.1", getPort());
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		out.println("GET /bootstrap");
		String post = "method=stop&key=" + properties.getProperty("key");
		out.println("Content-Length: " + post.length());
		out.println();
		out.println(post);
		socket.close();
	}

	private int getPort() {
		String portString = properties.getProperty("port");
		if (portString == null) portString = DEFAULT_PORT_STRING;
		return Integer.parseInt(portString);
	}

	private Properties getPropertys(File config) throws FileNotFoundException, IOException {
		Properties properties = new Properties();
		properties.load(new FileReader(config));
		return properties;

	}

	private static File getBootstrapConfFile() {
		/*
		 * if (configFilename == null) { configFilename = DEFAULT_CONFIG_FILE; }
		 */

		File home = new File("").getParentFile();
		File configFile = new File(home, "config.txt");
		return configFile;
	}
}
