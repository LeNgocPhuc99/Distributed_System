package rmi_example;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAccount extends Remote {
	public User getUser() throws RemoteException;
}
