package rmi_example;

import java.io.Serializable;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int ID;
	private String name;
	private String password;

	public int getId() {
		return this.ID;
	}

	public void setID(int id) {
		this.ID = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String passwd) {
		this.password = passwd;
	}

}
