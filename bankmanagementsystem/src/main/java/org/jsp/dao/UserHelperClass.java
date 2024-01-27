package org.jsp.dao;

public class UserHelperClass 
{
	public static UserDAO getUser()
	{
		return new UserDAOimp();
	}
}
