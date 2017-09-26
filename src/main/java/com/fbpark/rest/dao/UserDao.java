package com.fbpark.rest.dao;


import java.util.List;

import com.fbpark.rest.model.User;

public  interface UserDao extends BaseDao<User, Long>
{

	  public  User loginUser(String paramString1, String paramString2);

	  public  User loginByPhone(String paramString1, String paramString2, String paramString3);

	  public  void disableUser(Long paramLong);

	  public  User getUserByUsernameAndEmail(String paramString1, String paramString2);

	  public  User getUserByIdcard(String idcard);

	  public  User getUserByZoneAndPhone(String paramString1, String paramString2);

	  public  User getUserByUserName(String paramString);

	  public  List<User> getRandomUser();

	  public  List<User> getUsersByStoryIdAndNull(Long paramLong, int paramInt);

	  public  List<User> getUsersByStoryIdAndUserId(Long paramLong1, Long paramLong2, String paramString, int paramInt1, int paramInt2);

	  public  List<User> getRepostUsersByStoryId(Long paramLong, int paramInt);

	  public  List<User> getRepostUsersByStoryIdAndRepost(Long paramLong1, Long paramLong2, String paramString, int paramInt1, int paramInt2);

	  public  void updateUserByUserType(Long paramLong, String paramString);

	  public  List<User> getUserByName(String paramString);

	  public  User getUserByPhoneAndZone(String paramString1, String paramString2);

	  public  List<User> getUserByUserType();
	  
	  public List<User> getUserByUserType(String user_type);
	  
	  public List<User> getUserByPhoneOrIdcard(String phone,String idcard);
	  
	  public List<User> getUserRandom();
	  
	  public int getUserCount(String phone,String username,String idcard,String club_name,String start_time,String end_time);
	  
	  public List<User> getUserList(int count,int page,String phone,String username,String idcard,String club_name,String start_time,String end_time,String birthday_order,String time_order);
	  
	  public int getUserCountByLevel(String level,String phone,String username,String idcard,String club_name,String start_time,String end_time);
	  
	  public List<User> getUserListByLevel(int count,int page,String level,String phone,String username,String idcard,String club_name,String start_time,String end_time,String birthday_order,String time_order);
	  
	  public int getUserCountByUsername(String username);
	  
	  public List<User> getUserListByUsername(int count,int page,String username);
	  
	  public int getUserCountByPhone(String phone);
	  
	  public List<User> getUserListByPhone(int count,int page,String phone);
	  
	  public int getUserCountByIdcard(String idcard);
	  
	  public List<User> getUserListByIdcard(int count,int page,String idcard);
	  
	  public List<User> getUserListByLevel(String level,String start_time,String end_time);
	  
	  public User getUserListByIdcardAndPwd(String idcard,String password);
	  
	  public User getFirstUser();
	  
	  public List<User> getUserList(String create_time);
}

