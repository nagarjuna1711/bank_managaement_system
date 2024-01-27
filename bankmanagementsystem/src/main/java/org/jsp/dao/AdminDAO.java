package org.jsp.dao;

import java.sql.Connection;
import java.sql.ResultSet;

public interface AdminDAO 
{
	Connection connectToDB();
	void adminLogin(String emailId,String password);
	void registerUser();
	void allAccountHolders();
	ResultSet specifUserInfo(String accountNumber);
	void updateUserInfo();
	void updateUserPhoneNumber(String accountNumber);
	void updateUserEmailId(String accountNumber);
	void deleteUserByAccountNumber(String accountNumber);
}
