package com.rbruno.webserver.page;

import java.io.IOException;

import com.rbruno.webserver.Request;
import com.rbruno.webserver.Response;

public class ExamplePage extends Page {

	public ExamplePage() {
		super("/example");
	}

	public void called(Request request, Response response) throws IOException {
		response.addToBody("<h3>" + response.getSocket().getInetAddress().getHostAddress() + ":" +  response.getSocket().getPort() + "</h3>");
		response.send();
	}
	

}
