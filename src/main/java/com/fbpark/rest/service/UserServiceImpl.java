package com.fbpark.rest.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.fbpark.rest.common.CodeUtil;
import com.fbpark.rest.common.EncryptionUtil;
import com.fbpark.rest.common.HttpUtil;
import com.fbpark.rest.common.HttpUtils;
import com.fbpark.rest.common.IdcardUtils;
import com.fbpark.rest.common.ParseFile;
import com.fbpark.rest.common.PushNotificationUtil;
import com.fbpark.rest.common.QRCodeHelper;
import com.fbpark.rest.common.SendCode;
import com.fbpark.rest.common.Validator;
import com.fbpark.rest.common.ZybUtil;
import com.fbpark.rest.dao.CartDao;
import com.fbpark.rest.dao.ClassifyDao;
import com.fbpark.rest.dao.CollectDao;
import com.fbpark.rest.dao.ContacterDao;
import com.fbpark.rest.dao.ContentDao;
import com.fbpark.rest.dao.InsurantDao;
import com.fbpark.rest.dao.LinkAccountsDao;
import com.fbpark.rest.dao.NoticeDao;
import com.fbpark.rest.dao.NotificationDao;
import com.fbpark.rest.dao.OrdersDao;
import com.fbpark.rest.dao.OrdersTicketDao;
import com.fbpark.rest.dao.ParkMapDao;
import com.fbpark.rest.dao.PoiDao;
import com.fbpark.rest.dao.PushNotificationDao;
import com.fbpark.rest.dao.SettingDao;
import com.fbpark.rest.dao.SlideDao;
import com.fbpark.rest.dao.TicketDao;
import com.fbpark.rest.dao.UserDao;
import com.fbpark.rest.model.Cart;
import com.fbpark.rest.model.Classify;
import com.fbpark.rest.model.Collect;
import com.fbpark.rest.model.Contacter;
import com.fbpark.rest.model.Content;
import com.fbpark.rest.model.ContentElement;
import com.fbpark.rest.model.Insurant;
import com.fbpark.rest.model.LinkAccounts;
import com.fbpark.rest.model.Notice;
import com.fbpark.rest.model.Notification;
import com.fbpark.rest.model.Orders;
import com.fbpark.rest.model.OrdersTicket;
import com.fbpark.rest.model.ParkMap;
import com.fbpark.rest.model.Poi;
import com.fbpark.rest.model.PoiElement;
import com.fbpark.rest.model.PushNotification;
import com.fbpark.rest.model.Setting;
import com.fbpark.rest.model.Slide;
import com.fbpark.rest.model.Ticket;
import com.fbpark.rest.model.User;
import com.fbpark.rest.redis.MyRedisDao;
import com.fbpark.rest.service.model.GetuiModel;
import com.fbpark.rest.service.model.PasswordModel;
import com.fbpark.rest.service.model.Zyb;
import com.google.common.base.Strings;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.APIConnectionException;
import com.pingplusplus.exception.APIException;
import com.pingplusplus.exception.AuthenticationException;
import com.pingplusplus.exception.ChannelException;
import com.pingplusplus.exception.InvalidRequestException;
import com.pingplusplus.exception.RateLimitException;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Refund;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
public class UserServiceImpl implements UserService {
	private static final Log log = LogFactory.getLog(UserServiceImpl.class);
	@Autowired
	private UserDao userDao;

	@Autowired
	private LinkAccountsDao linkAccountsDao;

	@Autowired
	private ContentDao contentDao;

	@Autowired
	private TicketDao ticketDao;

	@Autowired
	private OrdersTicketDao ordersTicketDao;

	@Autowired
	private CollectDao collectDao;

	@Autowired
	private SlideDao slideDao;

	@Autowired
	private CartDao cartDao;

	@Autowired
	private OrdersDao ordersDao;

	@Autowired
	private ClassifyDao classifyDao;

	@Autowired
	private SettingDao settingDao;

	@Autowired
	private ContacterDao contacterDao;

	@Autowired
	private NoticeDao noticeDao;

	@Autowired
	private PoiDao poiDao;

	@Autowired
	private NotificationDao notificationDao;
	
	@Autowired
	private InsurantDao insurantDao;
	
	@Autowired
	private ParkMapDao parkMapDao;

	@Autowired
	private PushNotificationDao pushNotificationDao;
	
	@Autowired
	private MyRedisDao myRedisDao;
	
//	private JedisPool pool; 

	public Response signup(JSONObject user) {

		User u = new User();
		try {
			if (user != null) {
				if (user.containsKey("phone")) {
					String zone = user.get("zone").toString();
					String phone = user.get("phone").toString();
					String code = user.get("code").toString();
					if ((!Strings.isNullOrEmpty(zone)) && (!Strings.isNullOrEmpty(phone))
							&& (!Strings.isNullOrEmpty(code))) {
						User users = userDao.getUserByZoneAndPhone(zone, phone);
						if (users != null) {
							if (user.containsKey("link_account")) {
								JSONObject link = JSONObject.fromObject(user.get("link_account"));
								LinkAccounts la = new LinkAccounts();
								la.setAuth_token(link.getString("auth_token"));
								la.setUser_id(users.getId());
								la.setUnion_id(link.getString("union_id"));
								linkAccountsDao.save(la);
								JSONObject json = new JSONObject();
								json.put("userid", users.getId());
								String raw = users.getId() + users.getPassword() + users.getCreated_time();
								String token = EncryptionUtil.hashMessage(raw);
								if (!Strings.isNullOrEmpty(users.getUsername())) {
									json.put("username", users.getUsername());
								}
								json.put("access_token", token);
								json.put("token_timestamp", users.getCreated_time());
								return Response.status(Response.Status.OK).entity(json).build();
							} else {
								JSONObject jo = new JSONObject();
								jo.put("status", "phone_exists");
								jo.put("code", Integer.valueOf(10094));
								jo.put("error_message", "手机号已注册");
								return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
							}

						}

						String appkey = getClass().getResource("/../../META-INF/phone.json").getPath();

						JSONObject jsonObject = parseJson(appkey);
						String key = jsonObject.getString("appkey");

						String param = "appkey=" + key + "&phone=" + phone + "&zone=" + zone + "&&code=" + code;
						String result = "";
						result = requestData("https://webapi.sms.mob.com/sms/verify", param);
						if (!Strings.isNullOrEmpty(result)) {
							JSONObject json = JSONObject.fromObject(result);
							String status = json.get("status").toString();
							if (status.equals("200")) {
								u.setZone(zone);
								u.setPhone(phone);
							} else {
								if (status.equals("512")) {
									JSONObject j = new JSONObject();
									j.put("status", "验证失败");
									j.put("code", Integer.valueOf(10100));
									j.put("error_message", "服务器拒绝访问，或者拒绝操作");
									return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
								}
								if (status.equals("405")) {
									JSONObject j = new JSONObject();
									j.put("status", "验证失败");
									j.put("code", Integer.valueOf(10101));
									j.put("error_message", "求Appkey不存在或被禁用");
									return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
								}

								if (status.equals("406")) {
									JSONObject j = new JSONObject();
									j.put("status", "验证失败");
									j.put("code", Integer.valueOf(10101));
									j.put("error_message", "求Appkey不存在或被禁用");
									return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
								}
								if (status.equals("514")) {
									JSONObject j = new JSONObject();
									j.put("status", "验证失败");
									j.put("code", Integer.valueOf(10102));
									j.put("error_message", "权限不足");
									return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
								}
								if (status.equals("515")) {
									JSONObject j = new JSONObject();
									j.put("status", "验证失败");
									j.put("code", 10103);
									j.put("error_message", "服务器内部错误");
									return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
								}
								if (status.equals("517")) {
									JSONObject j = new JSONObject();
									j.put("status", "验证失败");
									j.put("code", Integer.valueOf(10104));
									j.put("error_message", "缺少必要的请求参数");
									return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
								}
								if (status.equals("518")) {
									JSONObject j = new JSONObject();
									j.put("status", "验证失败");
									j.put("code", Integer.valueOf(10105));
									j.put("error_message", "请求中用户的手机号格式不正确（包括手机的区号）");
									return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
								}
								if (status.equals("519")) {
									JSONObject j = new JSONObject();
									j.put("status", "验证失败");
									j.put("code", Integer.valueOf(10106));
									j.put("error_message", "请求发送验证码次数超出限制");
									return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
								}
								if (status.equals("468")) {
									JSONObject j = new JSONObject();
									j.put("status", "验证失败");
									j.put("code", Integer.valueOf(10111));
									j.put("error_message", "无效验证码");
									return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
								}

								if (status.equals("467")) {
									JSONObject j = new JSONObject();
									j.put("status", "验证失败");
									j.put("code", Integer.valueOf(10110));
									j.put("error_message", "请求校验验证码频繁");
									return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
								}

								JSONObject j = new JSONObject();
								j.put("status", "验证失败");
								j.put("code", Integer.valueOf(10109));
								j.put("error_message", "验证失败");
								return Response.status(Response.Status.BAD_REQUEST).entity(j).build();

							}
						} else {
							JSONObject j = new JSONObject();
							j.put("status", "短信验证失败");
							j.put("code", Integer.valueOf(10108));
							j.put("error_message", "shareSDK 报错");
							return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
						}
					} else {
						JSONObject jo = new JSONObject();
						jo.put("status", "request_invalid");
						jo.put("code", Integer.valueOf(10010));
						jo.put("error_message", "request is invalid");
						return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
					}

				}

				if (!user.containsKey("password")) {
					String chars = "abcde0f12g3hi4jk5l6m7n8o9pqrstuvwxyz";
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < 10; i++) {
						char c = chars.charAt((int) (Math.random() * 36));
						sb.append(c);
					}
					String pwd = Base64Utils.encodeToString(sb.toString().getBytes());
					u.setPassword(pwd);
				} else {
					u.setPassword(user.getString("password"));
				}

				if (user.containsKey("level") && !Strings.isNullOrEmpty(user.getString("level"))) {
					u.setLevel(user.getString("level"));
				} else {
					u.setLevel("normal");
				}

				u.setStatus("enable");
				u.setResource("register");
				this.userDao.save(u);
				if (user.containsKey("link_account")) {
					JSONObject link = JSONObject.fromObject(user.get("link_account"));
					LinkAccounts la = new LinkAccounts();
					la.setAuth_token(link.getString("auth_token"));
					la.setUser_id(u.getId());
					la.setUnion_id(link.getString("union_id"));
					linkAccountsDao.save(la);
				}

				System.out.println("create successful");
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject jo = new JSONObject();
			jo.put("status", "invalid request");
			jo.put("code", Integer.valueOf(10107));
			jo.put("error_message", "无效请求");
			return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
		}

		JSONObject json = new JSONObject();
		json.put("userid", u.getId());
		String raw = u.getId() + u.getPassword() + u.getCreated_time();
		String token = EncryptionUtil.hashMessage(raw);
		json.put("access_token", token);
		json.put("token_timestamp", u.getCreated_time());
		json.put("level", u.getLevel());
		if(!Strings.isNullOrEmpty(u.getClub_name())){
			json.put("club_name", u.getClub_name());
		}
		if(!Strings.isNullOrEmpty(u.getTrade())){
			json.put("trade", u.getTrade());
		}
		return Response.status(Response.Status.CREATED).entity(json).build();

	}
	
