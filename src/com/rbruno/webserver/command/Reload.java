package com.rbruno.webserver.command;

import com.rbruno.webserver.Server;
import com.rbruno.webserver.logger.Logger;

public class Reload extends Command {

	public Reload() {
		super("reload");
	}
	
	@Override
	public void called() {
		Logger.log("Reloading pages!");
		Server.getServer().reloadPages();
	}

}
