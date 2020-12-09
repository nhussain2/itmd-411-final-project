package javaapplication1;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dao {
	// instance fields
	static Connection connect = null;
	Statement statement = null;

	// constructor
	public Dao() {
	  
	}

	public Connection getConnection() {
		// Setup the connection with the DB
		try {
			connect = DriverManager
					.getConnection("jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false"
							+ "&user=fp411&password=411");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connect;
	}

	// CRUD implementation

	public void createTables() {
		// variables for SQL Query table creations
		final String createTicketsTable = "CREATE TABLE nhuss1_tickets(ticket_id INT AUTO_INCREMENT PRIMARY KEY, ticket_issuer VARCHAR(30), ticket_description VARCHAR(200), opened VARCHAR(100), status VARCHAR(20))";
		final String createUsersTable = "CREATE TABLE nhuss1_users(uid INT AUTO_INCREMENT PRIMARY KEY, uname VARCHAR(30), upass VARCHAR(30), admin int)";

		try {

			// execute queries to create tables

			statement = getConnection().createStatement();

			statement.executeUpdate(createTicketsTable);
			statement.executeUpdate(createUsersTable);
			System.out.println("Created tables in given database...");

			// end create table
			// close connection/statement object
			statement.close();
			connect.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// add users to user table
		addUsers();
	}

	public void addUsers() {
		// add list of users from userlist.csv file to users table

		// variables for SQL Query inserts
		String sql;

		Statement statement;
		BufferedReader br;
		List<List<String>> array = new ArrayList<>(); // list to hold (rows & cols)

		// read data from file
		try {
			br = new BufferedReader(new FileReader(new File("./userlist.csv")));

			String line;
			while ((line = br.readLine()) != null) {
				array.add(Arrays.asList(line.split(",")));
			}
		} catch (Exception e) {
			System.out.println("There was a problem loading the file");
		}

		try {

			// Setup the connection with the DB

			statement = getConnection().createStatement();

			// create loop to grab each array index containing a list of values
			// and PASS (insert) that data into your User table
			for (List<String> rowData : array) {

				sql = "insert into nhuss1_users(uname,upass,admin) " + "values('" + rowData.get(0) + "'," + " '"
						+ rowData.get(1) + "','" + rowData.get(2) + "');";
				statement.executeUpdate(sql);
			}
			System.out.println("Inserts completed in the given database...");

			// close statement object
			statement.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public int insertRecords(String ticketName, String ticketDesc) {
		int id = 0;
		try {
			statement = getConnection().createStatement();
			String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
			statement.executeUpdate("Insert into nhuss1_tickets" + "(ticket_issuer, ticket_description, opened, status) values(" + " '"
					+ ticketName + "','" + ticketDesc + "','" + timeStamp + "','" +  "OPEN" + "')", Statement.RETURN_GENERATED_KEYS);

			// retrieve ticket id number newly auto generated upon record insertion
			ResultSet resultSet = null;
			resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				// retrieve first field in table
				id = resultSet.getInt(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;

	}
	
	public ResultSet selectRecords(int tid) {
		ResultSet results = null;
		try {
			statement = connect.createStatement();
			results = statement.executeQuery("SELECT * FROM nhuss1_tickets WHERE ticket_id = " + tid);
			connect.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return results;
	}

	public ResultSet readRecords() {

		ResultSet results = null;
		try {
			statement = connect.createStatement();
			results = statement.executeQuery("SELECT * FROM nhuss1_tickets");
			//connect.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return results;
	}
	// continue coding for updateRecords implementation
	
	public void updateRecords(String tid, String desc, String status) {
		
		ResultSet results = null;
        String curr_desc = null;
        
        try{
            System.out.println("Connecting to database to obtain current ticket description...");
            String sql = "select ticket_description from nhuss1_tickets where ticket_id=?";
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setString(1, tid);
            results = ps.executeQuery();

            System.out.println("Successfully obtained current ticket description...");

            while(results.next()){
                curr_desc = results.getString("ticket_description");
            }
            
            
            String updatedDesc = curr_desc + "\n Update: " + desc;
            
            System.out.println("Connecting to database to update ticket...");
            String sql2 = "update nhuss1_tickets set ticket_description=?, status=? where ticket_id=?";
            PreparedStatement ps2 = connect.prepareStatement(sql2);
            ps2.setString(1, updatedDesc);
            ps2.setString(2, status);
            ps2.setString(3, tid);
            ps2.executeUpdate();
            ps2.close();
            ps.close();
            System.out.println("Successfully connected to database & updated ticket...");
        } catch (SQLException se) {
            se.printStackTrace();
        }
		
	}
	
	
	
	
	// continue coding for deleteRecords implementation
	public int deleteRecords(int tid) {
		try {
			statement = connect.createStatement();
		    String sql = "DELETE FROM nhuss1_tickets WHERE nhuss1_tickets.ticket_id = " + tid;
		    statement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tid;
	}
		
		
		
		
}
	
	
