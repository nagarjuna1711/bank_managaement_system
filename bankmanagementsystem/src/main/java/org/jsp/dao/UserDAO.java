package org.jsp.dao;

import java.sql.ResultSet;

public interface UserDAO 
{
	ResultSet accountSearch(int accountId);
	void userlogIn(String emailID,int password);
	boolean debit(double inputAmount);
	boolean credit(double inputAmount);
	void showbalance(ResultSet rs);
	void insertDebitedDetailsToStatement(ResultSet rs,int inputDebitAmount,double databaseAmount);
	void insertCreditedDetailsToStatement(ResultSet rs,int inputDebitAmount,double databaseAmount);
	void showStatements(ResultSet rs);
	void passwordUpdate(ResultSet rs);
	void mobileToMobileTransfer(ResultSet rs);
	void applyForAccount();
}
