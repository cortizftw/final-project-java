package FinalProject;
//Create a Staff class that will be used to encapsulate staff information

public class Staff implements Comparable<Staff> {
	private String id;
    private String lastName;
    private String firstName;
    private char mi;
    private int age;
    private String address;
    private String city;
    private String state;
    private String telephone;
    private String email;

    // Constructor
    public Staff(String id, String lastName, String firstName, char mi, int age, String address, String city, String state, String telephone, String email) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.mi = mi;
        this.age = age;
        this.address = address;
        this.city = city;
        this.state = state;
        this.telephone = telephone;
        this.email = email;
    }

    // Getters and setters for the fields	    
    public String getId() {
        return id;
    }

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public char getMi() {
		return mi;
	}

	public int getAge() {
		return age;
	}

	public String getAddress() {
		return address;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getTelephone() {
		return telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setMi(char mi) {
		this.mi = mi;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
    public int compareTo(Staff otherStaff) {
        // Compare staff by age
        return Integer.compare(this.age, otherStaff.age);
    }
}
