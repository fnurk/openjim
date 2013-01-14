import java.awt.FlowLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class GUI extends JFrame {
	private JTextArea chatField;
	private JTextField inputField;
	private JLabel inputFieldLabel;
	private Client client;
	private JScrollPane scrollpane;

	public GUI(final Client client) {
		super("JIMmy Client");
		setLayout(new FlowLayout());

		chatField = new JTextArea(10, 40);
		chatField.setToolTipText("Chat text will be displayed here");
		chatField.setEditable(false);

		scrollpane = new JScrollPane(chatField);
		add(scrollpane);

		inputFieldLabel = new JLabel("Enter anything:");
		add(inputFieldLabel);

		inputField = new JTextField("Enter something...");
		inputField.setToolTipText("Enter anything to send here");
		add(inputField);

		inputField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				client.sendMessage(event.getActionCommand());
				inputField.setText("");
			}
		});
	}

	public void showMessage(String x) {
		chatField.append("\n" + x);
		chatField.setCaretPosition(chatField.getDocument().getLength());
	}
	
	public void promptUser(String x)
	{
		chatField.append("\n" + x);
	}


}