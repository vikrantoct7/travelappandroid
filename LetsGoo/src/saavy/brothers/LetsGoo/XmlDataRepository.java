package saavy.brothers.LetsGoo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.os.*;

public final class XmlDataRepository {
	
	//private SparseArray<UserDTO> _userList = new SparseArray<UserDTO>();
	private UserDTO userDataDto =null;
	private File userDataFile;
	public XmlDataRepository (Context context) throws Exception 
	{
		try
		{
			String userStoragePath="";
			if(Environment.getExternalStorageDirectory().exists())
			{
				userStoragePath =Environment.getExternalStorageDirectory()+"/"+AppConstant.STORAGEFOLDER;
			}
			else 
			{
				userStoragePath=context.getFilesDir().getParent();
			}
			if(userStoragePath.length()>0)
			{
				File dirUserPath = new File(userStoragePath);
				boolean isDirectoryExist =dirUserPath.exists();
				if(!isDirectoryExist)
				{
					isDirectoryExist =dirUserPath.mkdirs();
				}
				
				if(isDirectoryExist)
				{
					userDataFile= new File(dirUserPath,AppConstant.DATABASEFILE);
					//userDataFile.delete();
					if(!userDataFile.exists())
					{
						userDataFile.createNewFile();
					}
					readUserDataFile();
				}
			}
		}
		catch(Exception ex)
		{
			//ex.printStackTrace();
			throw ex;
		}
	}
	
	private void readUserDataFile() throws IOException, ClassNotFoundException
	{	
		if(userDataFile !=null)
		{
			if(userDataFile.canRead())
			{
				FileInputStream fs =new FileInputStream(userDataFile);
				if(fs.available()>0)
				{
					if(userDataDto ==null)
					{
						//_userData.clear();
						userDataDto = new UserDTO();
					}
					ObjectInputStream  deserializer = new ObjectInputStream(fs);
					//_userData = (ArrayList<UserDTO>) deserializer.readObject();
					userDataDto = (UserDTO) deserializer.readObject();
					deserializer.close();
				}
				fs.close();
			}
		}
	}
	
	private void writeUserDataFile(UserDTO user) throws IOException
	{
		//_userData.add(user);
		userDataDto=user;
		if(userDataFile !=null)
		{
			if(userDataFile.canWrite())
			{
				ObjectOutputStream  serializer = new ObjectOutputStream(new FileOutputStream(userDataFile));
				serializer.writeObject(userDataDto);
				serializer.close();
				//_userData.clear();
			}
		}
	}
	
	public void addUserDTO(UserDTO user)
	{
		try
		{
			writeUserDataFile(user);
		}
		catch(IOException ex)
		{
			 //ex.printStackTrace();
		}
	}
	
	//public ArrayList<UserDTO> GetUsersData()
	public UserDTO getUsersData()
	{
		try
		{
			readUserDataFile();
		}
		catch(IOException ex)
		{
			 //ex.printStackTrace();
		}
		catch(ClassNotFoundException ex)
		{
			 ///ex.printStackTrace();
		}
		return userDataDto;
	}
	
	public void clearRepository()
	{
		if(userDataFile !=null)
		{
			userDataFile.delete();
		}
		//_userData.clear();
		userDataDto=null;
	}
	
	/*public void AddUserInList(int userId,UserDTO userObject)
	{
		if(_userList.indexOfKey(userId) != -1)
		{
			_userList.put(userId, userObject);
		}
	}
	
	public UserDTO GetUserFromList(int userId)
	{
		return _userList.get(userId);
	}
	
	public void ClearUserList()
	{
		_userList.clear();
	}*/
	
}
