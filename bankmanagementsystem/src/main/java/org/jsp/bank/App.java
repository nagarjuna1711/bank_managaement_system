package org.jsp.bank;

import java.util.Scanner;

import org.jsp.dao.AdminDAO;
import org.jsp.dao.AdminHelperClass;
import org.jsp.dao.UserDAO;
import org.jsp.dao.UserHelperClass;

/**
 * Hello world!
 *
 */
public class App 
{
	static Scanner sc = new Scanner(System.in);
	static UserDAO user = UserHelperClass.getUser();
	static AdminDAO admin = AdminHelperClass.getAdmin();
    public static void main( String[] args )
    {
    	boolean appStart = true;
    	while(appStart)
    	{
    		System.out.println("1. Admin login\n2. User Login\n3. Apply for account \n4. EXIT");
    		System.out.print("Enter option : ");
    		int UrA = sc.nextInt(); 
    		switch(UrA)
    		{
    		case 1:
    			System.out.print("Enter your mail id : ");
    			String adminInputEmail = sc.next();
    			System.out.print("Enter your password : ");
    			String adminInputPassword = sc.next();
    			admin.adminLogin(adminInputEmail, adminInputPassword);
    			break;
    		case 2: 
    			System.out.print("Enter your email id : ");
    			String inputEmailID = sc.next();
    			System.out.print("Enter your password : ");
    			int inputPassword = sc.nextInt();
    			user.userlogIn(inputEmailID, inputPassword);
    			break;
    		case 3:
    			user.applyForAccount();
    			break;
    		case 4: appStart = false;System.out.println("Visit again.... :)");break;
    		default : System.err.println("Please enter only given choices");
    		}
    	}
    }
}
