package summit;

import java.io.*;
import java.sql.*;
import java.util.*; 

import com.intersystems.jdbc.*;
import com.intersystems.xep.*;

import summit.Participant;
import summit.Cohort;
import summit._DummyDataGenerator;

public class App
{
    public App(){}

    public void Step1_CreateDatabaseSchema() throws Exception
    {
        EventPersister xepPersister = GetPersister();

        // Import flat schema
        xepPersister.importSchema("summit.Participant");
        xepPersister.importSchema("summit.Cohort");

        IRISConnection conn = GetConnection();
        Statement statement = conn.createStatement();
    
        String createTable = "" +
            "CREATE TABLE summit.TestResult (" +
            "    ID INT PRIMARY KEY IDENTITY (1, 1), " +
            "    participantID INT NOT NULL," +
            "    success INT" +
            ")";
        statement.executeUpdate(createTable);
        
        statement.close();
        conn.close();
    }

    public void Step2_CreateCohorts() throws Exception
    {
        EventPersister xepPersister = GetPersister();

        // Create dummy cohorts in memory
        Cohort[] c = _DummyDataGenerator.generateSampleCohortData();

        // Save Cohorts
        Event xepEvent = xepPersister.getEvent("summit.Cohort");
        xepEvent.store(c);
        xepEvent.close();

        xepPersister.close();
    }

    public void Step3_CreateParticipants( int participantCount ) throws Exception
    {
        EventPersister xepPersister = GetPersister();

        // Create Participants
        Participant[] p = _DummyDataGenerator.generateSampleParticipantData(participantCount);

        // Create and Save Particpants
        Event xepEvent = xepPersister.getEvent("summit.Participant");
        xepEvent.store(p);
        xepEvent.close();

        xepPersister.close();
    }

    // Assign participants randomly to cohorts via the native API
    public void Step4_AssignCohorts() throws Exception
    {
        IRISConnection conn = GetConnection();
        IRIS iris = IRIS.createIRIS(conn);

        iris.classMethodStatusCode("User.SummitAssigner","AssignParticipantsToCohorts");

        iris.close();
        conn.close();
    }

    public void Step5_SimulateTesting() throws Exception
    {
        Random rd = new Random();
        IRISConnection conn = GetConnection();
        IRIS iris = IRIS.createIRIS(conn);

        IRISList participants = iris.classMethodIRISList("User.SummitAssigner","GetUntestedParticipants");

        for (int i=1; i<= participants.count(); i++) {
            int participantId = Integer.parseInt(new String((byte[])(participants.get(i))));

            IRISObject test = (IRISObject)iris.classMethodObject("summit.TestResult", "%New");
            test.set("participantID", participantId);
            test.set("success", rd.nextBoolean());
            test.invokeVoid("%Save");
        }

        iris.close();
        conn.close();
    }

    public void Step6_Report() throws Exception
    {
        IRISConnection conn = GetConnection();

        String query = 
            "SELECT c.description, COUNT(p.ID) AS TotalParticipants, COUNT(t.ID) AS TotalTests, COUNT(t.success %AFTERHAVING) AS SuccessfulTests " +
            "FROM summit.participant p, summit.testresult t, summit.cohort c " +
            "WHERE t.participantID = p.ID AND p.cohortID = c.ID " +
            "GROUP BY c.ID " +
            "HAVING t.success > 0 " +
            "ORDER BY c.ID";

        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        System.out.println("Reporting which cohorts passed the "+inCyan("50%")+" success criteria...\n");
        while (resultSet.next()) {
            Boolean success = resultSet.getInt(4) > (resultSet.getInt(3) / 2);
            System.out.println(" " + ( (success) ? inGreen("+ "+resultSet.getString(1)) : inRed("- "+resultSet.getString(1))) + " " + inCyan(resultSet.getString(2)) + " participants, " + inCyan(resultSet.getString(4)) + " successful tests");
        }

        resultSet.close();
        statement.close();
        conn.close();
    }

    // Console coloring helpers
    private String inGreen(String msg) {
        return "\033[32m"+msg+"\033[0m";
    }
    private String inRed(String msg) {
        return "\033[31m"+msg+"\033[0m";
    }
    private String inCyan(String msg) {
        return "\033[36m"+msg+"\033[0m";
    }

    public void Step0_DeleteSchema() throws Exception 
    {
        EventPersister xepPersister = GetPersister();

        // Remove old test data, in case you want to run this tests more than once
        xepPersister.deleteExtent("summit.Participant");
        xepPersister.deleteExtent("summit.Cohort");
    
        IRISConnection conn = GetConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("DROP TABLE summit.TestResult");
            
        statement.close();
        conn.close();   
    }


    // Database connection helpers

    private EventPersister GetPersister() throws Exception
    {
        EventPersister xepPersister = PersisterFactory.createPersister();
        xepPersister.connect(_hostname, _port, _namespace, _user, _password);

        return xepPersister;
    }

    private IRISConnection GetConnection() throws Exception
    {
        Class.forName("com.intersystems.jdbc.IRISDriver");
        return (IRISConnection) DriverManager.getConnection("jdbc:IRIS://"+_hostname+":"+_port+"/"+_namespace,_user,_password);
    }

    // Database connection parameters.
    static public String _hostname = "localhost";
    static public int _port = 1972;
    static public String _namespace = "SUMMIT";
    static public String _user = "_SYSTEM";
    static public String _password = "SYS";
}
