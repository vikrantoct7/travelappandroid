package com.example.mindyourtravel;

import java.io.Serializable;

public class UserDTO implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int userId;
	private String firstName;
	private String lastName;
	private String contactNo;
	private int gender;
	private int age;
	private int travelId;
	private boolean isAppLoginUser;
	
	public String getFirstName()
	{
		return this.firstName;
	}
	
	public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
	
	public String getLastName()
	{
		return this.lastName;
	}
	
	public void setLastName(String lastName) {
        this.lastName = lastName;
    }
	
	public String getContactNo()
	{
		return this.contactNo;
	}
	
	public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	public int getTravelId() {
		return travelId;
	}

	public void setTravelId(int travelId) {
		this.travelId = travelId;
	}

	public boolean isAppLoginUser() {
		return isAppLoginUser;
	}

	public void setAppLoginUser(boolean isAppLoginUser) {
		this.isAppLoginUser = isAppLoginUser;
	}
	
	public String getUserFullName()
	{
		return this.firstName +  this.lastName;
	}
	
	public String getGenderStringValue()
	{
		String genderStr="M";
		if(this.gender ==1)
		{
			genderStr="M";
		}
		else if(this.gender ==2)
		{
			genderStr="F";
		}
		return genderStr;
	}
}
