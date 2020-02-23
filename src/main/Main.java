package main;
import functionalities.Searcher;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Main {
	
	private static void createWindow() {
		  String folderName = new String("documente_problema");
		  Searcher searcher = new Searcher(folderName);
		  
		  
	      //Create and set up the window.
	      JFrame frame = new JFrame("Searcher");
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      frame.setLayout(null);
	      frame.setPreferredSize(new Dimension(400, 300));
	      
	      final JTextArea textArea = new JTextArea();

			// Set the contents of the JTextArea.
			String text = "lapte && !(ala) && (mere || bala)";
			textArea.setText(text);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			
			JLabel textLabel = new JLabel("Result is: ", SwingConstants.CENTER);
		    
	      
			JButton button = new JButton("Search!");
	        button.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	// Here i should verify the input a little
	            	String query = textArea.getText();
	            	if(query.contentEquals("")) {
	            		textLabel.setText("Please enter valid query.");
	            		return;
	            	}
	            	
	        		// answer will be given back to GUI window
	        		String answer = searcher.mainFunctionality(query);
	        		textLabel.setText("Result is: " + answer);
	            }
	        });	
		
	      // x, y, width, height
	      textArea.setBounds(0,0,200,100);
	      button.setBounds(200, 0, 100, 100);
	      textLabel.setBounds(0, 100, 300, 100);
	      
	      frame.getContentPane().add(textLabel);
	      frame.getContentPane().add(textArea);
	      frame.getContentPane().add(button);
	      
	      //Display the window.
	      frame.setLocationRelativeTo(null);
	      frame.pack();
	      frame.setVisible(true);
	}

	public static void main(String[] args) {	
		createWindow();
	}
}
