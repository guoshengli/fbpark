package com.fbpark.rest.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fbpark.rest.common.EncryptionUtil;
import com.fbpark.rest.common.ParseFile;
import com.fbpark.rest.dao.AdminDao;
import com.fbpark.rest.dao.UserDao;
import com.fbpark.rest.model.Admin;
import com.fbpark.rest.model.User;
import com.fbpark.rest.service.model.GetuiModel;
import com.google.common.base.Strings;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
public class ClientRequestFilter extends OncePerRequestFilter implements Filter {
	private static final Log log = LogFactory.getLog(ClientRequestFilter.class);

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AdminDao adminDao;

	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
		response.setHeader("Access-Control-Allow-Headers",
				"authorization, content-type, x-tella-request-appversion, x-tella-request-provider, x-tella-request-timestamp, x-tella-request-token"
						+ ", x-tella-request-userid, x-tella-request-device, x-tella-request-usertype");
		String uri = request.getRequestURI();
		String regex = "[0-9]+";
		String method = request.getMethod();
		String ip = request.getHeader("X-Real-IP");
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	        ip = request.getHeader("X-Forwarded-For");
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	        ip = request.getHeader("Proxy-Client-IP");
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	        ip = request.getRemoteAddr();
		log.info("right ip ***"+ip);
		if ((uri.equals("/fbpark/v1/users/appsignup"))
				|| (uri.equals("/fbpark/v1/admin/signin"))
				|| (uri.equals("/fbpark/v1/admin/export_user"))
				|| (uri.equals("/fbpark/v1/admin/export_insure"))
				|| (uri.equals("/fbpark/v1/admin/export_order"))
				|| (uri.equals("/fbpark/v1/admin/orders_ticket/export_statistics"))
				|| (uri.matches("/fbpark/v1/admin/"+regex))
				|| (uri.equals("/fbpark/v1/users/signup"))
				|| (uri.equals("/fbpark/v1/users/webhooks"))
				|| (uri.equals("/fbpark/v1/basic_params"))
				|| (uri.equals("/fbpark/v1/users/homepage"))
				|| (uri.equals("/fbpark/v1/users/webhooks"))
				|| (uri.contains("/fbpark/v1/users/refund_response"))
				|| (uri.contains("/fbpark/v1/users/check_ticket"))
				|| (uri.equals("/fbpark/v1/users/record"))
				|| (uri.equals("/fbpark/v1/users/record_vip"))
				|| (uri.equals("/fbpark/v1/users/synchronousUser"))
				|| (uri.equals("/fbpark/v1/users/synchronousOrder"))
				|| (uri.equals("/fbpark/v1/users/verification_material"))
				|| (uri.equals("/fbpark/v1/users/verification_user"))
				|| (uri.equals("/fbpark/v1/users/material"))
				|| (uri.equals("/fbpark/v1/users/signin")) 
				|| (uri.equals("/fbpark/v1/users/hotel")) 
				|| (uri.equals("/fbpark/v1/users/pos/add_order")) 
				|| (uri.equals("/fbpark/v1/users/register")) 
				|| (uri.equals("/fbpark/v1/users/barcode")) 
				|| (uri.equals("/fbpark/v1/users/verification")) 
				|| (uri.equals("/fbpark/v1/users/checkin")) 
				|| (uri.equals("/fbpark/v1/users/login/linked_account")) 
				|| (uri.equals("/fbpark/v1/notifications/notifications_info")) 
				|| (uri.contains("/fbpark/v1/users/setting"))
				|| (uri.contains("/fbpark/v1/users/forgot_password"))
				|| (uri.contains("/fbpark/v1/users/forgot/phone"))
				|| (uri.contains("/fbpark/v1/users/phone"))
				|| (uri.contains("/fbpark/v1/users/error")) 
				|| (uri.contains("/fbpark/v1/users/content")) 
				|| (uri.contains("/fbpark/v1/pois")) 
				|| (uri.equals("/fbpark/WebContent/META-INF/qiniu.json"))
				|| (uri.equals("/fbpark/v1/admin/exception"))
				|| (uri.equals("/fbpark/v1/tickets/list"))
				|| (uri.equals("/fbpark/v1/users/get_code"))
				|| (uri.equals("/fbpark/v1/users/logout"))
				|| (uri.equals("/fbpark/v1/users/check_token"))) {
			
			if(uri.equals("/fbpark/v1/users/barcode") || uri.equals("/fbpark/v1/users/register")){
				JSONArray ips = getIp();
				if(ips.contains(ip)){
					filterChain.doFilter(request, response);
				}else{
					log.info("非法IP调用**********************"+ip);
					JSONObject jo = new JSONObject();
					jo.put("status", "您当前IP不允许访问");
					jo.put("code", 10680);
					jo.put("error_message", "您当前IP不允许访问");
					PrintWriter writer = response.getWriter();
					writer.write(jo.toString());
				}
			}else{
				filterChain.doFilter(request, response);
			}
		} else {
			if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(method)) {
				filterChain.doFilter(request, response);
			} else {
				String userId = request.getHeader("X-Tella-Request-Userid") != null
						? request.getHeader("X-Tella-Request-Userid") : "";
				String timestamp = request.getHeader("X-Tella-Request-Timestamp") != null
						? request.getHeader("X-Tella-Request-Timestamp") : "";
				String token = request.getHeader("X-Tella-Request-Token") != null
						? request.getHeader("X-Tella-Request-Token") : "";
//				String appVersion = request.getHeader("X-Tella-Request-AppVersion") != null
//						? request.getHeader("X-Tella-Request-AppVersion") : "";
				
				String device = request.getHeader("X-Tella-Request-Device") != null
						? request.getHeader("X-Tella-Request-Device") : "";


				if(!Strings.isNullOrEmpty(device)){
					if ((Strings.isNullOrEmpty(userId)) || (Strings.isNullOrEmpty(timestamp))
							|| (Strings.isNullOrEmpty(token))) {
						log.error(String.format(
								"[**Auth Failure**] Missing params: userId:%s timestamp:%s token:%s",
								new Object[] { userId, timestamp, token }));

						String url = request.getServletContext().getContextPath() + "/v1/users/error/noData";
						response.sendRedirect(url);
						log.debug("*** no auth /v1/users/error/noData ****");
						return;
					}
					if(device.equals("10")){
						Admin admin = null;
						if (!Strings.isNullOrEmpty(userId)) {
							admin = (Admin) this.adminDao.getAdminById(Long.parseLong(userId));
						}

						if (admin == null) {
							log.error(String.format("[**Auth Failure**] invalid userId:%s timestamp:%s token:%s",
									new Object[] { userId, timestamp, token }));
							JSONObject jo = new JSONObject();
							jo.put("status", "该用户不存在");
							jo.put("code", 10623);
							jo.put("error_message", "该用户不存在");
							PrintWriter writer = response.getWriter();
							writer.write(jo.toString());
							log.debug("*** no User /v1/users/error/noData ****");
							return;
						}

						String raw = admin.getId() + admin.getPassword() + timestamp;
						String generatedToken = EncryptionUtil.hashMessage(raw);
						log.debug("[**Debug Info**] raw: " + raw);
						log.debug("[**Debug Info**] server calculated hash is: " + generatedToken);
						log.debug("[**Debug Info**] provided hash is: " + token);

						if (!token.equals(generatedToken)) {
							log.error(String.format(
									"[**Auth Failure**] invalid token for userId:%s timestamp:%s token:%s",
									new Object[] { userId, timestamp, token }));
							JSONObject jo = new JSONObject();
							jo.put("status", "token失效");
							jo.put("code", 10624);
							jo.put("error_message", "token失效");
							PrintWriter writer = response.getWriter();
							writer.write(jo.toString());
							log.debug("*** no token /v1/users/error/noData ****");
							return;
						}else{
							filterChain.doFilter(request, response);
						}
					}else{

						User user = null;
						if (!Strings.isNullOrEmpty(userId)) {
							user = (User) this.userDao.get(Long.parseLong(userId));
						}

						if (user == null) {
							log.error(String.format("[**Auth Failure**] invalid userId:%s timestamp:%s token:%s",
									new Object[] { userId, timestamp, token }));
							String url = getServletContext().getContextPath() + "/v1/users/error/noUser";
							response.sendRedirect(url);
							log.debug("*** no User /v1/users/error/noData ****");
							return;
						}

						String raw = user.getId() + user.getPassword() + timestamp;
						String generatedToken = EncryptionUtil.hashMessage(raw);
						log.debug("[**Debug Info**] raw: " + raw);
						log.debug("[**Debug Info**] server calculated hash is: " + generatedToken);
						log.debug("[**Debug Info**] provided hash is: " + token);

						if (!token.equals(generatedToken)) {
							log.error(String.format(
									"[**Auth Failure**] invalid token for userId:%s timestamp:%s token:%s",
									new Object[] { userId, timestamp, token }));
							String url = getServletContext().getContextPath() + "/v1/users/error/invalid_token";
							response.sendRedirect(url);
							log.debug("*** no token /v1/users/error/noData ****");
							return;
						}else{
							filterChain.doFilter(request, response);
						}
					}
					

				}else{
					String url = request.getServletContext().getContextPath() + "/v1/users/error/noData";
					response.sendRedirect(url);
					log.debug("*** no auth /v1/users/error/noData ****");
					return;
				}
							

			}

		}
	}
	
	public JSONArray getIp() {
		String path = getClass().getResource("/../../META-INF/ip.json").getPath();
		JSONObject json1 = ParseFile.parseJson(path);
		JSONArray ja = json1.getJSONArray("ip");
		return ja;

	}
}
