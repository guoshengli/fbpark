package com.fbpark.rest.service;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;

import com.fbpark.rest.common.CodeUtil;
import com.fbpark.rest.common.EncryptionUtil;
import com.fbpark.rest.common.HttpUtil;
import com.fbpark.rest.common.ParseFile;
import com.fbpark.rest.common.PushNotificationUtil;
import com.fbpark.rest.common.ZybUtil;
import com.fbpark.rest.dao.AdDao;
import com.fbpark.rest.dao.AdminDao;
import com.fbpark.rest.dao.AdminGroupDao;
import com.fbpark.rest.dao.ClassifyDao;
import com.fbpark.rest.dao.ContentDao;
import com.fbpark.rest.dao.ContentElementDao;
import com.fbpark.rest.dao.GroupDao;
import com.fbpark.rest.dao.GroupPrivilegeDao;
import com.fbpark.rest.dao.HelpDao;
import com.fbpark.rest.dao.LinkAccountsDao;
import com.fbpark.rest.dao.MenuDao;
import com.fbpark.rest.dao.NewsDao;
import com.fbpark.rest.dao.NoticeDao;
import com.fbpark.rest.dao.NotificationDao;
import com.fbpark.rest.dao.OrdersDao;
import com.fbpark.rest.dao.OrdersTicketDao;
import com.fbpark.rest.dao.ParkMapDao;
import com.fbpark.rest.dao.PoiDao;
import com.fbpark.rest.dao.PoiElementDao;
import com.fbpark.rest.dao.PushNotificationDao;
import com.fbpark.rest.dao.RecommandationDao;
import com.fbpark.rest.dao.SendMessageDao;
import com.fbpark.rest.dao.SettingDao;
import com.fbpark.rest.dao.SlideDao;
import com.fbpark.rest.dao.TemplateDao;
import com.fbpark.rest.dao.TicketDao;
import com.fbpark.rest.dao.UserDao;
import com.fbpark.rest.model.Ad;
import com.fbpark.rest.model.Admin;
import com.fbpark.rest.model.AdminGroup;
import com.fbpark.rest.model.Classify;
import com.fbpark.rest.model.Contacter;
import com.fbpark.rest.model.Content;
import com.fbpark.rest.model.ContentElement;
import com.fbpark.rest.model.Group;
import com.fbpark.rest.model.GroupPrivilege;
import com.fbpark.rest.model.Help;
import com.fbpark.rest.model.LinkAccounts;
import com.fbpark.rest.model.Menu;
import com.fbpark.rest.model.News;
import com.fbpark.rest.model.Notice;
import com.fbpark.rest.model.Notification;
import com.fbpark.rest.model.Orders;
import com.fbpark.rest.model.OrdersTicket;
import com.fbpark.rest.model.ParkMap;
import com.fbpark.rest.model.Poi;
import com.fbpark.rest.model.PoiElement;
import com.fbpark.rest.model.PushNotification;
import com.fbpark.rest.model.Recommandation;
import com.fbpark.rest.model.RecommandationId;
import com.fbpark.rest.model.SendMessage;
import com.fbpark.rest.model.Setting;
import com.fbpark.rest.model.Slide;
import com.fbpark.rest.model.Template;
import com.fbpark.rest.model.Ticket;
import com.fbpark.rest.model.User;
import com.fbpark.rest.redis.MyRedisDao;
import com.fbpark.rest.service.model.GetuiModel;
import com.fbpark.rest.service.model.Zyb;
import com.google.common.base.Strings;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.APIConnectionException;
import com.pingplusplus.exception.APIException;
import com.pingplusplus.exception.AuthenticationException;
import com.pingplusplus.exception.ChannelException;
import com.pingplusplus.exception.InvalidRequestException;
import com.pingplusplus.exception.RateLimitException;
import com.pingplusplus.model.Refund;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

