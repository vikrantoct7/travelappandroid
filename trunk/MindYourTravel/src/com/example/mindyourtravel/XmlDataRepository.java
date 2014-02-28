package com.example.mindyourtravel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import android.os.*;

public final class XmlDataRepository {
	
	private ArrayList<UserDTO> _userData = new ArrayList<UserDTO>();
	private File _userDataFile;
	public XmlDataRepository () throws Exception 
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
	
	@SuppressWarnings("unchecked")
	private void ReadUserDataFile() throws IOException, ClassNotFoundException
	{	
		if(_userDataFile !=null)
		{
			if(_userDataFile.canRead())
			{
				FileInputStream fs =new FileInputStream(_userDataFile);
				if(fs.available()>0)
				{
					_userData.clear();
					ObjectInputStream  deserializer = new ObjectInputStream(fs);
					_userData = (ArrayList<UserDTO>) deserializer.readObject();
					deserializer.close();
				}
				fs.close();
			}
		}
	}
	
	private void WriteUserDataFile(UserDTO user) throws IOException
	{
		_userData.add(user);
		if(_userDataFile !=null)
		{
			if(_userDataFile.canWrite())
			{
				ObjectOutputStream  serializer = new ObjectOutputStream(new FileOutputStream(_userDataFile));
				serializer.writeObject(_userData);
				serializer.close();
				_userData.clear();
			}
		}
	}
	
	public void AddUserDTO(UserDTO user)
	{
		try
		{
			WriteUserDataFile(user);
		}
		catch(IOException ex)
		{
			 ex.printStackTrace();
		}
	}
	
	public ArrayList<UserDTO> GetUsersData() 
	{
		try
		{
			ReadUserDataFile();
		}
		catch(IOException ex)
		{
			 ex.printStackTrace();
		}
		catch(ClassNotFoundException ex)
		{
			 ex.printStackTrace();
		}
		return _userData;
	}
	
	public void ClearRepository()
	{
		if(_userDataFile !=null)
		{
			_userDataFile.delete();
		}
		_userData.clear();
	}
	
}
