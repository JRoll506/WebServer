package com.rbruno.webserver.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

	public static void log(String string){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd H:mm:ss]");
		String formattedDate = sdf.format(date);
		System.out.println(formattedDate + " " + string);
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)));
		    out.println(formattedDate + ": " + string);
		    out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void log(String string, Socket socket){
		log("[" + socket.getInetAddress().getHostName() + ":" + socket.getPort() + "] " + string);
	}
	
}
