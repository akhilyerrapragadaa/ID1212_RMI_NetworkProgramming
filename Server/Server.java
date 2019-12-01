package Server;

import java.rmi.registry.LocateRegistry;

import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import Common.Catalogue;
import Server.ServerDBC;
import static Common.Catalogue.CATALOGUE;

public class Server {

	 public static void main(String args[]) { 
	      try { 
	         // Instantiating the implementation class 
	    	  ServerDBC obj = new ServerDBC(); 
	    
	         // Exporting the object of implementation class (
	      //      here we are exporting the remote object to the stub) 
	    	  Catalogue stub = (Catalogue) UnicastRemoteObject.exportObject(obj, 0);  
	         
	         // Binding the remote object (stub) in the registry 
	         Registry registry = LocateRegistry.createRegistry(8082); 
	         
	         registry.bind(CATALOGUE, stub);  
	         ServerDBC oj = new ServerDBC();
	         oj.JDBCRun();
	         System.err.println("Server ready"); 
	         
	      } catch (Exception e) { 
	         System.err.println("Server exception: " + e.toString()); 
	         e.printStackTrace(); 
	      } 
	   } 

}
