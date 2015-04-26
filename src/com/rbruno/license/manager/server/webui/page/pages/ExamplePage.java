package com.rbruno.license.manager.server.webui.page.pages;

import java.io.IOException;

import com.rbruno.license.manager.server.webui.Request;
import com.rbruno.license.manager.server.webui.Response;
import com.rbruno.license.manager.server.webui.Server;
import com.rbruno.license.manager.server.webui.page.Page;

public class ExamplePage extends Page {

	public ExamplePage(Server WebUi) {
		super("/example", WebUi);
	}

	public void called(Request request, Response response) throws IOException {
		response.setContentType("text/html");
		response.addToBody("<h3>" + response.getSocket().getInetAddress().getHostAddress() + ":" +  response.getSocket().getPort() + "</h3>");
		response.send();
	}
	

}
