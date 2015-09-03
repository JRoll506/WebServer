package com.rbruno.webserver.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebLogger {

	public static Logger logger = Logger.getLogger("WebServer");

	public static void log(String string, Level level) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm:ss ");
		String formattedDate = sdf.format(date);
		String message = formattedDate + "[" + level.toString() + "] " + string;
		System.out.println(message);
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)));
			out.println(message);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void log(String string) {
		log(string, Level.INFO);
	}

	public static void log(String string, Socket socket) {
		log("[" + socket.getInetAddress().getHostName() + ":" + socket.getPort() + "] " + string);
	}

}
