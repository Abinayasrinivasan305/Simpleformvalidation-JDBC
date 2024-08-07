package ValidationJDBC;
import java.util.*;
import java.sql.*;
public class Loginform {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int option;

        do {
            System.out.println("Enter 1. Login 2. Signup 3. Exit");
            option = sc.nextInt();

            switch (option) {
                case 1:
                    handleLogin(sc);
                    break;
                case 2:
                    handleSignup(sc);
                    break;
                case 3:
                    System.out.println("Bye bye");
                    break;
                default:
                    System.out.println("Sorry, you pressed a wrong option!!!");
            }
        } while (option != 3);

        sc.close();
    }

    private static void handleLogin(Scanner sc) {
        System.out.println("Enter the username (consist of letters only):");
        String username = sc.next();
        System.out.println("Enter the password:");
        System.out.println("Your password must consist of 8 characters which include special chars, numerics, and letters");
        String password = sc.next();

        if (validateUsername(username) && validatePassword(password)) {
            try {
                int result = sendUserDetails(username, password);
                if (result == 2) {
                    System.out.println("There is no such username and password exists. Please sign up first.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        else {
        	System.out.println("Please enter the username and password in the correct format!!!!");
        }
    }

    private static void handleSignup(Scanner sc) {
        System.out.println("Enter the username (consist of letters only):");
        String username = sc.next();
        System.out.println("Enter the password:");
        System.out.println("Your password must consist of 8 characters which include special chars, numerics, and letters");
        String password = sc.next();

        if (validateUsername(username) && validatePassword(password)) {
            try {
                insertUser(username, password);
            } catch (Exception e) {
            	 System.out.println(e);
            }
        }
        else {
        	System.out.println("Please enter the username and password in the correct format!!!!");
        }
    }

    private static boolean validateUsername(String username) {
        for (int i = 0; i < username.length(); i++) {
            if (!Character.isLetter(username.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean validatePassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        int countLetters = 0, countSpec = 0, countNums = 0;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) {
                countNums++;
            } else if (Character.isLetter(password.charAt(i))) {
                countLetters++;
            } else if (!Character.isLetterOrDigit(password.charAt(i))) {
                countSpec++;
            }
        }
        return countNums > 0 && countLetters > 0 && countSpec > 0;
    }

    private static void insertUser(String username, String password) throws SQLException, ClassNotFoundException {
    	Connection con=null;
    	PreparedStatement checkSt = null;
        PreparedStatement insertSt = null;
        ResultSet rs=null;
		try {
			    con=Dbconnection.getConnection();
			    con.setAutoCommit(false);
			    String checkQuery = "SELECT COUNT(*) FROM userlogin WHERE username = ?";
			    checkSt = con.prepareStatement(checkQuery);
		        checkSt.setString(1, username);
		        rs = checkSt.executeQuery();
		     
		       while( rs.next()) {
		          if (rs.getInt(1) > 0) {
		            System.out.println("User Already registered");
		            return;
		            
		          }
		        }
		        String insertQuery = "INSERT INTO userlogin (username, password) VALUES (?,?)";
		        insertSt = con.prepareStatement(insertQuery);
		        insertSt.setString(1, username);
		        insertSt.setString(2, password);
		        insertSt.executeUpdate();
		        System.out.println("Registration successfully done");
		        con.commit();
		        
		}
		catch(Exception e) {
			con.rollback();
			System.out.println(e.getMessage());
	}
    }

    private static int sendUserDetails(String username, String password) throws SQLException, ClassNotFoundException {
    	Connection con=null;
		int number=0;
		try {
		con=Dbconnection.getConnection();
		String query="select * from userlogin";
		Statement set=con.createStatement();
		ResultSet rs=set.executeQuery(query);
		boolean isThere=false;
		while(rs.next()) {
			String name=rs.getString(1);
			String pass=rs.getString(2);
			if(username.equals(name) && password.equals(pass)) {
				isThere=true;
				System.out.println("You are Logined Successfully");
				break;
			}
			else if(username.equals(name) && !password.equals(pass)) {
				isThere=true;
				System.out.println("Entered password is Wrong!!!Please login with correct password");
				break;
			}
		}
		if(!isThere) {
			number=2;
		}
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return number;
    }
}
