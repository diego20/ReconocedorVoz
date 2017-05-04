package reconocedor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class AppGUI {

	private JFrame frame;
	private ReconocedorApp mundo;
	private boolean grabando;
	private JTextField respuestaTextField;
	private JPanel panelResumen;
	private JLabel lblResultado;
	private JButton btnGrabacion;
	private JButton btnIntroducirRespuesta;
	private JButton btnSiguiente;
	private boolean esconderResultado;
	private int preguntaActual;
	private int totalPreguntas;
	private JTextPane txtpnThisIsMy;
	private String currentAnswer;
	private String respuestaDeModelo;
	private int puntaje;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppGUI window = new AppGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AppGUI() {
		puntaje = 0;
		grabando = false;
		esconderResultado = true;
		totalPreguntas = 10;
		preguntaActual = 0;
		respuestaDeModelo = "";
		initialize();
		mundo = new ReconocedorApp();
		updateQuestion();
	}

	public void updateQuestion() {
		String[] preguntas = mundo.getQuestion(preguntaActual);
		txtpnThisIsMy.setText(preguntas[0]);
		currentAnswer = preguntas[1];
	}

	public boolean verificarRespuesta() {
		boolean isValid = false;
		if (!respuestaDeModelo.equals("")) {
			String resp = mapearNumero(respuestaDeModelo);
			isValid = resp.equals(currentAnswer);
		} else if (!respuestaTextField.getText().equals("")) {
			isValid = respuestaTextField.getText().equals(currentAnswer);
		}
		return isValid;
	}

	public String mapearNumero(String respuesta) {
		String number = "-1";
		switch (respuesta) {
		case "Uno":
			number = "1";
			break;
		case "Dos":
			number = "2";
			break;
		case "Tres":
			number = "3";
			break;
		case "Cuatro":
			number = "4";
			break;
		case "Cinco":
			number = "5";
			break;
		case "Seis":
			number = "6";
			break;
		case "Siete":
			number = "7";
			break;
		case "Ocho":
			number = "8";
			break;
		case "Nueve":
			number = "9";
			break;
		case "Diez":
			number = "10";
			break;
		}
		return number;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 750, 560);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panelSuperior = new JPanel();
		panelSuperior.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frame.getContentPane().add(panelSuperior, BorderLayout.NORTH);

		JLabel lblImagen = new JLabel("New label");
		lblImagen.setPreferredSize(new Dimension(750, 150));
		lblImagen.setIcon(new ImageIcon("/Users/diegofarfan/Downloads/futbolEdit.jpeg"));
		panelSuperior.add(lblImagen);

		JPanel panelCentral = new JPanel();
		frame.getContentPane().add(panelCentral, BorderLayout.CENTER);

		JPanel panelBienvenida = new JPanel();
		panelBienvenida.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel panelPregunta = new JPanel();
		panelPregunta.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		SpringLayout sl_panelPregunta = new SpringLayout();
		panelPregunta.setLayout(sl_panelPregunta);

		txtpnThisIsMy = new JTextPane();
		sl_panelPregunta.putConstraint(SpringLayout.NORTH, txtpnThisIsMy, 28, SpringLayout.NORTH, panelPregunta);
		sl_panelPregunta.putConstraint(SpringLayout.WEST, txtpnThisIsMy, 10, SpringLayout.WEST, panelPregunta);
		sl_panelPregunta.putConstraint(SpringLayout.EAST, txtpnThisIsMy, 449, SpringLayout.WEST, panelPregunta);
		txtpnThisIsMy.setEditable(false);
		txtpnThisIsMy.setOpaque(false);
		txtpnThisIsMy.setText(
				"This is my text pane, which apparently has a lot of extra space for super random and hell a long questions");
		panelPregunta.add(txtpnThisIsMy);

		JLabel lblbienvenidoAFutbol = new JLabel("¡Bienvenido a FUTBOL TRIVIA!");
		panelBienvenida.add(lblbienvenidoAFutbol);
		lblbienvenidoAFutbol.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel panelLateral = new JPanel();
		panelLateral.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelLateral.setAlignmentX(Component.LEFT_ALIGNMENT);
		GroupLayout gl_panelCentral = new GroupLayout(panelCentral);
		gl_panelCentral.setHorizontalGroup(gl_panelCentral.createParallelGroup(Alignment.LEADING)
				.addComponent(panelBienvenida, GroupLayout.PREFERRED_SIZE, 750, GroupLayout.PREFERRED_SIZE)
				.addGroup(gl_panelCentral.createSequentialGroup().addContainerGap()
						.addComponent(panelLateral, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(panelPregunta, GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE)
						.addContainerGap()));
		gl_panelCentral.setVerticalGroup(gl_panelCentral.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelCentral.createSequentialGroup()
						.addComponent(panelBienvenida, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addGap(18)
						.addGroup(gl_panelCentral.createParallelGroup(Alignment.LEADING)
								.addComponent(panelPregunta, GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
								.addComponent(panelLateral, GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE))
						.addContainerGap()));
		btnGrabacion = new JButton("Responder por voz");
		sl_panelPregunta.putConstraint(SpringLayout.WEST, btnGrabacion, 47, SpringLayout.WEST, panelPregunta);
		btnGrabacion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		panelPregunta.add(btnGrabacion);

		lblResultado = new JLabel("");
		sl_panelPregunta.putConstraint(SpringLayout.NORTH, lblResultado, 100, SpringLayout.NORTH, panelPregunta);
		sl_panelPregunta.putConstraint(SpringLayout.SOUTH, txtpnThisIsMy, -3, SpringLayout.NORTH, lblResultado);
		sl_panelPregunta.putConstraint(SpringLayout.WEST, lblResultado, 166, SpringLayout.WEST, panelPregunta);
		sl_panelPregunta.putConstraint(SpringLayout.EAST, lblResultado, -167, SpringLayout.EAST, panelPregunta);
		lblResultado.setToolTipText("");
		lblResultado.setHorizontalAlignment(SwingConstants.CENTER);
		lblResultado.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		panelPregunta.add(lblResultado);

		btnIntroducirRespuesta = new JButton("<html><u>Ingresar Manualmente</u></html>");
		sl_panelPregunta.putConstraint(SpringLayout.NORTH, btnGrabacion, -5, SpringLayout.NORTH,
				btnIntroducirRespuesta);
		sl_panelPregunta.putConstraint(SpringLayout.EAST, btnIntroducirRespuesta, -10, SpringLayout.EAST,
				panelPregunta);
		sl_panelPregunta.putConstraint(SpringLayout.SOUTH, btnIntroducirRespuesta, -18, SpringLayout.SOUTH,
				panelPregunta);
		btnIntroducirRespuesta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnIntroducirRespuesta.setForeground(Color.BLUE);
		btnIntroducirRespuesta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblResultado.setVisible(!esconderResultado);
				lblResultado.setText("");
				respuestaDeModelo = "";
				respuestaTextField.setVisible(esconderResultado);
				esconderResultado = !esconderResultado;
			}
		});
		btnIntroducirRespuesta.setBorder(null);
		panelPregunta.add(btnIntroducirRespuesta);

		respuestaTextField = new JTextField();
		sl_panelPregunta.putConstraint(SpringLayout.SOUTH, lblResultado, -43, SpringLayout.NORTH, respuestaTextField);
		sl_panelPregunta.putConstraint(SpringLayout.SOUTH, respuestaTextField, -58, SpringLayout.SOUTH, panelPregunta);
		sl_panelPregunta.putConstraint(SpringLayout.WEST, respuestaTextField, 225, SpringLayout.WEST, panelPregunta);
		sl_panelPregunta.putConstraint(SpringLayout.EAST, respuestaTextField, -225, SpringLayout.EAST, panelPregunta);
		respuestaTextField.setVisible(false);
		respuestaTextField.setHorizontalAlignment(SwingConstants.CENTER);
		panelPregunta.add(respuestaTextField);
		respuestaTextField.setColumns(10);
		btnGrabacion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!grabando) {
					respuestaTextField.setText("");
					respuestaTextField.setVisible(false);
					mundo.iniciarGrabacion();
					btnGrabacion.setText("Detener");
				} else {
					mundo.finish();
					mundo.obtenerMFCC();
					mundo.obtenerResultados();
					respuestaDeModelo = mundo.leerResultado();
					lblResultado.setVisible(true);
					esconderResultado = true;
					lblResultado.setText(respuestaDeModelo);
					btnGrabacion.setText("Responder por voz");
				}
				grabando = !grabando;
			}
		});
		SpringLayout sl_panelLateral = new SpringLayout();
		panelLateral.setLayout(sl_panelLateral);

		JLabel lblPregunta = new JLabel("Pregunta 1 de " + totalPreguntas);
		sl_panelLateral.putConstraint(SpringLayout.NORTH, lblPregunta, 10, SpringLayout.NORTH, panelLateral);
		sl_panelLateral.putConstraint(SpringLayout.WEST, lblPregunta, 10, SpringLayout.WEST, panelLateral);
		sl_panelLateral.putConstraint(SpringLayout.EAST, lblPregunta, 0, SpringLayout.EAST, panelLateral);
		panelLateral.add(lblPregunta);

		JLabel lblTextPuntaje = new JLabel("Puntaje:");
		sl_panelLateral.putConstraint(SpringLayout.NORTH, lblTextPuntaje, 6, SpringLayout.SOUTH, lblPregunta);
		sl_panelLateral.putConstraint(SpringLayout.WEST, lblTextPuntaje, 0, SpringLayout.WEST, lblPregunta);
		panelLateral.add(lblTextPuntaje);

		JLabel labelPuntaje = new JLabel("0");
		sl_panelLateral.putConstraint(SpringLayout.NORTH, labelPuntaje, 0, SpringLayout.NORTH, lblTextPuntaje);
		sl_panelLateral.putConstraint(SpringLayout.WEST, labelPuntaje, 6, SpringLayout.EAST, lblTextPuntaje);
		panelLateral.add(labelPuntaje);
		panelCentral.setLayout(gl_panelCentral);

		JPanel panelInferior = new JPanel();
		panelInferior.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frame.getContentPane().add(panelInferior, BorderLayout.SOUTH);
		GridBagLayout gbl_panelInferior = new GridBagLayout();
		gbl_panelInferior.columnWidths = new int[] { 306, 30, 102, 0 };
		gbl_panelInferior.rowHeights = new int[] { 30, 0 };
		gbl_panelInferior.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panelInferior.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panelInferior.setLayout(gbl_panelInferior);

		btnSiguiente = new JButton("Siguiente");
		btnSiguiente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (preguntaActual + 1 <= totalPreguntas
						&& (!lblResultado.getText().equals("") || !respuestaTextField.getText().equals("")) && !lblResultado.getText().equals("Intenta nuevamente")) {
					boolean respuestaCorrecta = verificarRespuesta();
					if (respuestaCorrecta) {
						labelPuntaje.setText(String.valueOf(puntaje += 10));
					}
					agregarIcono(respuestaCorrecta);
					lblResultado.setText("");
					respuestaTextField.setText("");
					respuestaDeModelo = "";
					if (preguntaActual + 1 < totalPreguntas) {
						preguntaActual++;
						lblPregunta.setText("Pregunta " + (preguntaActual + 1) + " de " + totalPreguntas);
						updateQuestion();
					} else {
						finalizarJuego();						
					}
				}
			}
		});

		panelResumen = new JPanel();
		GridBagConstraints gbc_panelResumen = new GridBagConstraints();
		gbc_panelResumen.anchor = GridBagConstraints.NORTHWEST;
		gbc_panelResumen.insets = new Insets(0, 0, 0, 5);
		gbc_panelResumen.gridx = 0;
		gbc_panelResumen.gridy = 0;
		panelInferior.add(panelResumen, gbc_panelResumen);

		GridBagConstraints gbc_btnSiguiente = new GridBagConstraints();
		gbc_btnSiguiente.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnSiguiente.gridx = 2;
		gbc_btnSiguiente.gridy = 0;
		panelInferior.add(btnSiguiente, gbc_btnSiguiente);

	}
	
	public void finalizarJuego() {
		lblResultado.setText("Juego Finalizado. ¡Gracias!");
		txtpnThisIsMy.setVisible(false);
		btnGrabacion.setVisible(false);
		btnIntroducirRespuesta.setVisible(false);
		respuestaTextField.setVisible(false);
		btnSiguiente.setVisible(false);
	}

	public void agregarIcono(boolean esCorrecto) {
		if (esCorrecto) {
			JLabel lblFs = new JLabel("");
			lblFs.setIcon(
					new ImageIcon("/Users/diegofarfan/Documents/workspace/ReconocedorVoz/res/green-check-small.png"));
			panelResumen.add(lblFs);
		} else {
			JLabel label = new JLabel("");
			label.setIcon(
					new ImageIcon("/Users/diegofarfan/Documents/workspace/ReconocedorVoz/res/red-cross-small.png"));
			panelResumen.add(label);
		}

	}
}
