package client_server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Server extends Application {	
	protected static int userNumber;
	protected static DataInputStream inputFromUser;
	@Override
	public void start(Stage primaryStage) throws Exception {
	    // Text area for displaying contents
	    TextArea serverResponse = new TextArea();
	    serverResponse.setPrefSize(500, 400);

	    // Create a scene and place it in the stage
	    Scene scene = new Scene(new ScrollPane(serverResponse), 500, 400);
	    primaryStage.setTitle("Server"); // Set the stage title
	    primaryStage.setScene(scene); // Place the scene in the stage
	    primaryStage.show(); // Display the stage
	    
	    new Thread( () -> {
	        try {
	          // Create a server socket
	          ServerSocket serverSocket = new ServerSocket(8000);
	          Platform.runLater(() ->
	            serverResponse.appendText("Server started at " + new Date() + '\n'));
	    
	          // Listen for a connection request
	          Socket socket = serverSocket.accept();
	    
	          // Create data input and output streams
	           inputFromUser = new DataInputStream(
	            socket.getInputStream());
	          DataOutputStream outputToUser = new DataOutputStream(
	            socket.getOutputStream());
	    
	          while (true) {
	            // Receive number from the client	        	  
	          	  try {
	        		  userNumber = inputFromUser.readInt();
	        	  }
	        	  catch(Exception e) {
	        		  System.out.println("You didn't enter a integer");
	        		  userNumber = -1;
	        		  break;
	        	  }
	    
	          	if(userNumber >= 0) {
	            // Compute area
	          		boolean primeOrNot = isPrime(userNumber);         
	          
	            // Send area back to the client
	          		outputToUser.writeBoolean(primeOrNot);
	    
	          		Platform.runLater(() -> {
	              serverResponse.appendText("The number received from client: " 
	                + userNumber + '\n');
	            });
	          }
	          	else {
	          		Platform.runLater(() -> {
	  	              serverResponse.appendText("The number received from client: " 
	  	                + "is not an integer" + '\n');
	  	            });
	          		continue;
	          	}
	          }
	        }
	        catch(IOException ex) {
	          ex.printStackTrace();
	        }
	      }).start();	   
	    }	
	
	public static boolean isPrime(int number){
		boolean isPrime = true;
		for(int divisor = 2; divisor <= number / 2; divisor ++ ){
			if(number % divisor == 0){
				isPrime = false;
				break;
			}
		}		
			return isPrime;				
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch();		
	}

}
