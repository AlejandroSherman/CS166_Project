/*
 * Alejandro Sherman (asher011, SID: 862062898)
 * Briana McGhee (bmcgh001, SID: 861295405)
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
import java.sql.Date;
import java.sql.Timestamp;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

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
   public static String authorisedUser = null;
   public static int connection_level = 0;
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
   //         String authorisedUser = null;
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
                System.out.println("7. View Incoming Connection Requests");
                System.out.println("8. View Messages");
                System.out.println(".........................");
                System.out.println("9. Log out");
		System.out.println();
		connection_level = 0;
                System.out.println("Connection Level: " + connection_level);
		System.out.println();
                switch (readChoice()){
                   case 1: FriendList(esql, authorisedUser); break;
                   case 2: UpdateProfile(esql, authorisedUser); break;
                   case 3: NewMessage(esql, authorisedUser); break;
		   case 5: SearchUser(esql); break;
                   case 4: SendRequest(esql, authorisedUser); break;
                   case 7: ViewRequests(esql, authorisedUser); break;
                   case 8: ViewMessages(esql, authorisedUser); break;
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
         String query2 = String.format("SELECT * FROM USR WHERE userId='%s'", login);
         int get_user = esql.executeQuery(query2);
         if (get_user <= 0){
           System.out.println("User not found. Try Again.");
           return null;
         }
         else{
           System.out.println("Incorrect Password. Try Again.");
           return null;
         }
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
         String query = String.format("SELECT * FROM USR WHERE userId='%s'", login);
         int get_user = esql.executeQuery(query);
         if (get_user <= 0){
           System.out.println("User not found. Try Again.");
           return;
         }
         else{
           System.out.print("\tEnter current user password: ");
           String old_password = in.readLine();
           String query2 = String.format("SELECT * FROM USR WHERE userId='%s' AND password='%s'", login, old_password);
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
             String query3 = String.format("UPDATE USR SET password='%s' WHERE userId='%s'", new_password, login);
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

   public static void DeleteFriend(ProfNetwork esql){
	boolean deletemenu = true;
	while (deletemenu){
		try {
   			System.out.print("\tEnter userId to remove: ");
        		String userToDelete = in.readLine();

        		if (userToDelete.length() == 0){
        			System.out.println("Invalid. Try again.");
        	        	return;
        		}

        		String query2 = String.format("SELECT * FROM USR WHERE userId='%s'", userToDelete);
        		int get_user = esql.executeQuery(query2);
        		if (get_user <= 0){
        			System.out.println("User not found. Try Again.");
        	        	return;
        		}

        		String status = "Reject";
        		String query3 = String.format("DELETE FROM CONNECTION_USR WHERE status='Accept' AND userId='%s' AND connectionId='%s'", userToDelete, authorisedUser);
        		String query4 = String.format("DELETE FROM CONNECTION_USR WHERE status='Accept' AND userId='%s' AND connectionId='%s'", authorisedUser, userToDelete);
        		esql.executeUpdate(query3);
        		esql.executeUpdate(query4);
        		System.out.println();
        		String output = String.format("%s has been removed from your friends list", userToDelete);
        		System.out.println(output);
        		System.out.println(".........................");
			deletemenu = false;
		} catch (Exception e) { System.err.println (e.getMessage ()); }
	}
   }
   public static void FriendList(ProfNetwork esql, String user){
	try {
		String query = String.format("SELECT userId FROM CONNECTION_USR WHERE connectionId = '%s' AND status = 'Accept'", user);
		List<List<String>> friends = new ArrayList<List<String>>();
		friends = esql.executeQueryAndReturnResult(query);
		if (friends.isEmpty()){
			System.out.println();
			System.out.println("This user has no friends");
			System.out.println();
			return;
		}
		System.out.println();
                System.out.println("FRIENDS LIST");
                System.out.println("---------");

		for (int i = 0; i < friends.size(); i++) { System.out.println(friends.get(i).get(0)); }

		System.out.println();
                System.out.println("1. Search a user");
		if (user == authorisedUser){
			System.out.println("2. Delete friend");
		}
		System.out.println(".........................");
		System.out.println("9. Go back");

		switch (readChoice()){
        		case 1: esql.SearchUser(esql); break;
                	case 2:	esql.DeleteFriend(esql); break;
                	case 9: return;
                	default : System.out.println("Unrecognized choice!"); break;
        	}
	} catch (Exception e) { System.err.println (e.getMessage()); }
   }

   public static void UpdateProfile(ProfNetwork esql, String authorisedUser){
     try{
       String query;
       int verify;
       boolean updatemenu = true;
       while(updatemenu) {
         System.out.println("UPDATE MENU");
         System.out.println("---------");
         System.out.println("1. Update your Name");
         System.out.println("2. Update your User Login");
         System.out.println("3. Update your Password");
         System.out.println("4. Update your Email");
         System.out.println("5. Update your Date of Birth");
         System.out.println(".........................");
         System.out.println("9. Exit Update Menu");
         switch (readChoice()){
            case 1:
               System.out.print("\tEnter new Name - Press enter by itself to cancel: ");
               String name = in.readLine(); //(Consider adding more edge case checks here. Such as inputting only a first name. Maybe check for at least one space present.)
               if (name.length() == 0){ //Only consider adding the further checks if we have time for it
                  System.out.println("Canceling.");
                  break;
               }
               query = String.format("UPDATE USR SET name='%s' WHERE userId='%s'", name, authorisedUser);
               esql.executeUpdate(query);
               System.out.println("Successfully updated Name.");
               break;
            case 2:
               System.out.print("\tEnter new User Login - Press enter by itself to cancel: ");
               String login = in.readLine();
               if (login.length() == 0){
                  System.out.println("Cancelling.");
                  break;
               }
               query = String.format("UPDATE USR SET userId='%s' WHERE userId='%s'", login, authorisedUser);
               esql.executeUpdate(query);
               System.out.println("Successfully updated User Login.");
               break;
            case 3:
               System.out.print("\tEnter new Password - Press enter by itself to cancel: ");
               String password = in.readLine();
               if (password.length() == 0){
                  System.out.println("Cancelling.");
                  break;
               }
               query = String.format("UPDATE USR SET password='%s' WHERE userId='%s'", password, authorisedUser);
               esql.executeUpdate(query);
               System.out.println("Successfully updated Password.");
               break;
            case 4:
               System.out.print("\tEnter new email - Press enter by itself to cancel: ");
               String email = in.readLine();
               if (email.length() == 0){
                  System.out.println("Cancelling.");
                  break;
               }
               query = String.format("UPDATE USR SET email='%s' WHERE userId='%s'", email, authorisedUser);
               esql.executeUpdate(query);
               System.out.println("Successfully updated Email.");
               break;
            case 5:
               System.out.print("\tEnter Four Digit Birth Year: "); //Could add another check to ensure they really input digits instead of letters
               String year = in.readLine();
               if (year.length() != 4){
                  System.out.println("Birth year must be 4 digits. Try again.");
                  break;
               }
               System.out.print("\tEnter Two Digit Birth Month: ");
               String month = in.readLine();
               if (month.length() != 2){
                  System.out.println("Birth month must be 2 digits. Try again.");
                  break;
               }
               System.out.print("\tEnter Two Digit Birth Day: ");
               String day = in.readLine();
               if (day.length() != 2){
                  System.out.println("Birth Day must be 2 digits. Try again.");
                  break;
               }
               String hold = year + '-' + month + '-' + day;
               Date dob = Date.valueOf(hold);//converting string into sql date
               query = String.format("UPDATE USR SET dateOfBirth='%s' WHERE userId='%s'", dob, authorisedUser);
               esql.executeUpdate(query);
               System.out.println("Successfully updated Date Of Birth.");
               break;
            case 9: updatemenu = false; break;
            default : System.out.println("Unrecognized choice!"); break;
         }
       }
     }
     catch(Exception e){
        System.err.println (e.getMessage ());
        return;
     }
   }

   public static void NewMessage(ProfNetwork esql, String authorisedUser){
     try{
        System.out.print("\tEnter userId to send a message to: ");
        String sendToUser = in.readLine();
        if (sendToUser.length() == 0){
           System.out.println("Recipent can't be empty. Try again.");
           return;
        }
        String query = String.format("SELECT * FROM USR WHERE userId='%s'", sendToUser);
        int get_user = esql.executeQuery(query);
        if (get_user <= 0){
          System.out.println("Recipent not found. Try Again.");
          return;
        }
        System.out.print("\tEnter the message to send: ");
		  	String message = esql.in.readLine();

        //Date date = new Date();
        //Timestamp ts=new Timestamp(date.getTime());
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String delete_status = "0";
        String status = "Delivered";
        String query2 = String.format("INSERT INTO MESSAGE (senderId, receiverId, contents, sendTime, deleteStatus, status) VALUES ('%s','%s','%s', '%s','%s','%s')", authorisedUser, sendToUser, message, ts, delete_status, status);
        esql.executeUpdate(query2);
        System.out.println("Successfully sent message.");
     }
     catch(Exception e){
        System.err.println (e.getMessage ());
        return;
     }
   }

   public static void NewMessageHelper(ProfNetwork esql, String authorisedUser, String sendToUser){ //Helper version of send request where the recipient is already known. Such as from the friendList
     try{
        String query = String.format("SELECT * FROM USR WHERE userId='%s'", sendToUser);
        int get_user = esql.executeQuery(query);
        if (get_user <= 0){
          System.out.println("Recipent not found. Try Again.");
          return;
        }
        System.out.print("\tEnter the message to send: ");
		  	String message = esql.in.readLine();

        //Date date = new Date();
        //Timestamp ts=new Timestamp(date.getTime());
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String delete_status = "0";
        String status = "Delivered";
        String query2 = String.format("INSERT INTO MESSAGE (senderId, receiverId, contents, sendTime, deleteStatus, status) VALUES ('%s','%s','%s', '%s','%s','%s')", authorisedUser, sendToUser, message, ts, delete_status, status);
        esql.executeUpdate(query2);
        System.out.println("Successfully sent message.");
     }
     catch(Exception e){
        System.err.println (e.getMessage ());
        return;
     }
   }

   public static void ViewMessages(ProfNetwork esql, String authorisedUser){
      try{
         boolean messagemenu = true;
         while(messagemenu) {
            System.out.println("MESSAGE MENU");
            System.out.println("---------");
            System.out.println("1. View Sent Messages");
            System.out.println("2. View Received Messages");
            System.out.println(".........................");
            System.out.println("9. Exit Message Menu");
            switch (readChoice()){
            case 1:
               boolean sentmenu = true;
               while(sentmenu){
                  System.out.println("Sent Messages Menu");
                  System.out.println("---------");
                  System.out.println("1. List All Sent Messages");
                  System.out.println("2. See Contents of Message");
                  System.out.println("3. Delete Message");
                  System.out.println(".........................");
                  System.out.println("9. Exit Sent Messages Menu");
                  switch (readChoice()){
                  case 1:
                     String delete_status1 = "0"; //Neither have deleted the message
                     String delete_status2 = "2"; //The recipient has deleted the message
                     String query = String.format("SELECT msgId, receiverId, sendTime, status FROM MESSAGE WHERE senderId='%s' AND (deleteStatus='%s' OR deleteStatus='%s')", authorisedUser, delete_status1, delete_status2);
                     int verify = esql.executeQueryAndPrintResult(query);
                     if(verify < 1){
                        System.out.println("No sent messages found.");
                        sentmenu = false; break;
                     }
                     break;
                  case 2:
                     System.out.println("Enter the integer MsgId of Message to see the contents: ");
                     String mes_id_str = in.readLine();
                     int mes_id_int = Integer.parseInt(mes_id_str.trim());
                     System.out.print("\nMessage Contents: ");
					           String query2 = String.format("SELECT contents FROM MESSAGE WHERE msgId='%s' AND senderId='%s'", mes_id_int, authorisedUser);
                     int verify2 = esql.executeQueryAndPrintResult(query2);
                     if(verify2 < 1){
                        System.out.println("No sent message with that ID found.");
                        break;
                     }
                     break;
                  case 3:
                     System.out.println("Enter the integer MsgId of Message to delete: ");
                     String mes_id_str2 = in.readLine();
                     int mes_id_int2 = Integer.parseInt(mes_id_str2.trim());
                     String query3 = String.format("SELECT deleteStatus FROM MESSAGE WHERE msgId='%s' AND senderId='%s'", mes_id_int2, authorisedUser);
                     List<List<String>> get_message_status = new ArrayList<List<String>>();
                     get_message_status = esql.executeQueryAndReturnResult(query3);
                     if (get_message_status.isEmpty()){
                        System.out.println("No sent message with that ID available to delete.");
                        break;
                     }
                     else if(get_message_status.get(0).get(0).equals("0")){ //Neither have deleted the message
                        String delete_status3 = "1";
							          String query4 = String.format("UPDATE MESSAGE set deleteStatus='%s' WHERE msgId='%s'", delete_status3, mes_id_int2);
							          esql.executeUpdate(query4); //Set that only the sender has deleted the message
                        System.out.println("Message Successfully deleted.");
						         }
                     else if (get_message_status.get(0).get(0).equals("2")) { //The recipient has already deleted the message
                       String delete_status4 = "3";
                       String query5 = String.format("UPDATE MESSAGE set deleteStatus='%s' WHERE msgId='%s'", delete_status4, mes_id_int2);
                       esql.executeUpdate(query5); //Set that both have deleted the message
                       System.out.println("Message Successfully deleted.");
                     }
                     else{
                       System.out.println("No sent message with that ID available to delete.");
                       break;
                     }
                     break;
                  case 9: sentmenu = false; break;
                  default : System.out.println("Unrecognized choice!"); break;
                  }
               }
               break;
            case 2:
                boolean receivemenu = true;
                while(receivemenu){
                  System.out.println("Recieved Messages Menu");
                  System.out.println("---------");
                  System.out.println("1. List All Unread Messages");
                  System.out.println("2. See Contents of Message");
                  System.out.println("3. List all Received Messages");
                  System.out.println("4. Delete Message");
                  System.out.println(".........................");
                  System.out.println("9. Exit Received Messages Menu");
                  switch (readChoice()){
                  case 1:
                    String delete_statusb1 = "0"; //Neither have deleted the message
                    String delete_statusb2 = "1"; //The sender has deleted the message
                    String statusb = "Delivered";
                    String queryb = String.format("SELECT msgId, senderId, sendTime FROM MESSAGE WHERE receiverId='%s' AND status='%s' AND (deleteStatus='%s' OR deleteStatus='%s')", authorisedUser, statusb, delete_statusb1, delete_statusb2);
                    int verifyb = esql.executeQueryAndPrintResult(queryb);
                    if(verifyb < 1){
                      System.out.println("No new unread messages found.");
                      break;
                    }
                    break;
                  case 2:
                    System.out.println("Enter the integer MsgId of Message to see the contents: ");
                    String mes_id_strb = in.readLine();
                    int mes_id_intb = Integer.parseInt(mes_id_strb.trim());
                    System.out.print("\nMessage Contents: ");
   					        String query2b = String.format("SELECT contents FROM MESSAGE WHERE msgId='%s' AND receiverId='%s'", mes_id_intb, authorisedUser);
                    int verify2b = esql.executeQueryAndPrintResult(query2b);
                    if(verify2b < 1){
                      System.out.println("No received message with that ID found.");
                      break;
                    }
                    String read = "Read";
                    String query9 = String.format("UPDATE MESSAGE SET status='%s' WHERE msgId = '%s'", read, mes_id_intb); //The message was just read by the recipient
					        	esql.executeUpdate(query9);
                    System.out.println("Setting current message to read.");
                    break;
                  case 3:
                    String delete_statusb9 = "0"; //Neither have deleted the message
                    String delete_statusb10 = "1"; //The sender has deleted the message
                    String status9b = "Read";
                    String status10b = "Delivered";
                    String query9b = String.format("SELECT msgId, senderId, sendTime FROM MESSAGE WHERE receiverId='%s' AND (status='%s' OR status='%s') AND (deleteStatus='%s' OR deleteStatus='%s')", authorisedUser, status9b, status10b, delete_statusb9, delete_statusb10);
                    int verify9b = esql.executeQueryAndPrintResult(query9b);
                    if(verify9b < 1){
                      System.out.println("No non deleted messages found.");
                      break;
                    }
                    break;
                  case 4:
                    System.out.println("Enter the integer MsgId of Message to delete: ");
                    String mes_id_strb2 = in.readLine();
                    int mes_id_intb2 = Integer.parseInt(mes_id_strb2.trim());
                    String queryb3 = String.format("SELECT deleteStatus FROM MESSAGE WHERE msgId='%s' AND receiverId='%s'", mes_id_intb2, authorisedUser);
                    List<List<String>> get_message_statusb = new ArrayList<List<String>>();
                    get_message_statusb = esql.executeQueryAndReturnResult(queryb3);
                    if (get_message_statusb.isEmpty()){
                      System.out.println("No received message with that ID available to delete.");
                      break;
                    }
                    else if(get_message_statusb.get(0).get(0).equals("0")){ //Neither have deleted the message
                      String delete_status3b = "2";
   							      String query4b = String.format("UPDATE MESSAGE set deleteStatus='%s' WHERE msgId='%s'", delete_status3b, mes_id_intb2);
   							      esql.executeUpdate(query4b); //Set that only the recipient has deleted the message
                      System.out.println("Message Successfully deleted.");
   						      }
                    else if (get_message_statusb.get(0).get(0).equals("1")) { //The sender has already deleted the message
                      String delete_status4b = "3";
                      String query5b = String.format("UPDATE MESSAGE set deleteStatus='%s' WHERE msgId='%s'", delete_status4b, mes_id_intb2);
                      esql.executeUpdate(query5b); //Set that both have deleted the message
                    }
                    else{
                      System.out.println("No sent message with that ID available to delete.");
                      break;
                    }
                    break;
                  case 9: receivemenu = false; break;
                  default : System.out.println("Unrecognized choice!"); break;
                  }
                }
                break;
            case 9: messagemenu = false; break;
            default : System.out.println("Unrecognized choice!"); break;
            }
         }
      }
      catch(Exception e){
         System.err.println (e.getMessage ());
         return;
      }
   }

   public static void SendRequest(ProfNetwork esql, String authorisedUser){
	try {
		String query3 = String.format("SELECT userId FROM CONNECTION_USR WHERE connectionId = '%s' AND status = 'Accept'", authorisedUser);
		int friend_count = esql.executeQuery(query3);
		String sendToUser = "";

		if (friend_count == 0){
			if (connection_level < 4){
                                System.out.print("\tEnter userId to send a request to: ");
                                sendToUser = in.readLine();
                                if (sendToUser.length() == 0){
                                	System.out.println("Recipient can't be empty. Try again.");
                                	return;
				}
			} else { System.out.println("You have reached the maximum connection level\n"); return; }
		} else if (friend_count > 0){
			if (connection_level < 6){
                           	System.out.print("\tEnter userId to send a request to: ");
                                sendToUser = in.readLine();
                                if (sendToUser.length() == 0){
                                	System.out.println("Recipient can't be empty. Try again.");
                                	return;
				}
			} else { System.out.println("You have reached the maximum connection level\n"); return; }
		 }

		String query = String.format("SELECT * FROM USR WHERE userId='%s'", sendToUser);
        	int get_user = esql.executeQuery(query);
        	if (get_user <= 0){
        		System.out.println("Recipient not found. Try Again.");
        		return;
        	}

		String status = "Request";
        	String query2 = String.format("INSERT INTO CONNECTION_USR (userId, connectionId, status) VALUES ('%s','%s','%s')", authorisedUser, sendToUser, status);
                                	esql.executeUpdate(query2);
        	System.out.println("Successfully sent connection request.");
     	} catch (Exception e) { System.err.println (e.getMessage ()); return; }
   }

   public static void SendRequestHelper(ProfNetwork esql, String authorisedUser, String sendToUser){ //Helper version of send request where the recipient is already known. Such as from the friendList
     try {
		String query3 = String.format("SELECT userId FROM CONNECTION_USR WHERE connectionId = '%s' AND status = 'Accept'", authorisedUser);
            	int friend_count = esql.executeQuery(query3);
                if (friend_count == 0){
                        if (connection_level >= 4) { System.out.println("You have reached the maximum connection level\n"); return; }
                } else if (friend_count > 0){
                        if (connection_level >= 6){ System.out.println("You have reached the maximum connection level\n"); return; }
                 }

        	String query = String.format("SELECT * FROM USR WHERE userId='%s'", sendToUser);
        	int get_user = esql.executeQuery(query);
        	if (get_user <= 0){
          		System.out.println("Recipient not found. Try Again.");
          		return;
        	}

        String status = "Request";
        String query2 = String.format("INSERT INTO CONNECTION_USR (userId, connectionId, status) VALUES ('%s','%s','%s')", authorisedUser, sendToUser, status);
				esql.executeUpdate(query2);
        System.out.println("Successfully sent connection request.");
     } catch(Exception e){ System.err.println (e.getMessage ()); return; }
   }

   public static void ViewRequests(ProfNetwork esql, String authorisedUser){
      try{
         boolean requestmenu = true;
         while(requestmenu) {
            System.out.println("CONNECTION REQUEST MENU");
            System.out.println("---------");
            System.out.println("1. View All Incoming Connection Requests");
            System.out.println("2. Accept/Delete Connection Request");
            System.out.println(".........................");
            System.out.println("9. Exit Connection Menu");
            switch (readChoice()){
            case 1:
               List<List<String>> get_requests = new ArrayList<List<String>>();
               String status = "Request";
               String query = String.format("SELECT userId FROM CONNECTION_USR WHERE connectionId='%s' AND status='%s'", authorisedUser, status);
               get_requests = esql.executeQueryAndReturnResult(query);
               if (get_requests.isEmpty()){
		  System.out.println();
                  System.out.println("No incoming Requests found.");
		  System.out.println();
               return;
               }
	       System.out.println();
               System.out.println("Here are the userIds sending an incoming request:");
   	       System.out.println();
               for(int i = 0; i < get_requests.size(); i++){
					 	      System.out.println(get_requests.get(i).get(0));
				       }
	       System.out.println();
               System.out.println("Successfully displayed incoming requests.");
               break;
            case 2:
               System.out.println("Enter userId of connection to Accept or Reject: ");
               String connection = in.readLine();
               String status2 = "Request";
               String query2 = String.format("SELECT userId FROM CONNECTION_USR WHERE userId='%s' AND connectionId='%s' AND status='%s'", connection, authorisedUser, status2);
               int verify = esql.executeQuery(query2);
               if (verify <= 0){
                  System.out.println("Incoming request not found for that User. Try Again.");
               break;
               }
               boolean acceptmenu = true;
               while(acceptmenu){
		  System.out.println();
                  System.out.println("Accept/Reject MENU");
                  System.out.println("---------");
                  System.out.println("1. Accept current request");
                  System.out.println("2. Delete current request");
                  System.out.println(".........................");
                  System.out.println("9. Cancel");
                  switch (readChoice()){
                  case 1:
                     String query3 = String.format("UPDATE CONNECTION_USR SET status='Accept' WHERE userId='%s' AND connectionId='%s'", connection, authorisedUser);
		     String query5 = String.format("INSERT INTO CONNECTION_USR (userId, connectionId, status) VALUES ('%s','%s','Accept')", authorisedUser, connection);
                     esql.executeUpdate(query3);
		     esql.executeUpdate(query5);
		     System.out.println();
                     System.out.println("Successfully accepted connection request.");
		     System.out.println();
                     acceptmenu = false; break;
                  case 2:
                     String query4 = String.format("DELETE FROM CONNECTION_USR WHERE userId='%s' AND connectionId='%s'", connection, authorisedUser);
                     esql.executeUpdate(query4);
                     System.out.println("Successfully rejected connection request.");
		     System.out.println();
                     acceptmenu = false; break;
                  case 9: acceptmenu = false; break;
                  default : System.out.println("Unrecognized choice!"); break;
                  }
               }
            case 9: requestmenu = false; break;
            default : System.out.println("Unrecognized choice!"); break;
            }
         }
      }
      catch(Exception e){
         System.err.println (e.getMessage ());
         return;
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
         		     esql.ViewProfile(esql, query);
		       }
   	 } catch (Exception e) {
		       System.err.println (e.getMessage ());
	   }
  }

   public void ViewProfile(ProfNetwork esql, String query){
   	try {
        	Statement stmt = this._connection.createStatement ();
        	ResultSet rs = stmt.executeQuery (query);
        	String [] rows = {"", "User ID", "Name", "Email", "DOB"};
        	ResultSetMetaData rsmd = rs.getMetaData ();
        	int numCol = rsmd.getColumnCount ();
        	boolean outputHeader = true;
		String username = "";
        	while (rs.next()){
                	for (int i=1; i<=numCol; ++i){
				if (i == 1){
					username = rs.getString(i);
					System.out.println();
					System.out.println(username + "'s PROFILE");
                			System.out.println("---------");
				}
                        	System.out.print (rows[i] + ": " + rs.getString (i) + "\t");
                        	System.out.println ();
                	}
        	}
        	stmt.close ();
		if (username == authorisedUser){
			connection_level = 0;
		}
		System.out.println();
        	System.out.println("1. Send a message");
        	System.out.println("2. View friends list");
		System.out.println("3. Add friend");
		System.out.println(".........................");
        	System.out.println("9. Go back");
		System.out.println();

		String query3 = String.format("SELECT userId FROM CONNECTION_USR WHERE userId = '%s' AND connectionId = '%s' AND status = 'Accept'", authorisedUser, username);
                int verify2 = esql.executeQuery(query3);
                if (verify2 > 0){connection_level = 0; }
		else { connection_level++; }
		System.out.println("Connection Level: " + connection_level);
		System.out.println();
        	switch (readChoice()){
        		case 1: NewMessageHelper(esql, authorisedUser, username); break;
                	case 2: FriendList(esql, username); break;
                	case 3:
				if (username == authorisedUser) { System.out.println("You can't be friends with yourself!"); break; }
   				else {
					String query2 = String.format("SELECT userId FROM CONNECTION_USR WHERE userId = '%s' AND connectionId = '%s' AND status = 'Accept'", authorisedUser, username);
                                        int verify = esql.executeQuery(query2);
                                        if (verify > 0){System.out.println("This user is already your friend."); break; }
					else { SendRequestHelper(esql, authorisedUser, username); break; }
				}
                	case 9: connection_level--; return;
                	default : System.out.println("Unrecognized choice!"); break;
        	}
  	} catch (Exception e) {
		System.err.println (e.getMessage ());
    	}
   }
}//end ProfNetwork
