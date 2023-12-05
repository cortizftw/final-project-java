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
 *     Example: Shivam Mahotra, Charina Ortiz and Grisha Grisha, 2023.12.04: Created
 *     			Shivam Mahotra, Charina Ortiz and Grisha Grisha, 2023.12.05: Updated
 *     	
 */

//Import libraries

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
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
	
	
	//Update a record query
	public static String updateQuery = "UPDATE STAFF "
	        + "SET lastName = ?, firstName = ?, mi = ?, age = ?, address = ?, "
	        + "city = ?, state = ?, telephone = ?, email = ? "
	        + "WHERE id = ?";

	
	//Delete a record query
	public static String deleteQuery = "DELETE FROM STAFF WHERE id = ?";

						
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
	public void selectID(Scanner sc) 
	{
		
		try (Connection conn = P2MahotraSOrtizCGrishaG.getConnection();
				PreparedStatement ps = conn.prepareStatement(selectQuery)) {
			
			//User prompt
			System.out.println("Please enter the Staff ID you want to retrieve");
			String StaffID = sc.next();
			ps.setString(1, StaffID);
			ResultSet rs = ps.executeQuery();
			
			boolean found = false;
			
			while(rs.next())
			{	
				found = true;
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
				System.out.println("\nPlease see staff information with ID: "+id);
				System.out.println("Name : "+firstName +" "+mi+" "+lastName);
				System.out.println("Age : "+age);
				System.out.println("Street Address : "+address);
				System.out.println("City : "+city);
				System.out.println("State : "+state);
				System.out.println("Telephone : "+telephone);
				System.out.println("Email : "+email);
				
				//User prompt back to the menu option
				System.out.println("\nChoose the operation you'd like to perform next.");
			}
			
			if (!found) {
	            System.out.println("Staff ID not found."); //Validation for non-existent Staff ID
	        }
			
		} catch (SQLException e) {
			System.out.println("Error retrieving staff information: " + e.getMessage());
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
	public void insertRecord(Scanner sc) {
		System.out.println(insertQuery);
		try (Connection conn = P2MahotraSOrtizCGrishaG.getConnection();
				PreparedStatement ps = conn.prepareStatement(insertQuery)) {
			
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
	
	
	//Update a record method
	public void updateRecord(Scanner sc) {
		System.out.println(updateQuery);
		try (Connection conn = P2MahotraSOrtizCGrishaG.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateQuery)) {
			
			//Might need to fetch record first then show before and after records
			//Check if user wants to update particular field, if no fetch currect record
			System.out.println("Enter Staff ID to update:");
			ps.setString(10, sc.next());
			System.out.println("Enter Updated Last Name:");
			ps.setString(1,  sc.next());
			System.out.println("Enter Updated First Name:");
			ps.setString(2, sc.next());
			System.out.println("Enter Updated Middle Initial:");
			ps.setString(3, sc.next()); 				//Check - should be char
			System.out.println("Enter Updated Age:");
			ps.setInt(4, sc.nextInt());
			System.out.println("Enter Updated Address:");
			ps.setString(5, sc.next());
			System.out.println("Enter Updated City:");
			ps.setString(6, sc.next());
			System.out.println("Enter Updated State:");
			ps.setString(7, sc.next());
			System.out.println("Enter Updated Telephone:");
			ps.setString(8, sc.next());
			System.out.println("Enter Updated Email:");
			ps.setString(9, sc.next());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Records are successfully added to the STAFF table...");
	}
	
	
	//Delete a record method
	public void deleteRecord(Scanner sc) {
		//Initialize the deletedId
		String deletedID = "";
		try (Connection conn = P2MahotraSOrtizCGrishaG.getConnection();
				PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
			
			//Might need to fetch record first then show before and after records
			//Check if user wants to update particular field, if no fetch currect record
			System.out.println("Enter the ID of the staff information you want to delete");
			//Capture the deletedId
			deletedID = sc.next();
			ps.setString(1, deletedID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Staff information with ID "+deletedID+" is deleted successfully...");
	}
	
	public static void main(String[] args) {
		
		//Create an instance of the class
		P2MahotraSOrtizCGrishaG ddl = new P2MahotraSOrtizCGrishaG();
		
		//Establish a connection
		System.out.println("Trying to connect to MySQL database...");
		ddl.getConnection();
		System.out.println("Success! Database connection has been established.");
		
		//Create a table
//		s.createTable();
		
		//Welcome message and menu button
		System.out.println("=====================================================================");
		System.out.println("Welcome to Java-MySQL Database integration console.");
		System.out.println("=====================================================================");
		System.out.println("Choose an operation from the menu below. Type only the number.");
	
		Scanner sc = new Scanner(System.in);
		boolean exit = false;
		
		while (exit == false) {
	        System.out.println("(1) View Staff Information by ID");
	        System.out.println("(2) View All Staff Information");
	        System.out.println("(3) Insert New Staff Information");
	        System.out.println("(4) Update Staff Information");
	        System.out.println("(5) Delete Staff Information");
	        System.out.println("(6) Exit");
	        
	        
            try {
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        ddl.selectID(sc);
                        break;
                    case 2:
                        ddl.selectAll();
                        break;
                    case 3:
                        ddl.insertRecord(sc);
                        break;
                    case 4:
                        ddl.updateRecord(sc);
                        break;
                    case 5:
                        ddl.deleteRecord(sc);
                        break;
                    case 6:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice. Please enter a valid option.");  //Validation - Raise an error message for input other than integer
                sc.next(); 
            }
	    }
	}
}
