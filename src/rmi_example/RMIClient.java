package rmi_example;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMIClient {

	public static void main(String args[]) {
		try {
			IAccount iAccount = (IAccount) Naming.lookup("rmi://127.0.0.1:6789/RMI");
			System.out.println("Name: " + iAccount.getUser().getName());
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
