package com.rbruno.webserver.command;

import com.rbruno.webserver.Server;
import com.rbruno.webserver.logger.WebLogger;

public class Reload extends Command {

	public Reload() {
		super("reload");
	}
	
	@Override
	public void called() {
		WebLogger.log("Reloading pages!");
		Server.getServer().reloadPages();
	}

}
