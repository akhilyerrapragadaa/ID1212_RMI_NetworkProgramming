package Client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import Common.Catalogue;

import Common.InfoText;



public class ClientInput implements Runnable{
	
	 private final String PROMPT = ">>";
	 private boolean receivingInputs = false;
	 private final Scanner console = new Scanner(System.in);
	 private Catalogue catalogue;
	 private  InputCommands command;
	 private NotificationServer outputHandler;
	 String UserName =null;
	 InfoTextOut infoTextOut= new InfoTextOut();
	// public String user;
	 String[] arr;
	 String fileIn;
	 
	  public void start(Catalogue catalogue) throws RemoteException {
	        this.catalogue = catalogue;
	        this.outputHandler = new NotificationServer();
	        if (receivingInputs) {
	            return;
	        }
	        receivingInputs = true;
	        new Thread(this).start();
	    }
	  
	  public void run() {
		  while(receivingInputs) {
			  command = command.valueOf(console.nextLine());   
			  if (UserName == null && !command.equals(InputCommands.REGISTER) &&
                      !command.equals(InputCommands.LOGIN) &&
                      !command.equals(InputCommands.QUIT)
                      ) {
				  infoTextOut.print("Login to use the file catalogue.\n");
                  continue;
              }
			  
			  switch (command) {
			  
			  case QUIT:
				  receivingInputs = false;
				  infoTextOut.print("Session ended!");
                  break;
              
			  case LOGOUT:
				  infoTextOut.println("Goodbye " + UserName);
                  try {
					catalogue.logout(UserName);
					 this.UserName = null;
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NullPointerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                  break;
                  
			  case REGISTER:
				  parse();
                  try {
				catalogue.registerUser(arr[0], arr[1]);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					infoTextOut.println(arr[0] + " already exists! Please choose another name");
					break;
					//e.printStackTrace();
				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                  infoTextOut.println(arr[0] + " has successfully been registered!");
                  break;
                  
			  case LOGIN:
				  parse();
                  try {
				UserName = catalogue.loginUser(arr[0], arr[1], this.outputHandler);
				
                  } catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					 infoTextOut.println("Invalid Credentials! Please retype your username and password");
					break;
					
				}
                  infoTextOut.println("Successfully logged in as: " + UserName);
                  break;             
			  
			  case UPLOAD:
				  parse();
				  try {
					catalogue.uploadFiles(UserName, arr[0]);
					catalogue.notifyAll(UserName);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					 infoTextOut.println("Filename exists! Please choose a new name!");
						break;
					//e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					infoTextOut.println("Cannot find the specified file!");
					break;
					//e.printStackTrace();
				} catch (NoSuchFileException e) {
					// TODO Auto-generated catch block
					infoTextOut.println("Cannot find the specified file!");
					break;
					//e.printStackTrace();
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  infoTextOut.println(UserName + " Uploaded " + arr[0]);
                  break;             
			  
			  case VIEWALLFILES:
				String op;
				try {
					op = catalogue.accessFiles();
					infoTextOut.println(op);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			    break;
			  
			  case VIEWMYFILES:
				String opf;
				try {
					opf =catalogue.accessMyFiles(UserName);
					infoTextOut.println(opf);    
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
				
			  break;
			  
			  case DOWNLOAD:
				  parse();
				  try {
					catalogue.downloadFiles(arr[0]);
					infoTextOut.println("File downloaded to resources"); 
					final String download = "DOWNLOAD";
					catalogue.notifyFile(UserName,arr[0],this.outputHandler, download);
				} catch (NoSuchFileException e) {
					// TODO Auto-generated catch block
					infoTextOut.println("Cannot find the specified file!");
					break;
					//e.printStackTrace();
				}catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  break;
				  
			  case READFILE:
				  parse();
				  try {
				byte[] filerF =catalogue.getFile(arr[0]);
				infoTextOut.println(new String(filerF));
				 final String read = "READ";
				catalogue.notifyFile(UserName,arr[0],this.outputHandler, read);
				break;
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFileException e) {
					// TODO Auto-generated catch block
					infoTextOut.println("Cannot find the specified file!");
					break;
					//e.printStackTrace();
				}catch (NullPointerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  
				  break;
			   
			  case UPDATEFILE:
				  parse();
				  try {
					catalogue.writeFile(arr[0],arr[1]);
					infoTextOut.println("File Updated Successfully!");
					final String update = "UPDATE";
					catalogue.notifyFile(UserName,arr[0],this.outputHandler, update);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFileException e) {
					// TODO Auto-generated catch block
					infoTextOut.println("Cannot find the specified file!");
					break;
					//e.printStackTrace();
				}catch (NullPointerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  break;
				  
				  
			  }
			  
	
		  }
	  }
	  

	  private String[] parse() {
		 String regIn  =  console.nextLine();
		arr = regIn.split(" ");
		return arr;
	  }
	 
	  
	  private class NotificationServer extends UnicastRemoteObject implements InfoText {
	        NotificationServer() throws RemoteException {
	        }

	       // @Override
	        public void infoout(String info)throws RemoteException {
	  		  System.out.print(info);
	  		  //parse();
	  	  }
	    }
	
	  
	/*  public void outputMessage(String message) {
		  
	  }*/
	  
	  
	  
	  
	
}
