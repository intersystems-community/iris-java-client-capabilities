package summit;

import java.sql.Date;

public class Participant {
	public String name;
	public Date dob;
	public Integer cohortID;

	// Constructors
	public Participant(){}
	
	public Participant(String name, Date dob) { 
		this.name = name;
		this.dob = dob;
	}

}
