package view;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class glowneOkno extends JFrame{
	private String name;
	
	private oknoLogowania logowanie;
	private oknoStartowe start;
	private oknoLista lista;
	private oknoZwrotu zwrot;
	

	
	public glowneOkno() {
		super("Baza danych - Sklep");
		setSize(400, 300);
		setResizable(true);
		setLocation(450, 200);
		
		logowanie = new oknoLogowania();
		start = new oknoStartowe();
		lista = new oknoLista();
		zwrot = new oknoZwrotu();
	}
	public void oknoLogowania(boolean badMsg) {
		logowanie.show(badMsg);
		add(logowanie);
	}
	public void oknoStartowe(boolean manager) {
		start.show(manager, name);
		add(start);
	}
	public void oknoZwrotu() {
		add(zwrot);
	}
	public void setName(String nameUser) {
		name = nameUser;
	}
	public void oknoLista(String tytul, String tab[], String buttonText) {
		lista.show(name, tytul, tab, buttonText);
		add(lista);
	}
	public static void showZwrot()
	{


	}
}