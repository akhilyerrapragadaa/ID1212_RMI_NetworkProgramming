package Client;

import static Common.Catalogue.CATALOGUE;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import Common.Catalogue;

public class Main {

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		  Registry registry = LocateRegistry.getRegistry(8082); 
		  Catalogue stub = (Catalogue) registry.lookup(CATALOGUE); 
		  new ClientInput().start(stub);
		InputCommands arr[] = InputCommands.values();
		for(InputCommands com: arr) {
         System.out.println(com);
	}     
}
}
