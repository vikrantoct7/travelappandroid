package com.example.mindyourtravel;

public class TravelPlanDTO {
	
	private String locationPosition;
	private String startLocation;
	private String locationValue;
	private String travelMode;
	private String startTime;
	private String totalNoOfPerson;
	
	public String getLocationPosition()
	{
		return this.locationPosition;
	}
	
	public void setLocationPosition(String locationPosition) {
        this.locationPosition = locationPosition;
    } 
	
	public String getStartLocation()
	{
		return this.startLocation;
	}
	
	public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    } 
	
	public String getLocationValue()
	{
		return this.locationValue;
	}
	
	public void setLocationValue(String locationValue) {
        this.locationValue = locationValue;
    } 
	
	public String getTravelMode()
	{
		return this.travelMode;
	}
	
	public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    } 
	
	public String getStartTime()
	{
		return this.startTime;
	}
	
	public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
	
	public String getTotalNoOfPerson()
	{
		return this.totalNoOfPerson;
	}
	
	public void setTotalNoOfPerson(String totalNoOfPerson) {
        this.totalNoOfPerson = totalNoOfPerson;
    }

}
