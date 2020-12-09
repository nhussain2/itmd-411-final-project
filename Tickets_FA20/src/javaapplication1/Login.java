package javaapplication1;

import java.awt.GridLayout; //useful for layouts
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//controls-label text fields, button
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Login extends JFrame {

	Dao conn;

	public Login() {

		super("IIT HELP DESK LOGIN");
		conn = new Dao();
		conn.createTables();
		setSize(400, 210);
		setLayout(new GridLayout(4, 2));
		setLocationRelativeTo(null); // centers window

		// SET UP CONTROLS
		JLabel lblUsername = new JLabel("Username", JLabel.LEFT);
		JLabel lblPassword = new JLabel("Password", JLabel.LEFT);
		JLabel lblStatus = new JLabel(" ", JLabel.CENTER);
		// JLabel lblSpacer = new JLabel(" ", JLabel.CENTER);

		JTextField txtUname = new JTextField(10);

		JPasswordField txtPassword = new JPasswordField();
		JButton btn = new JButton("Submit");
		JButton btnExit = new JButton("Exit");

		// constraints

		lblStatus.setToolTipText("Contact help desk to unlock password");
		lblUsername.setHorizontalAlignment(JLabel.CENTER);
		lblPassword.setHorizontalAlignment(JLabel.CENTER);
 
		// ADD OBJECTS TO FRAME
		add(lblUsername);  // 1st row filler
		add(txtUname);
		add(lblPassword); // 2nd row
		add(txtPassword);
		add(btn);         // 3rd row
		add(btnExit);
		add(lblStatus);   // 4th row

		btn.addActionListener(new ActionListener() {
			int count = 0; // count agent

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Login button clicked.");
				boolean adminFlag = false;
				int admin = 0;
				count = count + 1;
				// verify credentials of user (MAKE SURE TO CHANGE TO YOUR TABLE NAME BELOW)

				String query = "SELECT * FROM nhuss1_users WHERE uname = ? and upass = ?;";
				try (PreparedStatement stmt = conn.connect.prepareStatement(query)) {
					stmt.setString(1, txtUname.getText());
					stmt.setString(2, txtPassword.getText());
					ResultSet rs = stmt.executeQuery();
					if (rs.next()) {
						
						int isAdmin = rs.getInt("admin");
						if (isAdmin == 1) 
						{
							adminFlag = true;
							System.out.println("Login of admin success.");
						} 
						else {
							System.out.println("Login of user success.");
						}
						new Tickets(adminFlag);
						setVisible(false); // HIDE THE FRAME
						dispose(); // CLOSE OUT THE WINDOW
						} else
						lblStatus.setText("Try again! " + (3 - count) + " / 3 attempts left");
						
					
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
 			 
			}
		});
		btnExit.addActionListener(e -> System.exit(0));

		setVisible(true); // SHOW THE FRAME
	}

	public static void main(String[] args) {

		new Login();
	}
}
