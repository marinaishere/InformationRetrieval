package riws.fb.search;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class Main {

	private JFrame frame;
	private JTextField textField;
	private final JButton btnNewButton = new JButton("Go!");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
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
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		textField = new JTextField();
		textField.setBounds(0, 0, 318, 31);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		btnNewButton.setBounds(314, 0, 120, 31);
		frame.getContentPane().add(btnNewButton);

		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setBounds(0, 54, 434, 207);
		frame.getContentPane().add(textPane);

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					String output = Searcher.search(textField.getText());
					textPane.setText(output);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}
}
