package junit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

import snowflake.Snowflake;

/**
 * Tests Snowflake queries.
 */
public class TestSnowflake {   

    /**
	 * Class being tested
	 */
	private static Snowflake q;
	
	/**
	 * Connection to the database
	 */
	private static Connection con;
	
	/**
	 * Requests a connection to the database.
	 * 
	 * @throws Exception
	 * 		if an error occurs
	 */
	@BeforeAll
	public static void init() throws Exception 
	{		
		q = new Snowflake();
		con = q.connect();					
	}
	
	/**
	 * Closes connection.
	 * 
	 * @throws Exception
	 * 		if an error occurs
	 */
	@AfterAll
	public static void end() throws Exception 
	{
		q.close();        
	}

    /**
     * Tests query 1.
     */
    @Test
    public void testQuery1() throws SQLException
    {        	    	
    	ResultSet rst = q.query1();
    	
    	// Verify result
    	String answer = 
                "Total columns: 4"
                +"\nN_NAME, O_ORDERPRIORITY, NUMORDERS, TOTALORDERVALUE"
                +"\nINDONESIA, 3-MEDIUM, 10138, 1538656299.95"
                +"\nRUSSIA, 2-HIGH, 10127, 1524716120.36"
                +"\nCANADA, 2-HIGH, 10114, 1526605198.22"
                +"\nFRANCE, 2-HIGH, 10100, 1541481488.81"
                +"\nROMANIA, 3-MEDIUM, 10081, 1522568597.50"
                +"\nCHINA, 1-URGENT, 10071, 1500982477.43"
                +"\nINDONESIA, 2-HIGH, 10060, 1514514681.21"
                +"\nMOZAMBIQUE, 3-MEDIUM, 10059, 1523366396.29"
                +"\nRUSSIA, 3-MEDIUM, 10039, 1519233813.99"
                +"\nBRAZIL, 2-HIGH, 10028, 1511998411.58"
                +"\nTotal results: 10";

    	String queryResult = Snowflake.resultSetToString(rst, 100);
    	System.out.println(queryResult);
    	assertEquals(answer, queryResult);    	    
    }

    /**
     * Tests query 2.
     */
    @Test
    public void testQuery2() throws SQLException
    {        	    	
    	ResultSet rst = q.query2();
    	
    	// Verify result
    	String answer = 
                "Total columns: 3"
                +"\nEMPLOYEENAME, NUMORDERS, TOTALORDERVALUE"
                +"\nD. Davis, 15, 78.69"
                +"\nE. Edwards, 2, 39.45"
                +"\nM. Clark, 4, 24.84"
                +"\nA. Allen, 11, 24.29"
                +"\nI. Irvin, 1, 19.50"
                +"\nTotal results: 5";

    	String queryResult = Snowflake.resultSetToString(rst, 100);
    	System.out.println(queryResult);
    	assertEquals(answer, queryResult);    	    
    }

    /**
     * Tests query 3.
     */
    @Test
    public void testQuery3() throws SQLException
    {        	    	
    	ResultSet rst = q.query3();
    	
    	// Verify result
    	String answer = 
                "Total columns: 3"
                +"\nCOMPANY, YEAR, TOTALORDERS"
                +"\nmain company, 1992, 227089"
                +"\nmain company, 1993, 226645"
                +"\nmain company, 1994, 227597"
                +"\nmain company, 1995, 228637"
                +"\nmain company, 1996, 228626"
                +"\nmain company, 1997, 227783"
                +"\nmain company, 1998, 133623"
                +"\nnew company, 2021, 34"
                +"\nnew company, 2022, 29"
                +"\nTotal results: 9";

    	String queryResult = Snowflake.resultSetToString(rst, 100);
    	System.out.println(queryResult);
    	assertEquals(answer, queryResult);    	    
    }

    /**
     * Tests query 4.
     */
    @Test
    public void testQuery4() throws SQLException
    {        	    	
    	ResultSet rst = q.query4();
    	
    	// Verify result
    	String answer = 
                "Total columns: 3"
                +"\nCOMPANY, EMPLOYEENAME, TOTALORDERVALUE"
                +"\nnew company, D. Davis, 4134.80"
                +"\nnew company, I. Irvin, 2221.98"
                +"\nmain company, Clerk#000000828, 248531518.62"
                +"\nmain company, Clerk#000000081, 245978259.29"
                +"\nTotal results: 4";

    	String queryResult = Snowflake.resultSetToString(rst, 100);
    	System.out.println(queryResult);
    	assertEquals(answer, queryResult);    	    
    }
}