	@Override
	public Response fblife_user(JSONObject user) {
		try {
			if (user != null) {
				if (user.containsKey("identity_card") && user.containsKey("username")) {
					String idcard = user.get("identity_card").toString();
					String username = user.get("username").toString();
					
					if(!Strings.isNullOrEmpty(idcard) && !Strings.isNullOrEmpty(username)){
						User u = userDao.getUserByIdcard(idcard);
						if(u != null){
							Random random = new Random();
							String randStr = String.valueOf(100000 + random.nextInt(899999));
							u.setTmp_pass(randStr);
							userDao.update(u);
							JSONObject json = new JSONObject();
							json.put("userid", u.getId());
							json.put("password", u.getTmp_pass());
							String raw = u.getId() + u.getPassword() + u.getCreated_time();
							String token = EncryptionUtil.hashMessage(raw);
							json.put("access_token", token);
							json.put("token_timestamp", u.getCreated_time());
							json.put("level", u.getLevel());
							if(!Strings.isNullOrEmpty(u.getClub_name())){
								json.put("club_name", u.getClub_name());
							}
							if(!Strings.isNullOrEmpty(u.getTrade())){
								json.put("trade", u.getTrade());
							}
							return Response.status(Response.Status.OK).entity(json).build();
						}else{
							String userJson = getUserInfoByIdCard(idcard, username);
							if(!Strings.isNullOrEmpty(userJson)){
								JSONObject uJson = JSONObject.fromObject(userJson);
								JSONObject resp = uJson.getJSONObject("resp");
								int code = resp.getInt("code");
								if(code == 0){
									u = new User();
									JSONObject data = uJson.getJSONObject("data");
									u.setAddress(data.getString("address"));
									u.setGender(data.getString("sex"));
									u.setBirthday(data.getString("birthday"));
									u.setUsername(username);
									if(user.containsKey("club_name") && !Strings.isNullOrEmpty(user.getString("club_name"))){
										u.setClub_name(user.getString("club_name"));
									}
									if(user.containsKey("trade") && !Strings.isNullOrEmpty(user.getString("trade"))){
										u.setTrade(user.getString("trade"));
									}
									Random random = new Random();
									String randStr = String.valueOf(100000 + random.nextInt(899999));
									String pwd = CodeUtil.Encrypt(randStr);
									u.setPassword(pwd);
									u.setTmp_pass(randStr);
									u.setZone("86");
									u.setPhone(user.getString("phone"));
									u.setIdentity_card(idcard);
//									u.setTrade(user.getString("trade"));
									u.setCreated_time(new Date());
									u.setLevel("normal");
									u.setStatus("enable");
									u.setResource("fblife");
									userDao.save(u);
									JSONObject json = new JSONObject();
									json.put("userid", u.getId());
									json.put("password", u.getTmp_pass());
									String raw = u.getId() + u.getPassword() + u.getCreated_time();
									String token = EncryptionUtil.hashMessage(raw);
									json.put("access_token", token);
									json.put("token_timestamp", u.getCreated_time());
									json.put("level", u.getLevel());
									if(!Strings.isNullOrEmpty(u.getClub_name())){
										json.put("club_name", u.getClub_name());
									}
									if(!Strings.isNullOrEmpty(u.getTrade())){
										json.put("trade", u.getTrade());
									}
									elkUser(u);
									return Response.status(Response.Status.OK).entity(json).build();
								}else{
									JSONObject jo = new JSONObject();
									jo.put("status", "身份证号验证失败");
									jo.put("code", Integer.valueOf(10303));
									jo.put("error_message", "身份证号验证失败");
									return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
								}
							}else{
								JSONObject jo = new JSONObject();
								jo.put("status", "身份证号验证失败");
								jo.put("code", Integer.valueOf(10303));
								jo.put("error_message", "身份证号验证失败");
								return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
							}
						}
						
					}
					
					
					

				}



				System.out.println("create successful");
			} else {
				JSONObject resp = new JSONObject();
				resp.put("status", "缺少必要的请求参数");
				resp.put("code", Integer.valueOf(10009));
				resp.put("error_message", "缺少必要的请求参数");
				return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject jo = new JSONObject();
			jo.put("status", "invalid request");
			jo.put("code", Integer.valueOf(10107));
			jo.put("error_message", "无效请求");
			return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
		}

	
		return null;

	
	}

	public Response signin(JSONObject userJson) {

		String password = userJson.getString("password");
		String timestamp = userJson.getString("timestamp");
//		String zone = userJson.getString("zone");
		String phone = userJson.getString("phone");
		User user = null;
		JSONObject jo = new JSONObject();
		JSONObject auth = new JSONObject();
		if ((!Strings.isNullOrEmpty(phone))) {// && (!Strings.isNullOrEmpty(zone))
			if (Strings.isNullOrEmpty(password)) {
				jo.put("status", "密码不能为空");
				jo.put("code", Integer.valueOf(10007));
				jo.put("error_message", "密码不能为空");
				return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
			}
			if(Validator.isMobile(phone)){
				User phoneUser = this.userDao.getUserByPhoneAndZone("86", phone);

				if (phoneUser != null) {
					try {
						user = this.userDao.loginByPhone("86", phone, password);

						if (user == null)
							throw new Exception("invalid password");
					} catch (Exception e) {
						jo.put("status", "invalid_password");
						jo.put("code", Integer.valueOf(10007));
						jo.put("error_message", "密码不正确");
						return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
					}
				} else {
					jo.put("status", "invalid_phone");
					jo.put("code", Integer.valueOf(10006));
					jo.put("error_message", "手机号不存在");
					return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
				}
			}else if(Validator.isIDCard(phone)){
				User idUser = this.userDao.getUserByIdcard(phone);

				if (idUser != null) {
					try {
						user = this.userDao.getUserListByIdcardAndPwd(phone, password);

						if (user == null)
							throw new Exception("invalid password");
					} catch (Exception e) {
						jo.put("status", "invalid_password");
						jo.put("code", Integer.valueOf(10007));
						jo.put("error_message", "密码不正确");
						return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
					}
				} else {
					jo.put("status", "身份证号不存在");
					jo.put("code", Integer.valueOf(10308));
					jo.put("error_message", "身份证号不存在");
					return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
				}
			}else{
				jo.put("status", "手机号或身份证号不正确");
				jo.put("code", Integer.valueOf(10321));
				jo.put("error_message", "手机号或身份证号不正确");
				return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
			}
			

			String raw = user.getId() + user.getPassword() + timestamp;
			String token = EncryptionUtil.hashMessage(raw);

			System.out.println("userId--->" + user.getId());
			auth.put("userid", user.getId());
			if (!Strings.isNullOrEmpty(user.getUsername())) {
				auth.put("username", user.getUsername());
			}
			
			if(!Strings.isNullOrEmpty(user.getPhone())){
				auth.put("phone", user.getPhone());
			}

			auth.put("access_token", token);
			auth.put("token_timestamp", Long.valueOf(Long.parseLong(timestamp)));
			auth.put("level", user.getLevel());
			if(!Strings.isNullOrEmpty(user.getClub_name())){
				auth.put("club_name", user.getClub_name());
			}
			if(!Strings.isNullOrEmpty(user.getTrade())){
				auth.put("trade", user.getTrade());
			}
		}

		System.out.println(auth.toString());
		return Response.status(Response.Status.OK).entity(auth).build();

	}

	public Response forgetPassword(JSONObject data, String appVersion, String device) throws Exception {

		String phone = data.getString("phone");
		String zone = data.getString("zone");
		String code = data.getString("code");
		String timestamp = data.getString("timestamp");
		String password = data.getString("password");
		String appkey = "";
		appkey = getClass().getResource("/../../META-INF/phone.json").getPath();
		JSONObject jsonObject = parseJson(appkey);
		String key = jsonObject.getString("appkey");
		if ((!Strings.isNullOrEmpty(phone)) && (!Strings.isNullOrEmpty(zone)) && (!Strings.isNullOrEmpty(code))
				&& (!Strings.isNullOrEmpty(password))) {
			User user = this.userDao.getUserByZoneAndPhone(zone, phone);
			if (user != null) {
				String param = "appkey=" + key + "&phone=" + phone + "&zone=" + zone + "&&code=" + code;
				String result = "";
				result = requestData("https://webapi.sms.mob.com/sms/verify", param);
				if (!Strings.isNullOrEmpty(result)) {
					JSONObject json = JSONObject.fromObject(result);
					String status = json.get("status").toString();
					if (status.equals("200")) {
						user.setPassword(password);
						this.userDao.update(user);
						String raw = user.getId() + user.getPassword() + timestamp;
						String token = EncryptionUtil.hashMessage(raw);
						JSONObject auth = new JSONObject();
						System.out.println("userId--->" + user.getId());
						auth.put("userid", user.getId());
						if (!Strings.isNullOrEmpty(user.getUsername())) {
							auth.put("username", user.getUsername());
						}
						auth.put("access_token", token);
						auth.put("level", user.getLevel());
						auth.put("token_timestamp", Long.valueOf(Long.parseLong(timestamp)));
						if(!Strings.isNullOrEmpty(user.getClub_name())){
							auth.put("club_name", user.getClub_name());
						}
						if(!Strings.isNullOrEmpty(user.getTrade())){
							auth.put("trade", user.getTrade());
						}
						System.out.println(auth.toString());
						return Response.status(Response.Status.OK).entity(auth).build();
					}
					if (status.equals("512")) {
						JSONObject j = new JSONObject();
						j.put("status", "验证失败");
						j.put("code", Integer.valueOf(10100));
						j.put("error_message", "服务器拒绝访问，或者拒绝操作");
						return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
					}
					if (status.equals("513")) {
						JSONObject j = new JSONObject();
						j.put("status", "验证失败");
						j.put("code", Integer.valueOf(10101));
						j.put("error_message", "求Appkey不存在或被禁用。");
						return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
					}
					if (status.equals("514")) {
						JSONObject j = new JSONObject();
						j.put("status", "验证失败");
						j.put("code", Integer.valueOf(10102));
						j.put("error_message", "权限不足");
						return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
					}
					if (status.equals("515")) {
						JSONObject j = new JSONObject();
						j.put("status", "验证失败");
						j.put("code", Integer.valueOf(10103));
						j.put("error_message", "服务器内部错误");
						return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
					}
					if (status.equals("517")) {
						JSONObject j = new JSONObject();
						j.put("status", "验证失败");
						j.put("code", Integer.valueOf(10104));
						j.put("error_message", "缺少必要的请求参数");
						return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
					}
					if (status.equals("518")) {
						JSONObject j = new JSONObject();
						j.put("status", "验证失败");
						j.put("code", Integer.valueOf(10105));
						j.put("error_message", "请求中用户的手机号格式不正确（包括手机的区号）");
						return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
					}
					if (status.equals("519")) {
						JSONObject j = new JSONObject();
						j.put("status", "验证失败");
						j.put("code", Integer.valueOf(10106));
						j.put("error_message", "请求发送验证码次数超出限制");
						return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
					}
					if (status.equals("520")) {
						JSONObject j = new JSONObject();
						j.put("status", "验证失败");
						j.put("code", Integer.valueOf(10107));
						j.put("error_message", "无效验证码。");
						return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
					}
					
					if (status.equals("468")) {
						JSONObject j = new JSONObject();
						j.put("status", "验证失败");
						j.put("code", Integer.valueOf(10195));
						j.put("error_message", "Illegal check request.");
						return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
					}
				} else {
					JSONObject j = new JSONObject();
					j.put("status", "Invalid_phone");
					j.put("code", Integer.valueOf(10091));
					j.put("error_message", "phone or code is not true");
					return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
				}
			} else {
				JSONObject j = new JSONObject();
				j.put("status", "验证失败");
				j.put("code", Integer.valueOf(10103));
				j.put("error_message", "该用户不存在");
				return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
			}

			return null;
		}
		JSONObject j = new JSONObject();
		j.put("status", "Invalid_phone");
		j.put("code", Integer.valueOf(10091));
		j.put("error_message", "phone or code is not true");
		return Response.status(Response.Status.BAD_REQUEST).entity(j).build();

	}

	public Response updateUser(Long userId, JSONObject user, Long loginUserid) {
		try {
			System.out.println(user.toString());
			if (loginUserid.equals(userId)) {
				User u = (User) this.userDao.get(userId);
				if (user.containsKey("identity_card") && !Strings.isNullOrEmpty(user.getString("identity_card"))) {
					String idcard = user.getString("identity_card");
					User userModel = userDao.getUserByIdcard(idcard);
					if (userModel == null) {
						u.setIdentity_card(idcard);

						if (user.get("club_name") != null) {
							u.setClub_name(user.getString("club_name"));
						}
						if (user.containsKey("username") && !Strings.isNullOrEmpty(user.getString("username"))) {
							u.setUsername(user.getString("username"));
						}

						if (user.containsKey("gender") && !Strings.isNullOrEmpty(user.getString("gender"))) {
							u.setGender(user.getString("gender"));
						}

						if (user.containsKey("address") && !Strings.isNullOrEmpty(user.getString("address"))) {
							u.setAddress(user.getString("address"));
						}

						if (user.containsKey("trade") && !Strings.isNullOrEmpty(user.getString("trade"))) {
							u.setTrade(user.getString("trade"));
						}
						if (user.containsKey("club_name") && !Strings.isNullOrEmpty(user.getString("club_name"))) {
							u.setClub_name(user.getString("club_name"));
						}

						if (user.containsKey("birthday") && !Strings.isNullOrEmpty(user.getString("birthday"))) {
							u.setBirthday(user.getString("birthday"));
						}

						this.userDao.update(u);
						List<Contacter> cList = contacterDao.getContacterByIdentityCard(u.getIdentity_card(), u.getId());
						if(cList == null || cList.size() == 0){
							Contacter c = new Contacter();
							c.setIdentity_card(u.getIdentity_card());
							c.setName(u.getUsername());
							c.setPhone(u.getPhone());
							c.setBirthday(u.getBirthday());
							c.setGender(u.getGender());
							c.setUser(u);
							contacterDao.save(c);
						}
						
						
						JSONObject jo = new JSONObject();
						jo.put("username", u.getUsername());
						return Response.status(Response.Status.OK).entity(jo).build();
					} else {
						JSONObject j = new JSONObject();
						j.put("status", "该身份证号已被使用");
						j.put("code", Integer.valueOf(10802));
						j.put("error_message", "该身份证号已被使用");
						return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
					}
				} else {
					if (user.containsKey("trade")) {
						if (user.get("trade") != null) {
							u.setTrade(user.getString("trade"));
						}
					}

					if (user.containsKey("username")) {
						if (user.get("username") != null) {
							u.setUsername(user.getString("username"));
						}
					}

					if (user.get("club_name") != null) {
						u.setClub_name(user.getString("club_name"));
					}
					if (user.containsKey("username") && !Strings.isNullOrEmpty(user.getString("username"))) {
						u.setUsername(user.getString("username"));
					}

					if (user.containsKey("gender") && !Strings.isNullOrEmpty(user.getString("gender"))) {
						u.setGender(user.getString("gender"));
					}

					if (user.containsKey("address") && !Strings.isNullOrEmpty(user.getString("address"))) {
						u.setAddress(user.getString("address"));
					}

					if (user.containsKey("trade") && !Strings.isNullOrEmpty(user.getString("trade"))) {
						u.setTrade(user.getString("trade"));
					}
					if (user.containsKey("club_name") && !Strings.isNullOrEmpty(user.getString("club_name"))) {
						u.setClub_name(user.getString("club_name"));
					}

					if (user.containsKey("birthday") && !Strings.isNullOrEmpty(user.getString("birthday"))) {
						u.setBirthday(user.getString("birthday"));
					}

					this.userDao.update(u);
					JSONObject jo = new JSONObject();
					jo.put("username", u.getUsername());
					return Response.status(Response.Status.OK).entity(jo).build();

				}

			} else {
				JSONObject json1 = new JSONObject();
				json1.put("status", "invalid_request");
				json1.put("code", Integer.valueOf(10010));
				json1.put("error_message", "Invalid payload parameters");
				return Response.status(Response.Status.BAD_REQUEST).entity(json1).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject json1 = new JSONObject();
			json1.put("status", "invalid_request");
			json1.put("code", Integer.valueOf(10010));
			json1.put("error_message", "异常信息");
			return Response.status(Response.Status.BAD_REQUEST).entity(json1).build();
		}

	}

	public Response loginLinkAccounts(LinkAccounts la, HttpServletRequest request) {

		JSONObject auth = new JSONObject();
		Object[] link = null;
		if (la != null) {
			String union_id = la.getUnion_id();
			link = this.linkAccountsDao.getLinkAccountsByUUID(union_id);

			if (link != null) {
				User user = (User) link[1];
				String raw = user.getId() + user.getPassword() + user.getCreated_time();
				String token = EncryptionUtil.hashMessage(raw);

				System.out.println("userId--->" + user.getId());
				auth.put("userid", user.getId());
				auth.put("access_token", token);
				auth.put("level", user.getLevel());
				auth.put("token_timestamp", user.getCreated_time());
				if (!Strings.isNullOrEmpty(user.getUsername())) {
					auth.put("username", user.getUsername());
				}
				
				if(!Strings.isNullOrEmpty(user.getClub_name())){
					auth.put("club_name", user.getClub_name());
				}
				if(!Strings.isNullOrEmpty(user.getTrade())){
					auth.put("trade", user.getTrade());
				}
				System.out.println(auth.toString());
				return Response.status(Response.Status.OK).entity(auth).build();
			}
			auth.put("status", "no_user");
			auth.put("code", Integer.valueOf(10080));
			auth.put("error_message", "该用户不存在");

			return Response.status(Response.Status.BAD_REQUEST).entity(auth).build();
		}

		auth.put("status", "invalid request");
		auth.put("code", Integer.valueOf(10010));
		auth.put("error_message", "Invalid request payload");
		return Response.status(Response.Status.BAD_REQUEST).entity(auth).build();

	}

	public JSONObject parseJson(String path) {
		String sets = ReadFile(path);
		JSONObject jo = JSONObject.fromObject(sets);
		return jo;
	}

	public String requestData(String address, String params) {
		HttpURLConnection conn = null;
		try {
			TrustManager[] trustAllCerts = { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new SecureRandom());

			HostnameVerifier hv = new HostnameVerifier() {
				public boolean verify(String urlHostName, SSLSession session) {
					return urlHostName.equals(session.getPeerHost());
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(hv);

			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			URL url = new URL(address);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			if (params != null) {
				conn.setDoOutput(true);
				DataOutputStream out = new DataOutputStream(conn.getOutputStream());
				out.write(params.getBytes(Charset.forName("UTF-8")));
				out.flush();
				out.close();
			}
			conn.connect();

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				String result = parsRtn(conn.getInputStream());
				String str1 = result;
				return str1;
			}
			System.out.println(conn.getResponseCode() + " " + conn.getResponseMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.disconnect();
		}
		return null;
	}

	private String parsRtn(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = null;
		boolean first = true;
		while ((line = reader.readLine()) != null) {
			if (first)
				first = false;
			else {
				buffer.append("\n");
			}
			buffer.append(line);
		}
		return buffer.toString();
	}

	public String ReadFile(String path) {
		File file = new File(path);
		BufferedReader reader = null;
		String laststr = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;

			while ((tempString = reader.readLine()) != null) {
				laststr = laststr + tempString;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();

			if (reader != null)
				try {
					reader.close();
				} catch (IOException localIOException1) {
				}
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException localIOException2) {
				}
		}
		return laststr;
	}

	public Response updatePassword(Long loginUserid, Long userId, PasswordModel pwdModel) {
		JSONObject json = new JSONObject();
		if (userId.equals(loginUserid)) {
			User user = (User) this.userDao.get(loginUserid);

			if (user.getPassword().toUpperCase().equals(pwdModel.getCurrent_password().toUpperCase())) {
				user.setPassword(pwdModel.getPassword_new());
				this.userDao.update(user);
				String raw = user.getId() + user.getPassword() + user.getCreated_time();
				String token = EncryptionUtil.hashMessage(raw);

				System.out.println("userId--->" + user.getId());
				json.put("userid", user.getId());
				json.put("level", user.getLevel());
				json.put("username", user.getUsername());
				json.put("access_token", token);
				json.put("token_timestamp", user.getCreated_time());
			} else {
				JSONObject json1 = new JSONObject();
				json1.put("status", "invalid_original_password");
				json1.put("code", Integer.valueOf(10004));
				json1.put("error_message", "原密码错误");
				return Response.status(Response.Status.BAD_REQUEST).entity(json1).build();
			}

		}

		System.out.println(json.toString());
		return Response.status(Response.Status.OK).entity(json).build();
	}

	@Override
	public Response my_collect(Long loginUserid, HttpServletRequest request) {
		String countStr = request.getParameter("count");
		String maxIdStr = request.getParameter("max_id");
		JSONObject error = new JSONObject();
		int count = 20;
		// List<JSONObject> typeJSONList = new ArrayList<JSONObject>();
		if (loginUserid != null && loginUserid > 0) {
			List<Collect> collectList = null;
			if (Strings.isNullOrEmpty(countStr) && Strings.isNullOrEmpty(maxIdStr)) {
				collectList = collectDao.getCollectListByUseridAndCount(loginUserid, count);
			} else if (!Strings.isNullOrEmpty(countStr) && Strings.isNullOrEmpty(maxIdStr)) {
				count = Integer.parseInt(countStr);
				collectList = collectDao.getCollectListByUseridAndCount(loginUserid, count);
			} else if (Strings.isNullOrEmpty(countStr) && !Strings.isNullOrEmpty(maxIdStr)) {
				Long max_id = Long.parseLong(maxIdStr);
				collectList = collectDao.getCollectListByUseridAndCountAndId(loginUserid, count, max_id);
			} else if (!Strings.isNullOrEmpty(countStr) && !Strings.isNullOrEmpty(maxIdStr)) {
				Long max_id = Long.parseLong(maxIdStr);
				count = Integer.parseInt(countStr);
				collectList = collectDao.getCollectListByUseridAndCountAndId(loginUserid, count, max_id);
			}

			List<JSONObject> collectJSONList = new ArrayList<JSONObject>();
			if (collectList != null && collectList.size() > 0) {
				JSONObject collectJson = null;
				JSONObject content = null;
				for (Collect c : collectList) {
					collectJson = new JSONObject();
					content = new JSONObject();
					collectJson.put("id", c.getId());
					collectJson.put("reference_type", c.getReference_type());
					String type = c.getReference_type();
					if (type.equals("poi")) {
						Poi p = poiDao.get(c.getReference_id());
						content.put("id", p.getId());
						content.put("title", p.getTitle());
						content.put("cover_image", JSONObject.fromObject(p.getElements().get(0).getElement()));
						content.put("is_collect", true);
						collectJson.put("content", content);
					} else if (type.equals("content")) {

					}
					collectJson.put("create_time", c.getCreate_time());

					collectJSONList.add(collectJson);
				}
			}
			return Response.status(Response.Status.OK).entity(collectJSONList).build();
		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}
	}

	@Override
	public Response add_cart(JSONObject cart, Long loginUserid) {
		JSONObject error = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			if (cart != null) {
				Cart cartModel = new Cart();
				cartModel.setUser_id(loginUserid);
				cartModel.setNum(1);
				Long ticket_id = cart.getLong("ticket_id");
				Ticket ticket = ticketDao.get(ticket_id);
				cartModel.setTicket(ticket);
				cartDao.save(cartModel);
				JSONObject json = new JSONObject();
				json.put("status", "success");
				return Response.status(Response.Status.OK).entity(json).build();
			} else {
				error.put("status", "缺少必要的请求参数");
				error.put("code", Integer.valueOf(10009));
				error.put("error_message", "缺少必要的请求参数");
				return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
			}
		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}

	}

	@Override
	public Response cart_list(Long loginUserid, HttpServletRequest request) {
		String countStr = request.getParameter("count");
		String maxIdStr = request.getParameter("max_id");
		JSONObject error = new JSONObject();
		int count = 20;
		if (loginUserid != null && loginUserid > 0) {
			List<JSONObject> cartJSONList = new ArrayList<JSONObject>();
			List<Cart> cartList = null;
			if (Strings.isNullOrEmpty(countStr) && Strings.isNullOrEmpty(maxIdStr)) {
				cartList = cartDao.getCartList(loginUserid, count);
			} else if (!Strings.isNullOrEmpty(countStr) && Strings.isNullOrEmpty(maxIdStr)) {
				count = Integer.parseInt(countStr);
				cartList = cartDao.getCartList(loginUserid, count);
			} else if (Strings.isNullOrEmpty(countStr) && !Strings.isNullOrEmpty(maxIdStr)) {
				Long max_id = Long.parseLong(maxIdStr);
				cartList = cartDao.getCartList(loginUserid, count, max_id);
			} else if (!Strings.isNullOrEmpty(countStr) && !Strings.isNullOrEmpty(maxIdStr)) {
				Long max_id = Long.parseLong(maxIdStr);
				count = Integer.parseInt(countStr);
				cartList = cartDao.getCartList(loginUserid, count, max_id);
			}
			if (cartList != null && cartList.size() > 0) {
				for (Cart cart : cartList) {
					JSONObject cartJson = new JSONObject();
					cartJson.put("id", cart.getId());
					cartJson.put("num", cart.getNum());
					cartJson.put("create_time", cart.getCreate_time());
					Ticket ticket = cart.getTicket();
					JSONObject ticketJson = new JSONObject();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					String startTimeStr = ticket.getStart_date()+" "+ticket.getStart_time();
					String endTimeStr = ticket.getStart_date()+" "+ticket.getEnd_time();
					long start_time = 0l;
					long end_time = 0l;
					long current_time = 0l;
					try {
						Date startTime = sdf.parse(startTimeStr);
						Date endTime = sdf.parse(endTimeStr);
						Date current_date = sdf.parse(sdf.format(new Date()));
						start_time = startTime.getTime() / 1000;
						end_time = endTime.getTime() / 1000;
						current_time = current_date.getTime() / 1000;
					} catch (ParseException e) {
						e.printStackTrace();
					}
					ticketJson.put("ticket_id", ticket.getId());
					ticketJson.put("ticket_name", ticket.getTicket_name());
					ticketJson.put("price", ticket.getPrice());
					ticketJson.put("start_date", start_time);
					ticketJson.put("start_time", end_time);
					ticketJson.put("cover_image", JSONObject.fromObject(ticket.getPoi().getElements().get(0).getElement()));
					cartJson.put("ticket", ticketJson);
					cartJson.put("total", cart.getNum() * ticket.getPrice().longValue());
					cartJSONList.add(cartJson);
				}
			}

			return Response.status(Response.Status.OK).entity(cartJSONList).build();
		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}
	}

	@Override
	public Response delete_cart(Long loginUserid, Long cart_id) {
		JSONObject error = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			if (cart_id != null && cart_id > 0) {
				cartDao.delete(cart_id);
				JSONObject json = new JSONObject();
				json.put("id", cart_id);
				return Response.status(Response.Status.OK).entity(json).build();
			} else {
				error.put("status", "缺少必要的请求参数");
				error.put("code", Integer.valueOf(10009));
				error.put("error_message", "缺少必要的请求参数");
				return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
			}
		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}
	}

	@Override
	public Response add_charge(Long loginUserid, JSONObject orders, HttpServletRequest request) {
		JSONObject error = new JSONObject();
		String ip = request.getHeader("X-Real-IP");
		if (Strings.isNullOrEmpty(ip)) {
			ip = request.getRemoteAddr();
		}
		if (loginUserid != null && loginUserid > 0) {
			
			Charge c = null;
			if (orders != null) {
				Long id = orders.getLong("id");
				Orders o = ordersDao.get(id);
				if (o.getStatus() == 0) {
					
					List<OrdersTicket> otList = ordersTicketDao.getOrdersTicketByOrdersId(o.getId());
					int type = 0;
					if(otList != null && otList.size() > 0){
						OrdersTicket ot = otList.get(0);
						Ticket ticket = ot.getTicket();
						if(ticket.getType().equals("vip")){

							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//							String startTimeStr = ticket.getStart_date()+" "+ticket.getStart_time();
							String endTimeStr = ticket.getEnd_date()+" 23:59";
							long end_time = 0l;
							long current_time = 0l;
							try {
//								Date startTime = sdf.parse(startTimeStr);
								Date endTime = sdf.parse(endTimeStr);
								end_time = endTime.getTime() / 1000;
								current_time = new Date().getTime() / 1000;
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
//							List<String> idcardList = new ArrayList<String>();
//							for(OrdersTicket ots:otList){
//								idcardList.add(ots.getContacter().getIdentity_card());
//							}
							
							int number = ticket.getNumber();
							
							//是否有人已投保
//							List<Insurant> iList = insurantDao.getInsurantByIdentityCards(idcardList);
							
							if(current_time > end_time){
								type = 1;
							}else if(number == 0){
								type = 2;
							}else if(number < otList.size()){
								type = 3;
							}else{
								type = 0;
							}
						
						}else{
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//							String startTimeStr = ticket.getStart_date()+" "+ticket.getStart_time();
							String endTimeStr = ticket.getStart_date()+" "+ticket.getEnd_time();
							long end_time = 0l;
							long current_time = 0l;
							try {
//								Date startTime = sdf.parse(startTimeStr);
								Date endTime = sdf.parse(endTimeStr);
								end_time = endTime.getTime() / 1000;
								current_time = new Date().getTime() / 1000;
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
//							List<String> idcardList = new ArrayList<String>();
//							for(OrdersTicket ots:otList){
//								idcardList.add(ots.getContacter().getIdentity_card());
//							}
							
							int number = ticket.getNumber();
							
							//是否有人已投保
//							List<Insurant> iList = insurantDao.getInsurantByIdentityCards(idcardList);
							
							if(current_time > end_time){
								type = 1;
							}else if(number == 0){
								type = 2;
							}else if(number < otList.size()){
								type = 3;
							}else{
								type = 0;
							}
						}
						
					}
					
					double totalD = 0;
					for(OrdersTicket ot:otList){
						Double otTotal = ot.getTotal().doubleValue();
						totalD += otTotal;
					}
					BigDecimal total = BigDecimal.valueOf(totalD);
					
					if(!Strings.isNullOrEmpty(o.getCharge_id())){
						if(type == 1){
							error.put("status", "该票已过期");
							error.put("code", Integer.valueOf(10300));
							error.put("error_message", "该票已过期");
							return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
						}else if(type == 2){
							error.put("status", "该票已售完");
							error.put("code", Integer.valueOf(10301));
							error.put("error_message", "该票已售完");
							return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
						}else if(type == 3){
							error.put("status", "余票不足");
							error.put("code", Integer.valueOf(10302));
							error.put("error_message", "余票不足");
							return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
						}else if(type == 4){
							error.put("status", "该订单已失效");
							error.put("code", Integer.valueOf(10303));
							error.put("error_message", "该订单已失效");
							return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
						}else if(type == 0){

							String amountStr = orders.getString("amount");
							BigDecimal amount = BigDecimal.valueOf(Double.valueOf(amountStr));
							
							if (o.getAmount().compareTo(amount) == 0 && total.compareTo(amount) == 0) {
								Pingpp.privateKeyPath = getClass().getResource("/../../META-INF/ping_1024_priv.pem").getPath();
								Pingpp.apiKey = getPingSecretKey();
								Map<String, Object> chargeParams = new HashMap<String, Object>();
								chargeParams.put("order_no", o.getOrder_no());
								chargeParams.put("amount", amount.doubleValue() * 100);
								Map<String, String> app = new HashMap<String, String>();
								String appid = getPingAppid();
								String channel = orders.getString("pay_type");
								app.put("id", appid);
								chargeParams.put("app", app);
								chargeParams.put("channel", channel);
								chargeParams.put("currency", "cny");
								chargeParams.put("order_no", o.getOrder_no());
								chargeParams.put("client_ip", ip);
								chargeParams.put("subject", "收款");
								chargeParams.put("body", "Your Body");
								if(channel.equals("wx_pub")){
									Map<String,String> extra = new HashMap<String,String>();
									extra.put("open_id", orders.getString("open_id"));
									chargeParams.put("extra", extra);
								}
								try {
									c = Charge.create(chargeParams);
									System.out.println("aaaaaaaaaaaa---->>"+JSONObject.fromObject(c));
									o.setCharge_id(c.getId());
									o.setPay_type(channel);
									ordersDao.update(o);
								} catch (AuthenticationException e) {
									e.printStackTrace();
								} catch (InvalidRequestException e) {
									e.printStackTrace();
								} catch (APIConnectionException e) {
									e.printStackTrace();
								} catch (APIException e) {
									e.printStackTrace();
								} catch (ChannelException e) {
									e.printStackTrace();
								} catch (RateLimitException e) {
									e.printStackTrace();
								}
							}
						
						}else{
							error.put("status", "您提交的价格和订单价格不符");
							error.put("code", Integer.valueOf(10312));
							error.put("error_message", "您提交的价格和订单价格不符");
							return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
						}
					}else{

						if(type == 1){
							error.put("status", "该票已过期");
							error.put("code", Integer.valueOf(10300));
							error.put("error_message", "该票已过期");
							return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
						}else if(type == 2){
							error.put("status", "该票已售完");
							error.put("code", Integer.valueOf(10301));
							error.put("error_message", "该票已售完");
							return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
						}else if(type == 3){
							error.put("status", "余票不足");
							error.put("code", Integer.valueOf(10302));
							error.put("error_message", "余票不足");
							return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
						}else if(type == 4){
							error.put("status", "该订单已失效");
							error.put("code", Integer.valueOf(10303));
							error.put("error_message", "该订单已失效");
							return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
						}else if(type == 0){
							String amountStr = orders.getString("amount");
							BigDecimal amount = BigDecimal.valueOf(Double.valueOf(amountStr));
							if (o.getAmount().compareTo(amount) == 0 && total.compareTo(amount) == 0) {
								Pingpp.privateKeyPath = getClass().getResource("/../../META-INF/ping_1024_priv.pem").getPath();
								Pingpp.apiKey = getPingSecretKey();
								Map<String, Object> chargeParams = new HashMap<String, Object>();
								chargeParams.put("order_no", o.getOrder_no());
								chargeParams.put("amount", Double.valueOf(amountStr)*100);
								Map<String, String> app = new HashMap<String, String>();
								String appid = getPingAppid();
								String channel = orders.getString("pay_type");
								app.put("id", appid);
								chargeParams.put("app", app);
								chargeParams.put("channel", channel);
								chargeParams.put("currency", "cny");
								chargeParams.put("order_no", o.getOrder_no());
								chargeParams.put("client_ip", ip);
								chargeParams.put("subject", "收款");
								chargeParams.put("body", "Your Body");
								if(channel.equals("wx_pub")){
									Map<String,String> extra = new HashMap<String,String>();
									extra.put("open_id", orders.getString("open_id"));
									chargeParams.put("extra", extra);
								}
								try {
									c = Charge.create(chargeParams);
									System.out.println("aaaaaaaaaaaa---->>"+JSONObject.fromObject(c));
									o.setCharge_id(c.getId());
									o.setPay_type(channel);
									ordersDao.update(o);
								} catch (AuthenticationException e) {
									e.printStackTrace();
								} catch (InvalidRequestException e) {
									e.printStackTrace();
								} catch (APIConnectionException e) {
									e.printStackTrace();
								} catch (APIException e) {
									e.printStackTrace();
								} catch (ChannelException e) {
									e.printStackTrace();
								} catch (RateLimitException e) {
									e.printStackTrace();
								}
							}else{
								error.put("status", "您提交的价格和订单价格不符");
								error.put("code", Integer.valueOf(10312));
								error.put("error_message", "您提交的价格和订单价格不符");
								return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
							}
						}else{
							error.put("status", "您提交的价格和订单价格不符");
							error.put("code", Integer.valueOf(10312));
							error.put("error_message", "您提交的价格和订单价格不符");
							return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
						}
					

						
					
					
					}
					
					
					
					
				}

				return Response.status(Response.Status.OK).entity(c).build();
			} else {
				error.put("status", "缺少必要的请求参数");
				error.put("code", Integer.valueOf(10009));
				error.put("error_message", "缺少必要的请求参数");
				return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
			}
		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}
	}
	
	@Override
	public Response free_ticket(Long loginUserid, JSONObject orders, HttpServletRequest request)throws Exception {

		JSONObject error = new JSONObject();
		JSONObject resp = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			
			if (orders != null) {
				Long id = orders.getLong("id");
				Orders order = ordersDao.get(id);
				BigDecimal total = order.getAmount();
				BigDecimal bd = new BigDecimal(0);
				if (order.getStatus() == 0 && total.compareTo(bd) == 0) {
					
					List<OrdersTicket> otList = ordersTicketDao.getOrdersTicketByOrdersId(order.getId());
					if(otList != null && otList.size() > 0){
						Zyb zyb = getZybInfo();
						String xmlMsg = ZybUtil.createOrdersXml(order.getUser(), order, otList, zyb);
						String sign = CodeUtil.encode2hex("xmlMsg=" + xmlMsg + zyb.getMasterSecret());
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("xmlMsg", xmlMsg);
						map.put("sign", sign);
						String result = HttpUtil.sendClientPost(zyb.getUrl(), map);
						log.debug("*******************智游宝返回*******"+result);
						String codeStr = ZybUtil.getReturnCode(result);
						// 智游宝
						if (!Strings.isNullOrEmpty(codeStr)) {
							int code = Integer.parseInt(codeStr);
							if (code == 0) {
								String xmlMsgCode = ZybUtil.findImgXml(order.getOrder_no(), zyb);
								String signCode = CodeUtil.encode2hex("xmlMsg=" + xmlMsgCode + zyb.getMasterSecret());
								Map<String, Object> mapCode = new HashMap<String, Object>();
								mapCode.put("xmlMsg", xmlMsgCode);
								mapCode.put("sign", signCode);
								String resultCode = HttpUtil
										.sendClientPost(zyb.getUrl(), mapCode);
								String barcode = ZybUtil.getImg(resultCode);
								if (!Strings.isNullOrEmpty(barcode)) {

									// 智游宝 下单成功
									order.setStatus(1);
									ordersDao.update(order);
									int size = 0;
									
									if (otList != null && otList.size() > 0) {
										size = otList.size();
										for (OrdersTicket ot : otList) {
											barcode = barcode.replaceAll("gmCheckNo", "gmCheckCode");
											ot.setBarcode(barcode);// 测试智游宝通过后 完善这里
											ot.setStatus(0); // 未使用
											ordersTicketDao.update(ot);
										}
										Ticket t = otList.get(0).getTicket();
										t.setNumber(t.getNumber() - size);
										t.setSold(t.getSold()+size);
										ticketDao.update(t);
									}
									resp.put("status", "出票成功");
									return Response.status(Response.Status.OK).entity(resp).build();
								} else {
									
									for(OrdersTicket ot:otList){
										// 调智游宝的部分退单接口
										String d = ZybUtil.getDateByFormat(new Date(), "yyyyMMddHHmm");
										String thirdReturnCode = "rt" + d;
										ot.setReturnCode(thirdReturnCode);
										String xmlMsg1 = ZybUtil.returnOrdersXml(ot.getTicket_order_no(), zyb, thirdReturnCode);
										String sign1 = CodeUtil.encode2hex("xmlMsg=" + xmlMsg1 + zyb.getMasterSecret());
										Map<String, Object> map1 = new HashMap<String, Object>();
										map1.put("xmlMsg", xmlMsg1);
										map1.put("sign", sign1);
										String result1 = HttpUtil.sendClientPost(zyb.getUrl(), map1);
										Map<String, String> dataMap = ZybUtil.getReturnData(result1);

										String codeStr1 = dataMap.get("code");

										if (codeStr1 != null) {
											int code1 = Integer.parseInt(codeStr1);
											if (code1 == 0) {
												ot.setRetreatBatchNo(dataMap.get("retreatBatchNo"));
												ot.setStatus(2);
												ordersTicketDao.update(ot);
											} 
										} 
									}
									resp.put("status", "票务系统返回二维码图片错误");
									resp.put("code", 10220);
									resp.put("error_message", "票务系统返回二维码图片错误");
									return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
								}
							} else {
								resp.put("status", "票务系统下单失败");
								resp.put("code", 10221);
								resp.put("error_message", "票务系统下单失败");
								return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
							}
						} else {
							resp.put("status", "票务系统下单失败");
							resp.put("code", 10222);
							resp.put("error_message", "票务系统下单失败");
							return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
						}
					
						
					}else{
						resp.put("status", "该订单无效");
						resp.put("code", 10223);
						resp.put("error_message", "该订单无效");
						return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
					}
				}else{
					resp.put("status", "该订单无效");
					resp.put("code", 10223);
					resp.put("error_message", "该订单无效");
					return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
				}

			} else {
				error.put("status", "缺少必要的请求参数");
				error.put("code", Integer.valueOf(10009));
				error.put("error_message", "缺少必要的请求参数");
				return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
			}
		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}
	
	}

	@Override
	public Response orders_refund(Long loginUserid, JSONObject orders, HttpServletRequest request) {
		JSONObject error = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			if (orders != null) {
				Orders o = ordersDao.get(orders.getLong("id"));
				if (o.getStatus() == 1) {
					Refund refund = null;
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("description", "智游宝下单失败");
					params.put("amount", o.getAmount().doubleValue() * 100);
					try {
						refund = Refund.create(o.getCharge_id(), params);
						o.setRefund_id(refund.getId());
						ordersDao.update(o);
					} catch (AuthenticationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidRequestException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (APIConnectionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (APIException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ChannelException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (RateLimitException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// 需要通知
				Notification n = new Notification();
				n.setNotificationType(3);
				n.setObjectType(6);
				n.setObjectId(o.getId());
				n.setRead_already(false);
				n.setRecipientId(loginUserid);
				n.setTitle("退款申请已提交");
				n.setRemark("订单号:" + o.getOrder_no() + "，订单金额￥" + o.getAmount() + "，退款申请已提交，请耐心等待");
				n.setStatus("enable");
				notificationDao.save(n);
				List<PushNotification> list = pushNotificationDao.getPushNotificationByUserid(loginUserid);
				GetuiModel gm = getGetuiInfo();
				JSONObject json1 = new JSONObject();
				json1.put("description", n.getRemark());
				json1.put("title", n.getTitle());
				try {
					PushNotificationUtil.pushInfoAll(gm.getAppId(), gm.getAppKey(), gm.getMasterSecret(), list,
							n.getTitle(), json1.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				JSONObject json = new JSONObject();
				json.put("status", "success");
				return Response.status(Response.Status.OK).entity(json).build();
			} else {
				error.put("status", "缺少必要的请求参数");
				error.put("code", Integer.valueOf(10009));
				error.put("error_message", "缺少必要的请求参数");
				return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
			}
		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}
	}

	@Override
	public Response return_ticket(Long loginUserid, JSONObject orders_ticket, HttpServletRequest request)
			throws Exception {
		JSONObject error = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			if (orders_ticket != null) {
				OrdersTicket ot = ordersTicketDao.get(orders_ticket.getLong("id"));
				if (ot.getStatus() == 0 || ot.getStatus() == 4 || ot.getStatus() == 5) {
					// 调智游宝的部分退单接口
					Zyb zyb = getZybInfo();
					String d = ZybUtil.getDateByFormat(new Date(), "yyyyMMddHHmm");
					String thirdReturnCode = "rt" + d;
					ot.setReturnCode(thirdReturnCode);
					String xmlMsg = ZybUtil.returnOrdersXml(ot.getTicket_order_no(), zyb, thirdReturnCode);
					String sign = CodeUtil.encode2hex("xmlMsg=" + xmlMsg + zyb.getMasterSecret());
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("xmlMsg", xmlMsg);
					map.put("sign", sign);
					String result = HttpUtil.sendClientPost(zyb.getUrl(), map);
					Map<String, String> dataMap = ZybUtil.getReturnData(result);

					String codeStr = dataMap.get("code");

					if (codeStr != null) {
						int code = Integer.parseInt(codeStr);
						if (code == 0) {
							ot.setRetreatBatchNo(dataMap.get("retreatBatchNo"));
							ot.setStatus(2);
							ordersTicketDao.update(ot);
//							// 需要通知
//							Notification n = new Notification();
//							n.setNotificationType(3);
//							n.setObjectType(1);
//							n.setObjectId(ot.getId());
//							n.setRead_already(false);
//							n.setRecipientId(loginUserid);
//							n.setTitle("退票审核已提交");
//							n.setRemark("订单号:" + ot.getTicket_order_no() + "，该票已提交退票审核！");
//							n.setStatus("enable");
//							notificationDao.save(n);
//							List<PushNotification> list = pushNotificationDao.getPushNotificationByUserid(loginUserid);
//							GetuiModel gm = getGetuiInfo();
//							JSONObject json1 = new JSONObject();
//							json1.put("description", n.getRemark());
//							json1.put("title", n.getTitle());
//							try {
//								PushNotificationUtil.pushInfoAll(gm.getAppId(), gm.getAppKey(), gm.getMasterSecret(),
//										list, n.getTitle(), json1.toString());
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
							JSONObject json = new JSONObject();
							json.put("status", "success");
							return Response.status(Response.Status.OK).entity(json).build();
						} else {
							error.put("status", "提交退票申请失败");
							error.put("code", Integer.valueOf(10201));
							error.put("error_message", "提交退票申请失败");
							return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
						}
					} else {
						error.put("status", "提交退票申请失败");
						error.put("code", Integer.valueOf(10201));
						error.put("error_message", "提交退票申请失败");
						return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
					}

				} else {
					error.put("status", "该票的状态不能提交退票申请");
					error.put("code", Integer.valueOf(10200));
					error.put("error_message", "该票的状态不能提交退票申请");
					return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
				}

			} else {
				error.put("status", "缺少必要的请求参数");
				error.put("code", Integer.valueOf(10009));
				error.put("error_message", "缺少必要的请求参数");
				return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
			}
		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}
	}

	@Override
	public String refund_response(HttpServletRequest request) throws Exception {
		log.debug("退票通知回调开始");
		String retreatBatchNo = request.getParameter("retreatBatchNo");
		String order_Code = request.getParameter("orderCode");
		String subOrderCode = request.getParameter("subOrderCode");
		String auditStatus = request.getParameter("auditStatus");
		String returnNum = request.getParameter("returnNum");
		String sign = request.getParameter("sign");
		log.debug("*******退票通知返回的参数*****retreatBatchNo*"+retreatBatchNo+"*orderCode*"+order_Code+"*subOrderCode*"+subOrderCode+"**auditStatus**"+auditStatus);
		Zyb zyb = getZybInfo();
		String masterSecret = zyb.getMasterSecret();// 私钥
		if (!Strings.isNullOrEmpty(retreatBatchNo) && !Strings.isNullOrEmpty(order_Code)
				&& !Strings.isNullOrEmpty(subOrderCode) && !Strings.isNullOrEmpty(auditStatus)
				&& !Strings.isNullOrEmpty(returnNum) && !Strings.isNullOrEmpty(sign)) {
			Orders orders = ordersDao.getOrdersByOrderno(order_Code);
			OrdersTicket ot = ordersTicketDao.getOrdersTicketByOrdersNo(subOrderCode);
			if (orders != null && ot != null && orders.getId() == ot.getOrders().getId()) {
				String new_sign = CodeUtil.encode2hex(orders.getOrder_no() + masterSecret);
				log.debug("*******secret*****"+new_sign);
				if (sign.equals(new_sign)) {
					log.debug("*******returnNum*****"+returnNum);
					if (auditStatus.trim().equals("success")) {
						Pingpp.privateKeyPath = getClass().getResource("/../../META-INF/ping_1024_priv.pem").getPath();
						Pingpp.apiKey = getPingSecretKey();
						Refund refund = null;
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("description", "智游宝通过退票审核，退回票款");
						params.put("amount", ot.getTotal().doubleValue() * 100);
						try {
							refund = Refund.create(orders.getCharge_id(), params);
							ot.setRefund_id(refund.getId());
							ot.setStatus(3);// 已退票
							ordersTicketDao.update(ot);
							Ticket t = ot.getTicket();
							t.setNumber(t.getNumber() + ot.getNum());
							t.setSold(t.getSold() - ot.getNum());
							ticketDao.update(t);
							Long loginUserid = orders.getUser().getId();
							// 需要通知
							Notification n = new Notification();
							n.setNotificationType(1);
							n.setObjectType(1);
							n.setObjectId(ot.getId());
							n.setRead_already(false);
							n.setRecipientId(loginUserid);
							n.setTitle("退票成功");
							n.setRemark("子订单号:" + ot.getTicket_order_no() + "退票成功，订单金额￥" + ot.getTotal()
									+ "，退款申请已提交，请耐心等待");
							n.setStatus("enable");
							notificationDao.save(n);
							List<PushNotification> list = pushNotificationDao.getPushNotificationByUserid(loginUserid);
							GetuiModel gm = getGetuiInfo();
							JSONObject json = new JSONObject();
							json.put("description", n.getRemark());
							json.put("title", n.getTitle());
							try {
								PushNotificationUtil.pushInfoAll(gm.getAppId(), gm.getAppKey(), gm.getMasterSecret(),
										list, n.getTitle(), json.toString());
							} catch (Exception e) {
								e.printStackTrace();
							}
						} catch (AuthenticationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvalidRequestException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (APIConnectionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (APIException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ChannelException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (RateLimitException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else if (auditStatus.trim().equals("failure")) {
						ot.setStatus(5);// 退票申请被拒绝
						ordersTicketDao.update(ot);
						Long loginUserid = orders.getUser().getId();
						// 需要通知
						Notification n = new Notification();
						n.setNotificationType(2);
						n.setObjectType(1);
						n.setObjectId(ot.getId());
						n.setRead_already(false);
						n.setRecipientId(loginUserid);
						n.setTitle("退票申请审核失败");
						n.setRemark("子订单号:" + ot.getTicket_order_no() + "退票申请审核被驳回");
						n.setStatus("enable");
						notificationDao.save(n);
						List<PushNotification> list = pushNotificationDao.getPushNotificationByUserid(loginUserid);
						GetuiModel gm = getGetuiInfo();
						JSONObject json = new JSONObject();
						json.put("description", n.getRemark());
						json.put("title", n.getTitle());
						try {
							PushNotificationUtil.pushInfoAll(gm.getAppId(), gm.getAppKey(), gm.getMasterSecret(), list,
									n.getTitle(), json.toString());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		return "success";
	}
	
	@Override
	public JSONObject check_ticket(HttpServletRequest request) throws Exception {
		log.debug("************开始检票*****************");
		JSONObject returnJson = null;
		String barcode = request.getParameter("barcode");
		if(!Strings.isNullOrEmpty(barcode)){
			String idcard = barcode.substring(12, barcode.length()-8);
			String verify = barcode.substring(barcode.length()-6, barcode.length());
			String one = idcard.substring(0,2);
			System.out.println(one);
			String a = Integer.parseInt(one, 32)+"";
			if(a.length() == 1){
				a = "00"+a;
			}else if(a.length() == 2){
				a = "0"+a;
			}
			String second = idcard.substring(2,4);
			String b = Integer.parseInt(second, 32)+"";
			if(b.length() == 1){
				b = "00"+b;
			}else if(b.length() == 2){
				b = "0"+b;
			}
			String thrid = idcard.substring(4,6);
			String c = Integer.parseInt(thrid, 32)+"";
			if(c.length() == 1){
				c = "00"+c;
			}else if(c.length() == 2){
				c = "0"+c;
			}
			String four = idcard.substring(6,8);
			String d = Integer.parseInt(four, 32)+"";
			System.out.println(d);
			if(d.length() == 1){
				d = "00"+d;
			}else if(d.length() == 2){
				d = "0"+d;
			}
			String five = idcard.substring(8,10);
			String e = Integer.parseInt(five,32)+"";
			if(e.length() == 1){
				e = "00"+e;
			}else if(e.length() == 2){
				e = "0"+e;
			}
			String f = idcard.substring(10,13);
			String identity_card = a+b+c+d+e+f;
			List<OrdersTicket> otList = ordersTicketDao.getOrdersTicketListByIdcardAndVerify(identity_card, verify);
			if(otList != null && otList.size() > 0){
				OrdersTicket ot = otList.get(0);
				Ticket t = ot.getTicket();
				JSONObject resp = new JSONObject();
				if(t.getType().equals("hotel")){
//					Orders orders = ot.getOrders();
					String path = getClass().getResource("/../../META-INF/hotel.json").getPath();
					JSONObject hotel = ParseFile.parseJson(path);
					String url = hotel.getString("url");
					JSONArray ordersTicketArr = new JSONArray();
					for(OrdersTicket ots:otList){
						ordersTicketArr.add(ots.getTicket_order_no());
					}
					
					String result = HttpUtil.sendPostStr(url+"/order/getsub", ordersTicketArr.toString());
					if(!Strings.isNullOrEmpty(result)){
						JSONObject resJson = JSONObject.fromObject(result);
						int code = resJson.getInt("code");
						if(code == 1000){
							JSONArray ja = resJson.getJSONArray("data");
							JSONObject json = new JSONObject();
							json.put("hotel_room", ja);
							returnJson = CodeUtil.returnData(1000, "查询成功", json);
						}else{
							returnJson = CodeUtil.returnData(1000, "没有可用的房型", "");
						}
					}else{
						returnJson = CodeUtil.returnData(1000, "该订单不可用", "");
					}
				}else{
					String startTimeStr = t.getStart_date()+" "+t.getStart_time();
					String endTimeStr = t.getStart_date()+" "+t.getEnd_time();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					long start_time = 0l;
					long end_time = 0l;
					Date startTime = sdf.parse(startTimeStr);
					Date endTime = sdf.parse(endTimeStr);
					start_time = startTime.getTime() / 1000;
					end_time = endTime.getTime() / 1000;
					
					resp.put("orders_ticket_id", ot.getId());
					resp.put("poi_id", t.getPoi().getId());
					resp.put("poi_name", t.getPoi().getTitle());
					resp.put("ticket_name", t.getTicket_name());
					resp.put("verification", ot.getVerification());
					resp.put("start_time",start_time);
					resp.put("end_time",end_time);
					JSONObject user = new JSONObject();
					user.put("identity_card", ot.getContacter().getIdentity_card());
					user.put("username", ot.getContacter().getName());
					resp.put("user",user);
					returnJson = CodeUtil.returnData(1000, "查询成功", resp);
				}
			}else{
				returnJson = CodeUtil.returnData(1000, "该订单不可用", "");
			}
			
			
		}else{
			returnJson = CodeUtil.returnData(1004, "参数不能为空", "");
		}

		return returnJson;
	
	}
	
	@Override
	public JSONObject hotel(HttpServletRequest request) throws Exception {
		JSONObject returnJson = new JSONObject();
		JSONObject resp = new JSONObject();
		String orders_ticket_no = request.getParameter("ticket_order_no");
		if(!Strings.isNullOrEmpty(orders_ticket_no)){
			OrdersTicket ot = ordersTicketDao.getOrdersTicketByOrdersNo(orders_ticket_no);
			Ticket t = ot.getTicket();
			resp.put("orders_ticket_id", ot.getId());
			resp.put("poi_id", t.getPoi().getId());
			resp.put("poi_name", t.getPoi().getTitle());
			resp.put("ticket_name", t.getTicket_name());
			resp.put("verification", ot.getVerification());
			JSONObject user = new JSONObject();
			user.put("identity_card", ot.getContacter().getIdentity_card());
			user.put("username", ot.getContacter().getName());
			resp.put("user",user);
			String path = getClass().getResource("/../../META-INF/hotel.json").getPath();
			JSONObject hotel = ParseFile.parseJson(path);
			String url = hotel.getString("url");
			List<String> ordersTicketList = new ArrayList<String>();
			ordersTicketList.add(ot.getTicket_order_no());
			JSONObject ordersTicketJson = new JSONObject();
			ordersTicketJson.put("ticket_order_no", orders_ticket_no);
			String result = HttpUtil.sendPostStr(url+"/order/getrooms", ordersTicketJson.toString());
			if(!Strings.isNullOrEmpty(result)){
				JSONObject resJson = JSONObject.fromObject(result);
				int code = resJson.getInt("code");
				if(code == 1000){
					JSONArray ja = resJson.getJSONArray("data");
					resp.put("room_type", ja);
					returnJson = CodeUtil.returnData(1000, "查询成功", resp);
				}else{
					returnJson = CodeUtil.returnData(1000, "没有可用的房型", "");
				}
			}else{
				returnJson = CodeUtil.returnData(1000, "该订单不可用", "");
			}
		
		}else{
			returnJson = CodeUtil.returnData(1004, "参数不能为空", "");
		}

		return returnJson;
	}
	
	@Override
	public JSONObject checkin(JSONObject checkin) throws Exception {
		JSONObject returnJson = null;
		try{
			if(checkin != null 
					&& checkin.containsKey("ticket_order_no") 
					&& !Strings.isNullOrEmpty(checkin.getString("ticket_order_no")) 
					&& checkin.containsKey("room_id") && !Strings.isNullOrEmpty(checkin.getString("room_id"))){
				String ticket_order_no = checkin.getString("ticket_order_no");
				String path = getClass().getResource("/../../META-INF/hotel.json").getPath();
				JSONObject hotel = ParseFile.parseJson(path);
				String url = hotel.getString("url");
				String result = HttpUtil.sendPostStr(url+"/order/bind-rooms", checkin.toString());
				log.info("hotel ******"+result);
				if(!Strings.isNullOrEmpty(result)){
					JSONObject resJson = JSONObject.fromObject(result);
					int code = resJson.getInt("code");
					if(code == 1000){
						OrdersTicket ot = ordersTicketDao.getOrdersTicketByOrdersNo(ticket_order_no);
						if(ot.getStatus() == 8){
							returnJson = CodeUtil.returnData(1000, "入住成功", "");
						}else if(ot.getStatus() == 0){
							ot.setStatus(1);
							ordersTicketDao.update(ot);
						}else{
							returnJson = CodeUtil.returnData(1056, "已安排入住", "");
						}
						
					}else{
						returnJson = CodeUtil.returnData(1000, "没有房源，入住失败", "");
					}
				}
				returnJson = CodeUtil.returnData(1000, "入住成功", "");
			}else{
				returnJson = CodeUtil.returnData(1004, "参数不能为空", "");
			}
			Object o = returnJson.get("rspData");
			if(o == null){
				returnJson.discard("rspData");
			}
			
		}catch (Exception e) {
			returnJson = CodeUtil.returnData(1000, "没有房源，入住失败", "");
		}
		
		return returnJson;
	}
	
	@Override
	public Response check_charge(Long loginUserid, JSONObject orders, HttpServletRequest request) throws Exception {
		JSONObject error = new JSONObject();
		JSONObject resp = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			if (orders != null) {
				String charge_id = orders.getString("charge_id");
				String order_no = orders.getString("order_no");
				Orders order = ordersDao.getOrdersByOrderNoAndChargeId(order_no, charge_id);
				Pingpp.privateKeyPath = getClass().getResource("/../../META-INF/ping_1024_priv.pem").getPath();
				Pingpp.apiKey = getPingSecretKey();
				Charge charge = Charge.retrieve(order.getCharge_id());
				int status = order.getStatus();
				if(status == 1 && charge.getPaid()){
					resp.put("status", "success");
					return Response.status(Response.Status.OK).entity(resp).build();
				}else if(status == 0 && charge.getPaid()){
					
					boolean is_refund = false;
//					// 调智游宝的下单接口
//					Zyb zyb = getZybInfo();
					List<OrdersTicket> otList = ordersTicketDao.getOrdersTicketByOrdersId(order.getId());
//					String xmlMsg = ZybUtil.createOrdersXml(order.getUser(), order, otList, zyb);
//					String sign = CodeUtil.encode2hex("xmlMsg=" + xmlMsg + zyb.getMasterSecret());
//					Map<String, Object> map = new HashMap<String, Object>();
//					map.put("xmlMsg", xmlMsg);
//					map.put("sign", sign);
//					String result = HttpUtil.sendClientPost(zyb.getUrl(), map);
//					log.debug("*******************智游宝返回*******"+result);
//					String codeStr = ZybUtil.getReturnCode(result);
					// 智游宝
//					if (!Strings.isNullOrEmpty(codeStr)) {
//						int code = Integer.parseInt(codeStr);
//						if (code == 0) {
////							String xmlMsgCode = ZybUtil.findImgXml(order.getOrder_no(), zyb);
////							String signCode = CodeUtil.encode2hex("xmlMsg=" + xmlMsgCode + zyb.getMasterSecret());
////							Map<String, Object> mapCode = new HashMap<String, Object>();
////							mapCode.put("xmlMsg", xmlMsgCode);
////							mapCode.put("sign", signCode);
////							String resultCode = HttpUtil
////									.sendClientPost(zyb.getUrl(), mapCode);
////							String barcode = ZybUtil.getImg(resultCode);
//							if (!Strings.isNullOrEmpty(barcode)) {
//								Ticket ticket = null;
//								List<Contacter> contacterList = new ArrayList<Contacter>();
//								List<String> idcardList = new ArrayList<String>();
//								if (otList != null && otList.size() > 0) {
//									ticket = otList.get(0).getTicket();
//									for (OrdersTicket ot : otList) {
//										contacterList.add(ot.getContacter());
//										idcardList.add(ot.getContacter().getIdentity_card());
//									}
//								}
//								
//								//查询投保的身份证号
//								List<Insurant> insurantList = new ArrayList<Insurant>();
//								if(idcardList != null && idcardList.size() > 0){
//									insurantList = insurantDao.getInsurantByIdentityCards(idcardList);
//								}else{
//									insurantList = insurantDao.getInsurantByIdentityCards(idcardList);
//								}
//								
//								//未投保的常用联系人
//								List<Contacter> unInsurantContacterList = new ArrayList<Contacter>();
//								StringBuffer idCardSB = new StringBuffer();
//								if(contacterList != null && contacterList.size() > 0){
//									if(insurantList != null && insurantList.size() > 0){
//										for(Insurant insure:insurantList){
//											idCardSB.append(insure.getIdentity_card()+",");
//										}
//										for(Contacter c:contacterList){
//											if(!idCardSB.toString().contains(c.getIdentity_card())){
//												unInsurantContacterList.add(c);
//											}
//										}
//									}else{
//										unInsurantContacterList = contacterList;
//									}
//									
//								}
//								
////								if(ticket.getIs_insurance() == 1){
////									log.debug("*******************可以生成保险*******");
////									JSONObject insurant = getInsurantInfo();
////									String url = insurant.getString("url");
////									String key = insurant.getString("key");
////									String partnerld = insurant.getString("partnerld");
////									String caseCode = insurant.getString("caseCode");
////									String cardCode = insurant.getString("cardCode");
////									String company = insurant.getString("company");
////									if(unInsurantContacterList != null && unInsurantContacterList.size() > 0){
////										log.debug("*******************保险下单开始*******");
////										JSONObject insurantJson = getInsurantJson(order, unInsurantContacterList, ticket, partnerld,caseCode,cardCode,company);
////										String insurantSign = CodeUtil.encode2hex(key+insurantJson);
////										log.debug("*******************保险下单参数*******"+insurantJson+"****sign**"+insurantSign);
////										String insurantResult = HttpUtil.sendPostStr(url+"localInsure?sign="+insurantSign, insurantJson.toString());
////										log.debug("*******************保险下单返回*******"+insurantResult);
////										if(!Strings.isNullOrEmpty(insurantResult)){
////											
////											
////											JSONObject insurantResultJson = JSONObject.fromObject(insurantResult);
////											int respCode = insurantResultJson.getInt("respCode");
////											if(respCode == 0){
////												order.setStatus(1);
////												order.setPay_type(charge.getChannel());
////												ordersDao.update(order);
////												int size = 0;
////												JSONObject dataJson = insurantResultJson.getJSONObject("data");
////												if (otList != null && otList.size() > 0) {
////													size = otList.size();
////													for (OrdersTicket ot : otList) {
////														barcode = barcode.replaceAll("gmCheckNo", "gmCheckCode");
////														ot.setBarcode(barcode);// 测试智游宝通过后 完善这里
////														ot.setStatus(0); // 未使用
////														ot.setUpdate_time(new Date());
////														ot.setTransNo(dataJson.getString("transNo"));
////														ot.setPartnerId(dataJson.getInt("partnerId"));
////														ot.setInsureNum(dataJson.getString("insureNum"));
////														ot.setStartDate(dataJson.getString("startDate"));
////														ot.setEndDate(dataJson.getString("endDate"));
////														ordersTicketDao.update(ot);
////													}
////													Ticket t = otList.get(0).getTicket();
////													t.setNumber(t.getNumber() - size);
////													t.setSold(t.getSold()+size);
////													ticketDao.update(t);
////												}
////												for(Contacter uc : unInsurantContacterList){
////													Insurant insure = new Insurant();
////													insure.setIdentity_card(uc.getIdentity_card());
////													insurantDao.save(insure);
////												}
////												resp.put("status", "出票成功");
////												return Response.status(Response.Status.OK).entity(resp).build();
////											}else{
////												is_refund = true;
////												resp.put("is_refund", is_refund);
////												resp.put("status", "保险系统下单失败,您支付的款项返还到您支付的渠道");
////												resp.put("code", 10211);
////												resp.put("error_message", "保险系统下单失败,您支付的款项返还到您支付的渠道");
////												
////												// 调智游宝的部分退单接口
////												String d = ZybUtil.getDateByFormat(new Date(), "yyyyMMddHHmm");
////												if(otList != null && otList.size() > 0){
////													for(OrdersTicket ot:otList){
////														String thirdReturnCode = "rt" + d;
////														String xmlMsgReturn = ZybUtil.returnOrdersXml(order.getOrder_no(), zyb, thirdReturnCode);
////														String signReturn = CodeUtil.encode2hex("xmlMsg=" + xmlMsgReturn + zyb.getMasterSecret());
////														Map<String, Object> mapReturn = new HashMap<String, Object>();
////														mapReturn.put("xmlMsg", xmlMsgReturn);
////														mapReturn.put("sign", signReturn);
////														String resultReturn = HttpUtil.sendClientPost(zyb.getUrl(), mapReturn);
////														Map<String, String> dataMap = ZybUtil.getReturnData(resultReturn);
////
////														String codeReturn = dataMap.get("code");
////														if (codeReturn != null) {
////															int codeInt = Integer.parseInt(codeReturn);
////															if (codeInt == 0) {
////																ot.setReturnCode(thirdReturnCode);
////																ot.setRetreatBatchNo(dataMap.get("retreatBatchNo"));
////																ot.setStatus(2);
////																ordersTicketDao.update(ot);
////															}
////														}
////													}
////												}
////											}
////											
////										}else{
////											//*************此处应该有退票
////											is_refund = true;
////											resp.put("is_refund", is_refund);
////											resp.put("status", "保险系统下单失败");
////											resp.put("code", 10211);
////											resp.put("error_message", "保险系统下单失败");
////											
////											//*************此处应该有退票
////											
////											// 调智游宝的部分退单接口
////											String d = ZybUtil.getDateByFormat(new Date(), "yyyyMMddHHmm");
////											if(otList != null && otList.size() > 0){
////												for(OrdersTicket ot:otList){
////													String thirdReturnCode = "rt" + d;
////													String xmlMsgReturn = ZybUtil.returnOrdersXml(order.getOrder_no(), zyb, thirdReturnCode);
////													String signReturn = CodeUtil.encode2hex("xmlMsg=" + xmlMsgReturn + zyb.getMasterSecret());
////													Map<String, Object> mapReturn = new HashMap<String, Object>();
////													mapReturn.put("xmlMsg", xmlMsgReturn);
////													mapReturn.put("sign", signReturn);
////													String resultReturn = HttpUtil.sendClientPost(zyb.getUrl(), mapReturn);
////													Map<String, String> dataMap = ZybUtil.getReturnData(resultReturn);
////
////													String codeReturn = dataMap.get("code");
////													if (codeReturn != null) {
////														int codeInt = Integer.parseInt(codeReturn);
////														if (codeInt == 0) {
////															ot.setReturnCode(thirdReturnCode);
////															ot.setRetreatBatchNo(dataMap.get("retreatBatchNo"));
////															ot.setStatus(2);
////															ordersTicketDao.update(ot);
////														}
////													}
////												}
////											}
////										}
////									}else{
////
////										// 智游宝 下单成功
////										order.setStatus(1);
////										order.setPay_type(charge.getChannel());
////										ordersDao.update(order);
////										int size = 0;
////										if (otList != null && otList.size() > 0) {
////											size = otList.size();
////											for (OrdersTicket ot : otList) {
////												barcode = barcode.replaceAll("gmCheckNo", "gmCheckCode");
////												ot.setBarcode(barcode);// 测试智游宝通过后 完善这里
////												ot.setStatus(0); // 未使用
////												ot.setUpdate_time(new Date());
////												ordersTicketDao.update(ot);
////											}
////											Ticket t = otList.get(0).getTicket();
////											t.setNumber(t.getNumber() - size);
////											t.setSold(t.getSold()+size);
////											ticketDao.update(t);
////										}
////										resp.put("status", "出票成功");
////										return Response.status(Response.Status.OK).entity(resp).build();
////									
////									}
////									
////								}
////								else if(ticket.getIs_insurance() == 0){
////
////									// 智游宝 下单成功
////									order.setStatus(1);
////									order.setPay_type(charge.getChannel());
////									ordersDao.update(order);
////									int size = 0;
////									
////									if (otList != null && otList.size() > 0) {
////										size = otList.size();
////										for (OrdersTicket ot : otList) {
////											barcode = barcode.replaceAll("gmCheckNo", "gmCheckCode");
////											ot.setBarcode(barcode);// 测试智游宝通过后 完善这里
////											ot.setStatus(0); // 未使用
////											ordersTicketDao.update(ot);
////										}
////										Ticket t = otList.get(0).getTicket();
////										t.setNumber(t.getNumber() - size);
////										t.setSold(t.getSold()+size);
////										ticketDao.update(t);
////									}
////									resp.put("status", "出票成功");
////									return Response.status(Response.Status.OK).entity(resp).build();
////								
////								}
//								
//							} else {
//								is_refund = true;
//								resp.put("is_refund", is_refund);
//								resp.put("status", "票务系统返回二维码图片错误,您支付的款项返还到您支付的渠道");
//								resp.put("code", 10203);
//								resp.put("error_message", "票务系统返回二维码图片错误,您支付的款项返还到您支付的渠道");
//							}
//						} else {
//							is_refund = true;
//							resp.put("is_refund", is_refund);
//							resp.put("status", "票务系统下单失败,您支付的款项返还到您支付的渠道");
//							resp.put("code", 10204);
//							resp.put("error_message", "票务系统下单失败,您支付的款项返还到您支付的渠道");
//						}
//					} 
					List<String> mobiles = new ArrayList<String>();
					JSONArray ja = new JSONArray();
					String templateid = "";
					try{
						order.setStatus(1);
						order.setPay_type(charge.getChannel());
						ordersDao.update(order);
						int size = 0;
						if (otList != null && otList.size() > 0) {
							size = otList.size();
							OrdersTicket ordersTicket = otList.get(0);
							Ticket t = ordersTicket.getTicket();
							String checking_rule = t.getChecking_rule();
							Poi poi = t.getPoi();
							
							for (OrdersTicket ot : otList) {
//								barcode = barcode.replaceAll("gmCheckNo", "gmCheckCode");
								String verify = "";
								if(t.getType().equals("hotel")){
									String start_date = ot.getStartDate();
									String end_date = ot.getEndDate();
									String[] sd = start_date.split("-");
									String[] ed = end_date.split("-");
									String date = sd[1]+sd[2]+ed[1]+ed[2];
									if(checking_rule.equals("once")){
										verify = QRCodeHelper.createQRString(poi.getId().toString(), t.getId().toString(), date, ot.getContacter().getIdentity_card(), "01");
									}else if(checking_rule.equals("more")){
										verify = QRCodeHelper.createQRString(poi.getId().toString(), t.getId().toString(), date, ot.getContacter().getIdentity_card(), "02");
									}
									verify = verify.substring(verify.length()-6, verify.length());
									ot.setVerification(verify);
								}else if(t.getType().equals("normal")){
									if(checking_rule.equals("once")){
										verify = QRCodeHelper.createQRString(poi.getId().toString(), t.getId().toString(), "00000000", ot.getContacter().getIdentity_card(), "01");
									}else if(checking_rule.equals("more")){
										verify = QRCodeHelper.createQRString(poi.getId().toString(), t.getId().toString(), "00000000", ot.getContacter().getIdentity_card(), "02");
									}
									verify = verify.substring(verify.length()-6, verify.length());
									ot.setVerification(verify);
								}else if(t.getType().equals("vip")){
									if(checking_rule.equals("once")){
										verify = QRCodeHelper.createQRString(poi.getId().toString(), t.getId().toString(), "00000000", ot.getContacter().getIdentity_card(), "01");
									}else if(checking_rule.equals("more")){
										verify = QRCodeHelper.createQRString(poi.getId().toString(), t.getId().toString(), "00000000", ot.getContacter().getIdentity_card(), "02");
									}
									verify = verify.substring(verify.length()-6, verify.length());
									ot.setVerification(verify);
								}
								
								ot.setBarcode("");// 测试智游宝通过后 完善这里
								ot.setStatus(0); // 未使用
								ordersTicketDao.update(ot);
							}
							User user = order.getUser();
							String identity_card = user.getIdentity_card();
							String username = user.getUsername();
							int number = t.getNumber();
							if(t.getType().equals("vip")){
								if(number > 0){
									user.setLevel("vip");
									userDao.update(user);
									t.setNumber(t.getNumber() - size);
									t.setSold(t.getSold()+size);
									ticketDao.update(t);
									ja.add(username);
									ja.add(identity_card.substring(identity_card.length()-6, identity_card.length()));
									templateid = "3104033";
									elkUser(user);
									elkOrders(order, otList, "online");
								}else{

									Refund refund = null;
									Map<String, Object> params = new HashMap<String, Object>();
									params.put("description", "库存不足");
									params.put("amount", order.getAmount().doubleValue() * 100);
									try {
										refund = Refund.create(charge.getId(), params);
										order.setRefund_id(refund.getId());
										order.setStatus(3);
										ordersDao.update(order);
									} catch (AuthenticationException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (InvalidRequestException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (APIConnectionException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (APIException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (ChannelException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (RateLimitException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									resp.remove("is_refund");
									resp.put("status", "库存不足,您支付的款项返还到您支付的渠道");
									resp.put("code", 10404);
									resp.put("error_message", "库存不足,您支付的款项返还到您支付的渠道");
									
									return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
								
								}
								
							}else if(t.getType().equals("hotel")){
								t.setNumber(t.getNumber() - size);
								t.setSold(t.getSold()+size);
								ticketDao.update(t);
								ja.add(username);
								ja.add(t.getTicket_name()+" - "+ordersTicket.getReturnCode());
								String startDate = ordersTicket.getStartDate();
								String endDate = ordersTicket.getEndDate();
								ja.add(startDate);
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
								if(startDate.equals(endDate)){
									Date d = sdf.parse(endDate);
									long afterDay = (d.getTime() / 1000) + 60*60 *24;
									Date d1 = new Date(afterDay * 1000);
									String s1 = sdf.format(d1);
									ja.add(s1);
									ja.add(1+"晚");
								}else{
									ja.add(endDate);
									Date d = sdf.parse(startDate);
									Date d3 = sdf.parse(endDate);
									long sDay = (d.getTime() / 1000);
									long s3Day = (d3.getTime() / 1000 );
									long days = (s3Day-sDay)/(60*60*24) + 1;
									ja.add(days+"晚");
								}
								ja.add(ordersTicket.getStartDate()+"-"+ordersTicket.getEndDate());
								ja.add(identity_card.substring(identity_card.length()-6, identity_card.length()));
								templateid = "3103013";
								elkOrders(order, otList, "online");
								String path = getClass().getResource("/../../META-INF/hotel.json").getPath();
								JSONObject hotel = ParseFile.parseJson(path);
								String url = hotel.getString("url");
								JSONObject orderjson = new JSONObject();
								orderjson.put("order_no", order.getOrder_no());
								orderjson.put("status", 1);
								String result = HttpUtil.sendPostStr(url+"/order/pay", orderjson.toString());
								if(!Strings.isNullOrEmpty(result)){
									JSONObject resJson = JSONObject.fromObject(result);
									int code = resJson.getInt("code");
									if(code != 1000){
										Refund refund = null;
										Map<String, Object> params = new HashMap<String, Object>();
										params.put("description", "库存不足");
										params.put("amount", order.getAmount().doubleValue() * 100);
										try {
											refund = Refund.create(charge.getId(), params);
											order.setRefund_id(refund.getId());
											order.setStatus(3);
											ordersDao.update(order);
											JSONObject orderjsons = new JSONObject();
											orderjsons.put("order_no", order.getOrder_no());
											orderjsons.put("status", 0);
											String result1 = HttpUtil.sendPostStr(url+"/order/pay", orderjson.toString());
										} catch (AuthenticationException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (InvalidRequestException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (APIConnectionException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (APIException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (ChannelException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (RateLimitException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										resp.remove("is_refund");
										resp.put("status", "库存不足,您支付的款项返还到您支付的渠道");
										resp.put("code", 10404);
										resp.put("error_message", "库存不足,您支付的款项返还到您支付的渠道");
										
										return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
									
									
									}
								}else{


									Refund refund = null;
									Map<String, Object> params = new HashMap<String, Object>();
									params.put("description", "库存不足");
									params.put("amount", order.getAmount().doubleValue() * 100);
									try {
										refund = Refund.create(charge.getId(), params);
										order.setRefund_id(refund.getId());
										order.setStatus(3);
										ordersDao.update(order);
										JSONObject orderjsons = new JSONObject();
										orderjsons.put("order_no", order.getOrder_no());
										orderjsons.put("status", 0);
										String result1 = HttpUtil.sendPostStr(url+"/order/pay", orderjson.toString());
									} catch (AuthenticationException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (InvalidRequestException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (APIConnectionException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (APIException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (ChannelException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (RateLimitException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									resp.remove("is_refund");
									resp.put("status", "库存不足,您支付的款项返还到您支付的渠道");
									resp.put("code", 10404);
									resp.put("error_message", "库存不足,您支付的款项返还到您支付的渠道");
									
									return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
								
								
								}
							}else if(t.getType().equals("normal")){
								if(number >= size){
									t.setNumber(t.getNumber() - size);
									t.setSold(t.getSold()+size);
									ticketDao.update(t);
									ja.add(username);
									ja.add(t.getTicket_name());
									ja.add(t.getStart_date()+" "+t.getStart_time());
									ja.add(t.getStart_date()+" "+t.getEnd_time());
									ja.add(identity_card.substring(identity_card.length()-6, identity_card.length()));
									templateid = "3102016";
									elkOrders(order, otList, "online");
								}else{
									
									Refund refund = null;
									Map<String, Object> params = new HashMap<String, Object>();
									params.put("description", "库存不足");
									params.put("amount", order.getAmount().doubleValue() * 100);
									try {
										refund = Refund.create(charge.getId(), params);
										order.setRefund_id(refund.getId());
										order.setStatus(3);
										ordersDao.update(order);
									} catch (AuthenticationException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (InvalidRequestException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (APIConnectionException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (APIException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (ChannelException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (RateLimitException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									resp.remove("is_refund");
									resp.put("status", "库存不足,您支付的款项返还到您支付的渠道");
									resp.put("code", 10404);
									resp.put("error_message", "库存不足,您支付的款项返还到您支付的渠道");
									
									return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
								
								}
								
							}
							mobiles.add(user.getPhone());
							resp.put("status", "出票成功");
							SendCode.sendtemplate(mobiles, ja,templateid);
							return Response.status(Response.Status.OK).entity(resp).build();
						}else{
							Refund refund = null;
							Map<String, Object> params = new HashMap<String, Object>();
							params.put("description", "票务系统出票失败");
							params.put("amount", order.getAmount().doubleValue() * 100);
							try {
								refund = Refund.create(charge.getId(), params);
								order.setRefund_id(refund.getId());
								order.setStatus(3);
								ordersDao.update(order);
							} catch (AuthenticationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvalidRequestException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (APIConnectionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (APIException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ChannelException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (RateLimitException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							resp.remove("is_refund");
							resp.put("status", "下单失败,您支付的款项返还到您支付的渠道");
							resp.put("code", 10204);
							resp.put("error_message", "下单失败,您支付的款项返还到您支付的渠道");
							
							return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
						}
						
					}catch (Exception e1) {

						e1.printStackTrace();
						// 智游宝 下单失败 自动退款
						Refund refund = null;
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("description", "票务系统出票失败");
						params.put("amount", order.getAmount().doubleValue() * 100);
						try {
							refund = Refund.create(charge.getId(), params);
							order.setRefund_id(refund.getId());
							order.setStatus(3);
							ordersDao.update(order);
						} catch (AuthenticationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvalidRequestException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (APIConnectionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (APIException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ChannelException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (RateLimitException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						resp.remove("is_refund");
						resp.put("status", "下单失败,您支付的款项返还到您支付的渠道");
						resp.put("code", 10204);
						resp.put("error_message", "下单失败,您支付的款项返还到您支付的渠道");
						
						return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
					
					}
				}else if(status == 0 && !charge.getPaid()){
					error.put("status", "Ping ++支付失败");
					error.put("code", 10206);
					error.put("error_message", "Ping ++支付失败");
					return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
				}
				return null;
			} else {
				error.put("status", "缺少必要的请求参数");
				error.put("code", Integer.valueOf(10009));
				error.put("error_message", "缺少必要的请求参数");
				return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
			}
		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response add_order(Long loginUserid, JSONObject orders) {
		JSONObject error = new JSONObject();
		try{
			if (loginUserid != null && loginUserid > 0) {
				if (orders != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
					String date = sdf.format(new Date());
					Random random = new Random();
					String randStr = String.valueOf(100000 + random.nextInt(899999));
					String order_no = "o" + date + randStr;
					Orders order = new Orders();
					order.setOrder_no(order_no);
					/*
					 * if(orders.containsKey("pay_type")){ String pay_type =
					 * orders.getString("pay_type"); order.setPay_type(pay_type); }
					 */
					String amount = orders.getString("amount");
					BigDecimal bd = BigDecimal.valueOf(Double.valueOf(amount));
					order.setAmount(bd);
					if(orders.containsKey("description") 
							&& !Strings.isNullOrEmpty(orders.getString("description"))){
						String description = orders.getString("description");
						order.setDescription(description);
					}
					List<OrdersTicket> otsList = new ArrayList<OrdersTicket>();
					User user = userDao.get(loginUserid);
					order.setStatus(0);
					order.setUser(user);
					

					if (orders.containsKey("ticket")) {
						
						JSONObject ticketJson = orders.getJSONObject("ticket");
						Long id = ticketJson.getLong("id");
						Ticket ticket = ticketDao.get(id);
						String subRandStr = String.valueOf(100000 + random.nextInt(899999));
						int number = ticket.getNumber();
						if(ticket.getType().equals("vip")){
							if(number > 0){
								ordersDao.save(order);
								List<Contacter> cList = contacterDao.getContacterByIdentityCard(user.getIdentity_card(), user.getId());
								OrdersTicket ot = new OrdersTicket();
								ot.setOrders(order);
								ot.setTicket(ticket);
								String ticket_order_no = "sub" + date + subRandStr;
								ot.setTicket_order_no(ticket_order_no);
								Contacter c = cList.get(0);
								ot.setContacter(c);
								ot.setNum(1);
								ot.setStatus(7);
								ot.setTotal(ticket.getPrice());
								ot.setUpdate_time(new Date());
								ordersTicketDao.save(ot);
								otsList.add(ot);
							}else{
								error.put("status", "库存不足");
								error.put("code", 10401);
								error.put("error_message", "库存不足");
								return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
							}
							
						}else if(ticket.getType().equals("hotel")){
							JSONObject orderJson = new JSONObject();
							orderJson.put("order_no", order_no);
							orderJson.put("amount", bd);
							orderJson.put("status", 0);
							orderJson.put("phone",user.getPhone());
							orderJson.put("room_type_id", orders.getInt("room_type_id"));
							List<JSONObject> otJsonList = new ArrayList<JSONObject>();
							List<OrdersTicket> otList = new ArrayList<OrdersTicket>();
							JSONObject otJson = null;
							JSONArray contactersArr = JSONArray.fromObject(ticketJson.get("contacters"));
							Iterator<Object> iter = contactersArr.iterator();
							while (iter.hasNext()) {
								String d = sdf.format(new Date());
								Random ran = new Random();
								String randSt = String.valueOf(100000 + ran.nextInt(899999));
								String total = orders.getString("total");
								BigDecimal totalBd = BigDecimal.valueOf(Double.valueOf(total));
								Long contacter_id = Long.parseLong(iter.next().toString());
								Contacter c = contacterDao.get(contacter_id);
								otJson = new JSONObject();
								Poi p = ticket.getPoi();
								String ticket_order_no = "sub" + d + randSt;
								otJson.put("poi_id", p.getId());
								String startDate = orders.getString("start_date");
								String endDate = orders.getString("end_date");
								SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
							    String start_date = sdfs.format(new Date(Long.parseLong(startDate) * 1000));
							    String end_date = sdfs.format(new Date(Long.parseLong(endDate) * 1000));
								otJson.put("start_date", start_date);
								otJson.put("end_date", end_date);
								otJson.put("total", totalBd);
								otJson.put("name", c.getName());
								otJson.put("identity_card", c.getIdentity_card());
								otJson.put("ticket_order_no", ticket_order_no);
								otJson.put("status", 7);
								otJsonList.add(otJson);
								
								OrdersTicket ot = new OrdersTicket();
								ot.setTicket(ticket);
								ot.setTicket_order_no(ticket_order_no);
								ot.setContacter(c);
								ot.setNum(1);
								ot.setStatus(7);
								ot.setTotal(totalBd);
								ot.setUpdate_time(new Date());
								ot.setReturnCode(orders.getString("room_type"));
								ot.setStartDate(start_date);
								ot.setEndDate(end_date);
								otList.add(ot);
//								ordersTicketDao.save(ot);
								otsList = otList;
							}
							orderJson.put("orders_ticket", otJsonList);
							String path = getClass().getResource("/../../META-INF/hotel.json").getPath();
							JSONObject hotel = ParseFile.parseJson(path);
							String url = hotel.getString("url");
							String result = HttpUtil.sendPostStr(url+"/order/save", orderJson.toString());
							if(!Strings.isNullOrEmpty(result)){
								JSONObject resJson = JSONObject.fromObject(result);
								int code = resJson.getInt("code");
								if(code == 1000){
									ordersDao.save(order);
									if(otList != null && otList.size() > 0){
										for(OrdersTicket ot:otList){
											ot.setOrders(order);
											ordersTicketDao.save(ot);
										}
									}
								}else{
									error.put("status", "房源不足，无法预订");
									error.put("code", 10400);
									error.put("error_message", "房源不足，无法预订");
									return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
								}
							}else{
								error.put("status", "房源不足，无法预订");
								error.put("code", 10400);
								error.put("error_message", "房源不足，无法预订");
								return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
							}
								
							
						}else{
							if(number > 0){
								ordersDao.save(order);
								JSONArray contactersArr = JSONArray.fromObject(ticketJson.get("contacters"));
								Iterator<Object> iter = contactersArr.iterator();
								while (iter.hasNext()) {
									String d = sdf.format(new Date());
									Random ran = new Random();
									String randSt = String.valueOf(100000 + ran.nextInt(899999));
									Long contacter_id = Long.parseLong(iter.next().toString());
									OrdersTicket ot = new OrdersTicket();
									ot.setOrders(order);
									ot.setTicket(ticket);
									String ticket_order_no = "sub" + d + randSt;
									ot.setTicket_order_no(ticket_order_no);
									Contacter c = contacterDao.get(contacter_id);
									ot.setContacter(c);
									ot.setNum(1);
									ot.setStatus(7);
									ot.setTotal(ticket.getPrice());
									ot.setUpdate_time(new Date());
									ordersTicketDao.save(ot);
									otsList.add(ot);
								}
							}else{
								error.put("status", "库存不足");
								error.put("code", 10401);
								error.put("error_message", "库存不足");
								return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
							}
							
						
						}
						
					}else{
						error.put("status", "缺少必要的请求参数");
						error.put("code", Integer.valueOf(10009));
						error.put("error_message", "缺少必要的请求参数");
						return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
					}
					JSONObject json = new JSONObject();
					json.put("orders_id", order.getId());
					elkOrders(order, otsList, "online");
					return Response.status(Response.Status.OK).entity(json).build();
				} else {
					error.put("status", "缺少必要的请求参数");
					error.put("code", Integer.valueOf(10009));
					error.put("error_message", "缺少必要的请求参数");
					return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
				}
			} else {
				error.put("status", "用户未登录");
				error.put("code", Integer.valueOf(10010));
				error.put("error_message", "用户未登录");
				return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
			}
		}catch (Exception e) {
			e.printStackTrace();
			error.put("status", "下单异常，请稍候再试");
			error.put("code", Integer.valueOf(10313));
			error.put("error_message", "下单异常，请稍候再试");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}
		
	}
	
	@Override
	public JSONObject add_pos_order(JSONObject orders) {

		JSONObject returnJson = null;
		
		if (orders != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
			
			Orders order = new Orders();
			order.setOrder_no(orders.getString("order_no"));
			/*
			 * if(orders.containsKey("pay_type")){ String pay_type =
			 * orders.getString("pay_type"); order.setPay_type(pay_type); }
			 */
			String amount = orders.getString("amount");
			BigDecimal bd = BigDecimal.valueOf(Double.valueOf(amount));
			order.setAmount(bd);
			order.setDescription(orders.getString("device_code"));
			User user = userDao.getFirstUser();
			order.setStatus(6);
			order.setUser(user);
			List<Contacter> cList = contacterDao.getContacterList(user.getId());
			List<OrdersTicket> otsList = new ArrayList<OrdersTicket>();
			if (orders.containsKey("ticket")) {
				
				JSONObject ticketJson = orders.getJSONObject("ticket");
				Long id = ticketJson.getLong("id");
				Ticket ticket = ticketDao.get(id);
				int number = ticket.getNumber();//库存
				if(ticket.getType().equals("hotel")){
					JSONObject orderJson = new JSONObject();
					orderJson.put("order_no", orders.getString("order_no"));
					orderJson.put("amount", bd);
					orderJson.put("status", 6);
					orderJson.put("phone",user.getPhone());
					orderJson.put("room_type_id", orders.getInt("room_type_id"));
					JSONArray contactersArr = JSONArray.fromObject(ticketJson.get("contacters"));
					Iterator<Object> iter = contactersArr.iterator();
					JSONObject otJson = null;
					List<JSONObject> otJsonList = new ArrayList<JSONObject>();
					List<OrdersTicket> otList = new ArrayList<OrdersTicket>();
					while (iter.hasNext()) {
						String date = sdf.format(new Date());
						Random random = new Random();
						String randStr = String.valueOf(100000 + random.nextInt(899999));
						JSONObject contactJson = JSONObject.fromObject(iter.next());
						String identity_card = contactJson.getString("identity_card");
						OrdersTicket ot = new OrdersTicket();
						Contacter contacter = null;
						if(cList != null && cList.size() > 0){
							for(Contacter c:cList){
								if(c.getIdentity_card().equals(identity_card)){
									contacter = c;
									break;
								}
							}
						}
						
						if(contacter != null){
							ot.setContacter(contacter);
						}else{
							contacter = new Contacter();
							contacter.setIdentity_card(identity_card);
							contacter.setUser(user);
							contacter.setName(contactJson.getString("username"));
							contacterDao.save(contacter);
							ot.setContacter(contacter);
						}
						//---
						String total = orders.getString("total");
						BigDecimal totalBd = BigDecimal.valueOf(Double.valueOf(total));
//						Contacter c = contacterDao.get(contacter_id);
						otJson = new JSONObject();
						Poi p = ticket.getPoi();
						String ticket_order_no = "sub" + date + randStr;
						otJson.put("poi_id", p.getId());
						String startDate = orders.getString("start_date");
						String endDate = orders.getString("end_date");
						SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
					    String start_date = sdfs.format(new Date(Long.parseLong(startDate) * 1000));
					    String end_date = sdfs.format(new Date(Long.parseLong(endDate) * 1000));
						otJson.put("start_date", start_date);
						otJson.put("end_date", end_date);
						otJson.put("total", totalBd);
						otJson.put("name", contacter.getName());
						otJson.put("identity_card", contacter.getIdentity_card());
						otJson.put("ticket_order_no", ticket_order_no);
						otJson.put("status", 7);
						otJson.put("room_id", contactJson.getInt("room_id"));
						otJsonList.add(otJson);
						//--
						
						ot.setNum(1);
						ot.setTicket(ticket);
//						String ticket_order_no = "sub" + date + randStr;
						ot.setTicket_order_no(ticket_order_no);
						ot.setNum(1);
						ot.setStatus(8);
						ot.setTotal(ticket.getPrice());
						ot.setUpdate_time(new Date());
//						ordersTicketDao.save(ot);
						otList.add(ot);
					}
					
					orderJson.put("orders_ticket", otJsonList);
					String path = getClass().getResource("/../../META-INF/hotel.json").getPath();
					JSONObject hotel = ParseFile.parseJson(path);
					String url = hotel.getString("url");
					String result = HttpUtil.sendPostStr(url+"/order/save", orderJson.toString());
					log.info("hotel pos ********"+result);
					if(!Strings.isNullOrEmpty(result)){
						JSONObject resJson = JSONObject.fromObject(result);
						int code = resJson.getInt("code");
						if(code == 1000){
							log.info("hotel pos *****success***");
							ordersDao.save(order);
							log.info("hotel pos *****success***");
							if(otList != null && otList.size() > 0){
								for(OrdersTicket ot:otList){
									ot.setOrders(order);
									ordersTicketDao.save(ot);
									otsList.add(ot);
									log.info("hotel pos orders ticket *****success***");
									elkCheckTicket(ot);
								}
							}
						}else{
							returnJson = CodeUtil.returnData(1050, "房源不足，无法预订", "");
						}
					}else{
						returnJson = CodeUtil.returnData(1050, "房源不足，无法预订", "");
					}
					
				}else{
					JSONArray contactersArr = JSONArray.fromObject(ticketJson.get("contacters"));
					if(contactersArr != null && contactersArr.size() > 0){
						int size = contactersArr.size();
						if(number >= size){
							ordersDao.save(order);
							Iterator<Object> iter = contactersArr.iterator();
							while (iter.hasNext()) {
								String date = sdf.format(new Date());
								Random random = new Random();
								String randStr = String.valueOf(100000 + random.nextInt(899999));
								JSONObject contactJson = JSONObject.fromObject(iter.next());
								String identity_card = contactJson.getString("identity_card");
								Contacter contacter = null;
								OrdersTicket ot = new OrdersTicket();
								ot.setNum(1);
								ot.setOrders(order);
								ot.setTicket(ticket);
								String ticket_order_no = "sub" + date + randStr;
								ot.setTicket_order_no(ticket_order_no);
								if(cList != null && cList.size() > 0){
									for(Contacter c:cList){
										if(c.getIdentity_card().equals(identity_card)){
											contacter = c;
											break;
										}
									}
								}
								if(contacter != null){
									ot.setContacter(contacter);
								}else{
									Contacter contacterM = new Contacter();
									contacterM.setIdentity_card(identity_card);
									contacterM.setUser(user);
									contacterM.setName(contactJson.getString("username"));
									contacterDao.save(contacterM);
									ot.setContacter(contacterM);
								}
								ot.setNum(1);
								ot.setStatus(8);
								ot.setTotal(ticket.getPrice());
								ot.setUpdate_time(new Date());
								ordersTicketDao.save(ot);
								otsList.add(ot);
								Poi p = ticket.getPoi();
								if(p != null){
									Classify c = p.getClassify();
									if(c != null){
										if(c.getType().equals("tickets")){
											elkCheckTicket(ot);
										}
									}
								}
								
							}
						}else{
							returnJson = CodeUtil.returnData(1051, "库存不足", "");
						}
					}else{
						returnJson = CodeUtil.returnData(1052, "请添加联系人", "");
					}
					
				}
				
			}else{
				returnJson = CodeUtil.returnData(1045, "缺少必要的请求参数", "");
			}
			JSONObject json = new JSONObject();
			json.put("orders_id", order.getId());
			elkOrders(order, otsList, "offline");
			returnJson = CodeUtil.returnData(1000, "添加成功", json);
		} else {
			returnJson = CodeUtil.returnData(1045, "缺少必要的请求参数", "");
		}
		return returnJson;
	
	}

//	@Override
//	public Response pay_order(Long loginUserid, Long order_id, JSONObject orders) {
//		JSONObject error = new JSONObject();
//		if (loginUserid != null && loginUserid > 0) {
//			if (orders != null) {
//				Orders order = ordersDao.get(order_id);
//				order.setPay_type(orders.getString("pay_type"));
//				order.setStatus(1);
//				ordersDao.update(order);
//				JSONObject json = new JSONObject();
//				json.put("status", "success");
//				return Response.status(Response.Status.OK).entity(json).build();
//			} else {
//				error.put("status", "缺少必要的请求参数");
//				error.put("code", Integer.valueOf(10009));
//				error.put("error_message", "缺少必要的请求参数");
//				return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
//			}
//		} else {
//			error.put("status", "用户未登录");
//			error.put("code", Integer.valueOf(10010));
//			error.put("error_message", "用户未登录");
//			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
//		}
//	}

	public Response noData() {
		JSONObject jo = new JSONObject();
		jo.put("status", "no_data");
		jo.put("code", Integer.valueOf(10020));
		jo.put("error_message", "no logined in");
		return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
	}

	public Response noUser() {
		JSONObject jo = new JSONObject();
		jo.put("status", "no_user");
		jo.put("code", Integer.valueOf(10021));
		jo.put("error_message", "user does not exist");
		return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
	}

	public Response noAuthority() {
		JSONObject jo = new JSONObject();
		jo.put("status", "no_authority");
		jo.put("code", Integer.valueOf(10085));
		jo.put("error_message", "There is no access to");
		return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
	}

	public Response invalidToken() {
		JSONObject jo = new JSONObject();
		jo.put("status", "invalid_token");
		jo.put("code", Integer.valueOf(10022));
		jo.put("error_message", "invalid token");
		return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
	}

	public Response invalidVersion() {
		JSONObject jo = new JSONObject();
		jo.put("status", "old_version");
		jo.put("code", 10108);
		jo.put("error_message", "please update a application version");
		return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
	}

	@Override
	public Response profile(Long loginUserid, HttpServletRequest request) {
		return null;
	}

	@Override
	public Response unuse_ticket(Long loginUserid, HttpServletRequest request) {

		String countStr = request.getParameter("count");
		String maxIdStr = request.getParameter("max_id");
		JSONObject error = new JSONObject();
		int count = 20;
		JSONObject result = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			User user = userDao.get(loginUserid);
			JSONObject userJson = new JSONObject();
			userJson.put("id", loginUserid);
			userJson.put("username", user.getUsername());
			userJson.put("identity_card", user.getIdentity_card());
			userJson.put("level", user.getLevel());
			if(user.getLevel().equals("normal")){
				List<Insurant> iList = insurantDao.getInsurantByIdentityCard(user.getIdentity_card());
				if(iList != null && iList.size() > 0){
					userJson.put("barcode", user.getNormal_barcode());
				}
				
			}else if(user.getLevel().equals("vip")){
				userJson.put("barcode", user.getVip_barcode());
			}
			
			result.put("user", userJson);
			List<OrdersTicket> ordersTicketList = null;
			if (Strings.isNullOrEmpty(countStr) && Strings.isNullOrEmpty(maxIdStr)) {
				ordersTicketList = ordersTicketDao.getOrdersTicketByStatusAndUserid(loginUserid, count);

			} else if (!Strings.isNullOrEmpty(countStr) && Strings.isNullOrEmpty(maxIdStr)) {
				count = Integer.parseInt(countStr);
				ordersTicketList = ordersTicketDao.getOrdersTicketByStatusAndUserid(loginUserid, count);
			} else if (Strings.isNullOrEmpty(countStr) && !Strings.isNullOrEmpty(maxIdStr)) {
				Long max_id = Long.parseLong(maxIdStr);
				ordersTicketList = ordersTicketDao.getOrdersTicketByStatusAndUserid(loginUserid, count, max_id);
			} else if (!Strings.isNullOrEmpty(countStr) && !Strings.isNullOrEmpty(maxIdStr)) {
				Long max_id = Long.parseLong(maxIdStr);
				count = Integer.parseInt(countStr);
				ordersTicketList = ordersTicketDao.getOrdersTicketByStatusAndUserid(loginUserid, count, max_id);
			}

			List<JSONObject> ordersJSONList = new ArrayList<JSONObject>();
			if (ordersTicketList != null && ordersTicketList.size() > 0) {
				JSONObject orderTicketJson = null;
				for (OrdersTicket o : ordersTicketList) {
					Ticket t = o.getTicket();
					Poi poi = t.getPoi();
					orderTicketJson = new JSONObject();
					orderTicketJson.put("orderTicket_id", o.getId());
					orderTicketJson.put("ticket_id", t.getId());
					orderTicketJson.put("ticket_type", t.getType());
					if(poi != null){
						orderTicketJson.put("poi_id", poi.getId());
						orderTicketJson.put("place", poi.getPlace());
						orderTicketJson.put("direct_sales", poi.getDirect_sales());
					}
					orderTicketJson.put("checking_rule", t.getChecking_rule());
					orderTicketJson.put("identity_card", o.getContacter().getIdentity_card());
					orderTicketJson.put("ticket_order_no", o.getTicket_order_no());
					orderTicketJson.put("status", o.getStatus());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					String startTimeStr = t.getStart_date()+" "+t.getStart_time();
					String endTimeStr = t.getStart_date()+" "+t.getEnd_time();
					long start_time = 0l;
					long end_time = 0l;
					long current_time = 0l;
					try {
						Date startTime = sdf.parse(startTimeStr);
						Date endTime = sdf.parse(endTimeStr);
						Date current_date = sdf.parse(sdf.format(new Date()));
						start_time = startTime.getTime() / 1000;
						end_time = endTime.getTime() / 1000;
						current_time = current_date.getTime() / 1000;
					} catch (ParseException e) {
						e.printStackTrace();
					}
					if(current_time > end_time){
						orderTicketJson.put("type", 1);
					}
					
					if(t.getType().equals("hotel")){
						orderTicketJson.put("room_type", o.getReturnCode());
						orderTicketJson.put("start_day", o.getStartDate());
						orderTicketJson.put("end_day", o.getEndDate());
						orderTicketJson.put("hotel_price", o.getTotal());
					}
					orderTicketJson.put("start_time", start_time);
					orderTicketJson.put("end_time", end_time);
					orderTicketJson.put("ticket_name", t.getTicket_name());
					orderTicketJson.put("status", o.getStatus());
					orderTicketJson.put("num", o.getNum());
//					orderTicketJson.put("barcode", o.getBarcode());
					orderTicketJson.put("total", o.getTotal());
					ordersJSONList.add(orderTicketJson);
				}
			}
			result.put("order_tickets", ordersJSONList);
			return Response.status(Response.Status.OK).entity(result).build();
		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}

	}
	
	@Override
	public Response unpaid_orders(Long loginUserid, HttpServletRequest request) {

		String countStr = request.getParameter("count");
		String maxIdStr = request.getParameter("max_id");
		JSONObject error = new JSONObject();
		JSONObject result = new JSONObject();
		int count = 20;
		if (loginUserid != null && loginUserid > 0) {
			User user = userDao.get(loginUserid);
			JSONObject userJson = new JSONObject();
			userJson.put("id", loginUserid);
			userJson.put("username", user.getUsername());
			userJson.put("level", user.getLevel());
			userJson.put("identity_card", user.getIdentity_card());
			
			if(user.getLevel().equals("normal")){
				List<Insurant> iList = insurantDao.getInsurantByIdentityCard(user.getIdentity_card());
				if(iList != null && iList.size() > 0){
					userJson.put("barcode", user.getNormal_barcode());
				}
				
			}else if(user.getLevel().equals("vip")){
				userJson.put("barcode", user.getVip_barcode());
			}
			if(!Strings.isNullOrEmpty(user.getClub_name())){
				userJson.put("club_name", user.getClub_name());
			}
			if(!Strings.isNullOrEmpty(user.getTrade())){
				userJson.put("trade", user.getTrade());
			}
			
			result.put("user", userJson);
			List<Orders> ordersList = null;
			if (Strings.isNullOrEmpty(countStr) && Strings.isNullOrEmpty(maxIdStr)) {
				ordersList = ordersDao.getOrdersByStatusAndCount(0, count, loginUserid);

			} else if (!Strings.isNullOrEmpty(countStr) && Strings.isNullOrEmpty(maxIdStr)) {
				count = Integer.parseInt(countStr);
				ordersList = ordersDao.getOrdersByStatusAndCount(0, count, loginUserid);
			} else if (Strings.isNullOrEmpty(countStr) && !Strings.isNullOrEmpty(maxIdStr)) {
				Long max_id = Long.parseLong(maxIdStr);
				ordersList = ordersDao.getOrdersByStatusAndCount(0, count,max_id, loginUserid);
			} else if (!Strings.isNullOrEmpty(countStr) && !Strings.isNullOrEmpty(maxIdStr)) {
				Long max_id = Long.parseLong(maxIdStr);
				count = Integer.parseInt(countStr);
				ordersList = ordersDao.getOrdersByStatusAndCount(0, count,max_id, loginUserid);
			}

			List<JSONObject> ordersJSONList = new ArrayList<JSONObject>();
			if (ordersList != null && ordersList.size() > 0) {
				JSONObject orderJson = null;
				for (Orders o : ordersList) {
					orderJson = new JSONObject();
					
					orderJson.put("id", o.getId());
					orderJson.put("orders_no", o.getOrder_no());
					List<OrdersTicket> otList = ordersTicketDao.getOrdersTicketByOrdersId(o.getId());
					if (otList != null && otList.size() > 0) {
						orderJson.put("count", otList.size());
						OrdersTicket ot = otList.get(0);
						Ticket ticket = ot.getTicket();
						Poi poi = ticket.getPoi();
						if(poi != null){
							orderJson.put("direct_sales", poi.getDirect_sales());
						}
						orderJson.put("ticket_name", ticket.getTicket_name());
						orderJson.put("ticket_type", ticket.getType());
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						String startTimeStr = ticket.getStart_date()+" "+ticket.getStart_time();
						String endTimeStr = ticket.getStart_date()+" "+ticket.getEnd_time();
						long start_time = 0l;
						long end_time = 0l;
						long current_time = 0l;
						try {
							Date startTime = sdf.parse(startTimeStr);
							Date endTime = sdf.parse(endTimeStr);
							start_time = startTime.getTime() / 1000;
							end_time = endTime.getTime() / 1000;
							String current_date = sdf.format(new Date());
							current_time = sdf.parse(current_date).getTime() / 1000;
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						int number = ticket.getNumber();
//						List<String> idcardList = new ArrayList<String>();
//						for(OrdersTicket ots:otList){
//							idcardList.add(ots.getContacter().getIdentity_card());
//						}
						//是否有人已投保
//						List<Insurant> iList = insurantDao.getInsurantByIdentityCards(idcardList);
//						double current_total = 0;
						
						String status = ticket.getStatus();
						if(status.equals("disable")){
							orderJson.put("type", 4);
						}else{
							if(current_time > end_time){
								orderJson.put("type", 1);
							}else if(number == 0){
								orderJson.put("type", 2);
							}else if(number < otList.size()){
								orderJson.put("type", 3);
							}else{
								orderJson.put("type", 0);
							}
						}
						
						if(ticket.getType().equals("hotel")){
							orderJson.put("room_type", ot.getReturnCode());
							orderJson.put("start_day", ot.getStartDate());
							orderJson.put("end_day", ot.getEndDate());
							orderJson.put("hotel_price", ot.getTotal());
						}
						
						orderJson.put("start_time", start_time);
						orderJson.put("end_time", end_time);
					}
					ordersJSONList.add(orderJson);
				}
			}
			result.put("orders", ordersJSONList);
			return Response.status(Response.Status.OK).entity(result).build();
		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}

	}
	
	@Override
	public Response orders_info(Long loginUserid,String orders_no) {
		JSONObject resp = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			if(!Strings.isNullOrEmpty(orders_no)){
				Orders o = ordersDao.getOrdersByOrderno(orders_no);
				List<OrdersTicket> otList = ordersTicketDao.getOrdersTicketByOrdersId(o.getId());
				JSONObject orderJson = new JSONObject();
				
				if(otList != null && otList.size() > 0){
					OrdersTicket ot = otList.get(0);
					Ticket ticket = ot.getTicket();
					orderJson.put("orders_id", o.getId());
					orderJson.put("ticket_id", ticket.getId());
					orderJson.put("ticket_name", ticket.getTicket_name());
					orderJson.put("ticket_type", ticket.getType());
					orderJson.put("status", ot.getStatus());
					orderJson.put("price", ticket.getPrice());
//					orderJson.put("is_insurance", ticket.getIs_insurance());
//					orderJson.put("insurance_price", ticket.getInsurance_price());
					if(ticket.getType().equals("hotel")){
						orderJson.put("room_type", ot.getReturnCode());
						orderJson.put("hotel_price", ot.getTotal());
						orderJson.put("start_day", ot.getStartDate());
						orderJson.put("end_day", ot.getEndDate());
					}
					JSONObject insurant = getInsurantInfo();
					orderJson.put("insurance_link", insurant.getString("insurance_link"));
					orderJson.put("amount",o.getAmount());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					String startTimeStr = ticket.getStart_date()+" "+ticket.getStart_time();
					String endTimeStr = ticket.getStart_date()+" "+ticket.getEnd_time();
					long start_time = 0l;
					long end_time = 0l;
					long current_time = 0l;
					try {
						Date startTime = sdf.parse(startTimeStr);
						Date endTime = sdf.parse(endTimeStr);
						start_time = startTime.getTime() / 1000;
						end_time = endTime.getTime() / 1000;
						current_time = new Date().getTime() / 1000;
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					orderJson.put("start_time", start_time);
					orderJson.put("end_time", end_time);
					orderJson.put("count", otList.size());
					List<JSONObject> contacterJsonList = new ArrayList<JSONObject>();
					JSONObject contacterJson = null;
					
					int number = ticket.getNumber();
					
					//是否有人已投保
//					List<Insurant> iList = insurantDao.getInsurantByIdentityCards(idcardList);
					for(OrdersTicket ots:otList){
						contacterJson = new JSONObject();
						Contacter c = ots.getContacter();
						contacterJson.put("id", c.getId());
						contacterJson.put("name", c.getName());
						contacterJson.put("identity_card", c.getIdentity_card());
						boolean exist = false;
//						if(iList != null && iList.size() > 0){
//							for(Insurant insure:iList){
//								if(insure.getIdentity_card().equals(c.getIdentity_card())){
//									exist = true;
//								}
//							}
//						}
						if(exist){
							contacterJson.put("insurant", "yes");
						}else{
							contacterJson.put("insurant", "no");
						}
						contacterJsonList.add(contacterJson);
//						idcardList.add(ots.getContacter().getIdentity_card());
					}
					if(current_time > end_time){
						orderJson.put("type", 1);
					}else if(number == 0){
						orderJson.put("type", 2);
					}else if(number < otList.size()){
						orderJson.put("type", 3);
					}else{
						orderJson.put("type", 0);
					}
					
					orderJson.put("contacters", contacterJsonList);
				}
				
				return Response.status(Response.Status.OK).entity(orderJson).build();
				
				
			}else{
				resp.put("status", "缺少必要的请求参数");
				resp.put("code", Integer.valueOf(10009));
				resp.put("error_message", "缺少必要的请求参数");
				return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
			}
		} else {
			resp.put("status", "用户未登录");
			resp.put("code", Integer.valueOf(10010));
			resp.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
		
	}
	
	@Override
	public Response cancel_orders(Long loginUserid, JSONObject orders) {
		JSONObject resp = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			if(orders != null){
				Long id = orders.getLong("id");
				Orders or = ordersDao.get(id);
				if(or.getStatus() == 0){
					or.setStatus(2);
					ordersDao.update(or);
					resp.put("id", or.getId());
					return Response.status(Response.Status.OK).entity(resp).build();
				}else{
					resp.put("status", "该订单不能被取消");
					resp.put("code", Integer.valueOf(10185));
					resp.put("error_message", "该订单不能被取消");
					return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
				}
			}else{
				resp.put("status", "缺少必要的请求参数");
				resp.put("code", Integer.valueOf(10009));
				resp.put("error_message", "缺少必要的请求参数");
				return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
			}
		} else {
			resp.put("status", "用户未登录");
			resp.put("code", Integer.valueOf(10010));
			resp.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	@Override
	public Response used_ticket(Long loginUserid, HttpServletRequest request) {
		String countStr = request.getParameter("count");
		String maxIdStr = request.getParameter("max_id");
		JSONObject error = new JSONObject();
		JSONObject result = new JSONObject();
		int count = 20;
		if (loginUserid != null && loginUserid > 0) {
			User user = userDao.get(loginUserid);
			JSONObject userJson = new JSONObject();
			userJson.put("id", loginUserid);
			userJson.put("username", user.getUsername());
			userJson.put("level", user.getLevel());
			userJson.put("identity_card", user.getIdentity_card());
			if(user.getLevel().equals("normal")){
				List<Insurant> iList = insurantDao.getInsurantByIdentityCard(user.getIdentity_card());
				if(iList != null && iList.size() > 0){
					userJson.put("barcode", user.getNormal_barcode());
				}
				
			}else if(user.getLevel().equals("vip")){
				userJson.put("barcode", user.getVip_barcode());
			}
			result.put("user", userJson);
			List<OrdersTicket> ordersTicketList = null;
			if (Strings.isNullOrEmpty(countStr) && Strings.isNullOrEmpty(maxIdStr)) {
				ordersTicketList = ordersTicketDao.getOrdersTicketByUserid(loginUserid, count, 1);

			} else if (!Strings.isNullOrEmpty(countStr) && Strings.isNullOrEmpty(maxIdStr)) {
				count = Integer.parseInt(countStr);
				ordersTicketList = ordersTicketDao.getOrdersTicketByUserid(loginUserid, count, 1);
			} else if (Strings.isNullOrEmpty(countStr) && !Strings.isNullOrEmpty(maxIdStr)) {
				Long max_id = Long.parseLong(maxIdStr);
				ordersTicketList = ordersTicketDao.getOrdersTicketByUserid(loginUserid, count, max_id, 1);
			} else if (!Strings.isNullOrEmpty(countStr) && !Strings.isNullOrEmpty(maxIdStr)) {
				Long max_id = Long.parseLong(maxIdStr);
				count = Integer.parseInt(countStr);
				ordersTicketList = ordersTicketDao.getOrdersTicketByUserid(loginUserid, count, max_id, 1);
			}

			List<JSONObject> ordersJSONList = new ArrayList<JSONObject>();
			if (ordersTicketList != null && ordersTicketList.size() > 0) {
				JSONObject orderTicketJson = null;
				for (OrdersTicket o : ordersTicketList) {
					Ticket t = o.getTicket();
					orderTicketJson = new JSONObject();
					orderTicketJson.put("orderTicket_id", o.getId());
					orderTicketJson.put("ticket_order_no", o.getTicket_order_no());
					orderTicketJson.put("ticket_name", o.getTicket().getTicket_name());
					orderTicketJson.put("status", o.getStatus());
					
					Poi poi = t.getPoi();
					if(poi != null){
						orderTicketJson.put("poi_id", poi.getId());
						orderTicketJson.put("place", poi.getPlace());
						orderTicketJson.put("direct_sales", poi.getDirect_sales());
					}
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					String startTimeStr = t.getStart_date()+" "+t.getStart_time();
					String endTimeStr = t.getStart_date()+" "+t.getEnd_time();
					long start_time = 0l;
					long end_time = 0l;
					try {
						Date startTime = sdf.parse(startTimeStr);
						Date endTime = sdf.parse(endTimeStr);
						start_time = startTime.getTime() / 1000;
						end_time = endTime.getTime() / 1000;
					} catch (ParseException e) {
						e.printStackTrace();
					}
					orderTicketJson.put("start_time", start_time);
					orderTicketJson.put("end_time", end_time);
					orderTicketJson.put("num", o.getNum());
					orderTicketJson.put("barcode", o.getBarcode());

					ordersJSONList.add(orderTicketJson);
				}
				
			}
			result.put("order_tickets", ordersJSONList);
			return Response.status(Response.Status.OK).entity(result).build();
		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}
	}

	@Override
	public Response add_collect(Long loginUserid, Long poi_id) {
		JSONObject resp = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			Collect collect = new Collect();
			collect.setReference_id(poi_id);
			collect.setUser_id(loginUserid);
			collectDao.save(collect);
			resp.put("status", "success");
			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "用户未登录");
			resp.put("code", Integer.valueOf(10010));
			resp.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}

	}

	@Override
	public Response get_content(Long content_id, Long loginUserid, HttpServletRequest request) {
		Content content = contentDao.get(content_id);
		JSONObject contentJson = new JSONObject();
		if (content != null) {
			contentJson.put("id", content.getId());
			contentJson.put("title", content.getTitle());
			if (!Strings.isNullOrEmpty(content.getSummary())) {
				contentJson.put("summary", content.getSummary());
			}
			if (!Strings.isNullOrEmpty(content.getCover_image())) {
				contentJson.put("cover_image", JSONObject.fromObject(content.getCover_image()));
			}
			List<ContentElement> ceList = content.getElements();
			List<JSONObject> contentElementJsonList = new ArrayList<JSONObject>();
			JSONObject contentElementJson = null;
			if (ceList != null && ceList.size() > 0) {
				for (ContentElement ce : ceList) {
					contentElementJson = new JSONObject();
					contentElementJson.put("id", ce.getId());
					contentElementJson.put("content", JSONObject.fromObject(ce.getElement()));
					contentElementJsonList.add(contentElementJson);
				}
				contentJson.put("elements", contentElementJsonList);
			}
		}
		return Response.status(Response.Status.OK).entity(contentJson).build();
	}

	@Override
	public Response homepage() {
		
		JSONObject resp = new JSONObject();
		List<JSONObject> slideJsonList = new ArrayList<JSONObject>();
		JSONObject slideJson = null;
		
		if(myRedisDao.exists("homepage")){
			long start = System.currentTimeMillis();
//			Jedis jedis = getJedis();
			Object homepage = myRedisDao.get("homepage");
			long end = System.currentTimeMillis();
			System.out.println("connection redis ****"+(end-start));
//			String homepage = jedis.get("homepage");
			long end1 = System.currentTimeMillis();
			System.out.println("get redis value****"+(end1-end));
//			returnResource(jedis);
			resp = JSONObject.fromObject(homepage);
		}else{
			List<Slide> slideList = slideDao.getSlideByStatus("enable");
			if (slideList != null && slideList.size() > 0) {
				for (Slide slide : slideList) {
					slideJson = new JSONObject();
					slideJson.put("id", slide.getId());
					slideJson.put("slide_image", JSONObject.fromObject(slide.getSlide_image()));
					slideJson.put("title", slide.getTitle());
					String type = slide.getType(); // url content poi
					if (type.equals("url")) {
						slideJson.put("type", slide.getType());
						slideJson.put("url", slide.getUrl());
					} else {
						slideJson.put("type", slide.getType());
						slideJson.put("reference_id", slide.getReference_id());
					}
					slideJsonList.add(slideJson);
				}
				resp.put("slides", slideJsonList);
			}

			List<Classify> classifyList = classifyDao.getClassifyByOnlineAndHomepage();
			List<JSONObject> classifyJsonList = new ArrayList<JSONObject>();
			JSONObject classifyJson = null;
			if (classifyList != null && classifyList.size() > 0) {
				for (Classify c : classifyList) {
					classifyJson = new JSONObject();
					classifyJson.put("id", c.getId());
					classifyJson.put("classify_name", c.getClassify_name());
					List<Poi> pList = poiDao.getPoiListByClassify_idAndIs_online(1, c.getId());
					List<JSONObject> poiJsonList = new ArrayList<JSONObject>();
					JSONObject poiJson = null;
					if(pList != null && pList.size() > 0){
						for(Poi p:pList){
							PoiElement pe = p.getElements().get(0);
							poiJson = new JSONObject();
							poiJson.put("id", p.getId());
							poiJson.put("title", p.getTitle());
							poiJson.put("cover_image", JSONObject.fromObject(pe.getElement()));
							poiJson.put("location", JSONObject.fromObject(p.getLocation()));
							poiJsonList.add(poiJson);
						}
						classifyJson.put("POIs", poiJsonList);
					}
					classifyJsonList.add(classifyJson);
				}
				resp.put("classifies", classifyJsonList);
			}

			// 通知
			Notice notice = noticeDao.getNoticeByStatus(1);
			JSONObject noticeJson = null;
			if (notice != null) {
				noticeJson = new JSONObject();
				noticeJson.put("id", notice.getId());
				noticeJson.put("title", notice.getTitle());
				String type = notice.getType();
				noticeJson.put("type", type);
				if (type.equals("url")) {
					noticeJson.put("url", notice.getUrl());
				} else {
					noticeJson.put("reference_id", notice.getReference_id());
				}
				resp.put("notice", noticeJson);
			}
			myRedisDao.save("homepage", resp.toString(),-1);
//			returnResource(jedis);
		
		}
		
		
		
		return Response.status(Response.Status.OK).entity(resp).build();
	}

	@Override
	public Response setting(int type) {
		JSONObject resp = new JSONObject();
		if (type > 0) {
			List<Setting> sList = settingDao.getSettingListByType(type);
			JSONObject contentJson = new JSONObject();
			if (sList != null && sList.size() > 0) {
				Setting s = sList.get(0);
				Content content = s.getContent();

				if (content != null) {
					contentJson.put("id", content.getId());
					contentJson.put("title", content.getTitle());
					if (!Strings.isNullOrEmpty(content.getSummary())) {
						contentJson.put("summary", content.getSummary());
					}
					if (!Strings.isNullOrEmpty(content.getCover_image())) {
						contentJson.put("cover_image", JSONObject.fromObject(content.getCover_image()));
					}
					List<ContentElement> ceList = content.getElements();
					List<JSONObject> contentElementJsonList = new ArrayList<JSONObject>();
					JSONObject contentElementJson = null;
					if (ceList != null && ceList.size() > 0) {
						for (ContentElement ce : ceList) {
							contentElementJson = new JSONObject();
							contentElementJson.put("id", ce.getId());
							contentElementJson.put("content", JSONObject.fromObject(ce.getElement()));
							contentElementJsonList.add(contentElementJson);
						}
						contentJson.put("elements", contentElementJsonList);
					}
				}
			}
			return Response.status(Response.Status.OK).entity(contentJson).build();
		} else {
			resp.put("status", "用户未登录");
			resp.put("code", Integer.valueOf(10010));
			resp.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	@Override
	public Response webhooks(JSONObject data) throws Exception {
		log.debug("*** webhooks start ***");
		if (data != null) {
			log.debug("Ping ++返回数据 *******"+data);
			String type = data.getString("type");
			if (type.equals("charge.succeeded")) {
				log.debug("支付成功****************");
//				JSONObject dataJson = data.getJSONObject("data");
//				JSONObject objJson = dataJson.getJSONObject("object");
//				String order_no = objJson.getString("order_no");
//				String channel = objJson.getString("channel");
//				String charge_id = objJson.getString("id");
//				Orders orders = ordersDao.getOrdersByOrderNoAndChargeId(order_no, charge_id);
//				int status = orders.getStatus();
//				if(status == 0){
//					// int result = ordersDao.updateOrderStatus(order_no, channel);
//					boolean is_refund = false;
//					// 调智游宝的下单接口
//					Zyb zyb = getZybInfo();
//					List<OrdersTicket> otList = ordersTicketDao.getOrdersTicketByOrdersId(orders.getId());
//					String xmlMsg = ZybUtil.createOrdersXml(orders.getUser(), orders, otList, zyb);
//					String sign = CodeUtil.encode2hex("xmlMsg=" + xmlMsg + zyb.getMasterSecret());
//					Map<String, Object> map = new HashMap<String, Object>();
//					map.put("xmlMsg", xmlMsg);
//					map.put("sign", sign);
//					String result = HttpUtil.sendClientPost(zyb.getUrl(), map);
//					String codeStr = ZybUtil.getReturnCode(result);
//					// 智游宝
//					if (!Strings.isNullOrEmpty(codeStr)) {
//						int code = Integer.parseInt(codeStr);
//						if (code == 0) {
//							String xmlMsgCode = ZybUtil.findImgXml(orders.getOrder_no(), zyb);
//							String signCode = CodeUtil.encode2hex("xmlMsg=" + xmlMsgCode + zyb.getMasterSecret());
//							Map<String, Object> mapCode = new HashMap<String, Object>();
//							mapCode.put("xmlMsg", xmlMsgCode);
//							mapCode.put("sign", signCode);
//							String resultCode = HttpUtil
//									.sendClientPost(zyb.getUrl(), mapCode);
//							String barcode = ZybUtil.getImg(resultCode);
//							if (!Strings.isNullOrEmpty(barcode)) {
//								
//								Ticket ticket = null;
//								List<Contacter> contacterList = new ArrayList<Contacter>();
//								if (otList != null && otList.size() > 0) {
//									ticket = otList.get(0).getTicket();
//									for (OrdersTicket ot : otList) {
//										contacterList.add(ot.getContacter());
//									}
//								}
//								if(ticket.getIs_insurance() == 1){
//									JSONObject insurant = getInsurantInfo();
//									String url = insurant.getString("url");
//									String key = insurant.getString("key");
//									String partnerld = insurant.getString("partnerld");
//									String caseCode = insurant.getString("caseCode");
//
//									JSONObject insurantJson = getInsurantJson(orders, contacterList, ticket, partnerld,caseCode);
//									String insurantSign = CodeUtil.encode2hex(key+insurantJson);
//									String insurantResult = HttpUtil.sendPostStr(url+"localInsure?sign="+insurantSign, insurantJson.toString());
//									if(!Strings.isNullOrEmpty(insurantResult)){
//										// 智游宝 下单成功
//										orders.setStatus(1);
//										orders.setPay_type(channel);
//										ordersDao.update(orders);
//										String ticket_name = "";
//										int size = 0;
//										
//										if (otList != null && otList.size() > 0) {
//											ticket_name = ticket.getTicket_name();
//											size = otList.size();
//											for (OrdersTicket ot : otList) {
//												barcode = barcode.replaceAll("gmCheckNo", "gmCheckCode");
//												ot.setBarcode(barcode);// 测试智游宝通过后 完善这里
//												ot.setStatus(0); // 未使用
//												ordersTicketDao.update(ot);
//											}
//											Ticket t = otList.get(0).getTicket();
//											t.setNumber(t.getNumber() - size);
//											t.setSold(t.getSold()+size);
//											ticketDao.update(t);
//										}
//										
//									}else{
//										is_refund = true;
//									}
//									
//									
//								}else if(ticket.getIs_insurance() == 0){
//
//									// 智游宝 下单成功
//									orders.setStatus(1);
//									orders.setPay_type(channel);
//									ordersDao.update(orders);
//									String ticket_name = "";
//									int size = 0;
//									
//									if (otList != null && otList.size() > 0) {
//										ticket_name = ticket.getTicket_name();
//										size = otList.size();
//										for (OrdersTicket ot : otList) {
//											barcode = barcode.replaceAll("gmCheckNo", "gmCheckCode");
//											ot.setBarcode(barcode);// 测试智游宝通过后 完善这里
//											ot.setStatus(0); // 未使用
//											ordersTicketDao.update(ot);
//										}
//										Ticket t = otList.get(0).getTicket();
//										t.setNumber(t.getNumber() - size);
//										t.setSold(t.getSold()+size);
//										ticketDao.update(t);
//									}
//								}
//								
//							} else {
//								is_refund = true;
//							}
//						} else {
//							is_refund = true;
//						}
//					} else {
//						is_refund = true;
//					}
//
//					if (is_refund) {
//
//						// 智游宝 下单失败 自动退款
//						Refund refund = null;
//						Map<String, Object> params = new HashMap<String, Object>();
//						params.put("description", "智游宝下单失败");
//						params.put("amount", orders.getAmount());
//						try {
//							refund = Refund.create(charge_id, params);
//							orders.setRefund_id(refund.getId());
//							ordersDao.update(orders);
//						} catch (AuthenticationException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (InvalidRequestException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (APIConnectionException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (APIException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (ChannelException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (RateLimitException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//
//					}
//				}else{
//					
//				}
				
				return Response.status(Response.Status.OK).entity("").build();
			} else if (type.equals("refund.succeeded")) {
				// 退款有两种情况
				// 一种是智游宝下单没有成功 整个订单都退
				// 一种是单张票退
				log.debug("退款成功*****------->>>>>>>"+type);
				JSONObject dataJson = data.getJSONObject("data");
				JSONObject objJson = dataJson.getJSONObject("object");
				String order_no = objJson.getString("charge_order_no");
				String refund_id = objJson.getString("id");
				
				log.debug("订单号*****------->>>>>>>"+order_no);
				Orders orders = ordersDao.getOrdersByOrderNoAndRefundId(order_no, refund_id);
				if(orders != null){

					if (orders != null) {
						orders.setStatus(5);
						ordersDao.update(orders);
						List<OrdersTicket> otList = ordersTicketDao.getOrdersTicketByOrdersId(orders.getId());
						if(otList != null && otList.size() > 0){
							for(OrdersTicket ot:otList){
								ot.setStatus(6);
								ordersTicketDao.update(ot);
							}
						}
					}
					
					User user = orders.getUser();
					// 需要通知
					Notification n = new Notification();
					n.setNotificationType(5);
					n.setObjectType(6);
					n.setObjectId(orders.getId());
					n.setRead_already(false);
					n.setRecipientId(user.getId());
					n.setTitle("退款成功");
					n.setRemark("您的订单："+order_no+"退款成功，退款金额：￥"+orders.getAmount()+"，已按您的支付方式原路返回。");
					n.setStatus("enable");
					notificationDao.save(n);
					List<PushNotification> list = pushNotificationDao.getPushNotificationByUserid(user.getId());
					GetuiModel gm = getGetuiInfo();
					JSONObject json = new JSONObject();
					json.put("description", n.getRemark());
					json.put("title", n.getTitle());
					try {
						PushNotificationUtil.pushInfoAll(gm.getAppId(), gm.getAppKey(), gm.getMasterSecret(), list,
								n.getTitle(), json.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				
				}else{
					OrdersTicket ot = ordersTicketDao.getOrdersTicketByOrdersNoAndRefundId(order_no, refund_id);
					ot.setStatus(6);
					ordersTicketDao.update(ot);
					User user = ot.getOrders().getUser();
					// 需要通知
					Notification n = new Notification();
					n.setNotificationType(5);
					n.setObjectType(1);
					n.setObjectId(ot.getId());
					n.setRead_already(false);
					n.setRecipientId(user.getId());
					n.setTitle("退款成功");
					n.setRemark("您的订单："+ot.getTicket_order_no()+"退款成功，退款金额：￥"+ot.getTotal()+"，已按您的支付方式原路返回。");
					n.setStatus("enable");
					notificationDao.save(n);
					List<PushNotification> list = pushNotificationDao.getPushNotificationByUserid(user.getId());
					GetuiModel gm = getGetuiInfo();
					JSONObject json = new JSONObject();
					json.put("description", n.getRemark());
					json.put("title", n.getTitle());
					try {
						PushNotificationUtil.pushInfoAll(gm.getAppId(), gm.getAppKey(), gm.getMasterSecret(), list,
								n.getTitle(), json.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
			return Response.status(Response.Status.OK).entity("").build();
		} else {
			return Response.status(Response.Status.EXPECTATION_FAILED).entity("").build();
		}
	}

	@Override
	public Response getUser(Long userId) {
		User user = userDao.get(userId);
		JSONObject userJson = new JSONObject();
		if (user != null) {
			userJson.put("id", user.getId());
			User u = (User) this.userDao.get(userId);
			if (!Strings.isNullOrEmpty(u.getUsername())) {
				userJson.put("username", u.getUsername());
			}

			if (!Strings.isNullOrEmpty(u.getBirthday())) {
				userJson.put("birthday", u.getBirthday());
			}

			if (!Strings.isNullOrEmpty(u.getClub_name())) {
				userJson.put("club_name", u.getClub_name());
			}
			if (!Strings.isNullOrEmpty(u.getIdentity_card())) {
				userJson.put("identity_card", u.getIdentity_card());
			}
			if (!Strings.isNullOrEmpty(u.getLevel())) {
				userJson.put("level", u.getLevel());
			}

			if (!Strings.isNullOrEmpty(u.getTrade())) {
				userJson.put("trade", u.getTrade());
			}

		}
		return Response.status(Response.Status.OK).entity(userJson).build();
	}

	@Override
	public Response add_contacter(Long loginUserid, JSONObject data) {
		JSONObject resp = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			Contacter c = new Contacter();
			if (data != null) {
				if (data.containsKey("identity_card") && !Strings.isNullOrEmpty(data.getString("identity_card"))) {
					String identity_card = data.getString("identity_card");
					List<Contacter> cList = contacterDao.getContacterByIdentityCard(identity_card,loginUserid);
					if(cList != null && cList.size() > 0){
						resp.put("status", "该身份证号已是您的常用联系人");
						resp.put("code", Integer.valueOf(10180));
						resp.put("error_message", "该身份证号已是您的常用联系人");
						return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
					}else{
						c.setIdentity_card(identity_card);
						if (data.containsKey("name") && !Strings.isNullOrEmpty(data.getString("name"))) {
							c.setName(data.getString("name"));
						}

						if (data.containsKey("phone") && !Strings.isNullOrEmpty(data.getString("phone"))) {
							c.setPhone(data.getString("phone"));
						}
						
						if (data.containsKey("gender") && !Strings.isNullOrEmpty(data.getString("gender"))) {
							c.setGender(data.getString("gender"));
						}

						if (data.containsKey("birthday") && !Strings.isNullOrEmpty(data.getString("birthday"))) {
							c.setBirthday(data.getString("birthday"));
						}

						User user = userDao.get(loginUserid);
						c.setUser(user);
						contacterDao.save(c);
						List<Insurant> iList = insurantDao.getInsurantByIdentityCard(identity_card);
						if(iList != null && iList.size() > 0){
							resp = getContacterJsonInfo(c,user,true);
						}else{
							resp = getContacterJsonInfo(c,user,false);
						}
						
						return Response.status(Response.Status.OK).entity(resp).build();
					}
					
				}else{
					resp.put("status", "身份证号不能为空");
					resp.put("code", Integer.valueOf(10181));
					resp.put("error_message", "身份证号不能为空");
					return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
				}
				
			} else {
				resp.put("status", "缺少必要的请求参数");
				resp.put("code", Integer.valueOf(10009));
				resp.put("error_message", "缺少必要的请求参数");
				return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
			}
		} else {
			resp.put("status", "用户未登录");
			resp.put("code", Integer.valueOf(10010));
			resp.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	@Override
	public Response update_contacter(Long loginUserid, JSONObject data) {

		JSONObject resp = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			Contacter c = new Contacter();
			if (data != null) {
				if (data.containsKey("name") && !Strings.isNullOrEmpty(data.getString("name"))) {
					c.setName(data.getString("name"));
				}

				if (data.containsKey("identity_card") && !Strings.isNullOrEmpty(data.getString("identity_card"))) {
					c.setIdentity_card(data.getString("identity_card"));
				}

				if (data.containsKey("phone") && !Strings.isNullOrEmpty(data.getString("phone"))) {
					c.setPhone(data.getString("phone"));
				}

				contacterDao.update(c);
				User user = c.getUser();
				resp = getContacterJson(c,user);
				return Response.status(Response.Status.OK).entity(resp).build();
			} else {
				resp.put("status", "缺少必要的请求参数");
				resp.put("code", Integer.valueOf(10009));
				resp.put("error_message", "缺少必要的请求参数");
				return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
			}
		} else {
			resp.put("status", "用户未登录");
			resp.put("code", Integer.valueOf(10010));
			resp.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}

	}

	@Override
	public Response delete_contacter(Long id, Long loginUserid) {

		JSONObject resp = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			if (id != null && id > 0) {
				Contacter c = contacterDao.get(id);
				if(c != null){
					contacterDao.delete(id);
					resp.put("status", "success");
					return Response.status(Response.Status.OK).entity(resp).build();
				}else{
					resp.put("status", "该常用联系人不存在");
					resp.put("code", Integer.valueOf(10183));
					resp.put("error_message", "该常用联系人不存在");
					return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
				}
				
			} else {
				resp.put("status", "缺少必要的请求参数");
				resp.put("code", Integer.valueOf(10009));
				resp.put("error_message", "缺少必要的请求参数");
				return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
			}
		} else {
			resp.put("status", "用户未登录");
			resp.put("code", Integer.valueOf(10010));
			resp.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}

	}

	@Override
	public Response contacter_list(Long loginUserid) {
		JSONObject resp = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			List<Contacter> cList = contacterDao.getContacterList(loginUserid);
			JSONObject contacterJson = null;
			List<JSONObject> contacterJsonList = new ArrayList<JSONObject>();
			if (cList != null && cList.size() > 0) {
				User user = cList.get(0).getUser();
				for (Contacter c : cList) {
					contacterJson = new JSONObject();
					contacterJson = getContacterJson(c,user);
					contacterJsonList.add(contacterJson);
				}
			}
			return Response.status(Response.Status.OK).entity(contacterJsonList).build();
		} else {
			resp.put("status", "用户未登录");
			resp.put("code", Integer.valueOf(10010));
			resp.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	public JSONObject getContacterJson(Contacter c,User user) {
		JSONObject resp = new JSONObject();
		resp.put("id", c.getId());
		resp.put("name", c.getName());
		resp.put("identity_card", c.getIdentity_card());
		if (!Strings.isNullOrEmpty(c.getPhone())) {
			resp.put("phone", c.getPhone());
		}
		if(user.getIdentity_card().equals(c.getIdentity_card())){
			resp.put("current_user", "yes");
		}else{
			resp.put("current_user", "no");
		}

		return resp;
	}
	
	public JSONObject getContacterJsonInfo(Contacter c,User user,boolean is_insurant) {
		JSONObject resp = new JSONObject();
		resp.put("id", c.getId());
		resp.put("name", c.getName());
		resp.put("identity_card", c.getIdentity_card());
		if (!Strings.isNullOrEmpty(c.getPhone())) {
			resp.put("phone", c.getPhone());
		}
		if(user.getIdentity_card().equals(c.getIdentity_card())){
			resp.put("current_user", "yes");
		}else{
			resp.put("current_user", "no");
		}
		
		if(is_insurant){
			resp.put("insurant", "yes");
		}else{
			resp.put("insurant", "no");
		}

		return resp;
	}

	@Override
	public Response collect_list(Long loginUserid, HttpServletRequest request) {

		String countStr = request.getParameter("count");
		String maxIdStr = request.getParameter("max_id");
		JSONObject error = new JSONObject();
		int count = 20;
		if (loginUserid != null && loginUserid > 0) {
			List<Collect> collectList = null;
			if (Strings.isNullOrEmpty(countStr) && Strings.isNullOrEmpty(maxIdStr)) {
				collectList = collectDao.getCollectListByUseridAndCount(loginUserid, count);
			} else if (!Strings.isNullOrEmpty(countStr) && Strings.isNullOrEmpty(maxIdStr)) {
				count = Integer.parseInt(countStr);
				collectList = collectDao.getCollectListByUseridAndCount(loginUserid, count);
			} else if (Strings.isNullOrEmpty(countStr) && !Strings.isNullOrEmpty(maxIdStr)) {
				Long max_id = Long.parseLong(maxIdStr);
				collectList = collectDao.getCollectListByUseridAndCountAndId(loginUserid, count, max_id);
			} else if (!Strings.isNullOrEmpty(countStr) && !Strings.isNullOrEmpty(maxIdStr)) {
				Long max_id = Long.parseLong(maxIdStr);
				count = Integer.parseInt(countStr);
				collectList = collectDao.getCollectListByUseridAndCountAndId(loginUserid, count, max_id);
			}

			List<JSONObject> collectJSONList = new ArrayList<JSONObject>();
			if (collectList != null && collectList.size() > 0) {
				JSONObject collectJson = null;
				JSONObject content = null;
				for (Collect c : collectList) {
					collectJson = new JSONObject();
					content = new JSONObject();
					collectJson.put("id", c.getId());
					collectJson.put("reference_type", c.getReference_type());
					String type = c.getReference_type();
					if (type.equals("poi")) {
						Poi p = poiDao.get(c.getReference_id());
						content.put("id", p.getId());
						content.put("title", p.getTitle());
						content.put("cover_image", JSONObject.fromObject(p.getElements().get(0).getElement()));
						content.put("is_collect", true);
						collectJson.put("content", content);
					} else if (type.equals("content")) {

					}
					collectJson.put("create_time", c.getCreate_time());

					collectJSONList.add(collectJson);
				}
			}
			return Response.status(Response.Status.OK).entity(collectJSONList).build();
		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}

	}

	public GetuiModel getGetuiInfo() {
		GetuiModel gm = new GetuiModel();
		String path = getClass().getResource("/../../META-INF/getui.json").getPath();
		JSONObject json1 = ParseFile.parseJson(path);
		String appId = json1.getString("appId");
		String appKey = json1.getString("appKey");
		String masterSecret = json1.getString("masterSecret");
		gm.setAppId(appId);
		gm.setAppKey(appKey);
		gm.setMasterSecret(masterSecret);
		return gm;

	}
	
	public String getPingSecretKey() {
		String path = getClass().getResource("/../../META-INF/ping++.json").getPath();
		JSONObject ping = ParseFile.parseJson(path);
		String secretKey = ping.getString("secretKey");
		return secretKey;

	}
	
	public String getPingAppid() {
		String path = getClass().getResource("/../../META-INF/ping++.json").getPath();
		JSONObject ping = ParseFile.parseJson(path);
		String appid = ping.getString("appid");
		return appid;

	}

	public Zyb getZybInfo() {
		Zyb zyb = new Zyb();
		String path = getClass().getResource("/../../META-INF/zyb.json").getPath();
		JSONObject json1 = ParseFile.parseJson(path);
		String corpCode = json1.getString("corpCode");
		String userName = json1.getString("userName");
		String masterSecret = json1.getString("masterSecret");
		String normalZybCode = json1.getString("normal_zyb_code");
		String vipZybCode = json1.getString("vip_zyb_code");
		String url = json1.getString("url");
		zyb.setCorpCode(corpCode);
		zyb.setUserName(userName);
		zyb.setMasterSecret(masterSecret);
		zyb.setUrl(url);
		zyb.setNoramlZybCode(normalZybCode);
		zyb.setVipZybCode(vipZybCode);
		return zyb;

	}

	
	public JSONObject getInsurantJson(Orders orders,List<Contacter> cList,Ticket t,String partnerld,String caseCode,String cardCode,String company){
		JSONObject insureReqJson = new JSONObject();
		insureReqJson.put("transNo", orders.getOrder_no());
		insureReqJson.put("partnerId", Integer.parseInt(partnerld));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
		String yearStr = sdfYear.format(new Date());
		String startTimeStr = yearStr+"-10-01";
		String endTimeStr = yearStr+"-10-09";
		String currentTimeStr = "";
		long current_time = 0l;
		long start_time = 0l;
		long end_time = 0l;
		try {
			currentTimeStr = sdf.format(new Date());
			current_time = sdf.parse(currentTimeStr).getTime() / 1000;
			start_time = sdf.parse(startTimeStr).getTime() / 1000;
			end_time = sdf.parse(endTimeStr).getTime() / 1000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		insureReqJson.put("caseCode", caseCode);
		if(current_time < start_time){
			insureReqJson.put("startDate", startTimeStr);
		}else if(current_time > start_time && current_time < end_time){
			insureReqJson.put("startDate", currentTimeStr);
		}
		insureReqJson.put("endDate", endTimeStr);
		JSONObject applicantJson = new JSONObject();
//		企业投保
		applicantJson.put("applicantType", 1);
		applicantJson.put("cName", company);
		applicantJson.put("cardType", 14);
		applicantJson.put("cardCode", cardCode);
		applicantJson.put("mobile", "18648339308");
		applicantJson.put("email", "teambition.alashanbaoxian@163.com");//邮箱自己提供
		
		//--个人投保---
		User user = orders.getUser();
//		applicantJson.put("cName", user.getUsername());
//		applicantJson.put("cardType", 1);
//		String identity_card = user.getIdentity_card();
//		applicantJson.put("cardCode", user.getIdentity_card());
//		String ge = user.getGender();
//		if(ge.equals("男")){
//			applicantJson.put("sex", 1);//暂时没有
//		}else if(ge.equals("女")){
//			applicantJson.put("sex", 0);//暂时没有
//		}
//		applicantJson.put("birthday", user.getBirthday());
//		String phone = user.getPhone();
//		applicantJson.put("mobile", phone);
//		applicantJson.put("email", "teambition.alashanbaoxian@163.com");
		//--个人投保---
		
		insureReqJson.put("applicant", applicantJson);
		List<JSONObject> insurants = new ArrayList<JSONObject>();
		JSONObject insurantJson = null;
		if(cList != null && cList.size() > 0){
			for(Contacter c:cList){
				insurantJson = new JSONObject();
				insurantJson.put("insurantId", c.getId());
				insurantJson.put("cName", c.getName());
				insurantJson.put("cardType", 1);
				insurantJson.put("cardCode", c.getIdentity_card());
				String gender = c.getGender();
				if(gender.equals("男")){
					insurantJson.put("sex", 1);//暂时没有
				}else if(gender.equals("女")){
					insurantJson.put("sex", 0);//暂时没有
				}
				insurantJson.put("birthday", c.getBirthday());//暂时没有
				insurantJson.put("mobile", user.getPhone());
				//--个人投保---
//				if(identity_card.equals(c.getIdentity_card())){
//					insurantJson.put("relationId", 1); 
//				}else{
//					insurantJson.put("relationId", 23); 
//				}
				//----
				insurantJson.put("relationId", 23); 
				insurantJson.put("count",1);
//				insurantJson.put("singlePrice",t.getInsurance_price().longValue() * 100);
				insurants.add(insurantJson);
			}
			insureReqJson.put("insurants", insurants);
		}
		
		return insureReqJson;
	}
	
//	public Jedis getJedis(){
//		String path = getClass().getResource("/../../META-INF/redis.json").getPath();
//		JSONObject json1 = ParseFile.parseJson(path);
//		String host = json1.getString("host");
//		int port = json1.getInt("port");
//		String password = json1.getString("password");
//		Jedis jedis = null;
//		if(!Strings.isNullOrEmpty(password)){
//			JedisPoolConfig config = new JedisPoolConfig();
//			//最大空闲连接数, 应用自己评估，不要超过ApsaraDB for Redis每个实例最大的连接数
//			config.setMaxIdle(10000);
//			//最大连接数, 应用自己评估，不要超过ApsaraDB for Redis每个实例最大的连接数
//			config.setMaxTotal(10000);
//			config.setTestOnBorrow(false);
//			config.setTestOnReturn(false);
//		    pool = new JedisPool(config, host, port, 1000, password);
//			jedis = pool.getResource();
//		}else{
//			jedis = new Jedis(host, port);
//		}
//		return jedis;
//	}
	
	/**
     * 返还到连接池
     * 
     * @param pool 
     * @param redis
     */
//    public void returnResource(Jedis redis) {
//        if (redis != null) {
//            pool.returnResource(redis);
//        }
//    }
	
	public JSONObject getInsurantInfo(){
		String path = getClass().getResource("/../../META-INF/insurant.json").getPath();
		JSONObject insurant = ParseFile.parseJson(path);
		return insurant;
	}
	
	public String getUserInfoByIdCard(String idcard,String username){
		 String host = "http://idcard.market.alicloudapi.com";
		    String path = "/lianzhuo/idcard";
		    String method = "GET";
		    String appcode = "2434391486e746138d67d704f1942a12";
		    Map<String, String> headers = new HashMap<String, String>();
		    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		    headers.put("Authorization", "APPCODE " + appcode);
		    Map<String, String> querys = new HashMap<String, String>();
		    querys.put("cardno", idcard);
		    querys.put("name", username);

		    String userInfo = "";
		    try {
		    	/**
		    	* 重要提示如下:
		    	* HttpUtils请从
		    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
		    	* 下载
		    	*
		    	* 相应的依赖请参照
		    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
		    	*/
		    	HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
//		    	System.out.println(response.toString());
		    	//获取response的body
		    	userInfo = EntityUtils.toString(response.getEntity());
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
		    return userInfo;
	}

	@Override
	public Response vip_info(Long loginUserid, HttpServletRequest request) {
		List<Ticket> tList = ticketDao.getTicketByTypeAndStatus("vip");
		if(tList != null && tList.size() > 0){
			Ticket t = tList.get(0);
			JSONObject tJson = new JSONObject();
			tJson.put("id", t.getId());
			tJson.put("price",t.getPrice());
			tJson.put("number", t.getNumber());
			return Response.status(Response.Status.OK).entity(tJson).build();
		}else{
			JSONObject resp = new JSONObject();
			resp.put("status", "vip价格为设置");
			resp.put("code", Integer.valueOf(10304));
			resp.put("error_message", "vip价格为设置");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
		
	}

	@Override
	public JSONObject record_user(JSONObject user) throws Exception {
		JSONObject returnJson = new JSONObject();
		if(user != null){
			String identity_card = user.getString("identity_card");
			String username = user.getString("username");
			String password = user.getString("password");
			User u = userDao.getUserByIdcard(identity_card);
			if(u != null){
				JSONObject resp = new JSONObject();
				resp.put("id", u.getId());
				resp.put("level", u.getLevel());
				resp.put("type", 1);
				returnJson = CodeUtil.returnData(1000, "用户已存在", resp);
				log.info("如愿成功*************");
				elkRecordUser(u);
				log.info("如愿成功*************");
			}else{
				String userInfo = getUserInfoByIdCard(identity_card.trim(),username.trim());
				JSONObject idcardJson = JSONObject.fromObject(userInfo);
				JSONObject idcardResp = idcardJson.getJSONObject("resp");
				int code = idcardResp.getInt("code");
				if(code == 0){
					User uInfo = new User();
					JSONObject data = idcardJson.getJSONObject("data");
					uInfo.setIdentity_card(identity_card);
					uInfo.setUsername(username);
					String psw = CodeUtil.Encrypt(password);
					uInfo.setPassword(psw);
					uInfo.setTmp_pass(password);
					uInfo.setAddress(data.getString("address"));
					uInfo.setGender(data.getString("sex"));
					uInfo.setBirthday(data.getString("birthday"));
					uInfo.setLevel("normal");
					uInfo.setResource("record");
					uInfo.setStatus("enable");
					userDao.save(uInfo);
					Contacter c = new Contacter();
					c.setBirthday(uInfo.getBirthday());
					c.setGender(uInfo.getGender());
					c.setIdentity_card(uInfo.getIdentity_card());
					c.setName(uInfo.getUsername());
					c.setUser(uInfo);
					contacterDao.save(c);
					JSONObject resp = new JSONObject();
					resp.put("id", uInfo.getId());
					resp.put("level", uInfo.getLevel());
					resp.put("type", 0);
					resp.put("tmp_pass", uInfo.getTmp_pass());
					returnJson = CodeUtil.returnData(1000, "录入成功", resp);
					elkUser(uInfo);
					log.info("如愿成功*************");
					elkRecordUser(uInfo);
					log.info("如愿成功*************");
				}else{
					returnJson = CodeUtil.returnData(1002, "身份证无效", "");
				}
			}
			
		}else{
			returnJson = CodeUtil.returnData(1003, "参数不能为空", "");
		}
		return returnJson;
	}

	@Override
	public Response perfectUser(Long userId, JSONObject user, Long loginUserid) {

		try {
			System.out.println(user.toString());
			if (loginUserid.equals(userId)) {
				User u = userDao.get(userId);
				if (user.containsKey("phone") && !Strings.isNullOrEmpty(user.getString("phone"))) {
					String zone = user.getString("zone");
					String phone = user.getString("phone");
					String code = user.getString("code");
					
					String appkey = getClass().getResource("/../../META-INF/phone.json").getPath();

					JSONObject jsonObject = parseJson(appkey);
					String key = jsonObject.getString("appkey");

					String param = "appkey=" + key + "&phone=" + phone + "&zone=" + zone + "&&code=" + code;
					String result = "";
					result = requestData("https://webapi.sms.mob.com/sms/verify", param);
					if (!Strings.isNullOrEmpty(result)) {
						JSONObject json = JSONObject.fromObject(result);
						String status = json.get("status").toString();
						if (status.equals("200")) {
							u.setZone(zone);
							u.setPhone(phone);
							userDao.update(u);
							JSONObject j = new JSONObject();
							j.put("phone", phone);
							return Response.status(Response.Status.OK).entity(j).build();
						} else {
							if (status.equals("512")) {
								JSONObject j = new JSONObject();
								j.put("status", "验证失败");
								j.put("code", Integer.valueOf(10100));
								j.put("error_message", "服务器拒绝访问，或者拒绝操作");
								return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
							}
							if (status.equals("405")) {
								JSONObject j = new JSONObject();
								j.put("status", "验证失败");
								j.put("code", Integer.valueOf(10101));
								j.put("error_message", "求Appkey不存在或被禁用");
								return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
							}

							if (status.equals("406")) {
								JSONObject j = new JSONObject();
								j.put("status", "验证失败");
								j.put("code", Integer.valueOf(10101));
								j.put("error_message", "求Appkey不存在或被禁用");
								return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
							}
							if (status.equals("514")) {
								JSONObject j = new JSONObject();
								j.put("status", "验证失败");
								j.put("code", Integer.valueOf(10102));
								j.put("error_message", "权限不足");
								return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
							}
							if (status.equals("515")) {
								JSONObject j = new JSONObject();
								j.put("status", "验证失败");
								j.put("code", 10103);
								j.put("error_message", "服务器内部错误");
								return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
							}
							if (status.equals("517")) {
								JSONObject j = new JSONObject();
								j.put("status", "验证失败");
								j.put("code", Integer.valueOf(10104));
								j.put("error_message", "缺少必要的请求参数");
								return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
							}
							if (status.equals("518")) {
								JSONObject j = new JSONObject();
								j.put("status", "验证失败");
								j.put("code", Integer.valueOf(10105));
								j.put("error_message", "请求中用户的手机号格式不正确（包括手机的区号）");
								return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
							}
							if (status.equals("519")) {
								JSONObject j = new JSONObject();
								j.put("status", "验证失败");
								j.put("code", Integer.valueOf(10106));
								j.put("error_message", "请求发送验证码次数超出限制");
								return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
							}
							if (status.equals("468")) {
								JSONObject j = new JSONObject();
								j.put("status", "验证失败");
								j.put("code", Integer.valueOf(10111));
								j.put("error_message", "无效验证码");
								return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
							}

							if (status.equals("467")) {
								JSONObject j = new JSONObject();
								j.put("status", "验证失败");
								j.put("code", Integer.valueOf(10110));
								j.put("error_message", "请求校验验证码频繁");
								return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
							}

							JSONObject j = new JSONObject();
							j.put("status", "验证失败");
							j.put("code", Integer.valueOf(10109));
							j.put("error_message", "验证失败");
							return Response.status(Response.Status.BAD_REQUEST).entity(j).build();

						}
					} else {
						JSONObject j = new JSONObject();
						j.put("status", "短信验证失败");
						j.put("code", Integer.valueOf(10108));
						j.put("error_message", "shareSDK 报错");
						return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
					}
				} else {
					JSONObject jo = new JSONObject();
					jo.put("status", "request_invalid");
					jo.put("code", Integer.valueOf(10010));
					jo.put("error_message", "request is invalid");
					return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
				}

			}else{
				JSONObject jo = new JSONObject();
				jo.put("status", "不能修改该用户");
				jo.put("code", Integer.valueOf(10310));
				jo.put("error_message", "不能修改该用户");
				return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
			}

		} catch (Exception e) {
			e.printStackTrace();
			JSONObject json1 = new JSONObject();
			json1.put("status", "invalid_request");
			json1.put("code", Integer.valueOf(10010));
			json1.put("error_message", "异常信息");
			return Response.status(Response.Status.BAD_REQUEST).entity(json1).build();
		}

	
	}

	//推送到ELK 
	@Override
	public JSONObject verification_ticket(HttpServletRequest request) throws Exception {
		String otid = request.getParameter("orders_ticket_id");
		if(!Strings.isNullOrEmpty(otid)){
			try{
				Long orders_ticket_id = Long.parseLong(otid);
				OrdersTicket ot = ordersTicketDao.get(orders_ticket_id);
				ot.setStatus(1);
				ordersTicketDao.update(ot);
				JSONObject result = CodeUtil.returnData(1000, "检票成功", "");
				elkCheckTicket(ot);
				return result;
			}catch (Exception e) {
				JSONObject result = CodeUtil.returnData(1008, "检票失败", "");
				return result;
			}
			
		}else{
			JSONObject result = CodeUtil.returnData(1010, "参数不正确", "");
			return result;
		}
	}

	@Override
	public JSONObject record_vip(JSONObject user) {

		JSONObject returnJson = new JSONObject();
		if(user != null){
			String identity_card = user.getString("identity_card");
			String username = user.getString("username");
			String password = user.getString("password");
			User u = userDao.getUserByIdcard(identity_card);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
			String date = sdf.format(new Date());
			Random random = new Random();
			String randStr = String.valueOf(100000 + random.nextInt(899999));
			String order_no = "o" + date + randStr;
			if(u != null){
				if(u.getLevel().equals("normal")){
					List<Ticket> tList = ticketDao.getTicketByTypeAndStatus("vip");
					if(tList != null && tList.size() > 0){
						Ticket t = tList.get(0);
						int number = t.getNumber();
						if(number > 0){
							Orders order = new Orders();
							order.setOrder_no(order_no);
							BigDecimal bd = t.getPrice();
							order.setAmount(bd);
							order.setStatus(6);
							order.setUser(u);
							ordersDao.save(order);
							List<Contacter> cList = contacterDao.getContacterByIdentityCard(u.getIdentity_card(), u.getId());
							OrdersTicket ot = new OrdersTicket();
							ot.setOrders(order);
							ot.setTicket(t);
							String ticket_order_no = "sub" + date + randStr;
							ot.setTicket_order_no(ticket_order_no);
							Contacter c = null;
							if(cList != null && cList.size() > 0){
								c = cList.get(0);
								
							}else{
								c = new Contacter();
								c.setIdentity_card(u.getIdentity_card());
								c.setBirthday(u.getBirthday());
								c.setGender(u.getGender());
								c.setName(u.getUsername());
								c.setUser(u);
								contacterDao.save(c);
							}
							String checking_rule = t.getChecking_rule();
							Poi poi = t.getPoi();
							String verify = "";
							if(checking_rule.equals("once")){
								verify = QRCodeHelper.createQRString(poi.getId().toString(), t.getId().toString(), "00000000", c.getIdentity_card(), "01");
							}else if(checking_rule.equals("more")){
								verify = QRCodeHelper.createQRString(poi.getId().toString(), t.getId().toString(), "00000000", c.getIdentity_card(), "02");
							}
							verify = verify.substring(verify.length()-6, verify.length());
							ot.setVerification(verify);
							ot.setContacter(c);
							ot.setNum(1);
							ot.setStatus(8);
							ot.setTotal(t.getPrice());
							ot.setUpdate_time(new Date());
							ordersTicketDao.save(ot);
							u.setLevel("vip");
							userDao.update(u);
							List<OrdersTicket> otList = ordersTicketDao.getOrdersTicketListByIdcardAndStatus(u.getIdentity_card());
							List<JSONObject> otJsonList = new ArrayList<JSONObject>();
							if(otList != null && otList.size() > 0){
								JSONObject otJson = null;
								for(OrdersTicket ots:otList){
									otJson = new JSONObject();
									Ticket ticket = ots.getTicket();
//									Classify classify = ticket.getPoi().getClassify();
									if(ticket.getType().equals("vip")){
										otJson.put("ticket_name", ticket.getTicket_name());
										otJson.put("order_no", ots.getOrders().getOrder_no());
										otJson.put("total", ots.getTotal());
										otJson.put("identity_card", ots.getContacter().getIdentity_card());
										otJson.put("username", ots.getContacter().getName());
										otJson.put("verification", ots.getVerification());
										otJson.put("poi_id", poi.getId());
										otJson.put("ticket_id", ticket.getId());
										otJson.put("checking_rule", checking_rule);
										otJsonList.add(otJson);
									}
								}
							}
							JSONObject tJson = new JSONObject();
							tJson.put("tickets", otJsonList);
							tJson.put("user_id", u.getId());
							tJson.put("level", u.getLevel());
							tJson.put("type", 1);
							tJson.put("tmp_pass", u.getTmp_pass());
							returnJson = CodeUtil.returnData(1000, "VIP升级成功", tJson);
						}else{
							returnJson = CodeUtil.returnData(1000, "VIP库存不足", "");
						}
					}else{
						returnJson = CodeUtil.returnData(1000, "没有对应的票", "");
					}
				}else{
					List<OrdersTicket> otList = ordersTicketDao.getOrdersTicketListByIdcardAndStatus(u.getIdentity_card());
					List<JSONObject> otJsonList = new ArrayList<JSONObject>();
					if(otList != null && otList.size() > 0){
						JSONObject otJson = null;
						for(OrdersTicket ots:otList){
							otJson = new JSONObject();
							Ticket ticket = ots.getTicket();
//							Classify classify = ticket.getPoi().getClassify();
							if(ticket.getType().equals("vip")){
								otJson.put("ticket_name", ticket.getTicket_name());
								otJson.put("order_no", ots.getOrders().getOrder_no());
								otJson.put("total", ots.getTotal());
								otJson.put("identity_card", ots.getContacter().getIdentity_card());
								otJson.put("username", ots.getContacter().getName());
								otJson.put("verification", ots.getVerification());
								otJson.put("poi_id", ticket.getPoi().getId());
								otJson.put("ticket_id", ticket.getId());
								otJson.put("checking_rule", ticket.getChecking_rule());
								otJsonList.add(otJson);
							}
						}
					}
					JSONObject tJson = new JSONObject();
					tJson.put("tickets", otJsonList);
					tJson.put("user_id", u.getId());
					tJson.put("level", u.getLevel());
					tJson.put("type", 2);
					tJson.put("tmp_pass", u.getTmp_pass());
					returnJson = CodeUtil.returnData(1000, "该用户已是VIP用户", tJson);
				}
			}else{
				String userInfo = getUserInfoByIdCard(identity_card.trim(),username.trim());
				JSONObject idcardJson = JSONObject.fromObject(userInfo);
				JSONObject idcardResp = idcardJson.getJSONObject("resp");
				int code = idcardResp.getInt("code");
				if(code == 0){
					User uInfo = new User();
					JSONObject data = idcardJson.getJSONObject("data");
					uInfo.setIdentity_card(identity_card);
					uInfo.setUsername(username);
					String psw = CodeUtil.Encrypt(password);
					uInfo.setPassword(psw);
					uInfo.setTmp_pass(password);
					uInfo.setAddress(data.getString("address"));
					uInfo.setGender(data.getString("sex"));
					uInfo.setBirthday(data.getString("birthday"));
					uInfo.setLevel("vip");
					uInfo.setResource("record");
					uInfo.setStatus("enable");
//					userDao.save(uInfo);
//					Contacter c = new Contacter();
//					c.setBirthday(uInfo.getBirthday());
//					c.setGender(uInfo.getGender());
//					c.setIdentity_card(uInfo.getIdentity_card());
//					c.setName(uInfo.getUsername());
//					c.setUser(uInfo);
//					contacterDao.save(c);
					

					List<Ticket> tList = ticketDao.getTicketByTypeAndStatus("vip");
					if(tList != null && tList.size() > 0){
						Ticket t = tList.get(0);
						int number = t.getNumber();
						if(number > 0){
							userDao.save(uInfo);
							Contacter c = new Contacter();
							c.setBirthday(uInfo.getBirthday());
							c.setGender(uInfo.getGender());
							c.setIdentity_card(uInfo.getIdentity_card());
							c.setName(uInfo.getUsername());
							c.setUser(uInfo);
							contacterDao.save(c);
							Orders order = new Orders();
							order.setOrder_no(order_no);
							BigDecimal bd = t.getPrice();
							order.setAmount(bd);
							order.setStatus(6);
							order.setUser(uInfo);
							ordersDao.save(order);
							OrdersTicket ot = new OrdersTicket();
							ot.setOrders(order);
							ot.setTicket(t);
							String checking_rule = t.getChecking_rule();
							Poi poi = t.getPoi();
							String verify = "";
							if(checking_rule.equals("once")){
								verify = QRCodeHelper.createQRString(poi.getId().toString(), t.getId().toString(), "00000000", c.getIdentity_card(), "01");
							}else if(checking_rule.equals("more")){
								verify = QRCodeHelper.createQRString(poi.getId().toString(), t.getId().toString(), "00000000", c.getIdentity_card(), "02");
							}
							verify = verify.substring(verify.length()-6, verify.length());
							ot.setVerification(verify);
							String ticket_order_no = "sub" + date + randStr;
							ot.setTicket_order_no(ticket_order_no);
							ot.setContacter(c);
							ot.setNum(1);
							ot.setStatus(8);
							ot.setTotal(t.getPrice());
							ot.setUpdate_time(new Date());
							ordersTicketDao.save(ot);
//							List<OrdersTicket> otList = ordersTicketDao.getOrdersTicketListByIdcard(uInfo.getIdentity_card());
//							List<JSONObject> otJsonList = new ArrayList<JSONObject>();
//							if(otList != null && otList.size() > 0){
//								JSONObject otJson = null;
//								for(OrdersTicket ots:otList){
//									otJson = new JSONObject();
//									Ticket ticket = ots.getTicket();
////									Classify classify = ticket.getPoi().getClassify();
//									if(ticket.getType().equals("vip")){
//										
//									}
//								}
//							}
							List<JSONObject> otJsonList = new ArrayList<JSONObject>();
							JSONObject otJson = new JSONObject();
							otJson.put("ticket_name", t.getTicket_name());
							otJson.put("order_no", ot.getOrders().getOrder_no());
							otJson.put("total", ot.getTotal());
							otJson.put("identity_card", ot.getContacter().getIdentity_card());
							otJson.put("username", ot.getContacter().getName());
							otJson.put("verification", ot.getVerification());
							otJson.put("poi_id", t.getPoi().getId());
							otJson.put("ticket_id", t.getId());
							otJson.put("checking_rule", t.getChecking_rule());
							otJsonList.add(otJson);
							JSONObject tJson = new JSONObject();
							tJson.put("tickets", otJsonList);
							tJson.put("user_id", uInfo.getId());
							tJson.put("level", uInfo.getLevel());
							tJson.put("type", 0);
							tJson.put("tmp_pass", uInfo.getTmp_pass());
							
							returnJson = CodeUtil.returnData(1000, "VIP用户录入成功", tJson);
							elkUser(uInfo);
						}else{
							returnJson = CodeUtil.returnData(1000, "VIP库存不足", "");
						}
					}else{
						returnJson = CodeUtil.returnData(1000, "没有对应的票", "");
					}
				
				}else{
					returnJson = CodeUtil.returnData(1002, "身份证无效", "");
				}
			}
			
		}else{
			returnJson = CodeUtil.returnData(1003, "参数不能为空", "");
		}
		return returnJson;
	
	}

	@Override
	public Response barcode(JSONObject param) {
		JSONObject jo = new JSONObject();
		if(param != null){
			String identity_card = param.getString("identity_card");
			User user = userDao.getUserByIdcard(identity_card);
			String barcode = "";
			if(user != null){
				String level = user.getLevel();
				
				if(level.equals("normal")){
					barcode = QRCodeHelper.createPersonQRString(user.getId().toString(), "01", user.getIdentity_card());
				}else if(level.equals("vip")){
					barcode = QRCodeHelper.createPersonQRString(user.getId().toString(), "02", user.getIdentity_card());
				}
			}
			jo.put("barcode", barcode);
			return Response.status(Response.Status.OK).entity(jo).build();
		}else{
			
			jo.put("status", "不能修改该用户");
			jo.put("code", Integer.valueOf(10310));
			jo.put("error_message", "不能修改该用户");
			return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
		}
	}

	@Override
	public JSONObject verification_user(HttpServletRequest request) throws Exception {
		JSONObject returnJson = null;
		String barcode = request.getParameter("barcode");
		if(!Strings.isNullOrEmpty(barcode)){
			String idcard = barcode.substring(6, barcode.length()-8);
			String one = idcard.substring(0,2);
			System.out.println(one);
			String a = Integer.parseInt(one, 32)+"";
			if(a.length() == 1){
				a = "00"+a;
			}else if(a.length() == 2){
				a = "0"+a;
			}
			String second = idcard.substring(2,4);
			String b = Integer.parseInt(second, 32)+"";
			if(b.length() == 1){
				b = "00"+b;
			}else if(b.length() == 2){
				b = "0"+b;
			}
			String thrid = idcard.substring(4,6);
			String c = Integer.parseInt(thrid, 32)+"";
			if(c.length() == 1){
				c = "00"+c;
			}else if(c.length() == 2){
				c = "0"+c;
			}
			String four = idcard.substring(6,8);
			String d = Integer.parseInt(four, 32)+"";
			System.out.println(d);
			if(d.length() == 1){
				d = "00"+d;
			}else if(d.length() == 2){
				d = "0"+d;
			}
			String five = idcard.substring(8,10);
			String e = Integer.parseInt(five,32)+"";
			if(e.length() == 1){
				e = "00"+e;
			}else if(e.length() == 2){
				e = "0"+e;
			}
			String f = idcard.substring(10,13);
			String identity_card = a+b+c+d+e+f;
			User user = userDao.getUserByIdcard(identity_card);
			JSONObject uJson = new JSONObject();
			if(user != null){
				uJson.put("id", user.getId());
				uJson.put("username", user.getUsername());
				uJson.put("identity_card",user.getIdentity_card());
				uJson.put("level",user.getLevel());
				uJson.put("tmp_pass",user.getTmp_pass());
				returnJson = CodeUtil.returnData(1000, "查询成功", uJson);
				log.info("如愿成功*************");
				elkRecordUser(user);
				log.info("如愿成功*************");
			}else{
				returnJson = CodeUtil.returnData(1015, "该用户不存在", "");
			}
		}else{
			returnJson = CodeUtil.returnData(1003, "参数不能为空", "");
		}
		return returnJson;
	}

	@Override
	public JSONObject material(JSONObject user) {
		JSONObject returnJson = null;
		if(user != null){
			String identity_card = user.getString("identity_card");
			String username = user.getString("username");
			User u = userDao.getUserByIdcard(identity_card);
			if(u != null){
				List<OrdersTicket> otList = ordersTicketDao.getOrdersTicketListByIdcardAndOnlineAndOffline(u.getIdentity_card());
				List<JSONObject> otJsonUnuseList = new ArrayList<JSONObject>();
				List<JSONObject> otJsonUsedList = new ArrayList<JSONObject>();
				if(otList != null && otList.size() > 0){
					JSONObject otJson = null;
					for(OrdersTicket ots:otList){
						otJson = new JSONObject();
						Ticket ticket = ots.getTicket();
						Classify classify = ticket.getPoi().getClassify();
						if(classify.getType().equals("parking") || ticket.getType().equals("vip")){
							if(ots.getStatus() == 1){
								otJson.put("order_ticket_id", ots.getId());
								otJson.put("order_ticket_no", ots.getTicket_order_no());
								otJson.put("ticket_name", ticket.getTicket_name());
								otJson.put("status", ots.getStatus());
								if(classify.getType().equals("parking")){
									otJson.put("type", "parking");
								}else{
									otJson.put("type", ticket.getType());
								}
								otJsonUsedList.add(otJson);
							}else{
								otJson.put("order_ticket_id", ots.getId());
								otJson.put("order_ticket_no", ots.getTicket_order_no());
								otJson.put("ticket_name", ticket.getTicket_name());
								otJson.put("status", ots.getStatus());
								if(classify.getType().equals("parking")){
									otJson.put("type", "parking");
								}else{
									otJson.put("type", ticket.getType());
								}
								otJsonUnuseList.add(otJson);
							}
							
						}
						if(otJsonUsedList != null && otJsonUsedList.size() > 0){
							for(JSONObject jo:otJsonUsedList){
								otJsonUnuseList.add(jo);
							}
						}
					}
				}
				JSONObject tJson = new JSONObject();
				tJson.put("tickets", otJsonUnuseList);
				tJson.put("user_id", u.getId());
				tJson.put("level", u.getLevel());
				returnJson = CodeUtil.returnData(1000, "查询成功", tJson);
			}else{

				List<OrdersTicket> otList = ordersTicketDao.getOrdersTicketListByIdcardAndOnlineAndOffline(identity_card);
				List<JSONObject> otJsonUnuseList = new ArrayList<JSONObject>();
				List<JSONObject> otJsonUsedList = new ArrayList<JSONObject>();
				if(otList != null && otList.size() > 0){
					JSONObject otJson = null;
					for(OrdersTicket ots:otList){
						otJson = new JSONObject();
						Ticket ticket = ots.getTicket();
						Classify classify = ticket.getPoi().getClassify();
						if(classify.getType().equals("parking") || ticket.getType().equals("vip")){
							if(ots.getStatus() == 1){
								otJson.put("order_ticket_id", ots.getId());
								otJson.put("order_ticket_no", ots.getTicket_order_no());
								otJson.put("ticket_name", ticket.getTicket_name());
								otJson.put("status", ots.getStatus());
								if(classify.getType().equals("parking")){
									otJson.put("type", "parking");
								}else{
									otJson.put("type", ticket.getType());
								}
								otJsonUsedList.add(otJson);
							}else{
								otJson.put("order_ticket_id", ots.getId());
								otJson.put("order_ticket_no", ots.getTicket_order_no());
								otJson.put("ticket_name", ticket.getTicket_name());
								otJson.put("status", ots.getStatus());
								if(classify.getType().equals("parking")){
									otJson.put("type", "parking");
								}else{
									otJson.put("type", ticket.getType());
								}
								otJsonUnuseList.add(otJson);
							}
							
						}
						if(otJsonUsedList != null && otJsonUsedList.size() > 0){
							for(JSONObject jo:otJsonUsedList){
								otJsonUnuseList.add(jo);
							}
						}
					}
				}
				JSONObject tJson = new JSONObject();
				tJson.put("tickets", otJsonUnuseList);
				tJson.put("user_id", 0);
				tJson.put("level","游客");
				returnJson = CodeUtil.returnData(1000, "查询成功", tJson);
			
			}
			
		}else{
			returnJson = CodeUtil.returnData(1038, "参数不能为空", "");
		}
		return returnJson;
	}

	@Override
	public JSONObject verification_material(JSONObject orders_ticket) {
		JSONObject returnJson = null;
		if(orders_ticket != null){
			String order_ticket_no = orders_ticket.getString("order_ticket_no");
			if(!Strings.isNullOrEmpty(order_ticket_no)){
				OrdersTicket ot = ordersTicketDao.getOrdersTicketByOrdersNo(order_ticket_no);
				int status = ot.getStatus();
				if(status == 0 || status == 8){
					ot.setStatus(1);
					ordersTicketDao.update(ot);
					returnJson = CodeUtil.returnData(1000, "核销成功", "");
					elkCheckTicket(ot);
				}else{
					returnJson = CodeUtil.returnData(1000, "已核销", "");
				}
			}else{
				returnJson = CodeUtil.returnData(1038, "参数不能为空", "");
			}
		}else{
			returnJson = CodeUtil.returnData(1038, "参数不能为空", "");
		}
		return returnJson;
	}
	
	public void elkUser(User u){
		JSONObject uJson = new JSONObject();
		uJson.put("user_id", u.getId());
		uJson.put("username", u.getUsername());
		uJson.put("gender", u.getGender());
		uJson.put("fbclub", u.getClub_name());
		
		uJson.put("channel", u.getResource());
		uJson.put("phone", u.getPhone());
		uJson.put("ID_card", u.getIdentity_card());
		String province = "";
		if(!Strings.isNullOrEmpty(u.getIdentity_card())){
			province = IdcardUtils.getProvinceByIdCard(u.getIdentity_card());
		}
		uJson.put("province_name", province);
		uJson.put("birthday", u.getBirthday());
		uJson.put("register_date", u.getCreated_time());
		if(u.getLevel().equals("vip")){
			uJson.put("is_VIP",true);
		}else if(u.getLevel().equals("normal")){
			uJson.put("is_VIP",false);
		}
		
		String resource = u.getResource();
		if(!Strings.isNullOrEmpty(resource)){
			if(resource.equals("fblife")){
				uJson.put("is_FBMumber", true);
			}else{
				uJson.put("is_FBMumber", false);
			}
		}
		
		try {
			String path = getClass().getResource("/../../META-INF/elk.json").getPath();
			JSONObject hotel = ParseFile.parseJson(path);
			String url = hotel.getString("user");
			HttpUtils.doPost("admin", "admin1234", url, uJson);
		} catch (Exception e) {
			
		}
	}
	
	public void elkOrders(Orders o,List<OrdersTicket> otList,String channel){
		JSONObject sendELK = new JSONObject();
		if(otList != null && otList.size() > 0){
			OrdersTicket ot = otList.get(0);
			Ticket t = ot.getTicket();
			Poi p = t.getPoi();
			sendELK.put("order_id", o.getId());
			sendELK.put("order_no", o.getOrder_no());
			sendELK.put("amount", o.getAmount());
			sendELK.put("create_time", o.getCreate_time());
			sendELK.put("update_time", (new Date()).getTime() / 1000);
			sendELK.put("status", o.getStatus());
			sendELK.put("poi_id", p.getId());
			sendELK.put("poi_name", p.getTitle());
			sendELK.put("ticket_id", t.getId());
			sendELK.put("ticket_name", t.getTicket_name());
			sendELK.put("num", otList.size());
			sendELK.put("price", t.getPrice());
			sendELK.put("channel", channel);
			sendELK.put("key", 1);
			try {
				String path = getClass().getResource("/../../META-INF/elk.json").getPath();
				JSONObject hotel = ParseFile.parseJson(path);
				String url = hotel.getString("orders");
				HttpUtils.doPost("admin", "admin1234", url, sendELK);
				//HttpUtils.doPost("admin", "admin1234", "http://172.17.196.150:3333", sendELK);
			} catch (Exception e) {
				
			}
		}
		
	}
	
	public void elkRecordUser(User u){
		JSONObject sendELK = new JSONObject();
		sendELK.put("user_id", u.getId());
		sendELK.put("user_name", u.getUsername());
		sendELK.put("gender", u.getGender());
		sendELK.put("phone", u.getPhone());
		sendELK.put("ID_card", u.getIdentity_card());
		sendELK.put("comming_date", ((new Date()).getTime() / 1000));
		try {
			String path = getClass().getResource("/../../META-INF/elk.json").getPath();
			JSONObject hotel = ParseFile.parseJson(path);
			String url = hotel.getString("record_user");
			HttpUtils.doPost("admin", "admin1234", url, sendELK);
//			HttpUtils.doPost("admin", "admin1234", "http://172.17.196.150:3335", sendELK);
		} catch (Exception e) {
		}
	}
	
	public void elkCheckTicket(OrdersTicket ot){
		JSONObject uJson = new JSONObject();
		Contacter c = ot.getContacter();
		Ticket t = ot.getTicket();
		Poi p = t.getPoi();
		List<ParkMap> pmList = parkMapDao.getParkMapList("enable");
		if(pmList != null && pmList.size() > 0){
			ParkMap pm = pmList.get(0);
			String pointA = pm.getPointA();
			String pointB = pm.getPointB();
			String coordinateA = pm.getCoordinateA(); 
			String coordinateB = pm.getCoordinateB();
			JSONObject pA = JSONObject.fromObject(pointA);
			String lngA = pA.getString("lng");
			double lngAd = Double.valueOf(lngA);
			String latA = pA.getString("lat");
			double latAd = Double.valueOf(latA);
			JSONObject pB = JSONObject.fromObject(pointB);
			String lngB = pB.getString("lng");
			double lngBd = Double.valueOf(lngB);
			String latB = pB.getString("lat");
			double latBd = Double.valueOf(latB);
			
			JSONObject cA = JSONObject.fromObject(coordinateA);
			String coordinateAX = cA.getString("coordinateX");
			double cAX = Double.valueOf(coordinateAX);
			String coordinateAY = cA.getString("coordinateY");
			double cAY = Double.valueOf(coordinateAY);
			
			
			JSONObject cB = JSONObject.fromObject(coordinateB);
			String coordinateBX = cB.getString("coordinateX");
			double cBX = Double.valueOf(coordinateBX);
			String coordinateBY = cB.getString("coordinateY");
			double cBY = Double.valueOf(coordinateBY);
			
			
			JSONObject cC = JSONObject.fromObject(p.getLocation());
			String coordinateCX = cC.getString("lat");
			double cCX = Double.valueOf(coordinateCX);
			String coordinateCY = cC.getString("lng");
			double cCY = Double.valueOf(coordinateCY);
			double lngc = (cCX - cAX) * Math.abs((lngBd - lngAd) / (cBX - cAX)) + lngAd;
			double latc = (cCY - cAY) * Math.abs((latBd - latAd) / (cBY - cAY)) + latAd;
			JSONArray ja = new JSONArray();
			ja.add(lngc);
			ja.add(latc);
			uJson.put("location", ja);
		}
		uJson.put("poi_id", p.getId());
		uJson.put("poi_name", p.getTitle());
		uJson.put("poi_type", p.getClassify().getType());
		uJson.put("type_id", t.getId());
		uJson.put("type_name", t.getTicket_name());
		uJson.put("price", t.getPrice());
		uJson.put("type_name", t.getTicket_name());
		uJson.put("user_name", c.getName());
		uJson.put("gender", c.getGender());
		uJson.put("ID_card", c.getIdentity_card());
		String province = IdcardUtils.getProvinceByIdCard(c.getIdentity_card());
		uJson.put("province_name", province);
		uJson.put("birthday", c.getBirthday());
		uJson.put("checking_time", (new Date()).getTime() / 1000);
		try {
			String path = getClass().getResource("/../../META-INF/elk.json").getPath();
			JSONObject hotel = ParseFile.parseJson(path);
			String url = hotel.getString("check_ticket");
			HttpUtils.doPost("admin", "admin1234", url, uJson);
//			HttpUtils.doPost("admin", "admin1234", "http://172.17.196.150:3332", uJson);
		} catch (Exception e) {
			
		}
	}

	@Override
	public Response bind_wechat(Long loginUserid, JSONObject link) {
		JSONObject resp = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			if(link != null){
				Object[] oArr = linkAccountsDao.getLinkAccountsByUUID(link.getString("union_id"));
				if(oArr != null && oArr.length > 1){
					resp.put("status", "该微信号已绑定其他用户");
					resp.put("code", Integer.valueOf(10510));
					resp.put("error_message", "该微信号已绑定其他用户");
					return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
				}else{
					LinkAccounts la = new LinkAccounts();
					la.setAuth_token(link.getString("auth_token"));
					la.setUser_id(loginUserid);
					la.setUnion_id(link.getString("union_id"));
					linkAccountsDao.save(la);
					resp.put("status", "success");
					return Response.status(Response.Status.OK).entity(resp).build();
				}
				
			
			}else{
				resp.put("status", "验证失败");
				resp.put("code", Integer.valueOf(10104));
				resp.put("error_message", "缺少必要的请求参数");
				return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
			}
			
		} else {
			resp.put("status", "用户未登录");
			resp.put("code", Integer.valueOf(10010));
			resp.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	@Override
	public String synchronousUser(HttpServletRequest request) {
		String create_time = request.getParameter("create_time");
		List<User> uList = userDao.getUserList(create_time);
		if(uList != null && uList.size() > 0){
			for(User u:uList){
				elkUser(u);
			}
		}
		return "success";
	}

	@Override
	public String synchronousOrder(HttpServletRequest request) {
		String create_time = request.getParameter("create_time");
		List<Orders> oList = ordersDao.getOrdersByCreateTime(create_time);
		if(oList != null && oList.size() > 0){
			for(Orders o:oList){
				List<OrdersTicket> otList = o.getOrdersTickets();
				elkOrders(o, otList, "online");
			}
		}
		return "success";
	}
	
	

}
