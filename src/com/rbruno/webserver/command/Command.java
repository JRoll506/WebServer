package com.rbruno.webserver.command;

import java.util.ArrayList;

public class Command {

	private String name;
	
	public static ArrayList<Command> commands;
	
	public static void load() {
		commands.add(new Reload());
	}

	public Command(String name) {
		this.name = name;
	}
	
	public void called() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
