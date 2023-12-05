/* P2MahotraSOrtizCGrishaG
 * The purpose of this Java console program is to view, insert, update and delete 
 * staff information stored in a MySQL database.
 * 
 * A user can view a specific staff information by providing the ID of the staff
 * A user can insert a new staff information by providing the details
 * A user can update a staff information by providing the Staff ID
 * A user can delete the staff information record by providing the Staff ID
 * 
 * 
 * Revision History:
 *     Example: Shivam Mahotra and Charina Ortiz, 2023.12.04: Created
 *     			Shivam Mahotra and Charina Ortiz, 2023.12.05: Updated
 *     	
 */

//Import libraries

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class P2MahotraSOrtizCGrishaG {
	
	
	//Details for MySQL connection
	public static String jdbcurl = "jdbc:mysql://localhost:3306/dtkfall";
	public static String username = "root";
	public static String password = "1234";
		
	
	//Create table query
	public static String createTableQuery = "CREATE TABLE Staff ("
			+ "id char(9) not null, "
			+ "lastName varchar(15), "
			+ "firstName varchar(15), "
			+ "mi char(1), "
			+ "age int, "
			+ "address varchar(20), "
			+ "city varchar(20), "
			+ "state char(2), "
			+ "telephone char(10), "
			+ "email varchar(40), "
			+ "primary key (id))";
	
	
	//View a record query
	public static String selectQuery = "SELECT * FROM STAFF WHERE id = ?";
	
	
	//View all records query
	public static String selectAllQuery = "SELECT * FROM STAFF";
	
	//Insert a record query
	public static String insertQuery = "INSERT INTO STAFF"
			+ "(id, lastName, firstName, mi, age, address, city, state, telephone, email) "
			+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	
	//Methods
	//Establishing connection to MySQL database
	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(jdbcurl, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	//Creating a table method
	public void createTable() {
		System.out.println("Creating a table using the query as follows: \n");
		System.out.println(createTableQuery);
		try (Connection conn = P2MahotraSOrtizCGrishaG.getConnection(); 
								Statement s = conn.createStatement();) {
								s.execute(createTableQuery);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	//Viewing a record method
	public void selectID(String StaffId ) 
	{
		try (Connection conn = P2MahotraSOrtizCGrishaG.getConnection();
				PreparedStatement ps = conn.prepareStatement(selectQuery)) {
			ps.setString(1, StaffId);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next())
			{
				String id = rs.getString("id");
				String lastName = rs.getString("lastName");
				String firstName = rs.getString("firstName");
				char mi = rs.getString("mi").charAt(0); 	//Check
				int age = rs.getInt("age");					//Check
				String address = rs.getString("address");
				String city = rs.getString("city");
				String state = rs.getString("state");
				String telephone = rs.getString("telephone");
				String email = rs.getString("email");
				
				//Show the retrieved staff information
				System.out.println("Please see staff information with ID: "+id);
				System.out.println("Name : "+firstName +" "+mi+" "+lastName);
				System.out.println("Age : "+age);
				System.out.println("Street Address : "+address);
				System.out.println("City : "+city);
				System.out.println("State : "+state);
				System.out.println("Telephone : "+telephone);
				System.out.println("Email : "+email);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	// Viewing all records method
	public void selectAll() {
	    // Create an ArrayList to store all rows from the table
	    List<Staff> staffList = new ArrayList<>();
	    
	    try {
	        Connection conn = P2MahotraSOrtizCGrishaG.getConnection();
	        PreparedStatement ps = conn.prepareStatement(selectAllQuery);
	        ResultSet rs = ps.executeQuery();
	        
	        while (rs.next()) {
	            String id = rs.getString("id");
	            String lastName = rs.getString("lastName");
	            String firstName = rs.getString("firstName");
	            char mi = rs.getString("mi").charAt(0);
	            int age = rs.getInt("age");
	            String address = rs.getString("address");
	            String city = rs.getString("city");
	            String state = rs.getString("state");
	            String telephone = rs.getString("telephone");
	            String email = rs.getString("email");
	            
	            // Create a staff object for each retrieved row
	            Staff staff = new Staff(id, lastName, firstName, mi, age, address, city, state, telephone, email);
	            // Append the staff object to the ArrayList
	            staffList.add(staff);
	        }
	        
	        // Sort the collection by age in ascending order
	        Collections.sort(staffList, Comparator.comparingInt(Staff::getAge));
	        
	        // Display the sorted staff information
	        for (Staff staff : staffList) {
	            System.out.println("ID: " + staff.getId() + ", Name: " + staff.getFirstName() + " " + staff.getMi() + " " + staff.getLastName() + ", Age: " + staff.getAge());
	        }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	//Insert a record method
	public void insertRecord() {
		System.out.println(insertQuery);
		try (Connection conn = P2MahotraSOrtizCGrishaG.getConnection();
				PreparedStatement ps = conn.prepareStatement(insertQuery)) {
			
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter Staff ID:");
			ps.setString(1, sc.next());
			System.out.println("Enter Last Name:");
			ps.setString(2,  sc.next());
			System.out.println("Enter First Name:");
			ps.setString(3, sc.next());
			System.out.println("Enter Middle Initial:");
			ps.setString(4, sc.next()); 				//Check - should be char
			System.out.println("Enter Age:");
			ps.setInt(5, sc.nextInt());
			System.out.println("Enter Address:");
			ps.setString(6, sc.next());
			System.out.println("Enter City:");
			ps.setString(7, sc.next());
			System.out.println("Enter State:");
			ps.setString(8, sc.next());
			System.out.println("Enter Telephone:");
			ps.setString(9, sc.next());
			System.out.println("Enter Email:");
			ps.setString(10, sc.next());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Records are successfully added to the STAFF table...");
	}
	
	public static void main(String[] args) {
		
		//Create an instance of the class
		P2MahotraSOrtizCGrishaG s = new P2MahotraSOrtizCGrishaG();
		
		//Establish a connection
		s.getConnection();
		System.out.println("Trying to connect to MySQL database...\n");
		System.out.println("Success! Database connection has been established.\n");
		
		//Create a table
		s.createTable();
		
		//
	}

}
