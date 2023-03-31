package snowflake;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Performs queries on Snowflake cloud database.
 */
public class Snowflake {
	/**
	 * Connection to database
	 */
	private Connection con;
	
	/**
	 * Main method
	 * 
	 * @param args
	 *             no arguments required
	 */
	public static void main(String[] args) throws Exception {
		Snowflake sf = new Snowflake();
		sf.connect();	
		
		// Test sample query
		//System.out.println(Snowflake.resultSetToString(sf.queryExample(), 1000));

		// Test four queries that you must write
		//System.out.println(Snowflake.resultSetToString(sf.query1(), 1000));
		System.out.println(Snowflake.resultSetToString(sf.query2(), 1000));
		//System.out.println(Snowflake.resultSetToString(sf.query3(), 1000));
		//System.out.println(Snowflake.resultSetToString(sf.query4(), 1000));

		sf.close();
	}


	/**
	 * Connects to Snowflake and returns connection.
	 * 
	 * @return
	 *         connection
	 */
	public Connection connect() throws SQLException {
		// TODO: Fill in your url, user id, and password
		String accountNum = "tt26672."; // Can be found in the url when on the website
		String serverLoc = "west-us-2.azure"; // First part is server location, then server type
		String url = "jdbc:snowflake://" + accountNum + serverLoc + ".snowflakecomputing.com";
		String uid = "bluemailbox"; // Not username.west-us-2.azure
		String pw = "#08hONDAFIT"; // Account password

		System.out.println("Connecting to database.");
		con = DriverManager.getConnection(url, uid, pw);
		Statement stmt = con.createStatement();
		stmt.execute("ALTER SESSION SET JDBC_QUERY_RESULT_FORMAT='JSON'");
		return con;
	}

