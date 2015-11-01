package com.rbruno.webserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.rbruno.webserver.crypto.PasswordHash;

public class PasswordManager {

	public static File DATA_FILE = new File("data/users.txt");

	public PasswordManager() throws IOException {
		if (!DATA_FILE.exists()) {
			DATA_FILE.createNewFile();
		}
	}

	/**
	 * Sets a users password. Can be used if the user is in the data base or
	 * not.
	 * 
	 * @param user The users name.
	 * @param password What you wish the user's password to be.
	 * @throws Exception
	 */
	public void setPassword(String user, String password) throws Exception {
		Properties database = getDataBase();
		database.setProperty(user, PasswordHash.createHash(password));
		save(database);
	}

	/**
	 * Checks the password against the one in the database and returns true if
	 * matches.
	 * 
	 * @param user The user in who you wish to check.
	 * @param password The password to check.
	 * @return True if the user's password matches the one given.
	 * @throws Exception
	 */
	public boolean checkPassword(String user, String password) throws Exception {
		return PasswordHash.validatePassword(password, getDataBase().getProperty("user"));
	}

	/**
	 * Returns true if the user is in the database.
	 * @param user The user you wish to check.
	 * @return True if the user is in the database.
	 * @throws Exception
	 */
	public boolean isUser(String user) throws Exception {
		return getDataBase().containsKey(user);
	}

	private void save(Properties database) throws Exception {
		database.store(new FileOutputStream(DATA_FILE), "RBruno WebServer's user database");
	}

	private Properties getDataBase() throws FileNotFoundException, IOException {
		Properties database = new Properties();
		database.load(new FileReader(DATA_FILE));
		return database;
	}

}
