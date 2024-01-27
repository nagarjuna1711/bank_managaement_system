package org.jsp.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;
import java.util.Scanner;


public class UserDAOimp implements UserDAO
{
	Random random = new Random();
	Scanner s = new Scanner(System.in);
	final private String url = "jdbc:mysql://localhost:3306/advancejavaproject?user=root&password=12345";
	final private String selectToLogin = "select * from bank_user_details where user_email_id = ? && user_password = ?";
	final private String updateAmount = "update bank_user_details set user_amount = ? where user_password = ?";
	final private String debitedInserIntoStatement = "insert into statement values (?,?,?,?,?,?,?,?,?)";
	final private String selectStatementsByUserId = 
			"SELECT * FROM statement where user_id = ? and Date_Of_Transaction between ? and ?";
	final private String updatePassword = "update bank_user_details set user_password = ? where user_id = ?";
	final private String selectUserByAccountNumber = "select * from bank_user_details where User_Account_Number = ?";
	final private String applying = "insert into account_applications values(?,?,?,?,?,?,?)";


	public Connection connectToDB(String url)
	{
		try {
			return DriverManager.getConnection(url);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public ResultSet accountSearch(int accountNumber) 
	{
		ResultSet result = null;
		try {
			PreparedStatement ps = connectToDB(url).prepareStatement(selectUserByAccountNumber);
			ps.setInt(1, accountNumber);
			result = ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	@Override
	public void userlogIn(String emailID,int passowrd) 
	{
		Connection connection = connectToDB(url);
		try {
			PreparedStatement ps = connection.prepareStatement(selectToLogin);
			ps.setString(1, emailID);
			ps.setInt(2, passowrd);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				boolean userStart = true;
				while(userStart)
				{
					System.out.println("1. Debit\n2. Credit\n3. Check Balance\n4. Show statement"
							+ "\n5. Change password\n6. Mobile to Mobile Transfer\n7. <-- Go Back ");
					System.out.print("Enter option : ");
					int option = s.nextInt();
					switch(option)
					{
					case 1:{
						System.out.print("Enter amount to debit : ");
						int inputDebitAmount = s.nextInt();
						double databaseAmount = rs.getDouble("user_amount");
						if(inputDebitAmount<=(databaseAmount-500))
						{
							databaseAmount -= inputDebitAmount;
							boolean ok = debit(databaseAmount);
							if(ok)
							{
								insertDebitedDetailsToStatement(rs, inputDebitAmount, databaseAmount);
							}
							else
							{
								System.err.println("Something went wrong..!!!");
							}
						}
						else
						{
							System.err.println("Please maintain min balance of 500");
						}
						break;
					}
					case 2:{
						System.out.print("Enter amount to credit : ");
						int inputCreditAmount = s.nextInt();
						double databaseAmount = rs.getDouble("user_amount");
						if(inputCreditAmount>0)
						{
							databaseAmount += inputCreditAmount;
							boolean ok = credit(databaseAmount);
							if(ok)
							{
								insertCreditedDetailsToStatement(rs, inputCreditAmount, databaseAmount);
							}
							else
							{
								System.err.println("Something went wrong..!!!");
							}
						}
						else
						{
							System.err.println("Please maintain min balance of 500");
						}
						break;
					}
					case 3:
					{
						showbalance(rs);
						break;
					}
					case 4:{
						showStatements(rs);
						break;
					}
					case 5:{
						passwordUpdate(rs);
						break;
					}
					case 6:
					{
						mobileToMobileTransfer(rs);
						break;
					}
					case 7:
					{
						userStart = false;
						break;
					}
					}
				}
			}
			else
			{
				System.err.println("Please provide valid credential");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean credit(double inputAmount) 
	{
		boolean done = false;
		if(inputAmount>0)
		{
			try {
				PreparedStatement ps = connectToDB(url).prepareStatement(updateAmount);
				System.out.print("Enter password : ");
				int inputPassword = s.nextInt();
				ps.setDouble(1, inputAmount);
				ps.setInt(2, inputPassword);
				int res = ps.executeUpdate();
				if(res!=0)
				{
					done = true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		else
		{
			System.err.println("Please enter valid amount ");
		}
		return done;

	}

	@Override
	public boolean debit(double inputAmount) {

		boolean done = false;
		if(inputAmount%100 == 0)
		{
			if(inputAmount > 0)
			{
				try {
					System.out.print("Enter password : ");
					int inputPassword = s.nextInt();
					PreparedStatement ps = connectToDB(url).prepareStatement(updateAmount);
					ps.setDouble(1, inputAmount);
					ps.setInt(2, inputPassword);
					int result = ps.executeUpdate();
					if(result!=0)
					{
						done = true;

					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
			else
			{
				System.err.println("Please enter a valid amount...!!");
			}
		}
		else
		{
			System.err.println("Amount should be multiples of 100");
		}
		return done;

	}

	public void insertDebitedDetailsToStatement(ResultSet rs,int inputDebitAmount,double databaseAmount)
	{
		try {
			PreparedStatement ps1 = connectToDB(url).prepareStatement(debitedInserIntoStatement);

			ps1.setString(1, "Debited");
			ps1.setDate(2, Date.valueOf(LocalDate.now()));
			ps1.setString(3, "Online");
			int transactionId = random.nextInt(10000000);
			if(transactionId < 1000000)
			{
				transactionId += 1000000;
			}
			ps1.setInt(4, transactionId);
			ps1.setString(5, inputDebitAmount+"dr");
			ps1.setInt(6, rs.getInt("user_id"));
			ps1.setInt(7, rs.getInt("user_account_number"));
			ps1.setTime(8, Time.valueOf(LocalTime.now()));
			ps1.setString(9, databaseAmount+"rs");

			int result = ps1.executeUpdate();
			if(result!=0)
			{
				System.out.println("------------------\nDebited\n------------------");
			}
			else
			{
				System.err.println("Something went wrong...!!");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void insertCreditedDetailsToStatement(ResultSet rs,int inputCreditedAmount,double databaseAmount)
	{
		try {
			PreparedStatement ps1 = connectToDB(url).prepareStatement(debitedInserIntoStatement);

			ps1.setString(1, "Credited");
			ps1.setDate(2, Date.valueOf(LocalDate.now()));
			ps1.setString(3, "Online");
			int transactionId = random.nextInt(10000000);
			if(transactionId < 1000000)
			{
				transactionId += 1000000;
			}
			ps1.setInt(4, transactionId);
			ps1.setString(5, inputCreditedAmount+"dr");
			ps1.setInt(6, rs.getInt("user_id"));
			ps1.setInt(7, rs.getInt("user_account_number"));
			ps1.setTime(8, Time.valueOf(LocalTime.now()));
			ps1.setString(9, databaseAmount+"rs");

			int result = ps1.executeUpdate();
			if(result!=0)
			{
				System.out.println("------------------\nCredited\n------------------");
			}
			else
			{
				System.err.println("Something went wrong...!!");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void showbalance(ResultSet rs) {
		try {
			System.out.println("------------------\n"+rs.getDouble("user_amount")+"\n------------------");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void showStatements(ResultSet rs) {
		System.out.print("Enter from date in [YYYY-MM-DD/YY-MM-DD] format : ");
		String fromDate = s.next();
		System.out.print("Enter to date in [YYYY-MM-DD/YY-MM-DD] format : ");
		String toDate = s.next();
		try {
			int databaseUserId = rs.getInt("user_id");
			PreparedStatement ps = connectToDB(url).prepareStatement(selectStatementsByUserId);
			ps.setInt(1, databaseUserId);
			ps.setString(2, fromDate);
			ps.setString(3, toDate);
			ResultSet result = ps.executeQuery();
			while(result.next())
			{
				System.out.print("User ID : "+result.getInt("User_Id")+" | ");
				System.out.print("Transaction ID : "+result.getInt("Transaction_Id")+" | ");
				System.out.print("Status : "+result.getString("status")+" | ");
				System.out.print("Date of Transaction : "+result.getDate("Date_Of_Transaction")+" | ");
				System.out.print("Transaction Time : "+result.getTime("Transaction_Time")+" | ");
				System.out.print("Type of transaction : "+result.getString("Type_Of_Transaction")+" | ");
				System.out.print("Account number : XXX"+(result.getInt("Bank_Account_Number")%1000)+" | ");
				System.out.print("Transaction Amount : "+result.getString("Amount")+" | ");
				System.out.print("Remaining Amount : "+result.getString("Remaining_Amount")+"\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	@Override
	public void passwordUpdate(ResultSet rs) {
		try {
			PreparedStatement ps = connectToDB(url).prepareStatement(updatePassword);
			int databaseUserId = rs.getInt("user_id");
			System.out.print("Enter new password : ");
			int pswd1 = s.nextInt();
			System.out.print("Re-enter the password : ");
			int pswd2 = s.nextInt();
			if(pswd1 == pswd2)
			{
				ps.setInt(1, pswd2);
				ps.setInt(2, databaseUserId);
				int result = ps.executeUpdate();
				if(result!=0)
				{
					System.out.println("------------------\nPassword updated successfully\n------------------");
				}
				else
				{
					System.err.println("Something went wrong....");
				}
			}
			else
			{
				System.err.println("Both password should be same");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void mobileToMobileTransfer(ResultSet rs) {
		System.out.print("Enter account number to transer money : ");
		int inputAccountNumber = s.nextInt();
		ResultSet personOne = rs;
		ResultSet personTwo = accountSearch(inputAccountNumber);
		try {
			if(personTwo.next())
			{
				double personTwoDBAmount = personTwo.getDouble("user_amount");
				System.out.print("Enter amount to transfer : ");
				int inputAmount = s.nextInt();
				double personOneDBAmount = personOne.getDouble("user_amount");
				if(inputAmount<=personOneDBAmount)
				{
					int personOnePassword = personOne.getInt("user_password");
					for(int i=0;i<3;i++)
					{
						System.out.print("Enter your PIN : ");
						int pin = s.nextInt();
						if(passwordCheck(pin, personOnePassword))
						{
							double personTwoUpdatedAmount = personTwoDBAmount+inputAmount;
							double personOneUpdatedAmount = personOneDBAmount-inputAmount;
							Connection connection = connectToDB(url);
							PreparedStatement ps1 = connection.prepareStatement(updateAmount);
							ps1.setDouble(1, personOneUpdatedAmount);
							ps1.setInt(2, personOne.getInt("User_Password"));
							int oneUpdated = ps1.executeUpdate();
							PreparedStatement ps2 = connection.prepareStatement(updateAmount);
							ps2.setDouble(1, personTwoUpdatedAmount);
							ps2.setInt(2, personTwo.getInt("User_Password"));
							int twoUpdated = ps2.executeUpdate();
							if(oneUpdated!=0 && twoUpdated!=0)
							{
								insertDebitedDetailsToStatement(personOne, inputAmount, personOneDBAmount);
								insertCreditedDetailsToStatement(personTwo, inputAmount, personTwoDBAmount);
								System.out.println("------------------\nTransfer successful\n------------------");
							}
							break;
						}
						else
						{
							System.err.println("Wrong PIN\n re-enter");
						}
					}
				}
				else
				{
					System.err.println("Insufficient funds..!!!");
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean passwordCheck(int inputPass,int dbPass)
	{
		return inputPass == dbPass;
	}
	@Override
	public void applyForAccount() {
		System.out.print("Enter your name : ");
		String userName = s.next();
		System.out.print("Enter your gender : ");
		String userGender = s.next();
		System.out.print("Enter your address : ");
		String userAddress = s.next();
		System.out.print("Enter your date of birth : ");
		String userDateOfBirth = s.next();
		System.out.print("Enter your opening amount : ");
		double userOpeningAmount = s.nextDouble();
		while(userOpeningAmount<500)
		{
			System.err.println("Minimum opening amount is 500/- only");
			userOpeningAmount = s.nextDouble();
		}
		System.out.print("Enter your mobile number : ");
		String userMobileNumber = s.next();
		System.out.print("Enter your email id : ");
		String userEmailId = s.next();
		
		try {
			PreparedStatement ps = connectToDB(url).prepareStatement(applying);
			ps.setString(1, userName);
			ps.setNString(2, userGender);
			ps.setString(3, userAddress);
			ps.setString(4, userDateOfBirth);
			ps.setDouble(5, userOpeningAmount);
			ps.setString(6, userMobileNumber);
			ps.setString(7, userEmailId);
			int applicationSuccessful = ps.executeUpdate();
			if(applicationSuccessful != 0)
			{
				System.out.println("Application successfull....");
				System.out.println("Wait for few days to open your account\n");
			}
			else
			{
				System.err.println("Something went wrong");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


}









