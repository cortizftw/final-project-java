package FinalProject;

/*
 * CommonConstants.java
 * 
 * This file contains all the constants used in the program.
 * This includes all the hard-coded queries used in the program, 
 * the user name and password and other details used to connect to the database.
 * 
 * Revision History:
 *     Shivam Mahotra, Charina Ortiz and Grisha Grisha, 2023.12.04: Created
 * 
 */

public class CommonConstants {

	// Details for MySQL connection
		public static final String jdbcurl = "jdbc:mysql://localhost:3306/dtkfall";
		public static final String username = "root";
		public static final String password = "1234";

		// Create table query
		public static final String createTableQuery = "CREATE TABLE Staff (" + "id char(9) not null, " + "lastName varchar(15), "
				+ "firstName varchar(15), " + "mi char(1), " + "age int, " + "address varchar(20), " + "city varchar(20), "
				+ "state char(2), " + "telephone char(10), " + "email varchar(40), " + "primary key (id))";

		// View a record query
		public static final String selectQuery = "SELECT * FROM STAFF WHERE id = ?";

		// View all records query
		public static final String selectAllQuery = "SELECT * FROM STAFF";

		// Insert a record query
		public static final String insertQuery = "INSERT INTO STAFF"
				+ "(id, lastName, firstName, mi, age, address, city, state, telephone, email) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		// Update a record query
		public static final String updateQuery = "UPDATE STAFF "
				+ "SET lastName = ?, firstName = ?, mi = ?, age = ?, address = ?, "
				+ "city = ?, state = ?, telephone = ?, email = ? " + "WHERE id = ?";

		// Delete a record query
		public static final String deleteQuery = "DELETE FROM STAFF WHERE id = ?";

		//Check if ID exists query
		public static final String checkIfExistsQuery = "SELECT * FROM STAFF WHERE ID = ?";

		//Check count of rows with same staff id
		public static final String countStaffIds = "SELECT COUNT(*) FROM STAFF WHERE id = ?";

		//Check count of entries with same name and age
		public static final String countSameNameAndAge = "SELECT COUNT(*) FROM STAFF WHERE firstName = ? AND lastName = ? AND age = ?";
}
