import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VentanaEnigma extends JFrame {

	public JTextField mensajeOriginal = new JTextField(20);
	public JTextField codificacion[] = new JTextField[3];
	public JTextField mensajeCifrado = new JTextField(20);
	public JTextField errors = new JTextField(50);
	Boolean error = false;

	public VentanaEnigma() {

		super("Descifrando Enigma");
		setSize(900, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GridLayout gl = new GridLayout(5, 1);
		gl.setHgap(1);
		gl.setVgap(1);

		JPanel panelMensajeOriginal = new JPanel();
		panelMensajeOriginal.setLayout(new FlowLayout());
		JPanel panelCodificacion = new JPanel();
		panelCodificacion.setLayout(new FlowLayout());
		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new FlowLayout());
		JPanel panelMensajeFinal = new JPanel();
		panelMensajeFinal.setLayout(new FlowLayout());

		JLabel etiquetaMensajeOriginal = new JLabel("Escribe el mensaje que quieras cifrar: ");
		JLabel etiquetaCodificacion = new JLabel("Escribe que codificacion quieres usar:");
		JLabel etiquetaMensajeFinal = new JLabel("Mensaje cifrado: ");

		panelMensajeFinal.add(etiquetaMensajeFinal);
		panelCodificacion.add(etiquetaCodificacion);
		panelMensajeOriginal.add(etiquetaMensajeOriginal);

		panelMensajeOriginal.add(mensajeOriginal);

		for (int i = 0; i < codificacion.length; i++) {
			codificacion[i] = new JTextField(4);
			panelCodificacion.add(codificacion[i]);
		}

		panelMensajeFinal.add(mensajeCifrado);
		mensajeCifrado.setEditable(false);

		JButton botonCifrar = new JButton("Botó per iniciar la codificació");
		JButton botonDescifrar = new JButton("Botó per iniciar la descodificació");
		panelBotones.add(botonCifrar);
		panelBotones.add(botonDescifrar);
		botonCifrar.addActionListener(new EventBotoXifrar());
		botonDescifrar.addActionListener(new EventBotoDesxifrar());

		errors.setEditable(false);
		JPanel panelErrors = new JPanel();
		panelErrors.add(errors);
		panelErrors.setLayout(new FlowLayout());

		Container cp = getContentPane();
		cp.setLayout(gl);
		cp.add(panelMensajeOriginal);
		cp.add(panelCodificacion);
		cp.add(panelBotones);
		cp.add(panelMensajeFinal);
		cp.add(panelErrors);

	}

	class EventBotoXifrar implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			error = false;
			String text = mensajeOriginal.getText();
			char[] arrayChars = text.toCharArray();
			char[] caractersCanviats = new char[arrayChars.length];
			int[] codificador = new int[3];
			int pos = 0, lletra1 = 0;
			codificador(codificador);
			comprovarCaracters(arrayChars);
			if (!error) {
				Character.getNumericValue(arrayChars[1]);
				for (int i = 0; i < arrayChars.length; i++) {

					if (pos == 3) {
						pos = 0;
					}
					lletra1 = (int) arrayChars[i];
					int resultat = lletra1 + codificador[pos];
					if (resultat > 90) {
						resultat = resultat - 90;
						lletra1 = 64 + resultat;
					} else {
						lletra1 = lletra1 + codificador[pos];
					}
					pos++;
					caractersCanviats[i] = (char) lletra1;
				}
				String textFinal = new String(caractersCanviats);
				mensajeCifrado.setText(textFinal);
			}
		}
	}

	class EventBotoDesxifrar implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			error = false;
			String text = mensajeOriginal.getText();
			char[] arrayChars = text.toCharArray();
			char[] caractersCanviats = new char[arrayChars.length];
			int pos = 0, lletra1 = 0;
			int[] codificador = new int[3];
			codificador(codificador);
			comprovarCaracters(arrayChars);
			if (!error) {
				Character.getNumericValue(arrayChars[1]);
				for (int i = 0; i < arrayChars.length; i++) {

					if (pos == 3) {
						pos = 0;
					}
					lletra1 = (int) arrayChars[i];
					int resultat = lletra1 - codificador[pos];
					if (resultat < 65) {
						resultat = 65 - resultat;
						lletra1 = 91 - resultat;
					} else {
						lletra1 = lletra1 - codificador[pos];
					}
					pos++;
					caractersCanviats[i] = (char) lletra1;
				}
				String textFinal = new String(caractersCanviats);
				mensajeCifrado.setText(textFinal);
			}
		}
	}

	public void codificador(int[] codificador) {
		for (int i = 0; i < codificacion.length; i++) {
			if (codificacion[i].getText().isEmpty()) {
				errors.setText("Error: Els camps dels desplaçaments han de estar plens");
				error = true;
			} else {
				if (Integer.parseInt(codificacion[i].getText()) >= 0
						&& Integer.parseInt(codificacion[i].getText()) <= 25) {
					codificador[i] = Integer.parseInt(codificacion[i].getText());
				} else {
					errors.setText("Error: Els camps dels desplaçaments han de estar entre 0 i 25");
					error = true;
				}
			}
		}

	}

	public void comprovarCaracters(char[] array) {

		Pattern pattern = Pattern.compile("[A-Z]");
		String comprovarMajuscules = new String(array);
		if (mensajeOriginal.getText().isEmpty()) {
			errors.setText("Error: El text d'entrada no pot estar buit");
			error = true;
		} else {
			for (int i = 0; i < array.length; i++) {
				Matcher matcher = pattern.matcher(comprovarMajuscules);
				boolean matchFound = matcher.find();

				if (!matchFound) {
					errors.setText("Error: El text d'entrada a d'estar en majúscules");
					error = true;
				}
				if (array[i] == ' ') {
					errors.setText("Error: El text d'entrada no pot contindre espais en blanc");
					error = true;
				}
				if (array[i] == 'Ñ') {
					array[i] = 'N';
				}
				if (array[i] == 'Ç') {
					array[i] = 'C';
				}
			}
		}
	}

}
