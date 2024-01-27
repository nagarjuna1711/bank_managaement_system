package org.jsp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

public class AdminDAOimp implements AdminDAO
{
	Random random = new Random();
	Scanner s = new Scanner(System.in);
	final private String url = "jdbc:mysql://localhost:3306/advancejavaproject?user=root&password=12345";
	final private String adminLogIn = "select * from admin_details where admin_email_id = ? and admin_password = ?";
	final private String addUser = "insert into bank_user_details"
			+ "(User_Name, User_BankEmail_Id, User_Password, User_Gender, User_Address, User_Date_Of_Birth, User_Account_Number, "
			+ "User_Amount, User_Mobile_Number, IFSC_Code, User_email_id) values(?,?,?,?,?,?,?,?,?,?,?)";
	final private String getAllApplications = "select * from account_applications";
	final private String clearAccountApplications = "truncate account_applications";
	final private String allAccountHolders = "select * from bank_user_details";
	final private String userByAccountNumber = "select * from bank_user_details where user_account_number = ?";
	final private String updateUserPhoneNumber = "update bank_user_details set user_mobile_number = ? where user_account_number = ?";
	final private String updateUserEmailId = "update bank_user_details set user_email_id = ? where user_account_number = ?";
	final private String deleteUser = "delete from bank_user_details where user_account_number = ?";

