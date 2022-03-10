import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

class Profile {
	private static String userId;
	private static String password;
	private static String email;
	private static String fullname;
	private static String dateOfBirth;

	public static boolean active = false;

	private static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));
	
	Profile(String[] info){
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

	public static void viewProfile(Profile x){
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
}
