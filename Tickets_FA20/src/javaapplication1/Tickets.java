package javaapplication1;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class Tickets extends JFrame implements ActionListener {

	// class level member objects
	Dao dao = new Dao(); // for CRUD operations
	Boolean chkIfAdmin = false;

	// Main menu object items
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuAdmin = new JMenu("Admin");
	private JMenu mnuTickets = new JMenu("Tickets");

	// Sub menu item objects for all Main menu item objects
	JMenuItem mnuItemExit;
	JMenuItem mnuItemUpdate;
	JMenuItem mnuItemDelete;
	JMenuItem mnuItemOpenTicket;
	JMenuItem mnuItemViewTicket;
	JMenuItem mnuItemRefresh;
	JMenuItem mnuItemSelectTicket;

	public Tickets(Boolean isAdmin) {

		if (isAdmin != chkIfAdmin) {
			System.out.println("Admin approved");
			try {

				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
					jt.setBounds(30, 40, 200, 400);
			        jt.getTableHeader().setBackground(Color.blue);
			        jt.getTableHeader().setForeground(Color.white);
					JScrollPane sp = new JScrollPane(jt);
					add(sp);
					setVisible(true); // refreshes or repaints frame on screen
					System.out.println("Ticket view sucessfully created.");
					chkIfAdmin = true;
			} catch (SQLException e1) {
				System.out.println("Ticket view failed.");
				e1.printStackTrace();
			}
		}
		createMenu();
		prepareGUI();

	}

	private void createMenu() {

		// initialize sub menu item for File main menu
				mnuItemExit = new JMenuItem("Exit");
				// add to File main menu item
				mnuFile.add(mnuItemExit);
				
				// initialize sub menu item for File main menu
				mnuItemRefresh = new JMenuItem("Refresh");
				// add to File main menu item
				mnuFile.add(mnuItemRefresh);
				
				if (chkIfAdmin == true) {
					// initialize first sub menu items for Admin main menu
					mnuItemUpdate = new JMenuItem("Update Ticket");
					// add to Admin main menu item
					mnuAdmin.add(mnuItemUpdate);
				
					// initialize second sub menu items for Admin main menu
					mnuItemDelete = new JMenuItem("Delete Ticket");
					// add to Admin main menu item
					mnuAdmin.add(mnuItemDelete);
				}

				// initialize first sub menu item for Tickets main menu
				mnuItemOpenTicket = new JMenuItem("Open Ticket");
				// add to Ticket Main menu item
				mnuTickets.add(mnuItemOpenTicket);

				// initialize second sub menu item for Tickets main menu
				mnuItemViewTicket = new JMenuItem("View Ticket");
				// add to Ticket Main menu item
				mnuTickets.add(mnuItemViewTicket);
				
				// initialize second sub menu item for Tickets main menu
				mnuItemSelectTicket = new JMenuItem("Select Ticket");
				// add to Ticket Main menu item
				mnuTickets.add(mnuItemSelectTicket);

				/* Add action listeners for each desired menu item *************/
				mnuItemExit.addActionListener(this);
				mnuItemRefresh.addActionListener(this);
				if (chkIfAdmin == true) { 
					mnuItemUpdate.addActionListener(this);
					mnuItemDelete.addActionListener(this);
				}
				mnuItemOpenTicket.addActionListener(this);
				mnuItemViewTicket.addActionListener(this);
				mnuItemSelectTicket.addActionListener(this);

		 /*
		  * continue implementing any other desired sub menu items (like 
		  * for update and delete sub menus for example) with similar 
		  * syntax & logic as shown above*
		 */


	}

	private void prepareGUI() {

		// create JMenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); // add main menu items in order, to JMenuBar
		if (chkIfAdmin == true) {
			bar.add(mnuAdmin);
		}
		bar.add(mnuTickets);
		// add menu bar components to frame
		setJMenuBar(bar);

		addWindowListener(new WindowAdapter() {
			// define a window close operation
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}
		});
		// set frame options
		setSize(400, 400);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// implement actions for sub menu items
		if (e.getSource() == mnuItemExit) {
			System.exit(0);
		} else if (e.getSource() == mnuItemOpenTicket) {

			// get ticket information
			String ticketName = JOptionPane.showInputDialog(null, "Enter your name");
			String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");

			if(ticketName == null || (ticketName != null && ("".equals(ticketName))) || ticketDesc == null || (ticketDesc != null && ("".equals(ticketDesc))))   
			{
				JOptionPane.showMessageDialog(null, "Creating Ticket failed: empty name / description.");
				System.out.println("Creating Ticket failed: empty name / description.");
			}
			else {
				// insert ticket information to database
				
				int id = dao.insertRecords(ticketName, ticketDesc);
		
				// display results if successful or not to console / dialog box
				if (id != 0) {
				System.out.println("Ticket ID : " + id + " created successfully.");
				JOptionPane.showMessageDialog(null, "Ticket id: " + id + " created");
				
				try {

					// Use JTable built in functionality to build a table model and
					// display the table model off your result set!!!
					JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
						jt.setBounds(30, 40, 200, 400);
				        jt.getTableHeader().setBackground(Color.blue);
				        jt.getTableHeader().setForeground(Color.white);
						JScrollPane sp = new JScrollPane(jt);
						add(sp);
						setVisible(true); // refreshes or repaints frame on screen
						System.out.println("Ticket view sucessfully created.");
					} catch (SQLException e1) {
						System.out.println("Ticket view failed.");
						e1.printStackTrace();
					}
				} else
					System.out.println("Ticket creation failed.");
				}
			}

		else if (e.getSource() == mnuItemViewTicket || e.getSource() == mnuItemRefresh) {

			// retrieve all tickets details for viewing in JTable
			try {

				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
				jt.setBounds(30, 40, 200, 400);
				jt.getTableHeader().setBackground(Color.blue);
				jt.getTableHeader().setForeground(Color.white);
				JScrollPane sp = new JScrollPane(jt);
				add(sp);
				setVisible(true); // refreshes or repaints frame on screen

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		
		
		/*
		 * continue implementing any other desired sub menu items (like for update and
		 * delete sub menus for example) with similar syntax & logic as shown above
		 */
		
		else if (e.getSource() == mnuItemSelectTicket) {
			
			String ticketId = JOptionPane.showInputDialog(null, "Enter the ticket ID");
			if(ticketId == null || (ticketId != null && ("".equals(ticketId))))   
			{
				JOptionPane.showMessageDialog(null, "Ticket view failed: empty id.");
				System.out.println("Ticket view failed: empty id.");
			}
			
			else {
				// retrieve tickets details for viewing in JTable
				int tid = Integer.parseInt(ticketId);
				try {
	
					// Use JTable built in functionality to build a table model and
					// display the table model off your result set!!!
					JTable jt = new JTable(ticketsJTable.buildTableModel(dao.selectRecords(tid)));
						jt.setBounds(30, 40, 200, 400);
				        jt.getTableHeader().setBackground(Color.blue);
				        jt.getTableHeader().setForeground(Color.white);
						JScrollPane sp = new JScrollPane(jt);
						add(sp);
						setVisible(true); // refreshes or repaints frame on screen
						System.out.println("Ticket view sucessfully created.");
					} catch (SQLException e1) {
						System.out.println("Ticket view failed.");
						e1.printStackTrace();
					}
				}

			
			
		}
		
		else if (e.getSource() == mnuItemUpdate) 
		{
			
			String ticketId = JOptionPane.showInputDialog(null, "Please enter id of ticket to update");
			String ticketDesc = JOptionPane.showInputDialog(null, "Append to the ticket description");
			String ticketStatus = JOptionPane.showInputDialog(null, "Update the ticket status");
			
			if(ticketId == null || (ticketId != null && ("".equals(ticketId))) || ticketStatus == null || (ticketStatus != null && ("".equals(ticketStatus))))   
			{
				JOptionPane.showMessageDialog(null, "Ticket updating failed: empty id / status.");
				System.out.println("Ticket updating failed: empty id / status.");
			}
			else {
				int tid = Integer.parseInt(ticketId);
				
				dao.updateRecords(ticketId, ticketDesc, ticketStatus);
				if (tid != 0) {
					System.out.println("Ticket ID : " + tid + " updated successfully.");
					JOptionPane.showMessageDialog(null, "Ticket id: " + tid + " updated");
					} else
						System.out.println("Ticket update failed.");
				try {

					// Use JTable built in functionality to build a table model and
					// display the table model off your result set!!!
					JTable jt = new JTable(ticketsJTable.buildTableModel(dao.selectRecords(tid)));
						jt.setBounds(30, 40, 200, 400);
				        jt.getTableHeader().setBackground(Color.blue);
				        jt.getTableHeader().setForeground(Color.white);
						JScrollPane sp = new JScrollPane(jt);
						add(sp);
						setVisible(true); // refreshes or repaints frame on screen
						System.out.println("Ticket view sucessfully created.");
					} catch (SQLException e1) {
						System.out.println("Ticket view failed.");
						e1.printStackTrace();
					}
				
				
			}
			
		}
		
		
		else if (e.getSource() == mnuItemDelete) 
		{
			
			String ticketId = JOptionPane.showInputDialog(null, "Enter the ticket id to delete");
			
			if(ticketId == null || (ticketId != null && ("".equals(ticketId))))   
			{
				JOptionPane.showMessageDialog(null, "Ticket deleting failed: empty tid.");
				System.out.println("Ticket deleting failed: empty tid.");
			}
			else {
				// check ticket information to database
				int tid = Integer.parseInt(ticketId);
				
				int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete ticket " + tid + "?", "Warning!", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
		        	int id = dao.deleteRecords(tid);
		        	
		        	// display results if successful or not to console / dialog box
					if (id != 0) {
					System.out.println("Ticket ID : " + id + " deleted successfully.");
					JOptionPane.showMessageDialog(null, "Ticket id: " + id + " deleted");
					} else
						System.out.println("Ticket cannot be deleted!!!");
					}
				else {
			           JOptionPane.showMessageDialog(null, "Ticket " + tid + " was not deleted.");
			        }
			}
		}
		

	}

}
	
