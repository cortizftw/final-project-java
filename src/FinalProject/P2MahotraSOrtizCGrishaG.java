package FinalProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/* P2MahotraSOrtizCGrishaG.java
 * 
 * This Java console program serves as a management tool for staff information in a MySQL database.
 * To interact with the program, choose an operation from the menu below by entering the corresponding number.
 * 
 * Revision History:
 *     Shivam Mahotra, Charina Ortiz and Grisha Grisha, 2023.12.04: Created
 *     Shivam Mahotra, Charina Ortiz and Grisha Grisha, 2023.12.05: Updated
 *     	
 */

import java.util.InputMismatchException;
import java.util.Scanner;

public class P2MahotraSOrtizCGrishaG {

	@SuppressWarnings("static-access")
	public static void main(String[] args) {

		// Create an instance of the class
		DatabaseManager ddl = new DatabaseManager();

		// Establish a connection
		System.out.println("Trying to connect to MySQL database...");
		ddl.getConnection();
		System.out.println("Success! Database connection has been established.");

		// Create a table
//		ddl.createTable();

		// Welcome message and menu button
		System.out.println("=====================================================================");
		System.out.println("Welcome to Java-MySQL Database integration console.");
		System.out.println("=====================================================================");
		System.out.println("Choose an operation from the menu below. Type only the number.");

		Scanner sc = new Scanner(System.in);
		boolean exit = false;

		while (exit == false) {
			System.out.println("_________________________________________");
			System.out.println("|(1) View Staff Information by ID	|");
			System.out.println("|(2) View All Staff Information		|");
			System.out.println("|(3) Insert New Staff Information	|");
			System.out.println("|(4) Update Staff Information		|");
			System.out.println("|(5) Delete Staff Information		|");
			System.out.println("|(6) Exit				|");
			System.out.println("_________________________________________");

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
				System.out.println("Invalid choice. Please enter a valid option."); // Validation - Raise an error
				// message for input other than
				// integer
				sc.next();
			}
		}
		System.out.println("Have a nice day!");
	}

	//Establishing connection to MySQL database
	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(CommonConstants.jdbcurl, CommonConstants.username, CommonConstants.password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
