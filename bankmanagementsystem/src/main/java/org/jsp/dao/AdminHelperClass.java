package org.jsp.dao;

public class AdminHelperClass 
{
	public static AdminDAO getAdmin()
	{
		return new AdminDAOimp();
	}
}
