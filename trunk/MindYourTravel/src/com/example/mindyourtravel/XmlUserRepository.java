package com.example.mindyourtravel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import android.os.*;

public class XmlUserRepository {
	
	private ArrayList<UserDTO> _userData = new ArrayList<UserDTO>();
	private File _userDataFile;
	public XmlUserRepository () throws Exception 
	{
		try
		{
			if(Environment.getExternalStorageDirectory().exists())
			{
				String userStoragePath =Environment.getExternalStorageDirectory()+"/"+AppConstant.StorageFolder;
				File dirUserPath = new File(userStoragePath);
				boolean isDirectoryExist =dirUserPath.exists();
				if(!isDirectoryExist)
				{
					isDirectoryExist =dirUserPath.mkdirs();
				}
				
				if(isDirectoryExist)
				{
					_userDataFile= new File(dirUserPath,AppConstant.DatabaseFile);
					_userDataFile.delete();
					if(!_userDataFile.exists())
					{
						_userDataFile.createNewFile();
					}
					ReadUserDataFile();
				}
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			throw ex;
		}
	}
	
	private void ReadUserDataFile() throws Exception
	{	
		try
		{
			FileInputStream fs =new FileInputStream(_userDataFile);
			if(fs.available()>0)
			{
				ObjectInputStream  deserializer = new ObjectInputStream(fs);
				_userData = (ArrayList<UserDTO>) deserializer.readObject();
				deserializer.close();
			}
			fs.close();
		}
		catch(Exception ex)
		{
			 ex.printStackTrace();
			 throw ex;
		}
	}
	
	private void WriteUserDataFile() throws Exception
	{
		try
		{
			ObjectOutputStream  serializer = new ObjectOutputStream(new FileOutputStream(_userDataFile));
			serializer.writeObject(_userData);
			serializer.close();
		}
		catch(Exception ex)
		{
			 ex.printStackTrace();
			 throw ex;
		}
	}
	
	public void AddUserDTO(UserDTO user) throws Exception
	{
		_userData.add(user);
		WriteUserDataFile();
	}
	
	public ArrayList<UserDTO> GetUsersData()
	{
		return _userData;
	}
	
}
