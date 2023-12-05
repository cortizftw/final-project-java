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
 */

//Import libraries
import java.sql.Statement;
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
	
	
	//View a record
	public static String selectQuery = "SELECT * FROM STAFF WHERE id = ?";
	
	
	//View all records
	public static String selectAllQuery = "SELECT * FROM STAFF";
	
	
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
	
	//Creating a table
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
	
	//Viewing a record
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
