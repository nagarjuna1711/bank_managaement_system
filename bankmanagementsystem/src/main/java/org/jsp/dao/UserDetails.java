package org.jsp.dao;

public class UserDetails 
{
	private String userName;
	private String userGender;
	private String userAddress;
	private String userDateOfBirth;
	private double openingAmount;
	private String userMobileNumber;
	private String userEmailId;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserGender() {
		return userGender;
	}
	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}
	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	public String getUserDateOfBirth() {
		return userDateOfBirth;
	}
	public void setUserDateOfBirth(String userDateOfBirth) {
		this.userDateOfBirth = userDateOfBirth;
	}
	public double getOpeningAmount() {
		return openingAmount;
	}
	public void setOpeningAmount(double openingAmount) {
		this.openingAmount = openingAmount;
	}
	public String getUserMobileNumber() {
		return userMobileNumber;
	}
	public void setUserMobileNumber(String userMobileNumber) {
		this.userMobileNumber = userMobileNumber;
	}
	public String getUserEmailId() {
		return userEmailId;
	}
	public void setUserEmailId(String userEmailId) {
		this.userEmailId = userEmailId;
	}
	public UserDetails(String userName, String userGender, String userAddress, String userDateOfBirth,
			double openingAmount, String userMobileNumber, String userEmailId) {
		this.userName = userName;
		this.userGender = userGender;
		this.userAddress = userAddress;
		this.userDateOfBirth = userDateOfBirth;
		this.openingAmount = openingAmount;
		this.userMobileNumber = userMobileNumber;
		this.userEmailId = userEmailId;
	}
	@Override
	public String toString() {
		return "UserDetails [userName=" + userName + ", userGender=" + userGender + ", userAddress=" + userAddress
				+ ", userDateOfBirth=" + userDateOfBirth + ", openingAmount=" + openingAmount + ", userMobileNumber="
				+ userMobileNumber + ", userEmailId=" + userEmailId + "]";
	}
	
	
	
	
}