@Transactional
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminDao adminDao;

	@Autowired
	private GroupDao groupDao;

	@Autowired
	private HelpDao helpDao;

	@Autowired
	private NewsDao newsDao;

	@Autowired
	private GroupPrivilegeDao groupPrivilegeDao;

	@Autowired
	private ClassifyDao classifyDao;

	@Autowired
	private PoiDao poiDao;

	@Autowired
	private MenuDao menuDao;

	@Autowired
	private PoiElementDao poiElementDao;

	@Autowired
	private ContentDao contentDao;

	@Autowired
	private ContentElementDao contentElementDao;

	@Autowired
	private RecommandationDao recommandationDao;

	@Autowired
	private AdminGroupDao adminGroupDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private ParkMapDao parkMapDao;

	@Autowired
	private LinkAccountsDao linkAccountsDao;

	@Autowired
	private SettingDao settingDao;

	@Autowired
	private OrdersDao ordersDao;

	@Autowired
	private OrdersTicketDao ordersTicketDao;

	@Autowired
	private SlideDao slideDao;

	@Autowired
	private AdDao adDao;

	@Autowired
	private TemplateDao templateDao;

	@Autowired
	private NotificationDao notificationDao;

	@Autowired
	private NoticeDao noticeDao;

	@Autowired
	private SendMessageDao sendMessageDao;

	@Autowired
	private PushNotificationDao pushNotificationDao;

	@Autowired
	private TicketDao ticketDao;

	@Autowired
	private MyRedisDao myRedisDao;

	@Override
	public Response signin(JSONObject user) {
		String username = user.getString("username");
		String password = user.getString("password");
		String timestamp = user.getString("timestamp");

		JSONObject jo = new JSONObject();
		JSONObject auth = new JSONObject();
		if (!Strings.isNullOrEmpty(username)) {
			if (Strings.isNullOrEmpty(password)) {
				jo.put("status", "invalid_password");
				jo.put("code", Integer.valueOf(10007));
				jo.put("error_message", "invalid password");
				return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
			}
			Admin admin = this.adminDao.getUserByUsername(username);

			if (admin != null) {
				try {
					admin = this.adminDao.getUserByUsernameAndPassword(username, password);

					if (admin == null) {
						jo.put("status", "用户不存在");
						jo.put("code", Integer.valueOf(10008));
						jo.put("error_message", "用户不存在");
						return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
					}

				} catch (Exception e) {
					jo.put("status", "invalid_password");
					jo.put("code", Integer.valueOf(10007));
					jo.put("error_message", "invalid password");
					return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
				}
			} else {
				jo.put("status", "用户名不存在");
				jo.put("code", Integer.valueOf(10006));
				jo.put("error_message", "用户名不存在");
				return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
			}

			String raw = admin.getId() + admin.getPassword() + timestamp;
			String token = EncryptionUtil.hashMessage(raw);
			Set<Group> groupSet = admin.getGroups();
			Set<Menu> menuSet = new HashSet<Menu>();
			if (groupSet != null && groupSet.size() > 0) {
				Group group = groupSet.iterator().next();
				auth.put("group_name", group.getGroup_name());
				for (Group g : groupSet) {
					menuSet.addAll(g.getMenus());
				}
			}
			System.out.println("userId--->" + admin.getId());
			auth.put("userid", admin.getId());
			auth.put("username", admin.getUsername());

			auth.put("access_token", token);
			auth.put("menus", menuSet);
			auth.put("token_timestamp", Long.valueOf(Long.parseLong(timestamp)));
		}

		System.out.println(auth.toString());
		return Response.status(Response.Status.OK).entity(auth).build();

	}

	@Override
	public Response add_admin(JSONObject user) {
		if (user != null) {
			Admin admin = new Admin();
			admin.setUsername(user.getString("username"));
			admin.setPassword(user.getString("password"));
			admin.setStatus("enable");
			Set<Group> groupSet = new HashSet<Group>();
			Group group = null;
			if (user.containsKey("group_id")) {
				Long group_id = user.getLong("group_id");
				group = groupDao.get(group_id);
				groupSet.add(group);
				if (group != null) {
					admin.setGroups(groupSet);
				}
			}

			adminDao.save(admin);
			if (user.containsKey("group_id")) {
				Long group_id = user.getLong("group_id");
				AdminGroup adminGroup = adminGroupDao.getAdminGroupByAdminIdAndGroupId(admin.getId(), group_id);
				if (adminGroup == null) {
					adminGroupDao.deleteAdminGroup(admin.getId());
					AdminGroup ag = new AdminGroup();
					ag.setAdmin(admin);
					ag.setGroup(group);
					adminGroupDao.save(ag);
				}

			}

			JSONObject json = new JSONObject();
			json.put("id", admin.getId());
			json.put("username", admin.getUsername());
			json.put("create_time", admin.getCreate_time());
			json.put("password", admin.getPassword());
			if (group != null) {
				List<JSONObject> groupJsonList = new ArrayList<JSONObject>();
				JSONObject gJson = new JSONObject();
				gJson.put("id", group.getId());
				gJson.put("group_name", group.getGroup_name());
				groupJsonList.add(gJson);
				json.put("groups", groupJsonList);
			}

			return Response.status(Response.Status.OK).entity(json).build();
		} else {
			JSONObject jo = new JSONObject();
			jo.put("status", "数据有误");
			jo.put("code", Integer.valueOf(10007));
			jo.put("error_message", "数据有误");
			return Response.status(Response.Status.BAD_REQUEST).entity(jo).build();
		}
	}

	@Override
	public Response adminList(HttpServletRequest request, Long loginUserid) {
		List<Admin> adminList = new ArrayList<Admin>();
		List<JSONObject> adminJSONList = new ArrayList<JSONObject>();
		JSONObject error = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			adminList = adminDao.getAdminList();
			JSONObject groupJson = null;
			List<JSONObject> groupJsonList = null;
			Set<Group> groups = null;
			if (adminList != null && adminList.size() > 0) {
				JSONObject adminJson = null;
				for (Admin a : adminList) {
					groupJsonList = new ArrayList<JSONObject>();
					adminJson = new JSONObject();
					adminJson.put("id", a.getId());
					adminJson.put("username", a.getUsername());
					adminJson.put("password", a.getPassword());
					adminJson.put("status", a.getStatus());
					groups = a.getGroups();
					if (groups != null && groups.size() > 0) {
						for (Group g : groups) {
							groupJson = new JSONObject();
							groupJson.put("group_id", g.getId());
							groupJson.put("group_name", g.getGroup_name());
							groupJsonList.add(groupJson);
						}
						adminJson.put("groups", groupJsonList);
					}

					adminJSONList.add(adminJson);
				}
			}

			return Response.status(Response.Status.OK).entity(adminJSONList).build();
		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}

	}

	// @Override
	// public Response updateAdmin(Long admin_id, Long loginUserid,JSONObject
	// admin) {
	// JSONObject error = new JSONObject();
	// if(loginUserid != null && loginUserid > 0){
	// Admin super_admin = adminDao.getAdminById(loginUserid);
	// error.put("status", "该用户不具有删除权限");
	// error.put("code", Integer.valueOf(10011));
	// error.put("error_message", "该用户不具有删除权限");
	// return
	// Response.status(Response.Status.BAD_REQUEST).entity(error).build();
	//
	// }else{
	// error.put("status", "用户未登录");
	// error.put("code", Integer.valueOf(10010));
	// error.put("error_message", "用户未登录");
	// return
	// Response.status(Response.Status.BAD_REQUEST).entity(error).build();
	// }
	// }

	@Override
	public Response user_list(HttpServletRequest request, Long loginUserid) {
		String countStr = request.getParameter("count");
		String pageStr = request.getParameter("page");
		String level = request.getParameter("level");
		String username = request.getParameter("username");
		String phone = request.getParameter("phone");
		String idcard = request.getParameter("idcard");
		String club_name = request.getParameter("club_name");
		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		String birthday_order = request.getParameter("birthday_order");
		String time_order = request.getParameter("time_order");
		int count = 10;
		int page = 1;
		JSONObject error = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			List<User> userList = null;
			JSONObject result = new JSONObject();
			List<JSONObject> userJSONList = new ArrayList<JSONObject>();
			if (!Strings.isNullOrEmpty(level)) {
				if ((Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
					userList = userDao.getUserListByLevel(count, page, level, phone, username, idcard,club_name,start_time,end_time,birthday_order,time_order);
					int total = userDao.getUserCountByLevel(level, phone, username, idcard,club_name,start_time,end_time);
					result.put("total", total);
				} else if ((!Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
					count = Integer.parseInt(countStr);
					userList = userDao.getUserListByLevel(count, page, level, phone, username, idcard,club_name,start_time,end_time,birthday_order,time_order);
					int total = userDao.getUserCountByLevel(level, phone, username, idcard,club_name,start_time,end_time);
					result.put("total", total);
				} else if ((Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
					page = Integer.parseInt(pageStr);
					userList = userDao.getUserListByLevel(count, page, level, phone, username, idcard,club_name,start_time,end_time,birthday_order,time_order);
				} else if ((!Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
					count = Integer.parseInt(countStr);
					page = Integer.parseInt(pageStr);
					userList = userDao.getUserListByLevel(count, page, level, phone, username, idcard,club_name,start_time,end_time,birthday_order,time_order);
				}

			} else {

				if ((Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
					userList = userDao.getUserList(count, page, phone, username, idcard,club_name,start_time,end_time,birthday_order,time_order);
					int total = userDao.getUserCount(phone, username, idcard,club_name,start_time,end_time);
					result.put("total", total);
				} else if ((!Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
					count = Integer.parseInt(countStr);
					userList = userDao.getUserList(count, page, phone, username, idcard,club_name,start_time,end_time,birthday_order,time_order);
					int total = userDao.getUserCount(phone, username, idcard,club_name,start_time,end_time);
					result.put("total", total);
				} else if ((Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
					page = Integer.parseInt(pageStr);
					userList = userDao.getUserList(count, page, phone, username, idcard,club_name,start_time,end_time,birthday_order,time_order);
				} else if ((!Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
					count = Integer.parseInt(countStr);
					page = Integer.parseInt(pageStr);
					userList = userDao.getUserList(count, page, phone, username, idcard,club_name,start_time,end_time,birthday_order,time_order);
				}
			}

			if (userList != null && userList.size() > 0) {
				JSONObject userJson = null;
				for (User u : userList) {
					userJson = getUserJson(u);
					userJSONList.add(userJson);

				}
			}

			result.put("users", userJSONList);
			return Response.status(Response.Status.OK).entity(result).build();
		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}
	}

	@Override
	public Response search_user(HttpServletRequest request, Long loginUserid) {
		String countStr = request.getParameter("count");
		String pageStr = request.getParameter("page");
		String username = request.getParameter("username");
		String phone = request.getParameter("phone");
		String idcard = request.getParameter("idcard");
		int count = 10;
		int page = 0;
		JSONObject error = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			List<User> userList = null;
			JSONObject result = new JSONObject();
			if (!Strings.isNullOrEmpty(username)) {
				if ((Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
					userList = userDao.getUserListByUsername(count, page, username);
					int total = userDao.getUserCountByUsername(username);
					result.put("total", total);
				} else if ((!Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
					count = Integer.parseInt(countStr);
					userList = userDao.getUserListByUsername(count, page, username);
					int total = userDao.getUserCountByUsername(username);
					result.put("total", total);
				} else if ((Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
					page = Integer.parseInt(pageStr);
					userList = userDao.getUserListByUsername(count, page, username);
				} else if ((!Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
					count = Integer.parseInt(countStr);
					page = Integer.parseInt(pageStr);
					userList = userDao.getUserListByUsername(count, page, username);
				}
			} else if (!Strings.isNullOrEmpty(phone)) {
				if ((Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
					userList = userDao.getUserListByPhone(count, page, phone);
					int total = userDao.getUserCountByPhone(phone);
					result.put("total", total);
				} else if ((!Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
					count = Integer.parseInt(countStr);
					userList = userDao.getUserListByPhone(count, page, phone);
					int total = userDao.getUserCountByPhone(phone);
					result.put("total", total);
				} else if ((Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
					page = Integer.parseInt(pageStr);
					userList = userDao.getUserListByPhone(count, page, phone);
				} else if ((!Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
					count = Integer.parseInt(countStr);
					page = Integer.parseInt(pageStr);
					userList = userDao.getUserListByPhone(count, page, phone);
				}
			} else if (!Strings.isNullOrEmpty(idcard)) {
				if ((Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
					userList = userDao.getUserListByIdcard(count, page, idcard);
					int total = userDao.getUserCountByIdcard(idcard);
					result.put("total", total);
				} else if ((!Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
					count = Integer.parseInt(countStr);
					userList = userDao.getUserListByIdcard(count, page, idcard);
					int total = userDao.getUserCountByIdcard(idcard);
					result.put("total", total);
				} else if ((Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
					page = Integer.parseInt(pageStr);
					userList = userDao.getUserListByIdcard(count, page, idcard);
				} else if ((!Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
					count = Integer.parseInt(countStr);
					page = Integer.parseInt(pageStr);
					userList = userDao.getUserListByIdcard(count, page, idcard);
				}
			}

			List<JSONObject> userJSONList = new ArrayList<JSONObject>();
			if (userList != null && userList.size() > 0) {
				JSONObject userJson = null;
				for (User u : userList) {
					userJson = new JSONObject();
					userJson.put("id", u.getId());
					userJson.put("username", u.getUsername());
					userJson.put("phone", u.getPhone());
					userJson.put("level", u.getLevel());
					List<LinkAccounts> linkList = linkAccountsDao.getLinkAccountsByUserid(u.getId());

					if (linkList != null && linkList.size() > 0) {
						Set<String> serviceSet = new HashSet<String>();
						// for(LinkAccounts la:linkList){
						// serviceSet.add(la.getService());
						// }
						if (serviceSet.size() == 1) {
							String service = serviceSet.iterator().next();
							if (service.equals("wechat")) {
								userJson.put("wechat", true);
								userJson.put("weibo", false);
							} else if (service.equals("weibo")) {
								userJson.put("wechat", false);
								userJson.put("weibo", true);
							}
						} else if (serviceSet.size() == 2) {
							userJson.put("wechat", true);
							userJson.put("weibo", true);
						}
					} else {
						userJson.put("wechat", false);
						userJson.put("weibo", false);
					}
					userJSONList.add(userJson);

				}
			}
			result.put("users", userJSONList);
			return Response.status(Response.Status.OK).entity(result).build();
		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}
	}

	@Override
	public Response add_poi(JSONObject poi, Long loginUserid) {
		JSONObject res = new JSONObject();
		if (poi != null) {
			Poi poiModel = new Poi();
			poiModel.setTitle(poi.getString("title"));
			poiModel.setAttention(poi.getString("attention"));
			if (poi.containsKey("content_id")) {
				poiModel.setContent_id(poi.getLong("content_id"));
			}

			// poiModel.setCover_image(poi.getString("cover_image"));
			poiModel.setLocation(poi.getString("location"));
			poiModel.setPlace(poi.getString("place"));
			if (poi.containsKey("price")) {
				poiModel.setPrice(BigDecimal.valueOf(poi.getDouble("price")));
			}
			if (poi.containsKey("subtitle")) {
				String subtitle = poi.getString("subtitle");
				if (!Strings.isNullOrEmpty(subtitle)) {
					poiModel.setSubtitle(subtitle);
				}
			}
			
			if (poi.containsKey("direct_sales") && !Strings.isNullOrEmpty(poi.getString("direct_sales"))) {
				String direct_sales = poi.getString("direct_sales");
				poiModel.setDirect_sales(direct_sales);
			}
			
			if (poi.containsKey("time_info")) {
				String time_info = poi.getString("time_info");
				if (!Strings.isNullOrEmpty(time_info)) {
					poiModel.setTime_info(time_info);
				}
			}

			Classify classify = null;
			if (poi.containsKey("classify_id")) {
				Long classify_id = poi.getLong("classify_id");
				if (classify_id != null && classify_id > 0) {
					classify = classifyDao.get(classify_id);
					poiModel.setClassify(classify);
				}
			}

			JSONArray ele = poi.getJSONArray("elements");
			if (ele != null && ele.size() > 0) {
				List<PoiElement> peList = new ArrayList<PoiElement>();
				PoiElement pe = null;
				for (Object obj : ele) {
					pe = new PoiElement();
					pe.setElement(obj.toString());
					pe.setPoi(poiModel);
					peList.add(pe);
				}
				poiModel.setElements(peList);
			}
			poiModel.setAdmin_id(loginUserid);
			poiModel.setUpdate_time(new Date());
			List<Ticket> tList = new ArrayList<Ticket>();
			if (!classify.getType().equals("normal")) {

				if (poi.containsKey("tickets")) {
					JSONArray arr = poi.getJSONArray("tickets");
					if (arr != null && arr.size() > 0) {
						for (Object o : arr) {
							Long t_id = Long.parseLong(o.toString());
							Ticket t = ticketDao.get(t_id);
							tList.add(t);
						}

						poiModel.setTickets(tList);
					}
				}
			}

			poiDao.save(poiModel);
			if (!classify.getType().equals("normal")) {
				if (poi.containsKey("tickets")) {
					if (tList != null && tList.size() > 0) {
						for (Ticket o : tList) {
							Long t_id = o.getId();
							ticketDao.updateTicket(t_id, poiModel.getId());
							;
						}

					}
				}
			}

			if (poi.containsKey("recommandations")) {
				JSONArray arr = poi.getJSONArray("recommandations");
				recommandationDao.deleteRecommandationByPoi(poiModel.getId());
				RecommandationId r = null;
				Recommandation re = null;
				Poi reference = null;
				if (arr != null && arr.size() > 0) {
					for (Object o : arr) {
						reference = poiDao.get(Long.parseLong(o.toString()));
						re = new Recommandation();
						r = new RecommandationId();
						r.setPoi(poiModel);
						r.setReference(reference);
						re.setPk(r);
						recommandationDao.save(re);
					}
				}
			}

			JSONObject result = getPoiJSON(poiModel);

			return Response.status(Response.Status.OK).entity(result).build();

		} else {
			res.put("status", "参数不能为空");
			res.put("code", Integer.valueOf(10008));
			res.put("error_message", "参数不能为空");
			return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
		}
	}

	@Override
	public Response deleteAdmin(Long admin_id, Long loginUserid) {
		JSONObject error = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			Admin a = adminDao.getAdminById(admin_id);
			a.setStatus("disable");
			adminDao.update(a);
			JSONObject json = new JSONObject();
			json.put("id", a.getId());
			return Response.status(Response.Status.OK).entity(json).build();
		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}
	}

	@Override
	public Response order_search(HttpServletRequest request, Long loginUserid) {
		String order_no = request.getParameter("order_no");
		if (!Strings.isNullOrEmpty(order_no)) {
			Orders o = ordersDao.getOrdersByOrderno(order_no);
			JSONObject orderJson = new JSONObject();
			orderJson.put("id", o.getId());
			orderJson.put("order_no", o.getOrder_no());
			orderJson.put("userid", o.getUser().getId());
			orderJson.put("username", o.getUser().getUsername());
			orderJson.put("create_time", o.getCreate_time());
			orderJson.put("status", o.getStatus());
			orderJson.put("amount", o.getAmount());
			return Response.status(Response.Status.OK).entity(orderJson).build();
		} else {
			JSONObject resp = new JSONObject();
			resp.put("status", "参数不能为空");
			resp.put("code", 10001);
			resp.put("error_message", "参数不能为空");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	@Override
	public Response order_list(HttpServletRequest request, Long loginUserid) {
		String countStr = request.getParameter("count");
		String pageStr = request.getParameter("page");
		String type = request.getParameter("status"); // 已付款 paid 未付款 unpaid
		String start_date = request.getParameter("start_date");
		String end_date = request.getParameter("end_date");
		String order_no = request.getParameter("order_no");
		int count = 10;
		int page = 0;
		JSONObject error = new JSONObject();
		List<Orders> orderList = null;
		if (loginUserid != null && loginUserid > 0) {

			JSONObject result = new JSONObject();
			if ((Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
				orderList = ordersDao.getOrdersListByParams(count, page, type, start_date, end_date, order_no);
				Object[] total = ordersDao.getOrdersCountAndAmountByParams(type, start_date, end_date, order_no);
				result.put("total_count", total[0]);
				if (total[1] != null) {
					result.put("total_amount", total[1]);
				} else {
					result.put("total_amount", 0);
				}

			} else if ((!Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
				count = Integer.parseInt(countStr);
				orderList = ordersDao.getOrdersListByParams(count, page, type, start_date, end_date, order_no);
				Object[] total = ordersDao.getOrdersCountAndAmountByParams(type, start_date, end_date, order_no);

				result.put("total_count", total[0]);
				if (total[1] != null) {
					result.put("total_amount", total[1]);
				} else {
					result.put("total_amount", 0);
				}
			} else if ((Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
				page = Integer.parseInt(pageStr);
				orderList = ordersDao.getOrdersListByParams(count, page, type, start_date, end_date, order_no);

			} else if ((!Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
				count = Integer.parseInt(countStr);
				page = Integer.parseInt(pageStr);
				orderList = ordersDao.getOrdersListByParams(count, page, type, start_date, end_date, order_no);
			}
			List<JSONObject> orderJSONList = new ArrayList<JSONObject>();
			if (orderList != null && orderList.size() > 0) {
				JSONObject orderJson = null;
				for (Orders o : orderList) {
					orderJson = new JSONObject();
					orderJson.put("id", o.getId());
					orderJson.put("order_no", o.getOrder_no());
					orderJson.put("userid", o.getUser().getId());
					orderJson.put("username", o.getUser().getUsername());
					orderJson.put("create_time", o.getCreate_time());
					orderJson.put("status", o.getStatus());
					orderJson.put("amount", o.getAmount());
					orderJson.put("pay_type", o.getPay_type());
					orderJSONList.add(orderJson);
				}
			}
			result.put("orders", orderJSONList);
			return Response.status(Response.Status.OK).entity(result).build();

		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}
	}

	@Override
	public Response add_group(JSONObject group) {
		JSONObject resp = new JSONObject();
		if (group != null) {
			Group g = new Group();
			g.setGroup_name(group.getString("group_name"));

			groupDao.save(g);
			JSONArray menu_id_arr = group.getJSONArray("menu_ids");
			GroupPrivilege gp = null;
			Set<Menu> menuSet = new HashSet<Menu>();
			if (menu_id_arr != null && menu_id_arr.size() > 0) {
				for (Object obj : menu_id_arr) {
					gp = new GroupPrivilege();
					Long menu_id = Long.parseLong(obj.toString());
					Menu m = menuDao.get(menu_id);
					gp.setGroup(g);
					gp.setMenu(m);
					groupPrivilegeDao.save(gp);
					menuSet.add(m);
				}
			}
			resp.put("id", g.getId());
			resp.put("group_name", g.getGroup_name());
			List<Menu> menuList = menuDao.getAll();
			List<JSONObject> menuJsonList = new ArrayList<JSONObject>();
			JSONObject menuJson = null;
			if (menuList != null && menuList.size() > 0) {
				for (Menu m : menuList) {
					menuJson = new JSONObject();
					menuJson.put("id", m.getId());
					menuJson.put("menu_name", m.getMenu_name());
					if (menuSet.contains(m)) {
						menuJson.put("is_exist", 1);
					} else {
						menuJson.put("is_exist", 0);
					}

					menuJsonList.add(menuJson);
				}
			}
			resp.put("menus", menuJsonList);
			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "参数不能为空");
			resp.put("code", 10001);
			resp.put("error_message", "参数不能为空");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	@Override
	public Response groupList() {
		List<Group> gList = groupDao.getAll();
		List<JSONObject> groupJsonList = new ArrayList<JSONObject>();
		JSONObject groupJson = null;
		List<Menu> mList = menuDao.getAll();
		List<JSONObject> menuJsonList = null;
		JSONObject menuJson = null;
		if (gList != null && gList.size() > 0) {
			for (Group g : gList) {
				menuJsonList = new ArrayList<JSONObject>();
				groupJson = new JSONObject();
				groupJson.put("id", g.getId());
				groupJson.put("group_name", g.getGroup_name());
				Set<Menu> mSet = g.getMenus();
				if (mSet != null && mSet.size() > 0) {
					for (Menu pMenu : mList) {
						menuJson = new JSONObject();
						menuJson.put("id", pMenu.getId());
						menuJson.put("menu_name", pMenu.getMenu_name());
						boolean bool = mSet.contains(pMenu);
						if (bool) {
							menuJson.put("is_exist", 1);
						} else {
							menuJson.put("is_exist", 0);
						}

						menuJsonList.add(menuJson);
					}
				} else {
					if (mList != null && mList.size() > 0) {
						for (Menu m : mList) {
							menuJson = new JSONObject();
							menuJson.put("id", m.getId());
							menuJson.put("menu_name", m.getMenu_name());
							menuJson.put("is_exist", 0);
							menuJsonList.add(menuJson);
						}
					}

				}
				groupJson.put("menus", menuJsonList);
				groupJsonList.add(groupJson);
			}
		}
		return Response.status(Response.Status.OK).entity(groupJsonList).build();
	}

	@Override
	public Response update_admin(JSONObject user) {
		JSONObject resp = new JSONObject();
		if (user != null) {
			Long admin_id = user.getLong("id");
			Admin admin = adminDao.get(admin_id);
			admin.setUsername(user.getString("username"));
			admin.setPassword(user.getString("password"));
			Group group = null;
			Set<Group> groupSet = new HashSet<Group>();
			if (user.containsKey("group_id")) {
				Long group_id = user.getLong("group_id");
				group = groupDao.get(group_id);
				groupSet.add(group);
				if (group != null) {
					admin.setGroups(groupSet);
				}
			}
			adminDao.update(admin);
			if (user.containsKey("group_id")) {
				Long group_id = user.getLong("group_id");
				AdminGroup adminGroup = adminGroupDao.getAdminGroupByAdminIdAndGroupId(admin.getId(), group_id);
				if (adminGroup == null) {
					adminGroupDao.deleteAdminGroup(admin.getId());
					AdminGroup ag = new AdminGroup();
					ag.setAdmin(admin);
					ag.setGroup(group);
					adminGroupDao.save(ag);
				}

			}

			JSONObject json = new JSONObject();
			json.put("id", admin.getId());
			json.put("username", admin.getUsername());
			json.put("create_time", admin.getCreate_time());
			json.put("password", admin.getPassword());
			if (group != null) {
				List<JSONObject> groupJsonList = new ArrayList<JSONObject>();
				JSONObject gJson = new JSONObject();
				gJson.put("id", group.getId());
				gJson.put("group_name", group.getGroup_name());
				groupJsonList.add(gJson);
				json.put("groups", groupJsonList);
			}
			return Response.status(Response.Status.OK).entity(json).build();
		} else {
			resp.put("status", "参数不能为空");
			resp.put("code", 10001);
			resp.put("error_message", "参数不能为空");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}

	}

	@Override
	public Response del_group(Long group_id) {
		JSONObject resp = new JSONObject();
		if (group_id != null && group_id > 0) {
			groupDao.delete(group_id);
			groupPrivilegeDao.deleteGroupPrivilegeByGroupId(group_id);
			adminGroupDao.deleteAdminGroupByGroupId(group_id);
			resp.put("id", group_id);
			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "参数不能为空");
			resp.put("code", 10001);
			resp.put("error_message", "参数不能为空");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	@Override
	public Response add_member(JSONObject user) {

		JSONObject resp = new JSONObject();
		if (user != null) {
			User u = new User();
			String phone = user.getString("phone").trim();
			String idcard = user.getString("identity_card");
			List<User> userList = userDao.getUserByPhoneOrIdcard(phone, idcard);
			if (userList != null && userList.size() > 0) {
				resp.put("status", "该手机号或者身份证已被使用");
				resp.put("code", 10801);
				resp.put("error_message", "该手机号或者身份证已被使用");
				return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
			} else {
				u.setPhone(user.getString("phone").trim());
				if (!user.containsKey("password")) {
					String password = "12345678";
					String pwd = Base64Utils.encodeToString(password.getBytes());
					u.setPassword(pwd);
				} else {
					u.setPassword(user.getString("password"));
				}

				u.setUsername(user.getString("username"));
				u.setIdentity_card(user.getString("identity_card"));

				if (user.containsKey("trade") && !Strings.isNullOrEmpty(user.getString("trade"))) {
					u.setTrade(user.getString("trade"));
				}

				if (user.containsKey("club_name") && !Strings.isNullOrEmpty(user.getString("club_name"))) {
					u.setClub_name(user.getString("club_name"));
				}

				if (user.containsKey("level") && !Strings.isNullOrEmpty(user.getString("level"))) {
					u.setLevel(user.getString("level"));
				} else {
					u.setLevel("normal");
				}

				if (user.containsKey("birthday") && !Strings.isNullOrEmpty(user.getString("birthday"))) {
					u.setBirthday(user.getString("birthday"));
				}

				if (user.containsKey("address") && !Strings.isNullOrEmpty(user.getString("address"))) {
					u.setAddress(user.getString("address"));
				}

				if (user.containsKey("gender") && !Strings.isNullOrEmpty(user.getString("gender"))) {
					u.setGender(user.getString("gender"));
				}
				
				if (user.containsKey("zone") && !Strings.isNullOrEmpty(user.getString("zone"))) {
					u.setZone(user.getString("zone"));
				}

				u.setStatus("enable");
				this.userDao.save(u);
				resp.put("id", u.getId());
				resp.put("username", u.getUsername());
				resp.put("phone", u.getPhone());
				resp.put("password", u.getPassword());
				resp.put("identity_card", u.getIdentity_card());
				resp.put("level", u.getLevel());
				resp.put("birthday", u.getBirthday());
				resp.put("club_name", u.getClub_name());
				resp.put("create_time", u.getCreated_time());
				if (!Strings.isNullOrEmpty(u.getAvatar_image())) {
					resp.put("avatar_image", JSONObject.fromObject(u.getAvatar_image()));
				}
				resp.put("trade", u.getTrade());

				return Response.status(Response.Status.OK).entity(resp).build();
			}

		} else {
			resp.put("status", "参数不能为空");
			resp.put("code", 10001);
			resp.put("error_message", "参数不能为空");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}

	}

	@Override
	public Response setting_member_level(JSONObject member) {
		JSONObject resp = new JSONObject();
		if (member != null) {
			Long id = member.getLong("id");
			String level = member.getString("level");
			User user = userDao.get(id);
			user.setLevel(level);
			// 用户生成二维码
			if (Strings.isNullOrEmpty(user.getNormal_barcode()) || Strings.isNullOrEmpty(user.getVip_barcode())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
				String date = sdf.format(new Date());
				Random random = new Random();
				String randStr = String.valueOf(100000 + random.nextInt(899999));
				String order_no = "o" + date + randStr;

				String subRandStr = String.valueOf(100000 + random.nextInt(899999));
				String ticket_order_no = "sub" + date + subRandStr;
				Zyb zyb = getZybInfo();
				String xmlMsg = ZybUtil.createUserXml(user, order_no, ticket_order_no, zyb);
				String sign = CodeUtil.encode2hex("xmlMsg=" + xmlMsg + zyb.getMasterSecret());
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("xmlMsg", xmlMsg);
				map.put("sign", sign);
				String result = "";
				try {
					result = HttpUtil.sendClientPost(zyb.getUrl(), map);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				String codeStr = ZybUtil.getReturnCode(result);
				// 智游宝
				if (!Strings.isNullOrEmpty(codeStr)) {
					int code = Integer.parseInt(codeStr);
					if (code == 0) {
						String xmlMsgCode = ZybUtil.findImgXml(order_no, zyb);
						String signCode = CodeUtil.encode2hex("xmlMsg=" + xmlMsgCode + zyb.getMasterSecret());
						Map<String, Object> mapCode = new HashMap<String, Object>();
						mapCode.put("xmlMsg", xmlMsgCode);
						mapCode.put("sign", signCode);
						String resultCode = "";
						try {
							resultCode = HttpUtil.sendClientPost(zyb.getUrl(), mapCode);
						} catch (Exception e) {
							e.printStackTrace();
						}
						String barcode = ZybUtil.getImg(resultCode);
						if (!Strings.isNullOrEmpty(barcode)) {
							barcode = barcode.replaceAll("gmCheckNo", "gmCheckCode");
							if (level.equals("normal")) {
								user.setNormal_barcode(barcode);
							} else if (level.equals("vip")) {
								user.setVip_barcode(barcode);
							}

						} else {
							JSONObject j = new JSONObject();
							j.put("status", "获取二维码失败");
							j.put("code", Integer.valueOf(10803));
							j.put("error_message", "获取二维码失败");
							return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
						}
					} else {
						JSONObject j = new JSONObject();
						j.put("status", "获取二维码失败");
						j.put("code", Integer.valueOf(10803));
						j.put("error_message", "获取二维码失败");
						return Response.status(Response.Status.BAD_REQUEST).entity(j).build();
					}
				}

				// ----
			}

			userDao.update(user);
			resp.put("id", user.getId());
			resp.put("level", user.getLevel());
			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "参数不能为空");
			resp.put("code", 10001);
			resp.put("error_message", "参数不能为空");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	@Override
	public Response add_message_template(JSONObject message_template) {
		JSONObject resp = new JSONObject();
		if (message_template != null) {
			String content = message_template.getString("content");
			int type = message_template.getInt("type");
			Template template = new Template();
			template.setContent(content);
			template.setType(type);
			templateDao.save(template);
			resp.put("status", "success");
			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "参数不能为空");
			resp.put("code", 10001);
			resp.put("error_message", "参数不能为空");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	@Override
	public Response update_message_template(JSONObject message_template) {
		JSONObject resp = new JSONObject();
		if (message_template != null) {
			Template template = templateDao.get(message_template.getLong("id"));
			String content = message_template.getString("content");
			int type = message_template.getInt("type");
			template.setContent(content);
			template.setType(type);
			templateDao.update(template);
			resp.put("status", "success");
			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "参数不能为空");
			resp.put("code", 10001);
			resp.put("error_message", "参数不能为空");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	@Override
	public Response add_content_help(JSONObject content) {

		JSONObject resp = new JSONObject();
		if (content != null) {
			Content c = new Content();
			if (content.containsKey("title") && !Strings.isNullOrEmpty(content.getString("title"))) {
				c.setTitle(content.getString("title"));
			}

			if (content.containsKey("summary") && !Strings.isNullOrEmpty(content.getString("summary"))) {
				c.setSummary(content.getString("summary"));
			}

			if (content.containsKey("author") && !Strings.isNullOrEmpty(content.getString("author"))) {
				c.setAuthor(content.getString("author"));
			}

			if (content.containsKey("cover_image") && !Strings.isNullOrEmpty(content.getString("cover_image"))) {
				c.setCover_image(content.getString("cover_image"));
			}

			if (content.containsKey("type") && !Strings.isNullOrEmpty(content.getString("type"))) {
				c.setType(content.getInt("type"));
			}
			c.setUpdate_time(new Date());
			c.setStatus("enable");
			contentDao.save(c);
			JSONArray arr = content.getJSONArray("elements");
			ContentElement ce = null;
			List<ContentElement> ctList = new ArrayList<ContentElement>();
			if (arr != null && arr.size() > 0) {
				for (Object o : arr) {
					ce = new ContentElement();
					ce.setElement(o.toString());
					ce.setContent(c);
					contentElementDao.save(ce);
					ctList.add(ce);
				}
				c.setElements(ctList);
			}
			Help help = new Help();
			help.setContent(c);
			help.setSequence(1);
			helpDao.save(help);
			// JSONObject contentJson = getContentJson(c, false);

			JSONObject helpsJson = new JSONObject();
			helpsJson.put("id", help.getId());
			Content co = c;
			helpsJson.put("content_id", co.getId());
			helpsJson.put("title", co.getTitle());
			helpsJson.put("author", co.getAuthor());
			if (!Strings.isNullOrEmpty(co.getCover_image())) {
				helpsJson.put("cover_image", JSONObject.fromObject(co.getCover_image()));
			}
			String summary = co.getSummary();
			if (!Strings.isNullOrEmpty(summary)) {
				helpsJson.put("summary", co.getSummary());
			}
			List<ContentElement> ceList = co.getElements();
			if (ceList != null && ceList.size() > 0) {
				List<JSONObject> ceJsonList = new ArrayList<JSONObject>();
				JSONObject ceJson = null;
				for (ContentElement cet : ceList) {
					ceJson = new JSONObject();
					ceJson.put("id", cet.getId());
					ceJson.put("element", JSONObject.fromObject(cet.getElement()));
					ceJsonList.add(ceJson);
				}
				helpsJson.put("elements", ceJsonList);
			}

			helpsJson.put("create_time", c.getCreated_time());
			helpsJson.put("update_time", c.getUpdate_time());
			// 更新redis数据
			updateBasicParam();
			return Response.status(Response.Status.OK).entity(helpsJson).build();
		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}

	}

	@Override
	public Response helps_list(HttpServletRequest request) {
		List<Help> helpList = helpDao.getHelpBySequence();

		List<JSONObject> helpsJsonList = new ArrayList<JSONObject>();
		JSONObject helpsJson = null;
		if (helpList != null && helpList.size() > 0) {
			for (Help helps : helpList) {
				Content c = helps.getContent();
				if (c.getStatus().equals("enable")) {
					helpsJson = new JSONObject();
					helpsJson.put("id", helps.getId());

					helpsJson.put("content_id", c.getId());
					helpsJson.put("title", c.getTitle());
					helpsJson.put("author", c.getAuthor());
					if (!Strings.isNullOrEmpty(c.getCover_image())) {
						helpsJson.put("cover_image", JSONObject.fromObject(c.getCover_image()));
					}
					String summary = c.getSummary();
					if (!Strings.isNullOrEmpty(summary)) {
						helpsJson.put("summary", c.getSummary());
					}
					List<ContentElement> ceList = c.getElements();
					if (ceList != null && ceList.size() > 0) {
						List<JSONObject> ceJsonList = new ArrayList<JSONObject>();
						JSONObject ceJson = null;
						for (ContentElement ce : ceList) {
							ceJson = new JSONObject();
							ceJson.put("id", ce.getId());
							ceJson.put("element", JSONObject.fromObject(ce.getElement()));
							ceJsonList.add(ceJson);
						}
						helpsJson.put("elements", ceJsonList);
					}

					helpsJson.put("create_time", c.getCreated_time());
					helpsJson.put("update_time", c.getUpdate_time());

					helpsJsonList.add(helpsJson);
				}

			}
		}
		return Response.status(Response.Status.OK).entity(helpsJsonList).build();
	}

	@Override
	public Response content_help_sequence(JSONArray help) {
		JSONObject resp = new JSONObject();
		if (help != null) {

			for (Object o : help) {
				JSONObject helpJson = JSONObject.fromObject(o);
				Long id = helpJson.getLong("id");
				Help helpModel = helpDao.get(id);
				int sequence = helpJson.getInt("sequence");
				helpModel.setSequence(sequence);
				helpDao.update(helpModel);
			}
			resp.put("status", "success");
			// 更新redis数据
			updateBasicParam();
			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	@Override
	public Response add_content_news(JSONObject news) {

		JSONObject resp = new JSONObject();
		if (news != null) {
			Content c = new Content();

			if (news.containsKey("title") && !Strings.isNullOrEmpty(news.getString("title"))) {
				c.setTitle(news.getString("title"));
			}

			if (news.containsKey("summary") && !Strings.isNullOrEmpty(news.getString("summary"))) {
				c.setSummary(news.getString("summary"));
			}

			if (news.containsKey("author") && !Strings.isNullOrEmpty(news.getString("author"))) {
				c.setAuthor(news.getString("author"));
			}

			if (news.containsKey("cover_image") && !Strings.isNullOrEmpty(news.getString("cover_image"))) {
				c.setCover_image(news.getString("cover_image"));
			}

			if (news.containsKey("type") && !Strings.isNullOrEmpty(news.getString("type"))) {
				c.setType(news.getInt("type"));
			}

			c.setUpdate_time(new Date());
			c.setStatus("enable");
			contentDao.save(c);
			JSONArray arr = news.getJSONArray("elements");
			ContentElement ce = null;
			List<ContentElement> ctList = new ArrayList<ContentElement>();
			if (arr != null && arr.size() > 0) {
				for (Object o : arr) {
					ce = new ContentElement();
					ce.setElement(o.toString());
					ce.setContent(c);
					contentElementDao.save(ce);
					ctList.add(ce);
				}
				c.setElements(ctList);
			}
			News n = new News();
			n.setContent(c);
			n.setSequence(1);
			newsDao.save(n);
			JSONObject contentJson = getContentConditionJson(c, true);
			contentJson.put("id", n.getId());
			// 更新redis数据
			updateBasicParam();
			return Response.status(Response.Status.OK).entity(contentJson).build();
		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}

	}

	@Override
	public Response content_news_sequence(JSONArray news) {
		JSONObject resp = new JSONObject();
		if (news != null) {

			for (Object o : news) {
				JSONObject newsJson = JSONObject.fromObject(o);
				Long id = newsJson.getLong("id");
				News newsModel = newsDao.get(id);
				int sequence = newsJson.getInt("sequence");
				newsModel.setSequence(sequence);
				newsDao.update(newsModel);
			}
			resp.put("status", "success");
			// 更新redis数据
			updateBasicParam();
			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}

	}

	@Override
	public Response news_list(HttpServletRequest request) {

		List<News> newsList = newsDao.getNewsBySequence();

		List<JSONObject> newsJsonList = new ArrayList<JSONObject>();
		JSONObject newsJson = null;
		if (newsList != null && newsList.size() > 0) {
			for (News n : newsList) {
				Content c = n.getContent();
				if (c.getStatus().equals("enable")) {
					newsJson = new JSONObject();
					newsJson.put("id", n.getId());
					newsJson.put("content_id", c.getId());
					newsJson.put("title", c.getTitle());
					newsJson.put("author", c.getAuthor());
					if (!Strings.isNullOrEmpty(c.getCover_image())) {
						newsJson.put("cover_image", JSONObject.fromObject(c.getCover_image()));
					}
					String summary = c.getSummary();
					if (!Strings.isNullOrEmpty(summary)) {
						newsJson.put("summary", c.getSummary());
					}
					List<ContentElement> ceList = c.getElements();
					if (ceList != null && ceList.size() > 0) {
						List<JSONObject> ceJsonList = new ArrayList<JSONObject>();
						JSONObject ceJson = null;
						for (ContentElement ce : ceList) {
							ceJson = new JSONObject();
							ceJson.put("id", ce.getId());
							ceJson.put("element", JSONObject.fromObject(ce.getElement()));
							ceJsonList.add(ceJson);
						}
						newsJson.put("elements", ceJsonList);
					}

					newsJson.put("create_time", c.getCreated_time());
					newsJson.put("update_time", c.getUpdate_time());

					newsJsonList.add(newsJson);
				}

			}
		}
		return Response.status(Response.Status.OK).entity(newsJsonList).build();

	}

	@Override
	public Response add_content(JSONObject content) {
		JSONObject resp = new JSONObject();
		if (content != null) {
			Content c = new Content();
			if (content.containsKey("title") && !Strings.isNullOrEmpty(content.getString("title"))) {
				c.setTitle(content.getString("title"));
			}

			if (content.containsKey("summary") && !Strings.isNullOrEmpty(content.getString("summary"))) {
				c.setSummary(content.getString("summary"));
				;
			}

			if (content.containsKey("author") && !Strings.isNullOrEmpty(content.getString("author"))) {
				c.setAuthor(content.getString("author"));
			}

			if (content.containsKey("cover_image") && !Strings.isNullOrEmpty(content.getString("cover_image"))) {
				c.setCover_image(content.getString("cover_image"));
			}

			if (content.containsKey("type") && !Strings.isNullOrEmpty(content.getString("type"))) {
				c.setType(content.getInt("type"));
			} else {
				c.setType(0);
			}
			c.setUpdate_time(new Date());
			c.setStatus("enable");
			contentDao.save(c);
			JSONArray arr = content.getJSONArray("elements");
			List<ContentElement> ceList = new ArrayList<ContentElement>();
			ContentElement ce = null;
			if (arr != null && arr.size() > 0) {
				for (Object o : arr) {
					ce = new ContentElement();
					ce.setElement(o.toString());
					ce.setContent(c);
					contentElementDao.save(ce);
					ceList.add(ce);
				}
				c.setElements(ceList);
			}
			JSONObject contentJson = getContentJson(c, true);

			return Response.status(Response.Status.OK).entity(contentJson).build();
		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	@Override
	public Response del_content(Long content_id) {
		JSONObject resp = new JSONObject();
		if (content_id != null && content_id > 0) {
			Content content = contentDao.get(content_id);
			content.setStatus("disable");
			contentDao.update(content);
			resp.put("id", content.getId());
			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	@Override
	public Response update_content(JSONObject content) {

		JSONObject resp = new JSONObject();
		if (content != null) {

			Long id = content.getLong("id");
			Content c = contentDao.get(id);
			if (content.containsKey("title") && !Strings.isNullOrEmpty(content.getString("title"))) {
				c.setTitle(content.getString("title"));
			}

			if (content.containsKey("summary") && !Strings.isNullOrEmpty(content.getString("summary"))) {
				c.setSummary(content.getString("summary"));
				;
			}

			if (content.containsKey("author") && !Strings.isNullOrEmpty(content.getString("author"))) {
				c.setAuthor(content.getString("author"));
			}

			if (content.containsKey("cover_image") && !Strings.isNullOrEmpty(content.getString("cover_image"))) {
				c.setCover_image(content.getString("cover_image"));
			}
			c.setUpdate_time(new Date());
			contentDao.update(c);
			contentElementDao.deleteContentElementByContentId(id);
			JSONArray arr = content.getJSONArray("elements");
			ContentElement ce = null;
			if (arr != null && arr.size() > 0) {
				for (Object o : arr) {
					ce = new ContentElement();
					ce.setElement(o.toString());
					ce.setContent(c);
					contentElementDao.save(ce);
				}
			}
			JSONObject contentJson = getContentJson(c, true);
			return Response.status(Response.Status.OK).entity(contentJson).build();
		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}

	}

	@Override
	public Response add_slide(JSONObject content, Long loginUserid) {
		JSONObject resp = new JSONObject();
		Slide slide = new Slide();
		if (content != null) {
			if (content.containsKey("reference_url")) {
				slide.setUrl(content.getString("reference_url"));
			} else {
				slide.setReference_id(content.getLong("reference_id"));
			}
			slide.setType(content.getString("type"));
			slide.setAuthorId(loginUserid);
			slide.setStatus("enable");
			slide.setSequence(1);
			slide.setSlide_image(content.getString("slide_image"));
			slide.setTitle(content.getString("title"));
			slideDao.save(slide);
			resp.put("id", slide.getId());
			resp.put("type", slide.getType());
			resp.put("sequence", slide.getSequence());
			resp.put("slide_image", JSONObject.fromObject(slide.getSlide_image()));
			resp.put("title", slide.getTitle());

			if (slide.getType().equals("url")) {
				resp.put("url", slide.getUrl());
			} else {
				resp.put("reference_id", slide.getReference_id());
			}

			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	@Override
	public Response update_slide(JSONObject content, Long loginUserid) {

		JSONObject resp = new JSONObject();
		if (content != null) {

			Long id = content.getLong("id");
			Slide slide = slideDao.get(id);
			if (content.containsKey("reference_url")) {
				slide.setUrl(content.getString("reference_url"));
			} else {
				slide.setReference_id(content.getLong("reference_id"));
			}
			slide.setTitle(content.getString("title"));
			slide.setType(content.getString("type"));
			slide.setAuthorId(loginUserid);
			slide.setStatus("enable");
			slide.setSequence(1);
			slide.setSlide_image(content.getString("slide_image"));
			slideDao.update(slide);
			resp.put("id", slide.getId());
			resp.put("type", slide.getType());
			resp.put("sequence", slide.getSequence());
			resp.put("slide_image", JSONObject.fromObject(slide.getSlide_image()));
			resp.put("title", slide.getTitle());

			if (slide.getType().equals("url")) {
				resp.put("url", slide.getUrl());
			} else {
				resp.put("reference_id", slide.getReference_id());
			}
			// 更新redis数据
			updateHomepage();
			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}

	}

	@Override
	public Response delete_slide(Long slide_id) {
		JSONObject resp = new JSONObject();
		if (slide_id != null && slide_id > 0) {
			slideDao.delete(slide_id);
			resp.put("slide_id", slide_id);
			// 更新redis数据
			updateHomepage();
			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	@Override
	public Response slide_sequence(JSONArray content) {
		JSONObject resp = new JSONObject();
		if (content != null && content.size() > 0) {
			JSONObject slideJson = null;
			for (Object o : content) {
				slideJson = JSONObject.fromObject(o);
				Long id = slideJson.getLong("id");
				int sequence = slideJson.getInt("sequence");
				Slide slide = slideDao.get(id);
				slide.setSequence(sequence);
				slideDao.update(slide);
			}
			// 更新redis数据
			updateHomepage();
			resp.put("status", "success");
			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	@Override
	public Response slide_list() {
		List<Slide> slideList = slideDao.getSlideByStatus("enable");
		List<JSONObject> slideJsonList = new ArrayList<JSONObject>();
		JSONObject slideJson = null;
		if (slideList != null && slideList.size() > 0) {
			for (Slide slide : slideList) {
				slideJson = new JSONObject();
				slideJson.put("id", slide.getId());
				slideJson.put("title", slide.getTitle());
				slideJson.put("slide_image", JSONObject.fromObject(slide.getSlide_image()));
				slideJson.put("type", slide.getType());
				slideJson.put("sequence", slide.getSequence());
				if (slide.getType().equals("url")) {
					slideJson.put("url", slide.getUrl());
				} else {
					slideJson.put("reference_id", slide.getReference_id());
				}
				slideJsonList.add(slideJson);
			}
		}
		return Response.status(Response.Status.OK).entity(slideJsonList).build();
	}

	@Override
	public Response audit_order_ticket(JSONObject content) {
		JSONObject resp = new JSONObject();
		if (content != null && content.size() > 0) {
			Long id = content.getLong("id");
			OrdersTicket ot = ordersTicketDao.get(id);
			int status = content.getInt("status");
			// status 3 退票成功 4 审核被驳回
			ot.setStatus(status);
			ordersTicketDao.update(ot);
			Notification n = new Notification();
			n.setNotificationType(status);
			n.setObjectId(id);
			n.setObjectType(1);
			n.setRead_already(false);
			n.setRecipientId(ot.getOrders().getUser().getId());
			n.setStatus("enable");
			if (status == 4) {
				n.setRemark("原因：活动开始前1小时不允许退票");
			}
			notificationDao.save(n);
			resp.put("status", "success");
			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	@Override
	public Response group_menu(JSONObject group_menu) {
		JSONObject resp = new JSONObject();
		if (group_menu != null) {
			GroupPrivilege gp = null;
			Long group_id = group_menu.getLong("group_id");
			Group g = groupDao.get(group_id);
			JSONArray menu_id_arr = group_menu.getJSONArray("menu_ids");
			groupPrivilegeDao.deleteGroupPrivilegeByGroupId(group_id);
			if (menu_id_arr != null && menu_id_arr.size() > 0) {
				for (Object obj : menu_id_arr) {
					gp = new GroupPrivilege();
					Long menu_id = Long.parseLong(obj.toString());
					Menu m = menuDao.get(menu_id);
					gp.setGroup(g);
					gp.setMenu(m);
					groupPrivilegeDao.save(gp);
				}
			}

			List<JSONObject> menuJsonList = new ArrayList<JSONObject>();
			JSONObject groupJson = new JSONObject();
			groupJson.put("id", g.getId());
			groupJson.put("group_name", g.getGroup_name());
			Set<Menu> mSet = g.getMenus();
			List<Menu> mList = menuDao.getAll();
			JSONObject menuJson = null;
			if (mSet != null && mSet.size() > 0) {
				for (Menu pMenu : mList) {
					menuJson = new JSONObject();
					menuJson.put("id", pMenu.getId());
					menuJson.put("menu_name", pMenu.getMenu_name());
					boolean bool = mSet.contains(pMenu);
					if (bool) {
						menuJson.put("is_exist", 1);
					} else {
						menuJson.put("is_exist", 0);
					}

					menuJsonList.add(menuJson);
				}
			} else {
				if (mList != null && mList.size() > 0) {
					for (Menu m : mList) {
						menuJson = new JSONObject();
						menuJson.put("id", m.getId());
						menuJson.put("menu_name", m.getMenu_name());
						menuJson.put("is_exist", 0);
						menuJsonList.add(menuJson);
					}
				}

			}
			groupJson.put("menus", menuJsonList);

			return Response.status(Response.Status.OK).entity(groupJson).build();
		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	@Override
	public Response poi_list(HttpServletRequest request, Long loginUserid) {

		String countStr = request.getParameter("count");
		String pageStr = request.getParameter("page");
		int count = 10;
		int page = 0;
		JSONObject error = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			List<Poi> poiList = null;
			JSONObject result = new JSONObject();
			List<JSONObject> poiJSONList = new ArrayList<JSONObject>();

			if ((Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
				poiList = poiDao.getPoiList(count, page);
				int total = poiDao.getPoiCount();
				result.put("total", total);
				List<Classify> classifyList = classifyDao.getAll();
				List<JSONObject> classifyJsonList = new ArrayList<JSONObject>();
				JSONObject classifyJson = null;
				if (classifyList != null && classifyList.size() > 0) {
					for (Classify c : classifyList) {
						classifyJson = new JSONObject();
						classifyJson.put("classify_id", c.getId());
						classifyJson.put("classify_name", c.getClassify_name());
						classifyJsonList.add(classifyJson);
					}
				}
				result.put("classifies", classifyJsonList);
			} else if ((!Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
				count = Integer.parseInt(countStr);
				poiList = poiDao.getPoiList(count, page);
				int total = poiDao.getPoiCount();
				result.put("total", total);
				List<Classify> classifyList = classifyDao.getAll();
				List<JSONObject> classifyJsonList = new ArrayList<JSONObject>();
				JSONObject classifyJson = null;
				if (classifyList != null && classifyList.size() > 0) {
					for (Classify c : classifyList) {
						classifyJson = new JSONObject();
						classifyJson.put("classify_id", c.getId());
						classifyJson.put("classify_name", c.getClassify_name());
						classifyJsonList.add(classifyJson);
					}
				}
				result.put("classifies", classifyJsonList);
			} else if ((Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
				page = Integer.parseInt(pageStr);
				poiList = poiDao.getPoiList(count, page);
			} else if ((!Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
				count = Integer.parseInt(countStr);
				page = Integer.parseInt(pageStr);
				poiList = poiDao.getPoiList(count, page);
			}

			if (poiList != null && poiList.size() > 0) {
				JSONObject poiJson = null;
				for (Poi p : poiList) {
					poiJson = getPoiJSON(p);
					poiJSONList.add(poiJson);

				}
			}

			result.put("pois", poiJSONList);

			return Response.status(Response.Status.OK).entity(result).build();
		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}

	}

	@Override
	public Response poi_hot(HttpServletRequest request) {
		List<Poi> poiList = poiDao.getPoiListByHot();
		List<JSONObject> poiJSONList = new ArrayList<JSONObject>();
		if (poiList != null && poiList.size() > 0) {
			JSONObject poiJson = null;
			for (Poi p : poiList) {
				poiJson = getPoiJSON(p);
				poiJSONList.add(poiJson);
			}
		}
		// 更新redis数据
		updateBasicParam();
		return Response.status(Response.Status.OK).entity(poiJSONList).build();
	}

	@Override
	public Response update_poi_hot(JSONObject hot_poi) {
		JSONObject resp = new JSONObject();
		if (hot_poi != null) {
			Long poi_id = hot_poi.getLong("poi_id");
			Poi poi = poiDao.get(poi_id);
			poi.setHot(hot_poi.getInt("hot"));
			poiDao.update(poi);
			resp = getPoiJSON(poi);
			// 更新redis数据
			updateBasicParam();
			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	@Override
	public Response classify_poi(HttpServletRequest request, Long loginUserid) {

		String countStr = request.getParameter("count");
		String pageStr = request.getParameter("page");
		String poi_name = request.getParameter("poi_name");
		String classify_id = request.getParameter("classify_id");
		String is_online = request.getParameter("is_online");
		int count = 10;
		int page = 0;
		JSONObject error = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			List<Poi> poiList = null;
			JSONObject result = new JSONObject();
			List<JSONObject> poiJSONList = new ArrayList<JSONObject>();

			if ((Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
				poiList = poiDao.getPoiListByParams(count, page, poi_name, is_online, classify_id);
				int total = poiDao.getCountByParams(poi_name, is_online, classify_id);
				result.put("total", total);
			} else if ((!Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
				count = Integer.parseInt(countStr);
				poiList = poiDao.getPoiListByParams(count, page, poi_name, is_online, classify_id);
				int total = poiDao.getCountByParams(poi_name, is_online, classify_id);
				result.put("total", total);
			} else if ((Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
				page = Integer.parseInt(pageStr);
				poiList = poiDao.getPoiListByParams(count, page, poi_name, is_online, classify_id);
			} else if ((!Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
				count = Integer.parseInt(countStr);
				page = Integer.parseInt(pageStr);
				poiList = poiDao.getPoiListByParams(count, page, poi_name, is_online, classify_id);
			}

			if (poiList != null && poiList.size() > 0) {
				JSONObject poiJson = null;
				for (Poi p : poiList) {
					poiJson = getPoiJSON(p);
					poiJSONList.add(poiJson);

				}
			}
			result.put("pois", poiJSONList);
			return Response.status(Response.Status.OK).entity(result).build();
		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}

	}

	@Override
	public Response update_poi(JSONObject poi) {
		JSONObject resp = new JSONObject();
		if (poi != null) {
			Long id = poi.getLong("id");
			Poi poiModel = poiDao.get(id);
			if (poi.containsKey("is_online")) {

				int is_online = poi.getInt("is_online");
				poiModel.setIs_online(is_online);
				poiDao.update(poiModel);
				resp.put("id", poiModel.getId());
				resp.put("is_online", poiModel.getIs_online());
			} else {
				poiModel.setTitle(poi.getString("title"));
				poiModel.setAttention(poi.getString("attention"));
				if (poi.containsKey("content_id")) {
					poiModel.setContent_id(poi.getLong("content_id"));
				} else {
					poiModel.setContent_id(0l);
				}

				// poiModel.setCover_image(poi.getString("cover_image"));
				poiModel.setLocation(poi.getString("location"));
				poiModel.setPlace(poi.getString("place"));
				if (poi.containsKey("price")) {
					poiModel.setPrice(BigDecimal.valueOf(poi.getDouble("price")));
				}
				if (poi.containsKey("subtitle")) {
					String subtitle = poi.getString("subtitle");
					if (!Strings.isNullOrEmpty(subtitle)) {
						poiModel.setSubtitle(subtitle);
					}
				}
				if (poi.containsKey("time_info")) {
					String time_info = poi.getString("time_info");
					if (!Strings.isNullOrEmpty(time_info)) {
						poiModel.setTime_info(time_info);
					}
				}
				
				if (poi.containsKey("direct_sales")) {
					String direct_sales = poi.getString("direct_sales");
					if (!Strings.isNullOrEmpty(direct_sales)) {
						poiModel.setDirect_sales(direct_sales);
					}
				}
				
				if (poi.containsKey("classify_id")) {
					Long classify_id = poi.getLong("classify_id");
					if (classify_id != null && classify_id > 0) {
						Classify classify = classifyDao.get(classify_id);
						poiModel.setClassify(classify);
					}
				}
				if (poi.containsKey("elements")) {
					poiElementDao.deletePoiElementByPoiId(id);
					PoiElement pe = null;
					List<PoiElement> peList = new ArrayList<PoiElement>();
					JSONArray arr = poi.getJSONArray("elements");
					if (arr != null && arr.size() > 0) {
						for (Object o : arr) {
							pe = new PoiElement();
							pe.setElement(o.toString());
							pe.setPoi(poiModel);
							peList.add(pe);
						}
					}

					poiModel.setElements(peList);

				}
				List<Ticket> poiTList = poiModel.getTickets();
				if(poiTList != null && poiTList.size() > 0){
					for(Ticket t:poiTList){
						ticketDao.updateTicket(t.getId(), 0l);
					}
				}
				List<Ticket> tList = new ArrayList<Ticket>();
				
				if (poi.containsKey("tickets")) {
					JSONArray arr = poi.getJSONArray("tickets");
					if (arr != null && arr.size() > 0) {
						for (Object o : arr) {
							Ticket ticket = ticketDao.get(Long.parseLong(o.toString()));
							ticketDao.updateTicket(ticket.getId(), poiModel.getId());
							tList.add(ticket);
						}
						poiModel.setTickets(tList);
						
					}
				}
				poiDao.update(poiModel);
				if (poi.containsKey("recommandations")) {
					JSONArray arr = poi.getJSONArray("recommandations");
					recommandationDao.deleteRecommandationByPoi(id);
					RecommandationId r = null;
					Recommandation re = null;
					Poi reference = null;
					if (arr != null && arr.size() > 0) {
						for (Object o : arr) {
							reference = poiDao.get(Long.parseLong(o.toString()));
							re = new Recommandation();
							r = new RecommandationId();
							r.setPoi(poiModel);
							r.setReference(reference);
							re.setPk(r);
							recommandationDao.save(re);
						}
					}
				}
				resp = getPoiJSON(poiModel);
			}
			updateHomepage();
		}

		return Response.status(Response.Status.OK).entity(resp).build();
	}

	@Override
	public Response update_map_poi(JSONObject poi) {

		JSONObject resp = new JSONObject();
		if (poi != null) {
			Long id = poi.getLong("id");
			Poi poiModel = poiDao.get(id);

			poiModel.setLocation(poi.getString("location"));
			poiDao.update(poiModel);
			resp.put("id", poiModel.getId());
			resp.put("location", JSONObject.fromObject(poiModel.getLocation()));
			updateBasicParam();
		}

		return Response.status(Response.Status.OK).entity(resp).build();

	}

	@Override
	public Response map_poi_list(HttpServletRequest request) {
		String classify_id = request.getParameter("classify_id");
		String is_online = request.getParameter("is_online");
		List<Poi> poiList = null;
		JSONObject result = null;
		if (Strings.isNullOrEmpty(classify_id) && Strings.isNullOrEmpty(is_online)) {
			poiList = poiDao.getAll();
			List<Classify> cList = classifyDao.getAll();
			List<JSONObject> poiListJson = new ArrayList<JSONObject>();
			result = new JSONObject();
			if (poiList != null && poiList.size() > 0) {
				JSONObject poiJson = null;
				for (Poi poi : poiList) {
					poiJson = getPoiJSON(poi);
					poiListJson.add(poiJson);
				}
			}
			List<JSONObject> classifyJsonList = new ArrayList<JSONObject>();
			JSONObject classifyJson = null;
			if (cList != null && cList.size() > 0) {
				for (Classify c : cList) {
					classifyJson = new JSONObject();
					classifyJson.put("id", c.getId());
					classifyJson.put("classify_name", c.getClassify_name());
					classifyJsonList.add(classifyJson);

				}
				result.put("classifies", classifyJsonList);
			}
			result.put("pois", poiListJson);

			return Response.status(Response.Status.OK).entity(result).build();
		} else {
			poiList = poiDao.getPoiListByParams(is_online, classify_id);
			List<JSONObject> poiListJson = new ArrayList<JSONObject>();
			if (poiList != null && poiList.size() > 0) {
				JSONObject poiJson = null;
				for (Poi poi : poiList) {
					poiJson = getPoiJSON(poi);
					poiListJson.add(poiJson);
				}
			}
			return Response.status(Response.Status.OK).entity(poiListJson).build();
		}

	}

	@Override
	public Response content_list(HttpServletRequest request, Long loginUserid) {
		String title = request.getParameter("title");

		String countStr = request.getParameter("count");
		String pageStr = request.getParameter("page");
		int count = 10;
		int page = 0;
		JSONObject error = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			List<Content> contentList = null;
			JSONObject result = new JSONObject();
			List<JSONObject> contentJSONList = new ArrayList<JSONObject>();
			if (!Strings.isNullOrEmpty(title)) {

				if ((Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
					contentList = contentDao.getContentListByTitle(count, page, title);
					int total = contentDao.getContentCount(title);
					result.put("total", total);
				} else if ((!Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
					count = Integer.parseInt(countStr);
					contentList = contentDao.getContentListByTitle(count, page, title);
					int total = contentDao.getContentCount(title);
					result.put("total", total);
				} else if ((Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
					page = Integer.parseInt(pageStr);
					contentList = contentDao.getContentListByTitle(count, page, title);
				} else if ((!Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
					count = Integer.parseInt(countStr);
					page = Integer.parseInt(pageStr);
					contentList = contentDao.getContentListByTitle(count, page, title);
				}

			} else {
				if ((Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
					contentList = contentDao.getContentList(count, page);
					int total = contentDao.getContentCount();
					result.put("total", total);
				} else if ((!Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
					count = Integer.parseInt(countStr);
					contentList = contentDao.getContentList(count, page);
					int total = contentDao.getContentCount();
					result.put("total", total);
				} else if ((Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
					page = Integer.parseInt(pageStr);
					contentList = contentDao.getContentList(count, page);
				} else if ((!Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
					count = Integer.parseInt(countStr);
					page = Integer.parseInt(pageStr);
					contentList = contentDao.getContentList(count, page);
				}
			}

			if (contentList != null && contentList.size() > 0) {
				JSONObject contentJson = null;
				for (Content c : contentList) {
					contentJson = getContentJson(c, true);
					contentJSONList.add(contentJson);

				}
				result.put("contents", contentJSONList);
			}
			return Response.status(Response.Status.OK).entity(result).build();
		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}

	}

	@Override
	public Response order_detail(Long order_id) {
		JSONObject error = new JSONObject();
		if (order_id != null && order_id > 0) {
			Orders orders = ordersDao.get(order_id);
			JSONObject orderJson = new JSONObject();
			orderJson.put("id", orders.getId());
			orderJson.put("order_no", orders.getOrder_no());
			orderJson.put("amount", orders.getAmount());
			User user = orders.getUser();
			if (user != null) {
				orderJson.put("username", user.getUsername());
			}
			List<OrdersTicket> otList = ordersTicketDao.getOrdersTicketByOrdersId(order_id);
			List<JSONObject> ticketOrdersJsonList = new ArrayList<JSONObject>();
			JSONObject ticketOrdersJson = null;
			Ticket ticket = null;
			if (otList != null && otList.size() > 0) {
				for (OrdersTicket ot : otList) {
					ticketOrdersJson = new JSONObject();
					ticket = ot.getTicket();
					ticketOrdersJson.put("ticket_id", ticket.getId());
					ticketOrdersJson.put("ticket_order_no", ot.getTicket_order_no());
					ticketOrdersJson.put("ticket_name", ticket.getTicket_name());
//					ticketOrdersJson.put("zyb_code", ticket.getZyb_code());
					ticketOrdersJson.put("num", ot.getNum());
					ticketOrdersJson.put("total", ot.getTotal());

					int status = ot.getStatus();
					ticketOrdersJson.put("status", status);
					if (status == 2) {
						ticketOrdersJson.put("returnCode", ot.getReturnCode());
					}
					Contacter c = ot.getContacter();
					ticketOrdersJson.put("username", c.getName());
					ticketOrdersJson.put("identity_card", c.getIdentity_card());
					ticketOrdersJson.put("transNo", ot.getTransNo());
					ticketOrdersJson.put("insureNum", ot.getInsureNum());
					ticketOrdersJsonList.add(ticketOrdersJson);
				}
				orderJson.put("tickets", ticketOrdersJsonList);
			}

			return Response.status(Response.Status.OK).entity(orderJson).build();

		} else {
			error.put("status", "参数不能为空");
			error.put("code", Integer.valueOf(10011));
			error.put("error_message", "参数不能为空");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}
	}

	@Override
	public Response add_content_setting(JSONObject content) {

		JSONObject resp = new JSONObject();
		if (content != null) {
			Content c = new Content();
			if (content.containsKey("title") && !Strings.isNullOrEmpty(content.getString("title"))) {
				c.setTitle(content.getString("title"));
			}

			if (content.containsKey("summary") && !Strings.isNullOrEmpty(content.getString("summary"))) {
				c.setSummary(content.getString("summary"));
				;
			}

			if (content.containsKey("author") && !Strings.isNullOrEmpty(content.getString("author"))) {
				c.setAuthor(content.getString("author"));
			}

			if (content.containsKey("cover_image") && !Strings.isNullOrEmpty(content.getString("cover_image"))) {
				c.setCover_image(content.getString("cover_image"));
			}

			if (content.containsKey("type") && !Strings.isNullOrEmpty(content.getString("type"))) {
				c.setType(content.getInt("type"));
			}
			c.setUpdate_time(new Date());
			c.setStatus("enable");
			contentDao.save(c);
			JSONArray arr = content.getJSONArray("elements");
			ContentElement ce = null;
			List<ContentElement> ceList = new ArrayList<ContentElement>();
			if (arr != null && arr.size() > 0) {
				for (Object o : arr) {
					ce = new ContentElement();
					ce.setElement(o.toString());
					ce.setContent(c);
					contentElementDao.save(ce);
					ceList.add(ce);
				}
				c.setElements(ceList);
			}
			Setting setting = new Setting();
			setting.setContent(c);
			setting.setType(content.getInt("type"));
			settingDao.save(setting);
			JSONObject contentJson = getContentJson(c, false);
			return Response.status(Response.Status.OK).entity(contentJson).build();
		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}

	}

	@Override
	public Response orders_ticket_list(HttpServletRequest request, Long loginUserid) {

		String countStr = request.getParameter("count");
		String pageStr = request.getParameter("page");
		String type = request.getParameter("status"); // 0 未使用 1 已使用 2 退票中 3 已退票
														// 4 已拒绝
		String start_date = request.getParameter("start_date");
		String end_date = request.getParameter("end_date");
		String username = request.getParameter("username");
		int count = 10;
		int page = 0;
		JSONObject error = new JSONObject();
		List<OrdersTicket> orderTicketList = null;
		if (loginUserid != null && loginUserid > 0) {

			JSONObject result = new JSONObject();
			if ((Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
				orderTicketList = ordersTicketDao.getOrdersTicketListByParams(count, type, start_date, end_date,
						username);
				Object[] total = ordersTicketDao.getOrdersTicketCountAndAmountByParams(type, start_date, end_date,
						username);

				result.put("total_count", total[0]);
				result.put("total_amount", total[1]);
			} else if ((!Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
				count = Integer.parseInt(countStr);
				orderTicketList = ordersTicketDao.getOrdersTicketListByParams(count, type, start_date, end_date,
						username);
				Object[] total = ordersTicketDao.getOrdersTicketCountAndAmountByParams(type, start_date, end_date,
						username);

				result.put("total_count", total[0]);
				result.put("total_amount", total[1]);
			} else if ((Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
				page = Integer.parseInt(pageStr);
				orderTicketList = ordersTicketDao.getOrdersTicketListByParams(count, page, type, start_date, end_date,
						username);
			} else if ((!Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
				count = Integer.parseInt(countStr);
				page = Integer.parseInt(pageStr);
				orderTicketList = ordersTicketDao.getOrdersTicketListByParams(count, page, type, start_date, end_date,
						username);
			}
			List<JSONObject> orderTicketJSONList = new ArrayList<JSONObject>();
			if (orderTicketList != null && orderTicketList.size() > 0) {
				JSONObject orderTicketJson = null;
				for (OrdersTicket ot : orderTicketList) {
					orderTicketJson = new JSONObject();
					Ticket t = ot.getTicket();
					orderTicketJson.put("id", ot.getId());
					orderTicketJson.put("ticket_id", ot.getTicket().getId());
					orderTicketJson.put("ticket_name", ot.getTicket().getTicket_name());
//					orderTicketJson.put("zyb_code", t.getZyb_code());
					orderTicketJson.put("username", ot.getContacter().getName());
					orderTicketJson.put("update_time", ot.getUpdate_time());
					orderTicketJson.put("status", ot.getStatus());
					orderTicketJson.put("total", ot.getTotal());
					orderTicketJson.put("transNo", ot.getTransNo());
					orderTicketJson.put("insureNum", ot.getInsureNum());
					orderTicketJson.put("startDate", ot.getStartDate());
					orderTicketJson.put("endDate", ot.getEndDate());
					orderTicketJSONList.add(orderTicketJson);
				}
			}
			return Response.status(Response.Status.OK).entity(orderTicketJSONList).build();

		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}

	}

	@Override
	public Response orders_ticket_statistics(HttpServletRequest request, Long loginUserid) {
		String countStr = request.getParameter("count");
		String pageStr = request.getParameter("page");
		String type = request.getParameter("status"); // 0 未使用 1 已使用 2 退票中 3 已退票
														// 4 已拒绝
		String search = request.getParameter("search");
		String poiIdStr = request.getParameter("poi_id");
		int count = 10;
		int page = 0;
		JSONObject error = new JSONObject();
		List<OrdersTicket> orderTicketList = null;
		if (loginUserid != null && loginUserid > 0) {

			JSONObject result = new JSONObject();
			if ((Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
				orderTicketList = ordersTicketDao.getOrdersTicketListByStatistics(count, page, poiIdStr, type, search);
				Object[] total = ordersTicketDao.getOrdersTicketCountAndAmountByStatistics(poiIdStr, type, search);
				if (Strings.isNullOrEmpty(poiIdStr)) {
					List<Poi> poiList = poiDao.getPoiList();
					List<JSONObject> poiJsonList = new ArrayList<JSONObject>();
					JSONObject poiJson = null;
					if (poiList != null && poiList.size() > 0) {
						for (Poi p : poiList) {
							poiJson = new JSONObject();
							poiJson.put("id", p.getId());
							poiJson.put("title", p.getTitle());
							poiJsonList.add(poiJson);
						}
					}
					result.put("pois", poiJsonList);
				}

				result.put("total_count", total[0]);
				if (total[1] != null) {
					result.put("total_amount", total[1]);
				} else {
					result.put("total_amount", 0);
				}
			} else if ((!Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
				count = Integer.parseInt(countStr);
				orderTicketList = ordersTicketDao.getOrdersTicketListByStatistics(count, page, poiIdStr, type, search);
				Object[] total = ordersTicketDao.getOrdersTicketCountAndAmountByStatistics(poiIdStr, type, search);
				if (Strings.isNullOrEmpty(poiIdStr)) {
					List<Poi> poiList = poiDao.getPoiList();
					List<JSONObject> poiJsonList = new ArrayList<JSONObject>();
					JSONObject poiJson = null;
					if (poiList != null && poiList.size() > 0) {
						for (Poi p : poiList) {
							poiJson = new JSONObject();
							poiJson.put("id", p.getId());
							poiJson.put("title", p.getTitle());
							poiJsonList.add(poiJson);
						}
					}
					result.put("pois", poiJsonList);
				}
				result.put("total_count", total[0]);
				if (total[1] != null) {
					result.put("total_amount", total[1]);
				} else {
					result.put("total_amount", 0);
				}
			} else if ((Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
				page = Integer.parseInt(pageStr);
				orderTicketList = ordersTicketDao.getOrdersTicketListByStatistics(count, page, poiIdStr, type, search);
				// Object[] total =
				// ordersTicketDao.getOrdersTicketCountAndAmountByStatistics(poiIdStr,
				// type, search);
				// result.put("total_count", total[0]);
				// result.put("total_amount", total[1]);
			} else if ((!Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
				count = Integer.parseInt(countStr);
				page = Integer.parseInt(pageStr);
				orderTicketList = ordersTicketDao.getOrdersTicketListByStatistics(count, page, poiIdStr, type, search);
				// Object[] total =
				// ordersTicketDao.getOrdersTicketCountAndAmountByStatistics(poiIdStr,
				// type, search);
				// result.put("total_count", total[0]);
				// result.put("total_amount", total[1]);
			}
			List<JSONObject> orderTicketJSONList = new ArrayList<JSONObject>();
			if (orderTicketList != null && orderTicketList.size() > 0) {
				JSONObject orderTicketJson = null;
				for (OrdersTicket ot : orderTicketList) {
					orderTicketJson = new JSONObject();
					orderTicketJson.put("id", ot.getId());
					// orderTicketJson.put("ticket_id", ot.getTicket().getId());
					orderTicketJson.put("ticket_name", ot.getTicket().getTicket_name());
					orderTicketJson.put("userid", ot.getOrders().getUser().getId());
					orderTicketJson.put("username", ot.getOrders().getUser().getUsername());
					orderTicketJson.put("update_time", ot.getUpdate_time());
					Contacter c = ot.getContacter();
					orderTicketJson.put("contacter_name", c.getName());
					orderTicketJson.put("identity_card", c.getIdentity_card());
					orderTicketJson.put("status", ot.getStatus());
					orderTicketJson.put("total", ot.getTotal());
					orderTicketJSONList.add(orderTicketJson);
				}
			}
			result.put("orders_tickets", orderTicketJSONList);
			return Response.status(Response.Status.OK).entity(result).build();

		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}

	}

	@Override
	public Response setting_list(HttpServletRequest request) {
		List<Setting> list = new ArrayList<Setting>();
		String type = request.getParameter("type");
		if (!Strings.isNullOrEmpty(type)) {
			list = settingDao.getSettingListByType(Integer.parseInt(type));
		} else {
			list = settingDao.getAll();
		}
		// List<Setting> list = settingDao.getAll();
		List<JSONObject> settingJsonList = new ArrayList<JSONObject>();
		JSONObject settingJson = null;
		if (list != null && list.size() > 0) {
			for (Setting s : list) {
				settingJson = new JSONObject();
				settingJson.put("id", s.getId());
				Content c = s.getContent();
				settingJson.put("content_id", c.getId());
				settingJson.put("type", s.getType());
				settingJson.put("title", c.getTitle());
				settingJson.put("author", c.getAuthor());
				if (!Strings.isNullOrEmpty(c.getCover_image())) {
					settingJson.put("cover_image", JSONObject.fromObject(c.getCover_image()));
				}
				String summary = c.getSummary();
				if (!Strings.isNullOrEmpty(summary)) {
					settingJson.put("summary", c.getSummary());
				}
				List<ContentElement> ceList = c.getElements();
				if (ceList != null && ceList.size() > 0) {
					List<JSONObject> ceJsonList = new ArrayList<JSONObject>();
					JSONObject ceJson = null;
					for (ContentElement ce : ceList) {
						ceJson = new JSONObject();
						ceJson.put("id", ce.getId());
						ceJson.put("element", JSONObject.fromObject(ce.getElement()));
						ceJsonList.add(ceJson);
					}
					settingJson.put("elements", ceJsonList);
				}

				settingJsonList.add(settingJson);
			}
		}
		return Response.status(Response.Status.OK).entity(settingJsonList).build();
	}

	public JSONObject getContentJson(Content c, boolean is_need) {
		JSONObject contentJson = new JSONObject();
		contentJson.put("id", c.getId());
		contentJson.put("title", c.getTitle());
		if (!Strings.isNullOrEmpty(c.getAuthor())) {
			contentJson.put("author", c.getAuthor());
		}
		if (!Strings.isNullOrEmpty(c.getCover_image())) {
			contentJson.put("cover_image", JSONObject.fromObject(c.getCover_image()));
		}

		contentJson.put("create_time", c.getCreated_time());
		contentJson.put("update_time", c.getUpdate_time());
		contentJson.put("type", c.getType());
		if (is_need) {
			List<ContentElement> ceList = c.getElements();
			List<JSONObject> ceJsonList = new ArrayList<JSONObject>();
			JSONObject ceJson = null;
			if (ceList != null && ceList.size() > 0) {
				for (ContentElement ce : ceList) {
					ceJson = new JSONObject();
					ceJson.put("element", JSONObject.fromObject(ce.getElement()));
					ceJsonList.add(ceJson);
				}
				contentJson.put("elements", ceJsonList);
			}
		}
		return contentJson;

	}

	public JSONObject getContentConditionJson(Content c, boolean is_need) {
		JSONObject contentJson = new JSONObject();
		contentJson.put("content_id", c.getId());
		contentJson.put("title", c.getTitle());
		if (!Strings.isNullOrEmpty(c.getAuthor())) {
			contentJson.put("author", c.getAuthor());
		}
		if (!Strings.isNullOrEmpty(c.getCover_image())) {
			contentJson.put("cover_image", JSONObject.fromObject(c.getCover_image()));
		}

		contentJson.put("create_time", c.getCreated_time());
		contentJson.put("update_time", c.getUpdate_time());
		contentJson.put("type", c.getType());
		if (is_need) {
			List<ContentElement> ceList = c.getElements();
			List<JSONObject> ceJsonList = new ArrayList<JSONObject>();
			JSONObject ceJson = null;
			if (ceList != null && ceList.size() > 0) {
				for (ContentElement ce : ceList) {
					ceJson = new JSONObject();
					ceJson.put("element", JSONObject.fromObject(ce.getElement()));
					ceJsonList.add(ceJson);
				}
				contentJson.put("elements", ceJsonList);
			}
		}
		return contentJson;

	}

	@Override
	public Response classify_list(HttpServletRequest request, Long loginUserid) {
		List<Classify> cList = classifyDao.getAll();
		List<JSONObject> classifyJsonList = new ArrayList<JSONObject>();
		JSONObject classifyJson = null;
		if (cList != null && cList.size() > 0) {
			for (Classify c : cList) {
				classifyJson = new JSONObject();
				classifyJson.put("id", c.getId());
				classifyJson.put("classify_name", c.getClassify_name());
				classifyJsonList.add(classifyJson);
			}
		}
		return Response.status(Response.Status.OK).entity(classifyJsonList).build();
	}

	public JSONObject getPoiJSON(Poi poiModel) {
		JSONObject result = new JSONObject();
		result.put("id", poiModel.getId());
		result.put("title", poiModel.getTitle());
		if (!Strings.isNullOrEmpty(poiModel.getLocation())) {
			result.put("location", JSONObject.fromObject(poiModel.getLocation()));
		}

		Classify c = poiModel.getClassify();
		JSONObject classifyJson = new JSONObject();
		classifyJson.put("id", c.getId());
		classifyJson.put("classify_name", c.getClassify_name());
		result.put("classify", classifyJson);
		Admin admin = adminDao.get(poiModel.getAdmin_id());
		result.put("author", admin.getUsername());
		result.put("place", poiModel.getPlace());
		result.put("is_online", poiModel.getIs_online());
		result.put("create_time", poiModel.getCreate_time());
		result.put("update_time", poiModel.getUpdate_time());
		result.put("time_info", poiModel.getTime_info());
		result.put("attention", poiModel.getAttention());
		result.put("content_id", poiModel.getContent_id());
		result.put("direct_sales", poiModel.getDirect_sales());

		result.put("hot", poiModel.getHot());
		List<PoiElement> peList = poiModel.getElements();
		List<JSONObject> elementJsonList = new ArrayList<JSONObject>();
		JSONObject elementJson = null;
		if (peList != null && peList.size() > 0) {
			for (PoiElement pe : peList) {
				elementJson = JSONObject.fromObject(pe.getElement());
				elementJsonList.add(elementJson);
			}
			result.put("elements", elementJsonList);
		}

		List<Ticket> tList = poiModel.getTickets();
		List<JSONObject> tJsonList = new ArrayList<JSONObject>();
		JSONObject tJson = null;
		if (tList != null && tList.size() > 0) {
			for (Ticket t : tList) {
				tJson = new JSONObject();
				tJson.put("id", t.getId());
				tJson.put("ticket_name", t.getTicket_name());
				tJson.put("start_date", t.getStart_date());
				tJson.put("start_time", t.getStart_time());
				tJson.put("end_time", t.getEnd_time());
				tJsonList.add(tJson);
			}
			result.put("tickets", tJsonList);
		}

		List<Poi> recommandationPoiList = recommandationDao.getRecommandationListByPoiId(poiModel.getId());
		List<JSONObject> recommandationJsonList = new ArrayList<JSONObject>();
		JSONObject recommandationJson = null;
		if (recommandationPoiList != null && recommandationPoiList.size() > 0) {
			for (Poi p : recommandationPoiList) {
				recommandationJson = new JSONObject();
				recommandationJson.put("id", p.getId());
				recommandationJson.put("title", p.getTitle());
				recommandationJson.put("cover_image", JSONObject.fromObject(p.getElements().get(0).getElement()));
				recommandationJsonList.add(recommandationJson);
			}
			result.put("recommandation", recommandationJsonList);
		}
		return result;
	}

	@Override
	public Response add_notice(JSONObject content, Long loginUserid) {

		JSONObject resp = new JSONObject();
		Notice notice = new Notice();
		if (content != null) {
			if (content.containsKey("url")) {
				notice.setUrl(content.getString("url"));
			} else {
				notice.setReference_id(content.getLong("reference_id"));
			}
			notice.setType(content.getString("type"));
			notice.setAdmin_id(loginUserid);
			notice.setStatus(0);
			notice.setTitle(content.getString("title"));
			noticeDao.save(notice);
			resp = getNoticeJson(notice, loginUserid);
			// 更新redis数据
			updateHomepage();
			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}

	}

	@Override
	public Response notice_list(HttpServletRequest request, Long loginUserid) {

		String title = request.getParameter("title");
		String countStr = request.getParameter("count");
		String pageStr = request.getParameter("page");
		int count = 10;
		int page = 0;
		JSONObject error = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			List<Notice> noticeList = null;
			JSONObject result = new JSONObject();
			List<JSONObject> noticeJSONList = new ArrayList<JSONObject>();

			if (!Strings.isNullOrEmpty(title)) {
				if ((Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
					noticeList = noticeDao.getNoticeListByTitle(count, page, title);
					int total = noticeDao.getNoticeCountByTitle(title);
					result.put("total", total);
				} else if ((!Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
					count = Integer.parseInt(countStr);
					noticeList = noticeDao.getNoticeListByTitle(count, page, title);
					int total = noticeDao.getNoticeCountByTitle(title);
					result.put("total", total);
				} else if ((Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
					page = Integer.parseInt(pageStr);
					noticeList = noticeDao.getNoticeListByTitle(count, page, title);
				} else if ((!Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
					count = Integer.parseInt(countStr);
					page = Integer.parseInt(pageStr);
					noticeList = noticeDao.getNoticeListByTitle(count, page, title);
				}
			} else {
				if ((Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
					noticeList = noticeDao.getNoticeList(count, page);
					int total = noticeDao.getNoticeCount();
					result.put("total", total);
				} else if ((!Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
					count = Integer.parseInt(countStr);
					noticeList = noticeDao.getNoticeList(count, page);
					int total = noticeDao.getNoticeCount();
					result.put("total", total);
				} else if ((Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
					page = Integer.parseInt(pageStr);
					noticeList = noticeDao.getNoticeList(count, page);
				} else if ((!Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
					count = Integer.parseInt(countStr);
					page = Integer.parseInt(pageStr);
					noticeList = noticeDao.getNoticeList(count, page);
				}
			}

			if (noticeList != null && noticeList.size() > 0) {
				JSONObject noticeJson = null;
				for (Notice n : noticeList) {
					noticeJson = getNoticeJson(n, loginUserid);
					noticeJSONList.add(noticeJson);
				}
			}
			result.put("notices", noticeJSONList);
			return Response.status(Response.Status.OK).entity(result).build();
		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}

	}

	public JSONObject getNoticeJson(Notice notice, Long loginUserid) {
		JSONObject resp = new JSONObject();
		Admin admin = adminDao.get(loginUserid);
		resp.put("id", notice.getId());
		resp.put("type", notice.getType());
		resp.put("title", notice.getTitle());
		resp.put("status", notice.getStatus());
		if (notice.getType().equals("url")) {
			resp.put("url", notice.getUrl());
		} else if (notice.getType().equals("content")) {
			Content c = contentDao.get(notice.getReference_id());
			resp.put("reference_id", notice.getReference_id());
			resp.put("content_title", c.getTitle());
		} else if (notice.getType().equals("poi")) {
			Poi p = poiDao.get(notice.getReference_id());
			resp.put("reference_id", notice.getReference_id());
			resp.put("poi_title", p.getTitle());
		}

		resp.put("title", notice.getTitle());
		resp.put("author", admin.getUsername());
		resp.put("create_time", notice.getCreate_time());

		return resp;
	}

	@Override
	public Response update_notice(JSONObject notice, Long loginUserid) {
		JSONObject resp = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			if (notice != null) {
				Long id = notice.getLong("id");
				Notice n = noticeDao.get(id);
				n.setStatus(notice.getInt("status"));
				noticeDao.update(n);
				resp.put("id", n.getId());
				resp.put("status", n.getStatus());
				// 更新redis数据
				updateHomepage();
				return Response.status(Response.Status.OK).entity(resp).build();
			} else {
				resp.put("status", "缺少参数");
				resp.put("code", 10001);
				resp.put("error_message", "缺少参数");
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
	public Response add_message(JSONObject content, Long loginUserid) throws Exception {
		JSONObject resp = new JSONObject();
		SendMessage se = new SendMessage();
		if (content != null) {
			if (content.containsKey("type") && !Strings.isNullOrEmpty(content.getString("type"))) {
				String type = content.getString("type");
				se.setType(type);
				if (type.equals("url")) {
					se.setUrl(content.getString("url"));
				} else if (type.equals("content") || type.equals("poi")) {
					se.setReference_id(content.getLong("reference_id"));
				}
			}
			se.setAdmin_id(loginUserid);
			se.setTitle(content.getString("title"));
			int send_type = content.getInt("send_type");
			if (send_type == 0) {
				se.setSend_type(send_type);
			} else {
				se.setSend_type(send_type);
				if (content.containsKey("send_time") && !Strings.isNullOrEmpty(content.getString("send_time"))) {
					se.setSend_time(content.getString("send_time"));
				}

			}

			if (content.containsKey("description") && !Strings.isNullOrEmpty(content.getString("description"))) {
				se.setDescription(content.getString("description"));
			}

			sendMessageDao.save(se);
			String type = se.getType();
			List<User> userList = userDao.getAll();
			final List<PushNotification> pnList = new ArrayList<PushNotification>();
			Notification n = null;
			List<Notification> notificationList = new ArrayList<Notification>();
			if ((userList != null) && (userList.size() > 0)) {
				for (User admin : userList) {
					n = new Notification();
					n.setRecipientId(admin.getId());
					n.setNotificationType(0);
					n.setTitle(se.getDescription());
					if (type.equals("url")) {
						n.setObjectType(2);
						n.setReference_url(se.getUrl());
					} else if (type.equals("content")) {
						n.setObjectType(4);
						n.setObjectId(se.getReference_id());
					} else if (type.equals("poi")) {
						n.setObjectType(3);
						n.setObjectId(se.getReference_id());
					} else if (type.equals("normal")) {
						n.setObjectType(5);
					}
					n.setStatus("enable");
					n.setRead_already(false);
					notificationList.add(n);
				}
			}
			this.notificationDao.saveNotifications(notificationList);
			List<PushNotification> list = this.pushNotificationDao.getPushNotification();
			pnList.addAll(list);
			final String content_info = se.getTitle();
			final JSONObject json = new JSONObject();
			if (type.equals("url")) {
				json.put("type", se.getType());
				json.put("url", se.getUrl());
			} else if (type.equals("content")) {
				json.put("type", "content");
				json.put("content_id", se.getReference_id());
			} else if (type.equals("poi")) {
				json.put("type", "poi");
				json.put("poi_id", se.getReference_id());
			} else if (type.equals("normal")) {
				json.put("type", "normal");
			}
			json.put("title", se.getTitle());
			if (!Strings.isNullOrEmpty(se.getDescription())) {
				json.put("description", se.getDescription());
			}
			final GetuiModel gm = getGetuiInfo();
			if (send_type == 0) {

				PushNotificationUtil.pushInfoAll(gm.getAppId(), gm.getAppKey(), gm.getMasterSecret(), pnList,
						content_info, json.toString());
			} else if (send_type == 1) {
				String date = se.getSend_time();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date d = (Date) sdf.parse(date);
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						try {
							System.out.println("getui------->>>" + gm.getAppId() + "---" + content_info);
							PushNotificationUtil.pushInfoAll(gm.getAppId(), gm.getAppKey(), gm.getMasterSecret(),
									pnList, content_info, json.toString());

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, d);
			}
			resp = getSendMessageJson(se, loginUserid);
			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}

	}

	@Override
	public Response message_list(HttpServletRequest request, Long loginUserid) {

		String title = request.getParameter("title");
		String countStr = request.getParameter("count");
		String pageStr = request.getParameter("page");
		int count = 10;
		int page = 0;
		JSONObject error = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			List<SendMessage> seList = null;
			JSONObject result = new JSONObject();
			List<JSONObject> seJSONList = new ArrayList<JSONObject>();

			if (!Strings.isNullOrEmpty(title)) {
				if ((Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
					seList = sendMessageDao.getSendMessageListByTitle(count, page, title);
					int total = sendMessageDao.getSendMessageCountByTitle(title);
					result.put("total", total);
				} else if ((!Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
					count = Integer.parseInt(countStr);
					seList = sendMessageDao.getSendMessageListByTitle(count, page, title);
					int total = sendMessageDao.getSendMessageCountByTitle(title);
					result.put("total", total);
				} else if ((Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
					page = Integer.parseInt(pageStr);
					seList = sendMessageDao.getSendMessageListByTitle(count, page, title);
				} else if ((!Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
					count = Integer.parseInt(countStr);
					page = Integer.parseInt(pageStr);
					seList = sendMessageDao.getSendMessageListByTitle(count, page, title);
				}
			} else {
				if ((Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
					seList = sendMessageDao.getSendMessageList(count, page);
					int total = sendMessageDao.getSendMessageCount();
					result.put("total", total);
				} else if ((!Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
					count = Integer.parseInt(countStr);
					seList = sendMessageDao.getSendMessageList(count, page);
					int total = sendMessageDao.getSendMessageCount();
					result.put("total", total);
				} else if ((Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
					page = Integer.parseInt(pageStr);
					seList = sendMessageDao.getSendMessageList(count, page);
				} else if ((!Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
					count = Integer.parseInt(countStr);
					page = Integer.parseInt(pageStr);
					seList = sendMessageDao.getSendMessageList(count, page);
				}
			}

			if (seList != null && seList.size() > 0) {
				JSONObject seJson = null;
				for (SendMessage se : seList) {
					seJson = getSendMessageJson(se, loginUserid);
					seJSONList.add(seJson);
				}
			}
			result.put("sendMessages", seJSONList);
			return Response.status(Response.Status.OK).entity(result).build();
		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}

	}

	public JSONObject getSendMessageJson(SendMessage se, Long loginUserid) {

		JSONObject resp = new JSONObject();
		Admin admin = adminDao.get(se.getAdmin_id());
		resp.put("id", se.getId());

		resp.put("title", se.getTitle());
		int send_type = se.getSend_type();
		if (send_type == 0) {
			resp.put("send_type", se.getSend_type());
			resp.put("create_time", se.getCreate_time());
		} else {
			resp.put("send_type", se.getSend_type());
			resp.put("send_time", se.getSend_time());
		}

		String type = se.getType();
		if (type.equals("url")) {
			resp.put("url", se.getUrl());
			resp.put("info", se.getUrl());
		} else if (type.equals("content")) {
			Content c = contentDao.get(se.getReference_id());
			resp.put("info", c.getTitle());
			resp.put("reference_id", se.getReference_id());
		} else if (type.equals("poi")) {
			Poi p = poiDao.get(se.getReference_id());
			resp.put("info", p.getTitle());
			resp.put("reference_id", se.getReference_id());
		}
		resp.put("type", type);
		resp.put("author", admin.getUsername());

		return resp;

	}

	@Override
	public Response export_user(HttpServletRequest request, HttpServletResponse response, Long loginUserid) {
		String type = request.getParameter("level");
		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		List<User> userList = userDao.getUserListByLevel(type,start_time,end_time);
		// 适用List集合造一些数据作为要导出的数据

		List<JSONObject> list = new ArrayList<JSONObject>();
		JSONObject userJson = null;
		if (userList != null && userList.size() > 0) {
			for (User u : userList) {
				userJson = getUserJson(u);
				list.add(userJson);
			}
		}
		// 调用方法创建HSSFWorkbook工作簿对象
		HSSFWorkbook wb = export_user_excel(list);
		try {
			// 定义导出文件的名称
			String fileName = new String("会员信息.xls".getBytes("UTF-8"), "ISO-8859-1");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			OutputStream os = response.getOutputStream();

			// 将工作薄写入到输出流中
			wb.write(os);
			os.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
		return null;
	}

	// 创建HSSFWorkbook工作薄对象
	public static HSSFWorkbook export_user_excel(List<JSONObject> list) {
		try {
			// 创建工作薄对象
			HSSFWorkbook wb = new HSSFWorkbook();
			// 创建标题行样式
			HSSFCellStyle headStyle = headStyle(wb);
			// 创建内容行样式
			HSSFCellStyle contentStyle = contentStyle(wb);

			// 创建表
			HSSFSheet sheet_1 = wb.createSheet("人员信息");
			// 设置表的默认列宽
			sheet_1.setDefaultColumnWidth(30);

			// 创建标题行
			HSSFRow headRow = sheet_1.createRow(0);
			HSSFCell head_cell_1 = headRow.createCell(0); // 创建标题行第一列
			head_cell_1.setCellValue("会员编号"); // 第一列内容
			head_cell_1.setCellStyle(headStyle); // 将标题行样式添加

			HSSFCell head_cell_2 = headRow.createCell(1);
			head_cell_2.setCellValue("姓名");
			head_cell_2.setCellStyle(headStyle);

			HSSFCell head_cell_3 = headRow.createCell(2);
			head_cell_3.setCellValue("手机号");
			head_cell_3.setCellStyle(headStyle);

			HSSFCell head_cell_4 = headRow.createCell(3);
			head_cell_4.setCellValue("身份证号");
			head_cell_4.setCellStyle(headStyle);

			HSSFCell head_cell_5 = headRow.createCell(4);
			head_cell_5.setCellValue("出生日期");
			head_cell_5.setCellStyle(headStyle);

			HSSFCell head_cell_6 = headRow.createCell(5);
			head_cell_6.setCellValue("行业");
			head_cell_6.setCellStyle(headStyle);

			HSSFCell head_cell_7 = headRow.createCell(6);
			head_cell_7.setCellValue("俱乐部");
			head_cell_7.setCellStyle(headStyle);

			HSSFCell head_cell_8 = headRow.createCell(7);
			head_cell_8.setCellValue("用户等级");
			head_cell_8.setCellStyle(headStyle);

			HSSFCell head_cell_9 = headRow.createCell(8);
			head_cell_9.setCellValue("注册时间");
			head_cell_9.setCellStyle(headStyle);

			HSSFCell head_cell_10 = headRow.createCell(9);
			head_cell_10.setCellValue("地址");
			head_cell_10.setCellStyle(headStyle);

			HSSFCell head_cell_11 = headRow.createCell(10);
			head_cell_11.setCellValue("性别");
			head_cell_11.setCellStyle(headStyle);

			 HSSFCell head_cell_12 = headRow.createCell(11);
			 head_cell_12.setCellValue("保险单号");
			 head_cell_12.setCellStyle(headStyle);

			// 为内容行添加数据和样式
			for (int i = 1; i <= list.size(); i++) {
				JSONObject uJson = list.get(i - 1);
				HSSFRow contentRow = sheet_1.createRow(i);
				HSSFCell content_cell_1 = contentRow.createCell(0);
				content_cell_1.setCellValue(uJson.getString("id"));
				content_cell_1.setCellStyle(contentStyle);

				HSSFCell content_cell_2 = contentRow.createCell(1);
				if (uJson.containsKey("username") && !Strings.isNullOrEmpty(uJson.getString("username"))) {
					content_cell_2.setCellValue(uJson.getString("username"));
				} else {
					content_cell_2.setCellValue("");
				}

				content_cell_2.setCellStyle(contentStyle);

				HSSFCell content_cell_3 = contentRow.createCell(2);
				if (uJson.containsKey("phone") && !Strings.isNullOrEmpty(uJson.getString("phone"))) {
					content_cell_3.setCellValue(uJson.getString("phone"));
				} else {
					content_cell_3.setCellValue("");
				}
				content_cell_3.setCellStyle(contentStyle);

				HSSFCell content_cell_4 = contentRow.createCell(3);

				if (uJson.containsKey("identity_card") && !Strings.isNullOrEmpty(uJson.getString("identity_card"))) {
					content_cell_4.setCellValue(uJson.getString("identity_card"));
				} else {
					content_cell_4.setCellValue("");
				}
				content_cell_4.setCellStyle(contentStyle);

				HSSFCell content_cell_5 = contentRow.createCell(4);
				if (uJson.containsKey("birthday") && !Strings.isNullOrEmpty(uJson.getString("birthday"))) {
					content_cell_5.setCellValue(uJson.getString("birthday"));
				} else {
					content_cell_5.setCellValue("");
				}
				content_cell_5.setCellStyle(contentStyle);

				HSSFCell content_cell_6 = contentRow.createCell(5);
				if (uJson.containsKey("trade") && !Strings.isNullOrEmpty(uJson.getString("trade"))) {
					content_cell_6.setCellValue(uJson.getString("trade"));
				} else {
					content_cell_6.setCellValue("");
				}
				content_cell_6.setCellStyle(contentStyle);

				HSSFCell content_cell_7 = contentRow.createCell(6);
				if (uJson.containsKey("club_name") && !Strings.isNullOrEmpty(uJson.getString("club_name"))) {
					content_cell_7.setCellValue(uJson.getString("club_name"));
				} else {
					content_cell_7.setCellValue("");
				}
				content_cell_7.setCellStyle(contentStyle);

				HSSFCell content_cell_8 = contentRow.createCell(7);
				if (uJson.containsKey("level") && !Strings.isNullOrEmpty(uJson.getString("level"))) {
					String level = uJson.getString("level");
					if (level.equals("normal")) {
						content_cell_8.setCellValue("普通用户");
					} else if (level.equals("vip")) {
						content_cell_8.setCellValue("VIP用户");
					}

				} else {
					content_cell_8.setCellValue("");
				}
				content_cell_8.setCellStyle(contentStyle);
				
				String create_time = uJson.getString("create_time");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date(Long.parseLong(create_time) * 1000);
				String date_str = sdf.format(date);
				HSSFCell content_cell_9 = contentRow.createCell(8);
				content_cell_9.setCellValue(date_str);
				content_cell_9.setCellStyle(contentStyle);

				HSSFCell content_cell_10 = contentRow.createCell(9);
				if (uJson.containsKey("address") && !Strings.isNullOrEmpty(uJson.getString("address"))) {
					content_cell_10.setCellValue(uJson.getString("address"));
				} else {
					content_cell_10.setCellValue("");
				}
				content_cell_10.setCellStyle(contentStyle);

				HSSFCell content_cell_11 = contentRow.createCell(10);
				if (uJson.containsKey("gender") && !Strings.isNullOrEmpty(uJson.getString("gender"))) {
					content_cell_11.setCellValue(uJson.getString("gender"));
				} else {
					content_cell_11.setCellValue("");
				}
				content_cell_11.setCellStyle(contentStyle);
				
				HSSFCell content_cell_12 = contentRow.createCell(11);
				if (uJson.containsKey("insurant_no") && !Strings.isNullOrEmpty(uJson.getString("insurant_no"))) {
					content_cell_12.setCellValue(uJson.getString("insurant_no"));
				} else {
					content_cell_12.setCellValue("");
				}
				content_cell_12.setCellStyle(contentStyle);
			}

			return wb;
		} catch (Exception e) {
			e.getStackTrace();
		}

		return null;
	}

	// 创建HSSFWorkbook工作薄对象
	public static HSSFWorkbook export_insure_excel(List<OrdersTicket> list) {
		try {
			// 创建工作薄对象
			HSSFWorkbook wb = new HSSFWorkbook();
			// 创建标题行样式
			HSSFCellStyle headStyle = headStyle(wb);
			// 创建内容行样式
			HSSFCellStyle contentStyle = contentStyle(wb);

			// 创建表
			HSSFSheet sheet_1 = wb.createSheet("投保列表");
			// 设置表的默认列宽
			sheet_1.setDefaultColumnWidth(30);

			// 创建标题行
			HSSFRow headRow = sheet_1.createRow(0);
			HSSFCell head_cell_1 = headRow.createCell(0); // 创建标题行第一列
			head_cell_1.setCellValue("订单号"); // 第一列内容
			head_cell_1.setCellStyle(headStyle); // 将标题行样式添加

			HSSFCell head_cell_2 = headRow.createCell(1);
			head_cell_2.setCellValue("流水号");
			head_cell_2.setCellStyle(headStyle);

			HSSFCell head_cell_3 = headRow.createCell(2);
			head_cell_3.setCellValue("投保时间");
			head_cell_3.setCellStyle(headStyle);

			HSSFCell head_cell_4 = headRow.createCell(3);
			head_cell_4.setCellValue("被保人姓名");
			head_cell_4.setCellStyle(headStyle);

			HSSFCell head_cell_5 = headRow.createCell(4);
			head_cell_5.setCellValue("身份证号");
			head_cell_5.setCellStyle(headStyle);

			HSSFCell head_cell_6 = headRow.createCell(5);
			head_cell_6.setCellValue("性别");
			head_cell_6.setCellStyle(headStyle);

			HSSFCell head_cell_7 = headRow.createCell(6);
			head_cell_7.setCellValue("保险号");
			head_cell_7.setCellStyle(headStyle);

			HSSFCell head_cell_8 = headRow.createCell(7);
			head_cell_8.setCellValue("保险开始日期");
			head_cell_8.setCellStyle(headStyle);

			HSSFCell head_cell_9 = headRow.createCell(8);
			head_cell_9.setCellValue("保险结束日期");
			head_cell_9.setCellStyle(headStyle);

			// 为内容行添加数据和样式
			for (int i = 1; i <= list.size(); i++) {
				OrdersTicket ot = list.get(i - 1);
				HSSFRow contentRow = sheet_1.createRow(i);
				HSSFCell content_cell_1 = contentRow.createCell(0);
				content_cell_1.setCellValue(ot.getTicket_order_no());
				content_cell_1.setCellStyle(contentStyle);
				Contacter c = ot.getContacter();
				HSSFCell content_cell_2 = contentRow.createCell(1);
				content_cell_2.setCellValue(ot.getTransNo());
				content_cell_2.setCellStyle(contentStyle);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date d = new Date(ot.getUpdate_time() * 1000);
				String create_time = sdf.format(d);
				HSSFCell content_cell_3 = contentRow.createCell(2);
				content_cell_3.setCellValue(create_time);
				content_cell_3.setCellStyle(contentStyle);

				HSSFCell content_cell_4 = contentRow.createCell(3);
				content_cell_4.setCellValue(c.getName());
				content_cell_4.setCellStyle(contentStyle);

				HSSFCell content_cell_5 = contentRow.createCell(4);
				content_cell_5.setCellValue(c.getIdentity_card());
				content_cell_5.setCellStyle(contentStyle);

				HSSFCell content_cell_6 = contentRow.createCell(5);
				content_cell_6.setCellValue(c.getGender());
				content_cell_6.setCellStyle(contentStyle);

				HSSFCell content_cell_7 = contentRow.createCell(6);
				content_cell_7.setCellValue(ot.getInsureNum());
				content_cell_7.setCellStyle(contentStyle);

				HSSFCell content_cell_8 = contentRow.createCell(7);
				content_cell_8.setCellValue(ot.getStartDate());
				content_cell_8.setCellStyle(contentStyle);

				HSSFCell content_cell_9 = contentRow.createCell(8);
				content_cell_9.setCellValue(ot.getEndDate());
				content_cell_9.setCellStyle(contentStyle);
			}

			return wb;
		} catch (Exception e) {
			e.getStackTrace();
		}

		return null;
	}
	
	@Override
	public Response export_order(HttpServletRequest request, HttpServletResponse response, Long loginUserid) {
		String type = request.getParameter("status"); // 已付款 paid 未付款 unpaid
		String start_date = request.getParameter("start_date");
		String end_date = request.getParameter("end_date");
		String order_no = request.getParameter("order_no");
		List<Orders> ordersList = ordersDao.getOrdersListByParams(type, start_date, end_date,order_no);

		// 调用方法创建HSSFWorkbook工作簿对象
		HSSFWorkbook wb = export_orders_excel(ordersList);
		try {
			// 定义导出文件的名称
			String fileName = new String("订单信息.xls".getBytes("UTF-8"), "ISO-8859-1");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			OutputStream os = response.getOutputStream();

			// 将工作薄写入到输出流中
			wb.write(os);
			os.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
		return null;
	}

	// 创建HSSFWorkbook工作薄对象
	public static HSSFWorkbook export_orders_excel(List<Orders> list) {
		try {
			// 创建工作薄对象
			HSSFWorkbook wb = new HSSFWorkbook();
			// 创建标题行样式
			HSSFCellStyle headStyle = headStyle(wb);
			// 创建内容行样式
			HSSFCellStyle contentStyle = contentStyle(wb);

			// 创建表
			HSSFSheet sheet_1 = wb.createSheet("订单列表");
			// 设置表的默认列宽
			sheet_1.setDefaultColumnWidth(30);

			// 创建标题行
			HSSFRow headRow = sheet_1.createRow(0);
			HSSFCell head_cell_1 = headRow.createCell(0); // 创建标题行第一列
			head_cell_1.setCellValue("订单号"); // 第一列内容
			head_cell_1.setCellStyle(headStyle); // 将标题行样式添加

			HSSFCell head_cell_2 = headRow.createCell(1);
			head_cell_2.setCellValue("下单时间");
			head_cell_2.setCellStyle(headStyle);

			HSSFCell head_cell_3 = headRow.createCell(2);
			head_cell_3.setCellValue("用户姓名");
			head_cell_3.setCellStyle(headStyle);

			HSSFCell head_cell_4 = headRow.createCell(3);
			head_cell_4.setCellValue("用户ID");
			head_cell_4.setCellStyle(headStyle);

			HSSFCell head_cell_5 = headRow.createCell(4);
			head_cell_5.setCellValue("订单状态");
			head_cell_5.setCellStyle(headStyle);

			HSSFCell head_cell_6 = headRow.createCell(5);
			head_cell_6.setCellValue("总金额");
			head_cell_6.setCellStyle(headStyle);

			HSSFCell head_cell_7 = headRow.createCell(6);
			head_cell_7.setCellValue("支付方式");
			head_cell_7.setCellStyle(headStyle);

//			HSSFCell head_cell_8 = headRow.createCell(7);
//			head_cell_8.setCellValue("保险开始日期");
//			head_cell_8.setCellStyle(headStyle);
//
//			HSSFCell head_cell_9 = headRow.createCell(8);
//			head_cell_9.setCellValue("保险结束日期");
//			head_cell_9.setCellStyle(headStyle);

			// 为内容行添加数据和样式
			for (int i = 1; i <= list.size(); i++) {
				Orders ot = list.get(i - 1);
				HSSFRow contentRow = sheet_1.createRow(i);
				HSSFCell content_cell_1 = contentRow.createCell(0);
				content_cell_1.setCellValue(ot.getOrder_no());
				content_cell_1.setCellStyle(contentStyle);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d = new Date(ot.getCreate_time() * 1000);
				String create_time = sdf.format(d);
				HSSFCell content_cell_2 = contentRow.createCell(1);
				content_cell_2.setCellValue(create_time);
				content_cell_2.setCellStyle(contentStyle);

				User user = ot.getUser();
				HSSFCell content_cell_3 = contentRow.createCell(2);
				content_cell_3.setCellValue(user.getUsername());
				content_cell_3.setCellStyle(contentStyle);

				HSSFCell content_cell_4 = contentRow.createCell(3);
				content_cell_4.setCellValue(user.getId());
				content_cell_4.setCellStyle(contentStyle);

				//订单状态
				HSSFCell content_cell_5 = contentRow.createCell(4);
				int status = ot.getStatus();
				if(status == 0){
					content_cell_5.setCellValue("未支付");
				}else if(status == 1){
					content_cell_5.setCellValue("已支付");
				}else if(status == 2){
					content_cell_5.setCellValue("已取消");
				}else if(status == 3){
					content_cell_5.setCellValue("退款中");
				}else if(status == 4){
					content_cell_5.setCellValue("退款失败");
				}else if(status == 5){
					content_cell_5.setCellValue("退款成功");
				}
				
				content_cell_5.setCellStyle(contentStyle);

				HSSFCell content_cell_6 = contentRow.createCell(5);
				content_cell_6.setCellValue(ot.getAmount().toString());
				content_cell_6.setCellStyle(contentStyle);
				
				
				HSSFCell content_cell_7 = contentRow.createCell(6);
				if(!Strings.isNullOrEmpty(ot.getPay_type())){
					String pay_type = ot.getPay_type();
					if(pay_type.equals("wx")){
						content_cell_7.setCellValue("微信支付");
					}else if(pay_type.equals("wx_pub")){
						content_cell_7.setCellValue("微信公众号支付");
					}else if(pay_type.equals("alipay")){
						content_cell_7.setCellValue("支付宝支付");
					}
				}else{
					content_cell_7.setCellValue("");
				}
				
				content_cell_7.setCellStyle(contentStyle);

			}

			return wb;
		} catch (Exception e) {
			e.getStackTrace();
		}

		return null;
	}
	
	@Override
	public Response export_orders_ticket_statistics(HttpServletRequest request,HttpServletResponse response, Long loginUserid) {

		String type = request.getParameter("status"); // 0 未使用 1 已使用 2 退票中 3 已退票
														// 4 已拒绝
		String search = request.getParameter("search");
		String poiIdStr = request.getParameter("poi_id");
		List<OrdersTicket> list = ordersTicketDao.getOrdersTicketListByStatistics(poiIdStr, type, search);
		// 调用方法创建HSSFWorkbook工作簿对象
		HSSFWorkbook wb = export_orders_ticket_excel(list);
		try {
			// 定义导出文件的名称
			String fileName = new String("票务统计.xls".getBytes("UTF-8"), "ISO-8859-1");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			OutputStream os = response.getOutputStream();
			// 将工作薄写入到输出流中
			wb.write(os);
			os.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
		return null;
	
	}
	
	
	// 创建HSSFWorkbook工作薄对象
		public static HSSFWorkbook export_orders_ticket_excel(List<OrdersTicket> list) {
			try {
				// 创建工作薄对象
				HSSFWorkbook wb = new HSSFWorkbook();
				// 创建标题行样式
				HSSFCellStyle headStyle = headStyle(wb);
				// 创建内容行样式
				HSSFCellStyle contentStyle = contentStyle(wb);

				// 创建表
				HSSFSheet sheet_1 = wb.createSheet("票务统计");
				// 设置表的默认列宽
				sheet_1.setDefaultColumnWidth(30);

				// 创建标题行
				HSSFRow headRow = sheet_1.createRow(0);
				HSSFCell head_cell_1 = headRow.createCell(0); // 创建标题行第一列
				head_cell_1.setCellValue("ID"); // 第一列内容
				head_cell_1.setCellStyle(headStyle); // 将标题行样式添加

				HSSFCell head_cell_2 = headRow.createCell(1);
				head_cell_2.setCellValue("下单时间");
				head_cell_2.setCellStyle(headStyle);

				HSSFCell head_cell_3 = headRow.createCell(2);
				head_cell_3.setCellValue("用户姓名");
				head_cell_3.setCellStyle(headStyle);

				HSSFCell head_cell_4 = headRow.createCell(3);
				head_cell_4.setCellValue("用户ID");
				head_cell_4.setCellStyle(headStyle);

				HSSFCell head_cell_5 = headRow.createCell(4);
				head_cell_5.setCellValue("联系人姓名");
				head_cell_5.setCellStyle(headStyle);

				HSSFCell head_cell_6 = headRow.createCell(5);
				head_cell_6.setCellValue("联系人身份证号");
				head_cell_6.setCellStyle(headStyle);

				HSSFCell head_cell_7 = headRow.createCell(6);
				head_cell_7.setCellValue("票券名称");
				head_cell_7.setCellStyle(headStyle);

				HSSFCell head_cell_8 = headRow.createCell(7);
				head_cell_8.setCellValue("金额");
				head_cell_8.setCellStyle(headStyle);
	
				HSSFCell head_cell_9 = headRow.createCell(8);
				head_cell_9.setCellValue("票状态");
				head_cell_9.setCellStyle(headStyle);

				// 为内容行添加数据和样式
				for (int i = 1; i <= list.size(); i++) {
					OrdersTicket ot = list.get(i - 1);
					HSSFRow contentRow = sheet_1.createRow(i);
					HSSFCell content_cell_1 = contentRow.createCell(0);
					content_cell_1.setCellValue(ot.getId());
					content_cell_1.setCellStyle(contentStyle);
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date d = new Date(ot.getUpdate_time() * 1000);
					String create_time = sdf.format(d);
					HSSFCell content_cell_2 = contentRow.createCell(1);
					content_cell_2.setCellValue(create_time);
					content_cell_2.setCellStyle(contentStyle);

					User user = ot.getOrders().getUser();
					HSSFCell content_cell_3 = contentRow.createCell(2);
					content_cell_3.setCellValue(user.getUsername());
					content_cell_3.setCellStyle(contentStyle);

					HSSFCell content_cell_4 = contentRow.createCell(3);
					content_cell_4.setCellValue(user.getId());
					content_cell_4.setCellStyle(contentStyle);

					Contacter c = ot.getContacter();
					HSSFCell content_cell_5 = contentRow.createCell(4);
					content_cell_5.setCellValue(c.getName());
					content_cell_5.setCellStyle(contentStyle);

					HSSFCell content_cell_6 = contentRow.createCell(5);
					content_cell_6.setCellValue(c.getIdentity_card());
					content_cell_6.setCellStyle(contentStyle);
					
					Ticket t = ot.getTicket();
					HSSFCell content_cell_7 = contentRow.createCell(6);
					content_cell_7.setCellValue(t.getTicket_name());
					content_cell_7.setCellStyle(contentStyle);
					
					HSSFCell content_cell_8 = contentRow.createCell(7);
					content_cell_8.setCellValue(ot.getTotal().toString());
					content_cell_8.setCellStyle(contentStyle);
					
					int status = ot.getStatus();
					HSSFCell content_cell_9 = contentRow.createCell(9);
					content_cell_9.setCellValue(ot.getTotal().toString());
					if(status == 0){
						content_cell_9.setCellValue("未使用");
					}else if(status == 1){
						content_cell_9.setCellValue("已使用");
					}else if(status == 2){
						content_cell_9.setCellValue("退票中");
					}else if(status == 3){
						content_cell_9.setCellValue("已退票");
					}else if(status == 4){
						content_cell_9.setCellValue("已拒绝");
					}else if(status == 5){
						content_cell_9.setCellValue("退票申请被拒绝");
					}else if(status == 6){
						content_cell_9.setCellValue("已退款");
					}else if(status == 7){
						content_cell_9.setCellValue("未支付");
					}
					content_cell_9.setCellStyle(contentStyle);
				}

				return wb;
			} catch (Exception e) {
				e.getStackTrace();
			}

			return null;
		}

	/**
	 * 创建标题行样式
	 * 
	 * @param wb
	 * @return
	 */
	public static HSSFCellStyle headStyle(HSSFWorkbook wb) {
		HSSFCellStyle headStyle = wb.createCellStyle(); // 创建样式对象
		HSSFFont headFont = wb.createFont(); // 创建字体
		headFont.setFontName("微软雅黑");
		headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headFont.setColor(HSSFFont.COLOR_RED);

		headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headStyle.setFont(headFont);
		return headStyle;
	}

	/**
	 * 创建内容行样式
	 * 
	 * @param wb
	 * @return
	 */
	public static HSSFCellStyle contentStyle(HSSFWorkbook wb) {
		HSSFCellStyle contentStyle = wb.createCellStyle();
		HSSFFont contentFont = wb.createFont();
		contentFont.setFontName("微软雅黑");
		contentFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		contentFont.setColor(HSSFFont.COLOR_NORMAL);

		contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		contentStyle.setFont(contentFont);
		return contentStyle;
	}

	public JSONObject getUserJson(User u) {
		JSONObject userJson = new JSONObject();
		userJson.put("id", u.getId());
		userJson.put("username", u.getUsername());
		userJson.put("phone", u.getPhone());
		userJson.put("birthday", u.getBirthday());
		userJson.put("identity_card", u.getIdentity_card());
		if (!Strings.isNullOrEmpty(u.getTrade())) {
			userJson.put("trade", u.getTrade());
		}

		if (!Strings.isNullOrEmpty(u.getClub_name())) {
			userJson.put("club_name", u.getClub_name());
		}
		userJson.put("level", u.getLevel());
		userJson.put("create_time", u.getCreated_time());
		userJson.put("zone", u.getZone());
		userJson.put("password", u.getPassword());
		userJson.put("address", u.getAddress());
		userJson.put("gender", u.getGender());
		userJson.put("resource", u.getResource());
		userJson.put("insurant_no", u.getNormal_barcode());
		if(!Strings.isNullOrEmpty(u.getTmp_pass())){
			userJson.put("tmp_pass", u.getTmp_pass());
		}
		
		return userJson;
	}
	
	public JSONObject getAdmimJson(Admin admin) {
		JSONObject userJson = new JSONObject();
		userJson.put("id", admin.getId());
		userJson.put("username", admin.getUsername());
		return userJson;
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

	@Override
	public Response add_ticket(JSONObject ticket, Long loginUserid) {
		JSONObject resp = new JSONObject();
		if (ticket != null) {
			String type = ticket.getString("ticket_type");
			boolean bool = false;
			if(type.equals("vip")){
				List<Ticket> ticketList = ticketDao.getTicketByTypeAndStatus(type);
				if(ticketList != null && ticketList.size() > 0){
					resp.put("status", "已经存在VIP票，请修改或删除后添加");
					resp.put("code", 10302);
					resp.put("error_message", "已经存在VIP票，请修改或删除后添加");
					return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
				}else{
					bool = true;
				}
			}else{
				bool = true;
			}
			if(bool){
				Ticket t = new Ticket();

				t.setTicket_name(ticket.getString("ticket_name"));
				t.setNumber(ticket.getInt("number"));
//				t.setZyb_code(ticket.getString("zyb_code"));
				t.setStart_date(ticket.getString("start_date"));
				t.setStart_time(ticket.getString("start_time"));
				t.setEnd_time(ticket.getString("end_time"));
				t.setStatus("enable");
				t.setSold(0);
				t.setType(type);
				if(type.equals("hotel")){
					t.setEnd_date(ticket.getString("end_date"));
				}
//				t.setIs_insurance(1);
				t.setChecking_rule(ticket.getString("checking_rule"));
				t.setPrice(BigDecimal.valueOf(Double.parseDouble(ticket.getString("price"))));
				Admin admin = adminDao.get(loginUserid);
				t.setAdmin(admin);
				ticketDao.save(t);
				try {
					resp = getTicketJson(t);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return Response.status(Response.Status.OK).entity(resp).build();
			}else{
				return null;
			}

		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	public JSONObject getTicketJson(Ticket t) throws Exception {
		JSONObject ticketJson = new JSONObject();
		ticketJson.put("id", t.getId());
//		ticketJson.put("zyb_code", t.getZyb_code());
		ticketJson.put("ticket_name", t.getTicket_name());
		ticketJson.put("number", t.getNumber());
		ticketJson.put("sold", t.getSold());
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		// String start = t.getStart_date()+" "+t.getStart_time();
		// String end = t.getStart_date() + " "+ t.getEnd_time();
		// Date sDate = sdf.parse(start);
		// Date eDate = sdf.parse(end);
		ticketJson.put("start_date", t.getStart_date());
		ticketJson.put("start_time", t.getStart_time());
		ticketJson.put("end_time", t.getEnd_time());
		ticketJson.put("end_date", t.getEnd_date());
		ticketJson.put("price", t.getPrice());
		ticketJson.put("ticket_type", t.getType());
		ticketJson.put("checking_rule", t.getChecking_rule());
//		ticketJson.put("insurance_price", t.getInsurance_price());
		Poi poi = t.getPoi();
		JSONObject poiJson = null;
		if (poi != null) {
			poiJson = new JSONObject();
			poiJson.put("id", poi.getId());
			poiJson.put("title", poi.getTitle());
			ticketJson.put("poi", poiJson);
		}

		return ticketJson;
	}

	@Override
	public Response update_ticket(JSONObject ticket, Long loginUserid) {
		JSONObject resp = new JSONObject();
		if (ticket != null) {

			if (ticket.containsKey("status") && !Strings.isNullOrEmpty(ticket.getString("status"))) {
				ticketDao.updateTicket(ticket.getLong("id"), ticket.getString("status"));
				resp.put("id", ticket.getLong("id"));
				resp.put("status", ticket.getString("status"));
			} else {
				Ticket t = ticketDao.get(ticket.getLong("id"));
				t.setTicket_name(ticket.getString("ticket_name"));
				t.setNumber(ticket.getInt("number"));
//				t.setZyb_code(ticket.getString("zyb_code"));
				t.setStart_date(ticket.getString("start_date"));
				t.setStart_time(ticket.getString("start_time"));
				t.setEnd_time(ticket.getString("end_time"));
				t.setStatus("enable");
				t.setPrice(BigDecimal.valueOf(Double.parseDouble(ticket.getString("price"))));
				t.setChecking_rule(ticket.getString("checking_rule"));
				Admin admin = adminDao.get(loginUserid);
				t.setAdmin(admin);
				ticketDao.update(t);
				try {
					resp = getTicketJson(t);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	@Override
	public Response ticket_list(HttpServletRequest request, Long loginUserid) throws Exception {

		String countStr = request.getParameter("count");
		String pageStr = request.getParameter("page");
		String ticket_name = request.getParameter("ticket_name");
		String zyb_code = request.getParameter("zyb_code");
		String poiIdStr = request.getParameter("poi_id");
		int count = 10;
		int page = 0;
		JSONObject error = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			List<Ticket> ticketList = null;
			JSONObject result = new JSONObject();
			List<JSONObject> ticketJSONList = new ArrayList<JSONObject>();

			if ((Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
				ticketList = ticketDao.getTicketByParams(count, page, ticket_name, zyb_code, poiIdStr);
				int total = ticketDao.getTicketByParamsCount(count, page, ticket_name, zyb_code, poiIdStr);

				List<Poi> pois = poiDao.getAll();
				List<Ticket> ticketsList = null;
				List<JSONObject> poiJsonList = new ArrayList<JSONObject>();
				JSONObject poiJson = null;
				if (pois != null && pois.size() > 0) {
					for (Poi p : pois) {
						ticketsList = p.getTickets();
						if (ticketsList != null && ticketsList.size() > 0) {
							poiJson = new JSONObject();
							poiJson.put("id", p.getId());
							poiJson.put("title", p.getTitle());
							poiJsonList.add(poiJson);
						}
					}
				}
				result.put("total", total);
				result.put("pois", poiJsonList);
			} else if ((!Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
				count = Integer.parseInt(countStr);
				ticketList = ticketDao.getTicketByParams(count, page, ticket_name, zyb_code, poiIdStr);
				int total = ticketDao.getTicketByParamsCount(count, page, ticket_name, zyb_code, poiIdStr);
				List<Poi> pois = poiDao.getAll();
				List<Ticket> ticketsList = null;
				List<JSONObject> poiJsonList = new ArrayList<JSONObject>();
				JSONObject poiJson = null;
				if (pois != null && pois.size() > 0) {
					for (Poi p : pois) {
						ticketsList = p.getTickets();
						if (ticketsList != null && ticketsList.size() > 0) {
							poiJson = new JSONObject();
							poiJson.put("id", p.getId());
							poiJson.put("title", p.getTitle());
							poiJsonList.add(poiJson);
						}
					}
				}
				result.put("total", total);
				result.put("pois", poiJsonList);
			} else if ((Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
				page = Integer.parseInt(pageStr);
				ticketList = ticketDao.getTicketByParams(count, page, ticket_name, zyb_code, poiIdStr);
			} else if ((!Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
				count = Integer.parseInt(countStr);
				page = Integer.parseInt(pageStr);
				ticketList = ticketDao.getTicketByParams(count, page, ticket_name, zyb_code, poiIdStr);
			}

			if (ticketList != null && ticketList.size() > 0) {
				JSONObject ticketJson = null;
				for (Ticket t : ticketList) {
					ticketJson = getTicketJson(t);
					ticketJSONList.add(ticketJson);

				}
			}
			result.put("tickets", ticketJSONList);
			return Response.status(Response.Status.OK).entity(result).build();
		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}
	}

	@Override
	public Response ticket_poi_list(HttpServletRequest request, Long loginUserid) throws Exception {

		String countStr = request.getParameter("count");
		String pageStr = request.getParameter("page");
		int count = 10;
		int page = 0;
		JSONObject error = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			List<Ticket> ticketList = null;
			JSONObject result = new JSONObject();
			List<JSONObject> ticketJSONList = new ArrayList<JSONObject>();

			if ((Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
				ticketList = ticketDao.getTicketByNopoi(count, page);
				int total = ticketDao.getTicketByNopoiCount();

				List<Poi> pois = poiDao.getAll();
				List<Ticket> ticketsList = null;
				List<JSONObject> poiJsonList = new ArrayList<JSONObject>();
				JSONObject poiJson = null;
				if (pois != null && pois.size() > 0) {
					for (Poi p : pois) {
						ticketsList = p.getTickets();
						if (ticketsList != null && ticketsList.size() > 0) {
							poiJson = new JSONObject();
							poiJson.put("id", p.getId());
							poiJson.put("title", p.getTitle());
							poiJsonList.add(poiJson);
						}
					}
				}
				result.put("total", total);
				result.put("pois", poiJsonList);
			} else if ((!Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
				count = Integer.parseInt(countStr);
				ticketList = ticketDao.getTicketByNopoi(count, page);
				int total = ticketDao.getTicketByNopoiCount();
				List<Poi> pois = poiDao.getAll();
				List<Ticket> ticketsList = null;
				List<JSONObject> poiJsonList = new ArrayList<JSONObject>();
				JSONObject poiJson = null;
				if (pois != null && pois.size() > 0) {
					for (Poi p : pois) {
						ticketsList = p.getTickets();
						if (ticketsList != null && ticketsList.size() > 0) {
							poiJson = new JSONObject();
							poiJson.put("id", p.getId());
							poiJson.put("title", p.getTitle());
							poiJsonList.add(poiJson);
						}
					}
				}
				result.put("total", total);
				result.put("pois", poiJsonList);
			} else if ((Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
				page = Integer.parseInt(pageStr);
				ticketList = ticketDao.getTicketByNopoi(count, page);
			} else if ((!Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
				count = Integer.parseInt(countStr);
				page = Integer.parseInt(pageStr);
				ticketList = ticketDao.getTicketByNopoi(count, page);
			}

			if (ticketList != null && ticketList.size() > 0) {
				JSONObject ticketJson = null;
				for (Ticket t : ticketList) {
					ticketJson = getTicketJson(t);
					ticketJSONList.add(ticketJson);

				}
			}
			result.put("tickets", ticketJSONList);
			return Response.status(Response.Status.OK).entity(result).build();
		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}

	}

	@Override
	public Response add_ad(JSONObject content, Long loginUserid) {

		JSONObject resp = new JSONObject();
		Ad slide = new Ad();
		if (content != null) {
			if (content.containsKey("reference_url")) {
				slide.setUrl(content.getString("reference_url"));
			} else {
				slide.setReference_id(content.getLong("reference_id"));
			}
			slide.setType(content.getString("type"));
			slide.setAuthorId(loginUserid);
			slide.setStatus("enable");
			slide.setSequence(1);
			slide.setAd_image(content.getString("ad_image"));

			adDao.save(slide);
			resp.put("id", slide.getId());
			resp.put("type", slide.getType());
			resp.put("sequence", slide.getSequence());
			resp.put("slide_image", JSONObject.fromObject(slide.getAd_image()));

			if (slide.getType().equals("url")) {
				resp.put("url", slide.getUrl());
			} else {
				resp.put("reference_id", slide.getReference_id());
			}

			// 更新redis数据
			updateBasicParam();
			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}

	}

	@Override
	public Response update_ad(JSONObject content, Long loginUserid) {

		JSONObject resp = new JSONObject();
		if (content != null) {

			Long id = content.getLong("id");
			Ad slide = adDao.get(id);
			if (content.containsKey("reference_url")) {
				slide.setUrl(content.getString("reference_url"));
			} else {
				slide.setReference_id(content.getLong("reference_id"));
			}
			slide.setType(content.getString("type"));
			slide.setAuthorId(loginUserid);
			slide.setStatus("enable");
			slide.setSequence(1);
			slide.setAd_image(content.getString("ad_image"));
			adDao.update(slide);
			resp.put("id", slide.getId());
			resp.put("type", slide.getType());
			resp.put("sequence", slide.getSequence());
			resp.put("ad_image", JSONObject.fromObject(slide.getAd_image()));

			if (slide.getType().equals("url")) {
				resp.put("url", slide.getUrl());
			} else {
				resp.put("reference_id", slide.getReference_id());
			}
			// 更新redis数据
			updateBasicParam();
			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}

	}

	@Override
	public Response ad_list() {

		List<Ad> adList = adDao.getAdByStatus("enable");
		List<JSONObject> slideJsonList = new ArrayList<JSONObject>();
		JSONObject slideJson = null;
		if (adList != null && adList.size() > 0) {
			for (Ad slide : adList) {
				slideJson = new JSONObject();
				slideJson.put("id", slide.getId());
				slideJson.put("ad_image", JSONObject.fromObject(slide.getAd_image()));
				slideJson.put("type", slide.getType());
				slideJson.put("sequence", slide.getSequence());
				if (slide.getType().equals("url")) {
					slideJson.put("url", slide.getUrl());
				} else {
					slideJson.put("reference_id", slide.getReference_id());
				}
				slideJsonList.add(slideJson);
			}
		}
		return Response.status(Response.Status.OK).entity(slideJsonList).build();

	}

	@Override
	public Response delete_ad(Long ad_id) {

		JSONObject resp = new JSONObject();
		if (ad_id != null && ad_id > 0) {
			Ad ad = adDao.get(ad_id);
			ad.setStatus("disable");
			adDao.update(ad);
			resp.put("ad_id", ad_id);
			// 更新redis数据
			updateBasicParam();
			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}

	}

	@Override
	public Response add_map(JSONObject content, Long loginUserid) {

		JSONObject resp = new JSONObject();
		ParkMap map = new ParkMap();
		if (content != null) {
			map.setUrl(content.getString("url"));
			map.setStatus("enable");
			map.setVersion(content.getString("version"));
			map.setPointA(content.getString("pointA"));
			map.setPointB(content.getString("pointB"));
			map.setCoordinateA(content.getString("coordinateA"));
			map.setCoordinateB(content.getString("coordinateB"));
			map.setLng_lat_x(content.getString("lng_lat_x"));
			map.setLng_lat_y(content.getString("lng_lat_y"));
			parkMapDao.save(map);
			resp.put("id", map.getId());
			resp.put("url", map.getUrl());
			resp.put("version", map.getVersion());
			resp.put("pointA", map.getPointA());
			resp.put("pointB", map.getPointB());
			resp.put("coordinateA", map.getCoordinateA());
			resp.put("coordinateB", map.getCoordinateB());
			resp.put("lng_lat_x", map.getLng_lat_x());
			resp.put("lng_lat_y", map.getLng_lat_y());
			// 更新redis数据
			updateBasicParam();
			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}

	}

	@Override
	public Response update_map(JSONObject content, Long loginUserid) {
		JSONObject resp = new JSONObject();
		if (content != null) {

			Long id = content.getLong("id");
			ParkMap map = parkMapDao.get(id);
			map.setUrl(content.getString("url"));
			map.setStatus("enable");
			map.setVersion(content.getString("version"));
			map.setPointA(content.getString("pointA"));
			map.setPointB(content.getString("pointB"));
			map.setCoordinateA(content.getString("coordinateA"));
			map.setCoordinateB(content.getString("coordinateB"));
			map.setLng_lat_x(content.getString("lng_lat_x"));
			map.setLng_lat_y(content.getString("lng_lat_y"));
			parkMapDao.update(map);
			resp.put("id", map.getId());
			resp.put("url", map.getUrl());
			resp.put("version", map.getVersion());
			resp.put("pointA", map.getPointA());
			resp.put("pointB", map.getPointB());
			resp.put("coordinateA", map.getCoordinateA());
			resp.put("coordinateB", map.getCoordinateB());
			resp.put("lng_lat_x", map.getLng_lat_x());
			resp.put("lng_lat_y", map.getLng_lat_y());
			// 更新redis数据
			updateBasicParam();
			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	@Override
	public Response map_list() {
		List<ParkMap> pmList = parkMapDao.getParkMapList("enable");
		return Response.status(Response.Status.OK).entity(pmList).build();
	}

	@Override
	public Response map_detail() {
		List<ParkMap> pmList = parkMapDao.getParkMapList("enable");
		ParkMap pm = null;
		if (pmList != null && pmList.size() > 0) {
			pm = pmList.get(0);
		}
		return Response.status(Response.Status.OK).entity(pm).build();
	}

	@Override
	public Response delete_map(Long map_id) {
		JSONObject resp = new JSONObject();
		if (map_id != null && map_id > 0) {
			ParkMap map = parkMapDao.get(map_id);
			map.setStatus("disable");
			parkMapDao.update(map);
			resp.put("map_id", map_id);
			// 更新redis数据
			updateBasicParam();
			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "缺少参数");
			resp.put("code", 10001);
			resp.put("error_message", "缺少参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	public String getProjectUrl() {
		String path = getClass().getResource("/../../META-INF/project_url.json").getPath();
		JSONObject json1 = ParseFile.parseJson(path);
		String url = json1.getString("url");
		return url;
	}

	public String getInsurance_link() {
		String path = getClass().getResource("/../../META-INF/insurant.json").getPath();
		JSONObject json1 = ParseFile.parseJson(path);
		String insurance_link = json1.getString("insurance_link");
		return insurance_link;
	}

//	public Jedis getJedis() {
//		String path = getClass().getResource("/../../META-INF/redis.json").getPath();
//		JSONObject json1 = ParseFile.parseJson(path);
//		String host = json1.getString("host");
//		int port = json1.getInt("port");
//		String password = json1.getString("password");
//		Jedis jedis = null;
//		if (!Strings.isNullOrEmpty(password)) {
//			JedisPoolConfig config = new JedisPoolConfig();
//			// 最大空闲连接数, 应用自己评估，不要超过ApsaraDB for Redis每个实例最大的连接数
//			config.setMaxIdle(200);
//			// 最大连接数, 应用自己评估，不要超过ApsaraDB for Redis每个实例最大的连接数
//			config.setMaxTotal(300);
//			config.setTestOnBorrow(false);
//			config.setTestOnReturn(false);
//			JedisPool pool = new JedisPool(config, host, port, 1000, password);
//			jedis = pool.getResource();
//			
//		} else {
//			jedis = new Jedis(host, port);
//		}
//		return jedis;
//	}
//	
//	/**
//     * 返还到连接池
//     * 
//     * @param pool 
//     * @param redis
//     */
//    public void returnResource(Jedis redis) {
//        if (redis != null) {
//            pool.returnResource(redis);
//        }
//    }

	public void updateHomepage() {
//		Jedis jedis = getJedis();
		String url = getProjectUrl();
		myRedisDao.delete("homepage");
		String result = HttpUtil.sendGetStr(url + "users/homepage", "");
		myRedisDao.save("homepage", result,0);
//		returnResource(jedis);
	}

	public void updateBasicParam() {
//		Jedis jedis = getJedis();
		String url = getProjectUrl();
		myRedisDao.delete("basic_param");
		String result = HttpUtil.sendGetStr(url + "basic_params", "");
		myRedisDao.save("basic_param", result,0);
//		returnResource(jedis);
	}

	@Override
	public Response export_insure(HttpServletRequest request, HttpServletResponse response, Long loginUserid) {
		String start_date = request.getParameter("start_date");
		String end_date = request.getParameter("end_date");
		String name = request.getParameter("name");
		List<OrdersTicket> list = ordersTicketDao.getOrdersTicketListByInsurant(start_date, end_date, name);
		// 调用方法创建HSSFWorkbook工作簿对象
		HSSFWorkbook wb = export_insure_excel(list);
		try {
			// 定义导出文件的名称
			String fileName = new String("保险信息.xls".getBytes("UTF-8"), "ISO-8859-1");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			OutputStream os = response.getOutputStream();

			// 将工作薄写入到输出流中
			wb.write(os);
			os.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
		return null;
	}

	@Override
	public Response insurant_statistics(HttpServletRequest request, Long loginUserid) {

		String name = request.getParameter("name");
		String start_date = request.getParameter("start_date");
		String end_date = request.getParameter("end_date");

		String countStr = request.getParameter("count");
		String pageStr = request.getParameter("page");
		int count = 10;
		int page = 0;
		JSONObject error = new JSONObject();
		List<OrdersTicket> orderTicketList = null;
		if (loginUserid != null && loginUserid > 0) {

			JSONObject result = new JSONObject();
			if ((Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
				orderTicketList = ordersTicketDao.getOrdersTicketListByInsurant(count, page, start_date, end_date,
						name);
				int total = ordersTicketDao.getOrdersTicketCountAndAmountByInsurant(start_date, end_date, name);

				result.put("total_count", total);
//				if (total[1] != null) {
//					result.put("total_amount", total[1]);
//				} else {
//					result.put("total_amount", 0);
//				}

			} else if ((!Strings.isNullOrEmpty(countStr)) && (Strings.isNullOrEmpty(pageStr))) {
				count = Integer.parseInt(countStr);
				orderTicketList = ordersTicketDao.getOrdersTicketListByInsurant(count, page, start_date, end_date,
						name);
				int total = ordersTicketDao.getOrdersTicketCountAndAmountByInsurant(start_date, end_date, name);

				result.put("total_count", total);
//				if (total[1] != null) {
//					result.put("total_amount", total[1]);
//				} else {
//					result.put("total_amount", 0);
//				}
			} else if ((Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
				page = Integer.parseInt(pageStr);
				orderTicketList = ordersTicketDao.getOrdersTicketListByInsurant(count, page, start_date, end_date,
						name);
			} else if ((!Strings.isNullOrEmpty(countStr)) && (!Strings.isNullOrEmpty(pageStr))) {
				count = Integer.parseInt(countStr);
				page = Integer.parseInt(pageStr);
				orderTicketList = ordersTicketDao.getOrdersTicketListByInsurant(count, page, start_date, end_date,
						name);
			}
			List<JSONObject> orderTicketJSONList = new ArrayList<JSONObject>();
			if (orderTicketList != null && orderTicketList.size() > 0) {
				JSONObject orderTicketJson = null;
				JSONObject insurantJson = null;
				for (OrdersTicket ot : orderTicketList) {
					orderTicketJson = new JSONObject();
					insurantJson = new JSONObject();
					orderTicketJson.put("orders_ticket_no", ot.getTicket_order_no());
					orderTicketJson.put("transNo", ot.getTransNo());
					orderTicketJson.put("create_time", ot.getUpdate_time());
					Contacter c = ot.getContacter();
					insurantJson.put("name", c.getName());
					insurantJson.put("identity_card", c.getIdentity_card());
					insurantJson.put("gender", c.getGender());
					orderTicketJson.put("insurant", insurantJson);
					orderTicketJson.put("startDate", ot.getStartDate());
					orderTicketJson.put("endDate", ot.getEndDate());
					orderTicketJson.put("insureNum", ot.getInsureNum());
					orderTicketJSONList.add(orderTicketJson);
				}
			}
			result.put("insurants", orderTicketJSONList);
			return Response.status(Response.Status.OK).entity(result).build();

		} else {
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}

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

	@Override
	public Response import_member(JSONObject member) {
		return null;
	}

	@Override
	public Response getAdmin(Long admin_id) {
		if(admin_id != null && admin_id > 0){
			Admin admin = adminDao.get(admin_id);
			JSONObject adminJson = getAdmimJson(admin);
			return Response.status(Response.Status.OK).entity(adminJson).build();
		}else{
			JSONObject returnJson = new JSONObject();
			returnJson.put("status", "参数不正确");
			returnJson.put("code", Integer.valueOf(10330));
			returnJson.put("error_message", "参数不正确");
			return Response.status(Response.Status.BAD_REQUEST).entity(returnJson).build();
		}
		
		
	}

	@Override
	public Response poi_hotel(HttpServletRequest request) {
		List<Poi> list = poiDao.getPoiListByClassify();
		String path = getClass().getResource("/../../META-INF/hotel.json").getPath();
		JSONObject hotel = ParseFile.parseJson(path);
		String url = hotel.getString("url");
		
		List<JSONObject> joList = new ArrayList<JSONObject>();
		if(list != null && list.size() > 0){
			for(Poi p:list){
				boolean flag = false;
				String result = HttpUtil.sendPostStr(url+"/hotel/list/"+p.getId(), "");
				if(!Strings.isNullOrEmpty(result)){
					JSONObject resultJson = JSONObject.fromObject(result);
					int code = resultJson.getInt("code");
					if(code == 1000){
						JSONArray ja = resultJson.getJSONArray("data");
						if(ja != null && ja.size() > 0){
							flag = true;
						}
					}
				}
				
				JSONObject pJson = getPoiJSON(p);
				if(flag){
					pJson.put("is_exist", 1);
				}else{
					pJson.put("is_exist", 0);
				}
				joList.add(pJson);
			}
		}
		return Response.status(Response.Status.OK).entity(joList).build();
	}

	@Override
	public Response refund(JSONObject orders, Long loginUserid) {
		JSONObject error = new JSONObject();
		if (loginUserid != null && loginUserid > 0) {
			String orders_ticket_no = orders.getString("orders_ticket_no");
			if(!Strings.isNullOrEmpty(orders_ticket_no)){
				OrdersTicket ot = ordersTicketDao.getOrdersTicketByOrdersNo(orders_ticket_no);
				int status = ot.getStatus();
				if(status == 0 ){
					Orders o = ot.getOrders();
					Pingpp.privateKeyPath = getClass().getResource("/../../META-INF/ping_1024_priv.pem").getPath();
					Pingpp.apiKey = getPingSecretKey();
					Refund refund = null;
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("description", "管理员操作退款");
					params.put("amount", ot.getTotal().doubleValue() * 100);
					try {
						refund = Refund.create(o.getCharge_id(), params);
						ot.setRefund_id(refund.getId());
						ot.setStatus(3);// 已退票
						ordersTicketDao.update(ot);
						Ticket t = ot.getTicket();
						t.setNumber(t.getNumber() + ot.getNum());
						t.setSold(t.getSold() - ot.getNum());
						ticketDao.update(t);
						Long userid = o.getUser().getId();
						// 需要通知
						Notification n = new Notification();
						n.setNotificationType(1);
						n.setObjectType(1);
						n.setObjectId(ot.getId());
						n.setRead_already(false);
						n.setRecipientId(userid);
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
						error.put("status", "success");
						return Response.status(Response.Status.OK).entity(error).build();
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
				}else{
					error.put("status", "该订单不可退款");
					error.put("code", Integer.valueOf(10330));
					error.put("error_message", "该订单不可退款");
					return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
				}
			}else{
				error.put("status", "缺少参数");
				error.put("code", Integer.valueOf(10331));
				error.put("error_message", "缺少参数");
				return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
			}
		}else{
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}
		return null;
	}

	public String getPingSecretKey() {
		String path = getClass().getResource("/../../META-INF/ping++.json").getPath();
		JSONObject ping = ParseFile.parseJson(path);
		String secretKey = ping.getString("secretKey");
		return secretKey;

	}

	@Override
	public Response import_insurant(JSONArray arr, Long loginUserid) {
		JSONObject error = new JSONObject();
		if(loginUserid != null && loginUserid > 0){
			if(arr != null && arr.size() > 0){
				JSONObject input = null;
				List<JSONObject> inputList = new ArrayList<JSONObject>();
				for(Object o:arr){
					input = new JSONObject();
					JSONObject json = JSONObject.fromObject(o);
					String identity_card = json.getString("identity_card");
					String insurant_no = json.getString("insurant_no");
					User u = userDao.getUserByIdcard(identity_card);
					if(u != null){
						u.setNormal_barcode(insurant_no);
						userDao.update(u);
					}else{
						input.put("identity_card", identity_card);
						input.put("insurant_no", insurant_no);
						inputList.add(input);
					}
				}
				return Response.status(Response.Status.OK).entity(inputList).build();
			}else{
				error.put("status", "缺少参数");
				error.put("code", Integer.valueOf(10331));
				error.put("error_message", "缺少参数");
				return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
			}
		}else{
			error.put("status", "用户未登录");
			error.put("code", Integer.valueOf(10010));
			error.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}
	}

}
