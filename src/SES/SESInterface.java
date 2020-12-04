package SES;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface SESInterface extends Remote {
	public void receive(Message m) throws RemoteException;
}
