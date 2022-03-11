/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

/* PROFILE CLASS */
class Profile {	

	public static String userId;
	public static String password;
	public static String email;
	public static String fullname;
	public static String dateOfBirth;

	public static boolean active = false;

	private static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));
	
	Profile(String[] info) throws SQLException {
		setId(info[1]);
		setName(info[2]);
		setEmail(info[3]);
		setBirth(info[4]);
	}

	public static void setId(String id) { userId = id; }
	public static void setName(String name) { fullname = name; }
	public static void setPass(String pwd) { password = pwd; }
	public static void setEmail(String em) { email = em; }
	public static void setBirth(String birth) { dateOfBirth = birth; }
	public static void setActive(boolean x){ active = x; }
/*
	public static void viewProfile(){
		while(active){
			System.out.println(userId + "'s Profile");
			System.out.println("---------");
			System.out.println("Name: " + fullname);
			System.out.println("Email: " + email);
			System.out.println("Birthday: " + dateOfBirth);
			System.out.println();
			System.out.println("Select an option:");
			System.out.println("1. Send a message");
			System.out.println("2. View friends list");
			System.out.println("3. Remove friend");
			System.out.println("9. Go back");

			switch (readChoice()){
               			case 1: break;
               			case 2: break;
               			case 3: break;
               			case 9: setActive(false); return;
               			default : System.out.println("Unrecognized choice!"); break;
            		}
		}
	}

	public static int readChoice() {
      		int input;     
      		do {
         		System.out.print("Please make your choice: ");
         		try { 
            			input = Integer.parseInt(in.readLine());
            			break;
         		}catch (Exception e) {
            			System.out.println("Your input is invalid!");
            			continue;
         		}
      		} while (true);
      		return input;
   	}
*/
}

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class ProfNetwork {

   // reference to physical database connection.
   private Connection _connection = null;
   
   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));
   static boolean print = false;
   public static String[] profInfo;
   /**
    * Creates a new instance of ProfNetwork
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public ProfNetwork (String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end ProfNetwork

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
	 if(outputHeader){
	    for(int i = 1; i <= numCol; i++){
		System.out.print(rsmd.getColumnName(i) + "\t");
	    }
	    System.out.println();
	    outputHeader = false;
	 }
         for (int i=1; i<=numCol; ++i){
            System.out.print (rs.getString (i) + "\t");
            System.out.println ();
	    profInfo[i] = rs.getString(i);
            ++rowCount;
	
         }//end while
      }
	stmt.close ();
	return rowCount;
  }

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
          List<String> record = new ArrayList<String>();
         for (int i=1; i<=numCol; ++i)
            record.add(rs.getString (i));
         result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */

   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();
       
       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       if(rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   public String[] fillProfile (String query) throws SQLException {
	String[] data = new String[5];
	Statement stmt = this._connection.createStatement ();
	ResultSet rs = stmt.executeQuery (query);
	ResultSetMetaData rsmd = rs.getMetaData ();
	
	int numCol = rsmd.getColumnCount ();
        int rowCount = 0;
	boolean outputHeader = true;
      while (rs.next()){
	 if(outputHeader){
	    for(int i = 1; i <= numCol; i++){
		//System.out.print(rsmd.getColumnName(i) + "\t");
	    }
	    System.out.println();
	    outputHeader = false;
	 }
         for (int i=1; i<=numCol; ++i){
            //System.out.print (rs.getString (i) + "\t");
         System.out.println ();
	    data[i] = rs.getString(i);
      }
	stmt.close ();	
     }
	return data;
  }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
	Statement stmt = this._connection.createStatement ();

	ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
	if (rs.next())
		return rs.getInt(1);
	return -1;
   }

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            ProfNetwork.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      ProfNetwork esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the ProfNetwork object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new ProfNetwork (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("3. Change a user password");
            System.out.println("9. < EXIT");
            String authorisedUser = null;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); break;
               case 3: ChangePassword(esql); break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser != null) {
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("MAIN MENU");
                System.out.println("---------");
                System.out.println("1. Goto Friend List");
                System.out.println("2. Update Profile");
                System.out.println("3. Write a new message");
                System.out.println("4. Send Friend Request");
		System.out.println("5. Search for a user");
                System.out.println(".........................");
                System.out.println("9. Log out");
                switch (readChoice()){
                   case 1: FriendList(esql); break;
                   case 2: UpdateProfile(esql); break;
                   case 3: NewMessage(esql); break;
                   case 4: SendRequest(esql); break;
		   case 5: SearchUser(esql); break;
                   case 9: usermenu = false; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /*
    * Creates a new user with privided login, passowrd and phoneNum
    * An empty block and contact list would be generated and associated with a user
    **/
   public static void CreateUser(ProfNetwork esql){
      try{
         System.out.print("\tEnter user login: ");
         String login = in.readLine();
         System.out.print("\tEnter user password: ");
         String password = in.readLine();
         if (password.length() == 0){
            System.out.println("New password can't be empty. Try again.");
            return;
         }
         System.out.print("\tEnter user email: ");
         String email = in.readLine();

	 //Creating empty contact\block lists for a user
   //Due to an error from "contact list", for now, removed the contact list parameter. May need to add that variable to our create_tables.sql file later and do something with it
	 String query = String.format("INSERT INTO USR (userId, password, email) VALUES ('%s','%s','%s')", login, password, email);

   //orginal instrcution below
   //String query = String.format("INSERT INTO USR (userId, password, email, contact_list) VALUES ('%s','%s','%s')", login, password, email);

         esql.executeUpdate(query);
         System.out.println ("User successfully created!");
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }//end

   /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static String LogIn(ProfNetwork esql){
      try{
         System.out.print("\tEnter user login: ");
         String login = in.readLine();
         System.out.print("\tEnter user password: ");
         String password = in.readLine();

         String query = String.format("SELECT * FROM USR WHERE userId = '%s' AND password = '%s'", login, password);
         int userNum = esql.executeQuery(query);
	 if (userNum > 0)
		return login;
         return null;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end

// Rest of the functions definition go in here

   /*
    * Method to change password for a user without being logged in as that user
    **/
   public static void ChangePassword(ProfNetwork esql){
      try{
         System.out.print("\tEnter user login to change password for: ");
         String login = in.readLine();
         String query = String.format("SELECT * FROM USR WHERE userID='%s'", login);
         int get_user = esql.executeQuery(query);
         if (get_user <= 0){
           System.out.println("User not found. Try Again.");
           return;
         }
         else{
           System.out.print("\tEnter current user password: ");
           String old_password = in.readLine();
           String query2 = String.format("SELECT * FROM USR WHERE userID='%s' AND password='%s'", login, old_password);
           int verify = esql.executeQuery(query2);
           if (verify <= 0){
             System.out.println("Incorrect current Password. Try Again.");
             return;
           }
           else{
             System.out.print("\tEnter new user password: ");
             String new_password = in.readLine();
             if (new_password.length() == 0){
                System.out.println("New password can't be empty. Try again.");
                return;
             }
             String query3 = String.format("UPDATE USR SET password='%s' WHERE userID='%s'", new_password, login);
             esql.executeUpdate(query3);
             System.out.println("Successfully updated password.");
             return;
           }
         }
      }
      catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }//end

   public static void FriendList(ProfNetwork esql){

   }

   public static void UpdateProfile(ProfNetwork esql){

   }

   public static void NewMessage(ProfNetwork esql){

   }

   public static void SendRequest(ProfNetwork esql){

   }

   public static void viewProfile(Profile x, ProfNetwork esql, String query){
	try{
		profInfo = esql.fillProfile(query);
                Profile prof = new Profile(profInfo);
                prof.setActive(true);
                while(x.active){
                        System.out.println(x.userId + "'s Profile");
                        System.out.println("---------");
                        System.out.println("Name: " + x.fullname);
                        System.out.println("Email: " + x.email);
                        System.out.println("Birthday: " + x.dateOfBirth);
                        System.out.println();
                        System.out.println("Select an option:");
                        System.out.println("1. Send a message");
                        System.out.println("2. View friends list");
                        System.out.println("3. Remove friend");
                        System.out.println("9. Go back");

                        switch (readChoice()){
                                case 1: break;
                                case 2: break;
                                case 3: break;
                                case 9: x.setActive(false); return;
                                default : System.out.println("Unrecognized choice!"); break;
                        }
		} 
	} catch (Exception e) {
		System.err.println (e.getMessage ());
	}
    }

   public static void SearchUser(ProfNetwork esql){
	try {
   		System.out.print("\tEnter a user ID to search for: ");
        	String user = in.readLine();
        	String query = String.format("SELECT userId, name, email, dateOfBirth FROM USR WHERE userID='%s'", user);  

        	int get_user = esql.executeQuery(query);
        	if (get_user <= 0){
        		System.out.println("User not found. Try Again.");
                	return;
        	} else {
			profInfo = esql.fillProfile(query);
			Profile prof = new Profile(profInfo);
			prof.setActive(true);
			esql.viewProfile(prof, esql, query);
		}
		
   	} catch (Exception e) {
		System.err.println (e.getMessage ());
	}
  }
}//end ProfNetwork
