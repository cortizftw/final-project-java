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
import java.sql.SQLIntegrityConstraintViolationException;



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
	public Staff selectID(Scanner sc) {
	    Staff staff = null;
	    boolean validIdFound = false;

	    while (!validIdFound) {
	        try (Connection conn = P2MahotraSOrtizCGrishaG.getConnection();
	             PreparedStatement ps = conn.prepareStatement(selectQuery)) {

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
	            }

	        } catch (SQLException e) {
	            System.out.println("Error retrieving staff information: " + e.getMessage());
	        }
	    }
	    return staff;
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
	        
	        // Check if collection list if empty before returning the data
	        if (!staffList.isEmpty()) {
	        	System.out.println("Showing all Staff Information...\n");
	        	for (Staff staff : staffList) {
		            System.out.println("ID: " + staff.getId());
		            System.out.println("Name: " + staff.getFirstName() + " " + staff.getMi() + " " + staff.getLastName());
		            System.out.println("Age: " + staff.getAge());
		            System.out.println("Address: " + staff.getAddress() + " " +staff.getCity() + " " + staff.getState());
		            System.out.println("Telephone: " + staff.getTelephone());
		            System.out.println("Email: " + staff.getEmail()+"\n");
		        }
	        	System.out.println("\nChoose the operation you'd like to perform next.");
	        } else {
	            System.out.println("No Staff Information found.");
	        }
	        
	    } catch (SQLException e) {
            System.out.println("Error retrieving staff information: " + e.getMessage());
        }
	}

	
	//Insert a record method
	public void insertRecord(Scanner sc) {
		
		try (Connection conn = P2MahotraSOrtizCGrishaG.getConnection();
				PreparedStatement psCheck = conn.prepareStatement("SELECT COUNT(*) FROM STAFF WHERE firstName = ? AND lastName = ? AND age = ?");
				PreparedStatement ps = conn.prepareStatement(insertQuery)) {
			
			
			//Validate user input for Staff ID
			String staffID;
			while (true) {
				System.out.println("Enter Staff ID (9 characters):");
				staffID = sc.next();
				if (staffID.length() == 9) {
	                break;
	            } else {
	                System.out.println("Staff ID should be 9 characters long. Please try again.");
	            }
			}
			ps.setString(1, staffID);
			
			
			//Validate user input for Last Name
			String lastName;
			while (true) {
				System.out.println("Enter Last Name (max 15 characters):");
				lastName = sc.next();
				if (lastName.length() <= 15) {
	                break;
	            } else {
	                System.out.println("Last name should not exceed 15 characters long. Please try again.");
	            }
			}
			ps.setString(2,  lastName);
			
			
			//Validate user input for First Name
			String firstName;
			while (true) {
				System.out.println("Enter First Name (max 15 characters):");
				firstName = sc.next();
				if (firstName.length() <= 15) {
	                break;
	            } else {
	                System.out.println("First name should not exceed 15 characters long. Please try again.");
	            }
			}
			ps.setString(3, firstName);
			
			
			//Validate user input for Middle Initial
			String middleInitial;
			while (true) {
				System.out.println("Enter Middle Initial (only 1 character):");
				middleInitial = sc.next();
				if (middleInitial.length() == 1) {
	                break;
	            } else {
	                System.out.println("Middle initial should not exceed 1 character long. Please try again.");
	            }
			}
			ps.setString(4, middleInitial); 				
			

			//Validate user input for Age
			int age = 0;
			boolean isValidAge = false;
			
			while (!isValidAge) {
				System.out.println("Enter Age (round off to the nearest year):");
				
				
				// Read the entire line as a string
			    String inputAge = sc.nextLine();

			    // Check if the input can be parsed as an integer
			    try {
			        age = Integer.parseInt(inputAge);
			        isValidAge = true; 
			    } catch (NumberFormatException e) {
			        System.out.println("Please enter a valid integer for age.");
			    }
			}
			ps.setInt(5, age);
			
			
			//Validate user input for Address
			String address;
			while (true) {
				System.out.println("Enter Address (max 20 characters):");
				address = sc.next();
				if (address.length() <= 20) {
	                break;
	            } else {
	                System.out.println("Address should not exceed 20 characters long. Please try again.");
	            }
			}
			ps.setString(6, address);
			
			
			//Validate user input for City
			String city;
			while (true) {
				System.out.println("Enter City (max 20 characters):");
				city = sc.next();
				if (city.length() <= 20) {
	                break;
	            } else {
	                System.out.println("City should not exceed 20 characters long. Please try again.");
	            }
			}
			ps.setString(7, city);
			
			
			//Validate user input for State
			String state;
			while (true) {
				System.out.println("Enter State (only 2 characters):");
				state = sc.next();
				if (state.length() == 2) {
	                break;
	            } else {
	                System.out.println("State should have exactly 2 characters. Please try again.");
	            }
			}
			ps.setString(8, state);
			
			
			//Validate user input for Telephone
			String telephone;
			while (true) {
				System.out.println("Enter Telephone (max 10 characters):");
				telephone = sc.next();
				if (telephone.length() <= 10) {
	                break;
	            } else {
	                System.out.println("Telephone should have at most 10 characters. Please try again.");
	            }
			}
			ps.setString(9, telephone);
			
			
			//Validate user input for Email
			String email;
			while (true) {
				System.out.println("Enter Email (max 40 characters):");
				email = sc.next();
				if (email.length() <= 40) {
	                break;
	            } else {
	                System.out.println("Email should have at most 40 characters. Please try again.");
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
	        	ps.executeUpdate();
	            System.out.println("Record successfully added to the STAFF table.");
	        }
		} catch (SQLIntegrityConstraintViolationException e) {
	        System.out.println("Duplicate ID error: The record with the same Staff ID already exists in the database.");
	        System.out.println("Back to menu options...");
	    } catch (SQLException e) {
            System.out.println("Error inserting staff information: " + e.getMessage());
        }
		
	}
	
	
	//Update a record method
	public void updateRecord(Scanner sc) {
	    try (Connection conn = P2MahotraSOrtizCGrishaG.getConnection();
	         PreparedStatement psCheckId = conn.prepareStatement("SELECT COUNT(*) FROM STAFF WHERE id = ?");
	         PreparedStatement psCheck = conn.prepareStatement("SELECT COUNT(*) FROM STAFF WHERE firstName = ? AND lastName = ? AND age = ?");
	         PreparedStatement psUpdate = conn.prepareStatement(updateQuery)) {

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

	                    //Validate user input for Last Name
	                    String lastName;
	                    while (true) {
	                        System.out.println("Enter Updated Last Name (max 15 characters):");
	                        lastName = sc.next();
	                        if (lastName.length() <= 15) {
	                            break;
	                        } else {
	                            System.out.println("Last name should not exceed 15 characters long. Please try again.");
	                        }
	                    }


	                    //Validate user input for First Name
	                    String firstName;
	                    while (true) {
	                        System.out.println("Enter Updated First Name (max 15 characters):");
	                        firstName = sc.next();
	                        if (firstName.length() <= 15) {
	                            break;
	                        } else {
	                            System.out.println("First name should not exceed 15 characters long. Please try again.");
	                        }
	                    }

				
	                    //Validate user input for Middle Initial
	                    String middleInitial;
	                    while (true) {
	                        System.out.println("Enter Updated Middle Initial (only 1 character):");
	                        middleInitial = sc.next();
	                        if (middleInitial.length() == 1) {
	                            break;
	                        } else {
	                            System.out.println("Middle initial should not exceed 1 character long. Please try again.");
	                        }
	                    }
	                    		

	                    //Validate user input for Age
	                    int age = 0;
	                    boolean isValidAge = false;
	                    
	                    while (!isValidAge) {
	                        System.out.println("Enter Age (round off to the nearest year):");
	                        
	                        
	                        // Read the entire line as a string
	                        String inputAge = sc.nextLine();

	                        // Check if the input can be parsed as an integer
	                        try {
	                            age = Integer.parseInt(inputAge);
	                            isValidAge = true; 
	                        } catch (NumberFormatException e) {
	                            System.out.println("Please enter a valid integer for age.");
	                        }
	                    }
	                    

	                    //Validate user input for Address
	                    String address;
	                    while (true) {
	                        System.out.println("Enter Address (max 20 characters):");
	                        address = sc.next();
	                        if (address.length() <= 20) {
	                            break;
	                        } else {
	                            System.out.println("Address should not exceed 20 characters long. Please try again.");
	                        }
	                    }
	                   


	                    //Validate user input for City
	                    String city;
	                    while (true) {
	                        System.out.println("Enter City (max 20 characters):");
	                        city = sc.next();
	                        if (city.length() <= 20) {
	                            break;
	                        } else {
	                            System.out.println("City should not exceed 20 characters long. Please try again.");
	                        }
	                    }
	                    


	                    //Validate user input for State
	                    String state;
	                    while (true) {
	                        System.out.println("Enter State (only 2 characters):");
	                        state = sc.next();
	                        if (state.length() == 2) {
	                            break;
	                        } else {
	                            System.out.println("State should have exactly 2 characters. Please try again.");
	                        }
	                    }
	                    


	                    //Validate user input for Telephone
	                    String telephone;
	                    while (true) {
	                        System.out.println("Enter Telephone (max 10 characters):");
	                        telephone = sc.next();
	                        if (telephone.length() <= 10) {
	                            break;
	                        } else {
	                            System.out.println("Telephone should have at most 10 characters. Please try again.");
	                        }
	                    }
	                    

	                    //Validate user input for Email
	                    String email;
	                    while (true) {
	                        System.out.println("Enter Email (max 40 characters):");
	                        email = sc.next();
	                        if (email.length() <= 40) {
	                            break;
	                        } else {
	                            System.out.println("Email should have at most 40 characters. Please try again.");
	                        }
	                    }
	                    
	                    

	                    psCheck.setString(1, firstName);
	                    psCheck.setString(2, lastName);
	                    psCheck.setInt(3, age);
	                    ResultSet rsCheck = psCheck.executeQuery();
	                    rsCheck.next();
	                    int count = rsCheck.getInt(1);

	                    if (count > 0) {
	                        System.out.println("Record with the same first name, last name, and age already exists. Update aborted.");
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
                System.out.println("Invalid choice. Please enter a valid option.");  //Validation - Raise an error message for input other than integer
                sc.next(); 
            }
	    }
		System.out.println("Have a nice day!");
	}
}
