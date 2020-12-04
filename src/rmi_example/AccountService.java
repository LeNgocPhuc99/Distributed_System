package rmi_example;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AccountService extends UnicastRemoteObject implements IAccount {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccountService() throws RemoteException {

	}

	public User getUser() {
		User u = new User();
		u.setID(23);
		u.setName("ASURA");
		u.setPassword("141099");
		return u;
	}

}
