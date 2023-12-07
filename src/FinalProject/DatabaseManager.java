package FinalProject;


/* DatabaseManager.java
 * 
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
 *     Shivam Mahotra, Charina Ortiz and Grisha Grisha, 2023.12.04: Created
 *     Shivam Mahotra, Charina Ortiz and Grisha Grisha, 2023.12.05: Updated
 *     	
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class DatabaseManager {

	// Adding a class-level variable to store the collection
	private static List<Staff> staffList = new ArrayList<>();

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

	//Creating a table method
	public void createTable() {
		System.out.println("Creating a table using the query as follows: \n");
		System.out.println(CommonConstants.createTableQuery);
		try (Connection conn = P2MahotraSOrtizCGrishaG.getConnection(); Statement s = conn.createStatement();) {
			s.execute(CommonConstants.createTableQuery);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Viewing a record method
	public Staff selectID(Scanner sc) {
		Staff staff = null;
		boolean validIdFound = false;

		while (!validIdFound) {
			try (Connection conn = P2MahotraSOrtizCGrishaG.getConnection();
					PreparedStatement ps = conn.prepareStatement(CommonConstants.selectQuery)) {

				System.out.println("Please enter the Staff ID you want to retrieve");
				String staffID = sc.next();
				ps.setString(1, staffID);
				ResultSet rs = ps.executeQuery();

				boolean found = false;

				while (rs.next()) {
					found = true;
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

					// Create a Staff object using retrieved information
					staff = new Staff(id, lastName, firstName, mi, age, address, city, state, telephone, email);

					System.out.println("\nPlease see staff information with ID: " + id);
					System.out.println("Name : " + firstName + " " + mi + " " + lastName);
					System.out.println("Age : " + age);
					System.out.println("Street Address : " + address);
					System.out.println("City : " + city);
					System.out.println("State : " + state);
					System.out.println("Telephone : " + telephone);
					System.out.println("Email : " + email);

					System.out.println("\nChoose the operation you'd like to perform next.");
				}

				if (!found) {
					System.out.println("Staff ID not found. Please try again.");
				} else {
					validIdFound = true;
					// Add the retrieved Staff object to the collection
					staffList.add(staff);
				}

			} catch (SQLException e) {
				System.out.println("Error retrieving staff information: " + e.getMessage());
			}
		}
		return staff;
	}

	public void selectAll() {
		// Create an ArrayList to store all rows from the table

		try (Connection conn = P2MahotraSOrtizCGrishaG.getConnection();
				PreparedStatement ps = conn.prepareStatement(CommonConstants.selectAllQuery)) {

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

				// Check if staff with the same ID exists in the staffList
	            boolean found = false;
	            for (Staff staff : staffList) {
	                if (staff.getId().equals(id)) {
	                    // Update existing staff information
	                    staff.setLastName(lastName);
	                    staff.setFirstName(firstName);
	                    staff.setMi(mi);
	                    staff.setAge(age);
	                    staff.setAddress(address);
	                    staff.setCity(city);
	                    staff.setState(state);
	                    staff.setTelephone(telephone);
	                    staff.setEmail(email);
	                    found = true;
	                    break;
	                }
	            }

	            // If staff with the same ID was not found, create and add a new staff object
	            if (!found) {
	                Staff staff = new Staff(id, lastName, firstName, mi, age, address, city, state, telephone, email);
	                staffList.add(staff);
	            }
	        }

			// Sort the collection by age in ascending order
			Collections.sort(staffList, Comparator.comparingInt(Staff::getAge));

			// Check if collection list if empty before returning the data
			if (!staffList.isEmpty()) {
				System.out.println("Showing all Staff Information...\n");
				for (Staff staff : staffList) {
					System.out.println("ID: " + staff.getId());
					System.out
					.println("Name: " + staff.getFirstName() + " " + staff.getMi() + " " + staff.getLastName());
					System.out.println("Age: " + staff.getAge());
					System.out
					.println("Address: " + staff.getAddress() + " " + staff.getCity() + " " + staff.getState());
					System.out.println("Telephone: " + staff.getTelephone());
					System.out.println("Email: " + staff.getEmail() + "\n");
				}
				System.out.println("\nChoose the operation you'd like to perform next.");
			} else {
				System.out.println("No Staff Information found.");
			}

		} catch (SQLException e) {
			System.out.println("Error retrieving staff information: " + e.getMessage());
		}
	}


	// Insert a record method
	public void insertRecord(Scanner sc) {

		try (Connection conn = P2MahotraSOrtizCGrishaG.getConnection();
				PreparedStatement psCheck = conn.prepareStatement(CommonConstants.countSameNameAndAge);
				PreparedStatement ps = conn.prepareStatement(CommonConstants.insertQuery)) {

			// Validate user input for Staff ID
			String staffID;
			while (true) {
				System.out.println("Enter Staff ID (9 alphanumeric characters):");
				staffID = sc.next();
				if (staffID.length() == 9 && staffID.matches("[a-zA-Z0-9]+")) {
					// Check if the Staff ID is 9 characters long and contains only alphanumeric characters
					break;
				} else {
					System.out.println("Invalid Staff ID format. Please enter 9 alphanumeric characters.");
				}
			}
			ps.setString(1, staffID);


			// Validate user input for Last Name
			String lastName;
			while (true) {
				System.out.println("Enter Last Name (max 15 characters, alphabets only):");
				lastName = sc.next();
				if (lastName.matches("[a-zA-Z]+") && lastName.length() <= 15) {
					break;
				} else {
					System.out.println("Last name should contain only alphabets and not exceed 15 characters. Please try again.");
				}
			}
			ps.setString(2, lastName);

			// Validate user input for First Name
			String firstName;
			while (true) {
				System.out.println("Enter First Name (max 15 characters, alphabets only):");
				firstName = sc.next();
				if (firstName.matches("[a-zA-Z]+") && firstName.length() <= 15) {
					break;
				} else {
					System.out.println("First name should contain only alphabets and not exceed 15 characters. Please try again.");
				}
			}
			ps.setString(3, firstName);

			// Validate user input for Middle Initial
			String middleInitial;
			while (true) {
				System.out.println("Enter Middle Initial (only 1 alphabet):");
				middleInitial = sc.next();
				if (middleInitial.length() == 1 && Character.isLetter(middleInitial.charAt(0))) {
					// Check if the character is an alphabet
					break;
				} else {
					System.out.println("Invalid middle initial format. Please enter a single alphabet.");
				}
			}
			ps.setString(4, middleInitial.toUpperCase()); // Store the middle initial as uppercase


			// Validate user input for Age
			int age = 0;
			boolean isValidAge = false;

			while (!isValidAge) {
				System.out.println("Enter Age (round off to the nearest year):");

				// Read the entire line as a string
				String inputAge = sc.next();

				// Check if the input can be parsed as an integer
				try {
					age = Integer.parseInt(inputAge);
					isValidAge = true;
				} catch (NumberFormatException e) {
					System.out.println("Please enter a valid integer for age.");
				}
			}
			ps.setInt(5, age);

			// Validate user input for Address
			String address;

			while (true) {
				System.out.println("Enter Address (max 20 characters, alphanumeric with ',' and '-'):");
				sc.nextLine();
				address = sc.nextLine();
				if (address.matches("[a-zA-Z0-9,\\- ]+") && address.length() <= 20) {
					break;
				} else {
					System.out.println("Invalid address format or exceeds 20 characters. Please try again.");
				}
			}
			ps.setString(6, address);


			// Validate user input for City
			String city;
			while (true) {
				System.out.println("Enter City (max 20 characters, alphabets only):");
				city = sc.next();
				if (city.matches("[a-zA-Z]+") && city.length() <= 20) {
					break;
				} else {
					System.out.println("City should contain only alphabets and not exceed 20 characters. Please try again.");
				}
			}
			ps.setString(7, city);


			// Validate user input for State
			String state;
			while (true) {
				System.out.println("Enter State (only 2 characters, alphabets only):");
				state = sc.next();
				if (state.matches("[a-zA-Z]{2}")) {
					break;
				} else {
					System.out.println("State should contain only alphabets and have exactly 2 characters. Please try again.");
				}
			}
			ps.setString(8, state);

			// Validate user input for Telephone
			String telephone;
			while (true) {
				System.out.println("Enter Telephone (max 10 characters, digits only):");
				telephone = sc.next();
				if (telephone.matches("\\d+") && telephone.length() <= 10) {
					break;
				} else {
					System.out.println("Telephone should contain only digits and not exceed 10 characters. Please try again.");
				}
			}
			ps.setString(9, telephone);

			// Validate user input for Email
			String email;
			while (true) {
				System.out.println("Enter Email (max 40 characters):");
				email = sc.next();
				if (email.length() <= 40 && email.contains("@")) {
					break;
				} else {
					System.out.println("Invalid email format or exceeds 40 characters. Please try again.");
				}
			}
			ps.setString(10, email);

			// Check if the record with the same first name, last name, and age already exists
			psCheck.setString(1, firstName);
			psCheck.setString(2, lastName);
			psCheck.setInt(3, age);
			ResultSet rs = psCheck.executeQuery();
			rs.next();
			int count = rs.getInt(1);

			if (count > 0) {
				System.out.println("Record with the same first name, last name, and age already exists. Insertion aborted.");
				System.out.println("Back to menu options...");
			} else {
				// Execute the insert query
				ps.executeUpdate();
				System.out.println("Record successfully added to the STAFF table.");

				// Create a new Staff object for the inserted record
				Staff newStaff = new Staff(staffID, lastName, firstName, middleInitial.charAt(0), age, address, city, state, telephone, email);

				// Add the new Staff object to the collection
				staffList.add(newStaff);
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println("Duplicate ID error: The record with the same Staff ID already exists in the database.");
			System.out.println("Back to menu options...");
		} catch (SQLException e) {
			System.out.println("Error inserting staff information: " + e.getMessage());
		}

	}

	// Update a record method
	public void updateRecord(Scanner sc) {
		try (Connection conn = P2MahotraSOrtizCGrishaG.getConnection();
				PreparedStatement psCheckId = conn.prepareStatement(CommonConstants.countStaffIds);
				PreparedStatement psCheck = conn.prepareStatement(CommonConstants.countSameNameAndAge);
				PreparedStatement psUpdate = conn.prepareStatement(CommonConstants.updateQuery)) {

			boolean validIdFound = false;

			while (!validIdFound) {
				try {
					System.out.println("Enter Staff ID to update:");
					String staffID = sc.next();
					psCheckId.setString(1, staffID);
					ResultSet rsId = psCheckId.executeQuery();

					rsId.next();
					int countId = rsId.getInt(1);

					if (countId > 0) {
						validIdFound = true;

						// Validate user input for Last Name
						String lastName;
						while (true) {
							System.out.println("Enter Updated Last Name (max 15 alphabetic characters):");
							lastName = sc.next();
							if (lastName.length() <= 15 && lastName.matches("[a-zA-Z]+")) {
								// Check if Last Name is 15 characters or less and contains only alphabetic characters
								break;
							} else {
								System.out.println("Invalid Last Name format. Please enter up to 15 alphabetic characters.");
							}
						}

						// Validate user input for First Name
						String firstName;
						while (true) {
							System.out.println("Enter Updated First Name (max 15 alphabetic characters):");
							firstName = sc.next();
							if (firstName.length() <= 15 && firstName.matches("[a-zA-Z]+")) {
								// Check if First Name is 15 characters or less and contains only alphabetic characters
								break;
							} else {
								System.out.println("Invalid First Name format. Please enter up to 15 alphabetic characters.");
							}
						}

						// Validate user input for Middle Initial
						String middleInitial;
						while (true) {
							System.out.println("Enter Updated Middle Initial (only 1 alphabetic character):");
							middleInitial = sc.next();
							if (middleInitial.length() == 1 && middleInitial.matches("[a-zA-Z]")) {
								// Check if Middle Initial is 1 character long and contains only alphabetic characters
								break;
							} else {
								System.out.println("Invalid Middle Initial format. Please enter exactly 1 alphabetic character.");
							}
						}

						// Validate user input for Age
						int age = 0;
						boolean isValidAge = false;

						while (!isValidAge) {
							System.out.println("Enter Age (round off to the nearest year):");

							// Read the entire line as a string
							String inputAge = sc.next();

							// Check if the input can be parsed as an integer
							try {
								age = Integer.parseInt(inputAge);
								isValidAge = true;
							} catch (NumberFormatException e) {
								System.out.println("Please enter a valid integer for age.");
							}
						}

						// Validate user input for Address
						String address;
						while (true) {
							System.out.println("Enter Address (max 20 alphanumeric characters):");
							sc.nextLine();
							address = sc.nextLine();
							if (address.length() <= 20 && address.matches("[a-zA-Z0-9,\\- ]+")) {
								// Check if Address is 20 characters or less and contains only alphanumeric characters, ',' and '-'
								break;
							} else {
								System.out.println("Invalid Address format. Please enter up to 20 alphanumeric characters.");
							}
						}

						// Validate user input for City
						String city;
						while (true) {
							System.out.println("Enter City (max 20 alphabetic characters):");
							city = sc.next();
							if (city.length() <= 20 && city.matches("[a-zA-Z]+")) {
								// Check if City is 20 characters or less and contains only alphabetic characters
								break;
							} else {
								System.out.println("Invalid City format. Please enter up to 20 alphabetic characters.");
							}
						}

						// Validate user input for State
						String state;
						while (true) {
							System.out.println("Enter State (only 2 alphabetic characters):");
							state = sc.next();
							if (state.length() == 2 && state.matches("[a-zA-Z]+")) {
								// Check if State is exactly 2 characters long and contains only alphabetic characters
								break;
							} else {
								System.out.println("Invalid State format. Please enter exactly 2 alphabetic characters.");
							}
						}

						// Validate user input for Telephone
						String telephone;
						while (true) {
							System.out.println("Enter Telephone (only digits, max 10 characters):");
							telephone = sc.next();
							if (telephone.length() <= 10 && telephone.matches("[0-9]+")) {
								// Check if Telephone is 10 characters or less and contains only digits
								break;
							} else {
								System.out.println("Invalid Telephone format. Please enter up to 10 digits.");
							}
						}

						// Validate user input for Email
						String email;
						while (true) {
							System.out.println("Enter Email (max 40 characters):");
							email = sc.next();
							if (email.length() <= 40) {
								// You may want to add a more sophisticated email validation here
								break;
							} else {
								System.out.println("Invalid Email format. Please enter up to 40 characters.");
							}
						}


						psCheck.setString(1, firstName);
						psCheck.setString(2, lastName);
						psCheck.setInt(3, age);
						ResultSet rsCheck = psCheck.executeQuery();
						rsCheck.next();
						int count = rsCheck.getInt(1);

						if (count > 0) {
							System.out.println(
									"Record with the same first name, last name, and age already exists. Update aborted.");
							System.out.println("Back to menu options...");
						} else {
							psUpdate.setString(1, lastName);
							psUpdate.setString(2, firstName);
							psUpdate.setString(3, middleInitial);
							psUpdate.setInt(4, age);
							psUpdate.setString(5, address);
							psUpdate.setString(6, city);
							psUpdate.setString(7, state);
							psUpdate.setString(8, telephone);
							psUpdate.setString(9, email);
							psUpdate.setString(10, staffID);

							int affectedRows = psUpdate.executeUpdate();

							if (affectedRows > 0) {
								System.out.println("Record updated successfully.");

								// Find the corresponding Staff object in the collection and update its fields
								for (Staff staff : staffList) {
									if (staff.getId().equals(staffID)) {
										staff.setLastName(lastName);
										staff.setFirstName(firstName);
										staff.setMi(middleInitial.charAt(0));
										staff.setAge(age);
										staff.setAddress(address);
										staff.setCity(city);
										staff.setState(state);
										staff.setTelephone(telephone);
										staff.setEmail(email);
										break; // No need to continue searching after updating
									}
								}
							} else {
								System.out.println("No records were updated.");
							}
						}
					} else {
						System.out.println("Staff ID not found. Please enter an existing ID.");
					}
				} catch (SQLException e) {
					System.out.println("Error updating staff information: " + e.getMessage());
				}
			}
		} catch (SQLException e) {
			System.out.println("Database error: " + e.getMessage());
		}
	}

	// Delete a record method
	public void deleteRecord(Scanner sc) {
		// Initialize the deletedId
		String deletedID = "";
		try (Connection conn = P2MahotraSOrtizCGrishaG.getConnection();
				PreparedStatement checkIfExistsPs = conn.prepareStatement(CommonConstants.checkIfExistsQuery);
				PreparedStatement ps = conn.prepareStatement(CommonConstants.deleteQuery)) {

			// Check if user wants to update a particular field, if no fetch current record
			System.out.println("Enter the ID of the staff information you want to delete:");
			// Capture the deletedId
			deletedID = sc.next();

			// Check if the staff ID exists in the database
			checkIfExistsPs.setString(1, deletedID);
			ResultSet resultSet = checkIfExistsPs.executeQuery();

			if (resultSet.next()) {
				// If staff ID exists, proceed with deletion
				ps.setString(1, deletedID);
				ps.executeUpdate();
				System.out.println("Staff information with ID " + deletedID + " has been deleted successfully.");

				// Create a final variable to capture deletedID value
				final String finalDeletedID = deletedID;

				// Remove the corresponding Staff object from the collection
				staffList.removeIf(staff -> staff.getId().equals(finalDeletedID));
			} else {
				// If staff ID does not exist, inform the user
				System.out.println("Staff information with ID " + deletedID + " does not exist in the database.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
