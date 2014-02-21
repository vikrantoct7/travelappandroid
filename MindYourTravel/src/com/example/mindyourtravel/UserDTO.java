package com.example.mindyourtravel;

import java.io.Serializable;

public class UserDTO implements Serializable
{
	private String firstName;
	private String lastName;
	private String contactNo;
	
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
}
