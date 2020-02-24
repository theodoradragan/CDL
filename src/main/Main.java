package main;
import functionalities.Searcher;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Main {
	
	private static void createWindow(String folderName) {
			
		Searcher searcher = new Searcher(folderName);	  
				  
		//Create and set up the window.
		JFrame frame = new JFrame("Searcher");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		frame.setPreferredSize(new Dimension(400, 300));
		
		// Thsis textArea will be filled with the query
		final JTextArea textArea = new JTextArea();
		
		// Set the contents of the JTextArea.
		String text = "open && !(source) && (security || C)";
		textArea.setText(text);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		JLabel textLabel = new JLabel("Result is: ", SwingConstants.CENTER);
		    
		  
		JButton button = new JButton("Search");
		button.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
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
		  
		c.ipady = 40;
		c.weightx = 0.5;
		c.weighty = 0.0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0,0,0,0);
		c.gridx = 0;
		c.gridy = 0;
		frame.getContentPane().add(textArea, c);
		  
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		frame.getContentPane().add(button, c);
		
		c.weightx = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.PAGE_END;
		frame.getContentPane().add(textLabel, c);
		  
		//Display the window.
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);
}

public static void main(String[] args) {
	if(args.length < 1) {
		System.out.println("Usage: java <program_name> <folder_name>");
			return;
		}
		createWindow(args[0]);
	}
}
