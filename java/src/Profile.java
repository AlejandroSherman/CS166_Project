class Profile {
	String userId;
	String password;
	String email;
	String fullname;
	String dateOfBirth;

	boolean active = false;

	public void setId(String id){
		userId = id;	
	}

	public void setPass(String pwd){
		password = pwd;
	}

	String void setEmail(String em){
		email = em;
	}

	String void setBirth(String birth){
		dateOfBirth = birth;
	}

	String void setActive(boolean x){
		active = x;
	}

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
               			case 9: return; break;
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
