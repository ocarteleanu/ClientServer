package client_server;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;
/**
 * This class creates an GUI client type that will connect to a GUI server type
 * to send and receive data
 * @author octavian
 *
 */
public class Client extends Application {
	  DataOutputStream dataToServer = null;
	  DataInputStream dataFromServer = null;
	  TextField numberInput;
	  Label labelForInput;
	  TextArea responseArea;
	  private static String serverAnswer = "";
	  protected static int numberToCheck;
	@Override
	/**
	 * This method will create the elements displayed inside the GUI and connect 
	 * to the server GUI
	 */
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Pane topPane = new Pane();
		Pane bottomPane = new Pane();
		VBox mainBox = new VBox();
		
		numberInput = new TextField();		
		numberInput.setPrefSize(150.0, 30.0);
		numberInput.setLayoutX(320);
		numberInput.setLayoutY(10);		
		numberInput.setFont(Font.font("verdana", 25));
		numberInput.setEditable(true);
		numberInput.requestFocus();
		labelForInput = new Label();
		labelForInput.setPrefSize(300.0, 30.0);
		labelForInput.setLayoutX(10);
		labelForInput.setLayoutY(10);
		labelForInput.setFont(Font.font("verdana", 15));
		labelForInput.setLabelFor(numberInput);
		labelForInput.setText("Enter a number to check if it's prime");
		responseArea = new TextArea();
		responseArea.setPrefSize(500, 325);
		responseArea.setFont(Font.font("verdana", 15));
		responseArea.setEditable(false);
		topPane.setStyle("-fx-background-color: #" + "ebdab4;");
		topPane.setPrefSize(500, 75);
		topPane.getChildren().addAll(numberInput, labelForInput);
		bottomPane.setStyle("-fx-background-color: #" + "cce7e8;");
		bottomPane.setPrefSize(500, 325);
		bottomPane.getChildren().add(responseArea);
		mainBox.getChildren().addAll(topPane, bottomPane);
		Scene mainScene = new Scene(mainBox, 500, 400);
		primaryStage.setScene(mainScene);
		primaryStage.setTitle("Is a prime number?");
		primaryStage.show();
		
		numberInput.setOnAction(e -> {
		      try {
		        // Get the number from the text field
		    	  if(Integer.parseInt(numberInput.getText().trim()) % 1 == 0) {
		    		  numberToCheck = Integer.parseInt(numberInput.getText().trim());
			        // Send the radius to the server
			        dataToServer.writeInt(numberToCheck);
			        dataToServer.flush();
		    	  }
		    	  else {		    		
		    		  numberToCheck = - 1;
				      dataToServer.writeInt(numberToCheck);
				      dataToServer.flush();
		    	  }
		  

		  
		        // Get area from the server
		        boolean primeOrNot = dataFromServer.readBoolean();
		        
	            if(primeOrNot)
	            	serverAnswer = "prime";
	            else 
	            	serverAnswer = "not prime";
		  
		        // Display to the text area
		        responseArea.appendText("The number is " + numberToCheck + "\n");
		        responseArea.appendText(numberToCheck + " is " + serverAnswer + '\n');
		      }
		      catch (IOException ex) {
		        System.out.println("You did not enter an integer");
		      }
		      catch (NumberFormatException nfe) {
		    	System.out.println("You did not enter an integer");
	    		  numberToCheck = - 1;
			      try {
					dataToServer.writeInt(numberToCheck);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			      try {
					dataToServer.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		      }
		    });
		
	    try {
	        // Create a socket to connect to the server
	        Socket socket = new Socket("localhost", 8000);
	        
	        // Create an input stream to receive data from the server
	        dataFromServer = new DataInputStream(socket.getInputStream());

	        // Create an output stream to send data to the server
	        dataToServer = new DataOutputStream(socket.getOutputStream());
	      }
	      catch (IOException ex) {
	        responseArea.appendText(ex.toString() + '\n');
	      }
	}
	/**
	 * This is the main method of the Client class that will launch
	 * the client GUI
	 * @param args String array used to execute the application
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);		
	}

}
