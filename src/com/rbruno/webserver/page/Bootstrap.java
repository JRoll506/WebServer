package com.rbruno.webserver.page;

import java.io.File;
import java.io.IOException;

import com.rbruno.webserver.Request;
import com.rbruno.webserver.Response;

public class Bootstrap extends Page {

	public Bootstrap() {
		super("/bootstrap");
	}
	
	@Override
	public void called(Request request, Response response) throws IOException {
		if (request.getPost().containsKey("method") && request.getPost().containsKey("key")) {
			if (request.getPost().get("key").equals(this.getServer().getConfig().getKey() + "")) {
				switch (request.getPost().get("method")) {
				case "stop":
					try {
						this.getServer().stop();
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
		response.sendFile(new File("404.html"));
	}

}
