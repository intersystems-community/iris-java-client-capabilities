package summit;
import java.util.Random;
import java.sql.Date;

import summit.Participant;
import summit.Cohort;

public class _DummyDataGenerator {
	// Create dummy data
	public static Cohort[] generateSampleCohortData() throws Exception 
	{
		Cohort[] data = new Cohort[PossibleDescriptions.length];
		
		for (int i=0; i < PossibleDescriptions.length; i++) 
		{
			data[i] = new Cohort(PossibleDescriptions[i]);
		}

        return data;
	}

	private static String[] PossibleDescriptions = {
		"Mass. General Hospital",
		"Florida Hospital",
		"New York-Presbyterian Hospital",
		"Jackson Memorial Hospital", 
		"University of Pittsburgh Medical Center", 
		"Montefiore Medical Center",
		"The Cleveland Clinic",
		"Methodist University Hospital",
		"Barnes-Jewish Hospital",
		"The Mount Sinai Medical Center",
		"Norton Hospital",
		"Erie County Medical Center",
		"Memorial Hermann Southwest Hospital",
		"UAB Hospital",
		"Christiana Hospital",
		"Beaumont Hospital",
		"Spectrum Health Butterworth Hospital",
		"Memorial Regional Hospital",
		"Bergen Regional Medical Center",
		"Beth Israel Medical Center",
		"Ohio State University Hospital",
		"Brookdale University Hospital and Medical Center"
    };
    
	// Create dummy data
	public static Participant[] generateSampleParticipantData(int objectCount) throws Exception 
	{
		Participant[] data = new Participant[objectCount];
		
		for (int i=0; i<objectCount; i++) 
		{
			data[i] = new Participant(randomName(), randomDateOfBirth());
		}

        return data;
	}

	private static String randomName() {
	    Random rand = new Random();
		return NotableImmunologists[rand.nextInt(NotableImmunologists.length)];
	}

	private static Date randomDateOfBirth() {
		Random rand = new Random();

		int year  = rand.nextInt(100);
		int month = 1+rand.nextInt(11);
		int day = 1+rand.nextInt(27);
		return new Date(year, month, day);
	}
	
	private static String[] NotableImmunologists = {
		"Paul Ehrlich",
		"Bruce A. Beutler",
		"Ralph M. Steinman",
		"Jules Hoffmann", 
		"Alan Aderem", 
		"Lorne Babiuk",
		"William Coley",
		"Yehuda Danon",
		"Deborah Doniach",
		"Eva Engvall",
		"Denise Faustman",
		"William Frankland",
		"Ian Frazer",
		"Samuel O. Freedman",
		"Jules T. Freund",
		"Sankar Ghosh",
		"John Grange",
		"Waldemar Haffkine",
		"Michael Heidelberger",
		"George Heist",
		"Leonard Herzenberg",
		"Miroslav Holub",
		"Charles Janeway",
		"Dermot Kelleher",
		"Tadamitsu Kishimoto",
		"Jan Klein",
		"Mary Loveless",
		"Tak Wah Mak",
		"Polly Matzinger",
		"Ira Mellman",
		"Jacques Miller",
		"Avrion Mitchison",
		"Michael Neuberger",
		"Alan Munro",
		"Gustav Nossal",
		"Santa J. Ono",
		"Thomas Platts-Mills",
		"Nicholas P. Restifo",
		"Fred Rosen",
		"Richard R. Rosenthal",
		"Louis W. Sauer",
		"Emil Skamene",
		"David Talmage",
		"Reyes Tamez",
		"Kevin J. Tracey",
		"Jan Vilcek",
		"Ellen Vitetta",
		"Alexander S. Wiener",
		"Don Wiley",
		"Ian Wilson",
		"Ernst Witebsky",
		"Jian Zhou",
		"Ivan Roitt",
		"James S. Tan",
		"Mike Belosevic",
		"Shimon Sakaguchi",
		"Alberto Mantovani"
	};


}