	/**
	 * Closes connection to database.
	 */
	public void close()
	{
		System.out.println("Closing database connection.");
		try
		{
			if (con != null)
	            con.close();
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}

	/**
	 * Performs a query on sample TPCH-SF1 data.
	 * Note the use of DATABASE_NAME.SCHEMA_NAME.TABLE_NAME when referencing a particular table.
	 */
	public ResultSet queryExample() throws SQLException {
		Statement stmt = con.createStatement();
		
		return stmt.executeQuery("SELECT * FROM SNOWFLAKE_SAMPLE_DATA.TPCH_SF1.Nation N LIMIT 5");
	}

	/**
	 * Write the following query on the SNOWFLAKE_SAMPLE_DATA.TPCH_SF1 dataset.
	 * 
	 * Query must return the top 10 countries (N_NAME) based on number of orders for customers in that 
	 * country. Number of orders are grouped by country and by order priority.
	 * Only consider customers with account balance greater than $1000 and order priorities
	 * of '1-URGENT', '2-HIGH', '3-MEDIUM'.
	 * Also return totalOrderValue which adds up all of the order totals (o_totalprice).
	 */
	public ResultSet query1() throws SQLException {				
		// TODO: Write query
		Statement stmt = con.createStatement();
		return stmt.executeQuery("SELECT N_NAME, O_ORDERPRIORITY, COUNT(*) AS NUMORDERS, SUM(O_TOTALPRICE) as TOTALORDERVALUE " + 
		"FROM SNOWFLAKE_SAMPLE_DATA.TPCH_SF1.Nation " + 
		"JOIN SNOWFLAKE_SAMPLE_DATA.TPCH_SF1.Customer " +
		"JOIN SNOWFLAKE_SAMPLE_DATA.TPCH_SF1.Orders " +
		"ON NATION.N_NATIONKEY = CUSTOMER.C_NATIONKEY " +
		"AND CUSTOMER.C_CUSTKEY = ORDERS.O_CUSTKEY " +
		"WHERE C_ACCTBAL > 1000 AND (O_ORDERPRIORITY = '1-URGENT' OR O_ORDERPRIORITY = '2-HIGH' OR O_ORDERPRIORITY = '3-MEDIUM') " +
		"GROUP BY N_NAME, O_ORDERPRIORITY " + 
		"ORDER BY COUNT(*) DESC " +
		"LIMIT 10");
			
	}


	/**
	 * Write the following query on the loaded data set (LAB).
	 * 
	 * Query must return the top 5 employees based on total value of ordered items descending (totalOrderValue). 
	 * Also return the number of orders (numOrders).
	 * Products and orders are only counted if the product list price is between 50 cents and $100.
	 * A customer on an order must have an 'e' in their name (case-insensitive.)
	 * Note: String comparison in Snowflake is case-sensitive so will need a comparison in WHERE clause
	 * for both upper and lower case e.
	 * 
	 * NOTE: Answering this query requires the data in the CSV files to have been loaded into Snowflake.
	 */
	public ResultSet query2() throws SQLException {
		// TODO: Write query
		Statement stmt = con.createStatement();
		return stmt.executeQuery("SELECT EMPLOYEENAME, COUNT(DISTINCT ORDERS.ORDERID) AS NUMORDERS, SUM(ORDEREDPRODUCT.QUANTITY * ORDEREDPRODUCT.PRICE) as TOTALORDERVALUE " +
		"FROM LAB.PUBLIC.ORDERS " +
		"JOIN LAB.PUBLIC.ORDEREDPRODUCT " + 
		"JOIN LAB.PUBLIC.PRODUCT " + 
		"JOIN LAB.PUBLIC.CUSTOMER " + 
		"JOIN LAB.PUBLIC.EMPLOYEE " +
		"ON ORDERS.ORDERID = ORDEREDPRODUCT.ORDERID " +
		"AND ORDEREDPRODUCT.PRODUCTID = PRODUCT.PRODUCTID " +
		"AND ORDERS.CUSTOMERID = CUSTOMER.CUSTOMERID " +
		"AND ORDERS.EMPLOYEEID = EMPLOYEE.EMPLOYEEID " +
		"WHERE LISTPRICE > '0.50' AND LISTPRICE < '100.00' AND (CUSTOMERNAME LIKE '%e%' OR CUSTOMERNAME LIKE '%E%') " +
		"GROUP BY EMPLOYEENAME " +
		"ORDER BY TOTALORDERVALUE DESC " +
		"LIMIT 5");
	}
	
	/**
	 * Write the following query that uses both the sample TPCH-SF1 data and the loaded data set (LAB).
	 * 
	 * Assume the TPCH-SF1 data is for the "main company" and the lab data is for the "new company" just acquired.
	 *
	 * Return the total number of orders for the main company and the new company grouped by year.
	 * 
	 * NOTE: Answering this query requires the data in the CSV files to have been loaded into Snowflake.
	 */
	public ResultSet query3() throws SQLException {
		// TODO: Write query
		Statement stmt = con.createStatement();
        return stmt.executeQuery("(SELECT 'new company' AS COMPANY, YEAR(ORDERDATE) as Year, count(*) as TOTALORDERS " + 
		"FROM LAB.PUBLIC.ORDERS " + 
		"GROUP BY YEAR(ORDERDATE) " + 
		"UNION SELECT 'main company' AS COMPANY, YEAR(O_ORDERDATE) as Year, count(*) as TOTALORDERS " + 
		"FROM SNOWFLAKE_SAMPLE_DATA.TPCH_SF1.Orders " + 
		"GROUP BY YEAR(O_ORDERDATE)) " + 
		"ORDER BY Year");		
	}

	/**
	 * Write the following query that uses both the sample TPCH-SF1 data and the loaded data set (LAB).
	 * 
	 * Assume the TPCH-SF1 data is for the "main company" and the lab data is for the "new company" just acquired.
	 *
	 * Return the top two employees from each company in terms of total sales (totalOrderValue).
	 * For the "new company", use the employee name.
	 * For the "main company", use the o_clerk field in the Orders table.	 
	 * 
	 * HINT: LIMIT can be used with UNION ALL if you put each query in parenthesis.
	 * 
	 * NOTE: Answering this query requires the data in the CSV files to have been loaded into Snowflake.
	 */
	public ResultSet query4() throws SQLException {
		// TODO: Write query
		Statement stmt = con.createStatement();
        return stmt.executeQuery("(SELECT 'new company' AS COMPANY, EMPLOYEE.EMPLOYEENAME, SUM(TOTAL) as TOTALORDERVALUE " +
		"FROM LAB.PUBLIC.ORDERS JOIN LAB.PUBLIC.EMPLOYEE ON ORDERS.EMPLOYEEID = EMPLOYEE.EMPLOYEEID " +
		"GROUP BY EMPLOYEENAME ORDER BY SUM(TOTAL) DESC LIMIT 2) " +
		"UNION ALL (SELECT 'main company' AS COMPANY, O_CLERK, SUM(O_TOTALPRICE) as TOTALORDERVALUE " +
		"FROM SNOWFLAKE_SAMPLE_DATA.TPCH_SF1.Orders GROUP BY O_CLERK ORDER BY SUM(O_TOTALPRICE) DESC LIMIT 2)");					
	}

	/*
	 * Do not change anything below here.
	 */
	/**
     * Converts a ResultSet to a string with a given number of rows displayed.
     * Total rows are determined but only the first few are put into a string.
     * 
     * @param rst
     * 		ResultSet
     * @param maxrows
     * 		maximum number of rows to display
     * @return
     * 		String form of results
     * @throws SQLException
     * 		if a database error occurs
     */    
    public static String resultSetToString(ResultSet rst, int maxrows) throws SQLException
    {                       
        StringBuffer buf = new StringBuffer(5000);
        int rowCount = 0;
        ResultSetMetaData meta = rst.getMetaData();
        buf.append("Total columns: " + meta.getColumnCount());
        buf.append('\n');
        if (meta.getColumnCount() > 0)
            buf.append(meta.getColumnName(1));
        for (int j = 2; j <= meta.getColumnCount(); j++)
            buf.append(", " + meta.getColumnName(j));
        buf.append('\n');
                
        while (rst.next()) 
        {
            if (rowCount < maxrows)
            {
                for (int j = 0; j < meta.getColumnCount(); j++) 
                { 
                	Object obj = rst.getObject(j + 1);                	 	                       	                                	
                	buf.append(obj);                    
                    if (j != meta.getColumnCount() - 1)
                        buf.append(", ");                    
                }
                buf.append('\n');
            }
            rowCount++;
        }            
        buf.append("Total results: " + rowCount);
        return buf.toString();
    }
    
    /**
     * Converts ResultSetMetaData into a string.
     * 
     * @param meta
     * 		 ResultSetMetaData
     * @return
     * 		string form of metadata
     * @throws SQLException
     * 		if a database error occurs
     */
    public static String resultSetMetaDataToString(ResultSetMetaData meta) throws SQLException
    {
	    StringBuffer buf = new StringBuffer(5000);                                   
	    buf.append(meta.getColumnName(1)+" ("+meta.getColumnLabel(1)+", "+meta.getColumnType(1)+"-"+meta.getColumnTypeName(1)+", "+meta.getColumnDisplaySize(1)+", "+meta.getPrecision(1)+", "+meta.getScale(1)+")");
	    for (int j = 2; j <= meta.getColumnCount(); j++)
	        buf.append(", "+meta.getColumnName(j)+" ("+meta.getColumnLabel(j)+", "+meta.getColumnType(j)+"-"+meta.getColumnTypeName(j)+", "+meta.getColumnDisplaySize(j)+", "+meta.getPrecision(j)+", "+meta.getScale(j)+")");
	    return buf.toString();
    }	
}
