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
	private boolean gender;
	private int age;
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

	public boolean getGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public boolean isAppLoginUser() {
		return isAppLoginUser;
	}

	public void setAppLoginUser(boolean isAppLoginUser) {
		this.isAppLoginUser = isAppLoginUser;
	}
}
