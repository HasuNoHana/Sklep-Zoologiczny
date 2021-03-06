package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class oknoLogowania extends JPanel implements ActionListener{
	private View view;
	
	private JLabel msg;
	private JLabel nameLabel;
	private JTextField nameField;
	private JLabel surnameLabel;
	private JTextField surnameField;
	private JButton loginButton;
	private GridBagLayout layout;
	private GridBagConstraints pom;
	
	public oknoLogowania(View view) {
	this.view = view;
	layout = new GridBagLayout();
	pom = new GridBagConstraints();
	
	msg = new JLabel("Nie ma takiego pracownika!");
	msg.setForeground(Color.RED);
	
	nameLabel = new JLabel("Imie:");
	surnameLabel = new JLabel("Nazwisko:");
	nameField = new JTextField();
	nameField.setPreferredSize(new Dimension(70, 20));
	surnameField = new JTextField();
	surnameField.setPreferredSize(new Dimension(70, 20));
	loginButton = new JButton("Zaloguj");
	loginButton.addActionListener(this);
	

	setLayout(layout);
	
	pom.gridx = 0;
	pom.gridy = 1;
	add(nameLabel, pom);
	
	pom.gridx = 1;
	add(nameField, pom);
	
	pom.gridx = 0;
	pom.gridy = 2;
	add(surnameLabel, pom);
	
	pom.gridx = 1;
	add(surnameField, pom);
	
	pom.insets = new Insets(20, 0, 0, 0);
	pom.gridx = 0;
	pom.gridy = 3;
	pom.gridwidth = 2;
	add(loginButton, pom);
	
	pom.insets = new Insets(0, 0, 20, 0);
	pom.gridx = 0;
	pom.gridy = 0;
	pom.gridwidth = 2;
	add(msg, pom);
	}
	
	public void show(boolean badMsg) {
		if(badMsg) {
			msg.setVisible(true);
		} else {
			msg.setVisible(false);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JButton button = (JButton) arg0.getSource();
		if(button == loginButton);
		{
		
		String name = nameField.getText();
		String surname = surnameField.getText();
		view.probaLogowania(name, surname);
		}
		//wywo�anie odpowiedniej metody podaj�c jako argumenty "name" i "surname"
	}
	
}