	@Override
	public Connection connectToDB() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	@Override
	public void adminLogin(String emailId, String password) {
		try {
			PreparedStatement ps = connectToDB().prepareStatement(adminLogIn);
			ps.setString(1, emailId);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				boolean adminStart = true;
				while(adminStart)
				{
					System.out.println("1. User Registration\n2. Select all the user details\n3. Specific user details"
							+ "\n4. Update user details\n5. Delete user account\n6. <--- Go back");
					System.out.print("Enter option : ");
					int option = s.nextInt();
					switch(option)
					{
					case 1:{
						registerUser();
						break;
					}
					case 2:{
						allAccountHolders();
						break;
					}
					case 3:{
						System.out.print("Enter user account number : ");
						String inpuAccountNumbe = s.next();
						ResultSet singleUser = specifUserInfo(inpuAccountNumbe);
						if(singleUser.next())
						{
							System.out.print("User ID : "+singleUser.getInt("user_id")+"|| ");
							System.out.print("User name : "+singleUser.getString("user_name")+"|| ");
							System.out.print("User account number : "+singleUser.getInt("user_account_number")+"|| ");
							System.out.print("User mobile number : "+singleUser.getString("user_mobile_number")+"|| ");
							System.out.print("User Balance amount : "+singleUser.getDouble("user_amount")+"|| ");
							System.out.print("User email id : "+singleUser.getString("user_email_id")+"\n");
						}
						else
						{
							System.err.println("No user found with account numbe : "+inpuAccountNumbe);
						}
						break;
					}
					case 4:{
						updateUserInfo();
						break;
					}
					case 5:{
						System.out.print("Enter user account number to delete : ");
						String inputAccountNumber = s.next();
						System.out.print("press 1 to continue\npress any key to go back");
						int confirm = s.nextInt();
						if(confirm == 1)
						{
							deleteUserByAccountNumber(inputAccountNumber);
						}
						break;
					}
					case 6:{
						adminStart = false;
						System.out.println("...Thank you...");
						break;
					}
					}
				}
			}
			else
			{
				System.err.println("Please enter valid credentials ");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void registerUser() 
	{
		try {
			Connection con = connectToDB();
			PreparedStatement ps2 = con.prepareStatement(getAllApplications);
			ResultSet allApplications = ps2.executeQuery();
			while(allApplications.next())
			{
				PreparedStatement ps = con.prepareStatement(addUser);
				ps.setString(1, allApplications.getString("user_name"));
				int suffix = random.nextInt(1000);
				String bankEmail = allApplications.getString("user_name")+suffix+"@teca53.com";
				ps.setString(2, bankEmail);
				int defaultPassword = random.nextInt(10000);
				if(defaultPassword<1000)
				{
					defaultPassword+=10000;
				}
				ps.setInt(3, defaultPassword);
				ps.setString(4, allApplications.getString("user_gender"));
				ps.setString(5, allApplications.getString("user_address"));
				ps.setString(6, allApplications.getString("user_date_of_birth"));
				int accountNumber = random.nextInt(10000000);
				if(accountNumber<1000000)
				{
					accountNumber += 10000000;
				}
				ps.setInt(7, accountNumber);
				ps.setDouble(8, allApplications.getDouble("user_opening_amount"));
				ps.setString(9, allApplications.getString("user_mobile_number"));
				ps.setString(10,"500085");
				ps.setString(11, allApplications.getString("user_email_id"));

				int added = ps.executeUpdate();
				if(added != 0)
				{
					System.out.println("------------------\nUsers added succesfully\n------------------");
					PreparedStatement cl = con.prepareStatement(clearAccountApplications);
					cl.executeUpdate();

					System.out.println("Applications are completed");
				}
				else
				{
					System.err.println("Something went wrong");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void allAccountHolders() {
		try {
			PreparedStatement ps = connectToDB().prepareStatement(allAccountHolders);
			ResultSet allUsers = ps.executeQuery();
			while(allUsers.next())
			{
				System.out.print("User ID : "+allUsers.getInt("user_id")+"|| ");
				System.out.print("User name : "+allUsers.getString("user_name")+"|| ");
				System.out.print("User account number : "+allUsers.getInt("user_account_number")+"|| ");
				System.out.print("User mobile number : "+allUsers.getString("user_mobile_number")+"|| ");
				System.out.print("User Balance amount : "+allUsers.getDouble("user_amount")+"|| ");
				System.out.print("User email id : "+allUsers.getString("user_email_id")+"\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ResultSet specifUserInfo(String accountNumber) {
		ResultSet result = null;
		try {
			PreparedStatement ps = connectToDB().prepareStatement(userByAccountNumber);
			ps.setString(1, accountNumber);
			result = ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void updateUserInfo() 
	{
		System.out.print("Enter account number : ");
		String inpuAccountNumber = s.next();
		boolean startUpdating = true;
		while(startUpdating)
		{
			System.out.println("1. Update phone number\n2. Update mail id\n3. <-- Go Back");
			System.out.print("Enter option : ");
			int option = s.nextInt();
			switch(option)
			{
			case 1:{
				updateUserPhoneNumber(inpuAccountNumber);
				break;
			}
			case 2:{
				updateUserEmailId(inpuAccountNumber);
				break;
			}
			case 3:{
				startUpdating = false;
				break;
			}
			default:System.out.println("Enter only given option");break;
			}
		}
	}

	@Override
	public void updateUserPhoneNumber(String accountNumber) {
		try {
			PreparedStatement ps = connectToDB().prepareStatement(updateUserPhoneNumber);
			System.out.print("Enter new phone number : ");
			String newPhoneNumber = s.next();
			ps.setString(1, newPhoneNumber);
			ps.setString(2, accountNumber);
			if(ps.executeUpdate() != 0)
			{
				System.out.println("Phone number updated successfully........");
			}
			else
			{
				System.err.println("Something went wrong");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void updateUserEmailId(String accountNumber) {
		try {
			PreparedStatement ps = connectToDB().prepareStatement(updateUserEmailId);
			System.out.print("Enter new email id : ");
			String newEmailId = s.next();
			ps.setString(1, newEmailId);
			ps.setString(2, accountNumber);
			if(ps.executeUpdate() != 0)
			{
				System.out.println("Email Id updated successfully........");
			}
			else
			{
				System.err.println("Something went wrong");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteUserByAccountNumber(String accountNumber) {
		try {
			PreparedStatement ps = connectToDB().prepareStatement(deleteUser);
			ps.setString(1, accountNumber);
			if(ps.executeUpdate() != 0)
			{
				System.out.println("User deleted successfully....");
			}
			else
			{
				System.err.println("No user found with account number ");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}



}
