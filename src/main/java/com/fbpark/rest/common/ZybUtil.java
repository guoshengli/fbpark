package com.fbpark.rest.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.fbpark.rest.model.Contacter;
import com.fbpark.rest.model.Orders;
import com.fbpark.rest.model.OrdersTicket;
import com.fbpark.rest.model.User;
import com.fbpark.rest.service.model.Zyb;
import com.google.common.base.Strings;

public class ZybUtil {

	// 提交订单接口xml生成
	public static String createOrdersXml(User user, Orders orders, List<OrdersTicket> ordersTicketList,Zyb zyb) {
		// DocumentHelper提供了创建Document对象的方法
		Document document = DocumentHelper.createDocument();
		// 添加节点信息
		Element rootElement = document.addElement("PWBRequest");
		Element transactionNameElement = rootElement.addElement("transactionName");
		transactionNameElement.setText("SEND_CODE_REQ");

		// header 信息
		Element headerElement = rootElement.addElement("header");
		Element applicationElement = headerElement.addElement("application");
		applicationElement.setText("SendCode");
		String date = getDateByFormat(new Date(),"yyyy-MM-dd");
		Element requestTimeElement = headerElement.addElement("requestTime");
		requestTimeElement.setText(date);

		// identityInfo 信息
		Element identityInfoElement = rootElement.addElement("identityInfo");
		Element corpCodeElement = identityInfoElement.addElement("corpCode");
		corpCodeElement.setText(zyb.getCorpCode());

		Element userNameElement = identityInfoElement.addElement("userName");
		userNameElement.setText(zyb.getUserName());

		// orderRequest 订单请求
		Element orderRequestElement = rootElement.addElement("orderRequest");
		Element orderElement = orderRequestElement.addElement("order");
		Element certificateNoElement = orderElement.addElement("certificateNo");
		certificateNoElement.setText(user.getIdentity_card());

		Element linkNameElement = orderElement.addElement("linkName");
		linkNameElement.setText(user.getUsername());

		Element linkMobileElement = orderElement.addElement("linkMobile");
		linkMobileElement.setText(user.getPhone());

		Element orderCodeElement = orderElement.addElement("orderCode");
		orderCodeElement.setText(orders.getOrder_no());
		Element orderPriceElement = orderElement.addElement("orderPrice");
		orderPriceElement.setText(orders.getAmount().toString());

		// Element srcElement = orderElement.addElement("src");
		// srcElement.setText("weixin");

		// Element groupNoElement = orderElement.addElement("groupNo");
		// groupNoElement.setText("");
		Element payMethodElement = orderElement.addElement("payMethod");// 支付方式待定
		if(!Strings.isNullOrEmpty(orders.getPay_type())){
			payMethodElement.setText(orders.getPay_type());
		}else{
			payMethodElement.setText("vm");
		}
		
		Element ticketOrdersElement = orderElement.addElement("ticketOrders");

		if(ordersTicketList != null && ordersTicketList.size() > 0){
			for(OrdersTicket ot:ordersTicketList){
				Element ticketOrderElement = ticketOrdersElement.addElement("ticketOrder");

				Element orderCodeTicketElement = ticketOrderElement.addElement("orderCode");// 子订单号的编码
				orderCodeTicketElement.setText(ot.getTicket_order_no());
				Contacter contacter = ot.getContacter();
				if(contacter != null){
					//根据票是否是实名制买票
					 Element credentialsElement =
					 ticketOrderElement.addElement("credentials");
					 Element credentialElement =
					 credentialsElement.addElement("credential");
					 Element nameElement = credentialElement.addElement("name");
					 nameElement.setText(contacter.getName());
					 Element idElement = credentialElement.addElement("id");
					 idElement.setText(contacter.getIdentity_card());
				}
				
				Element priceTicketElement = ticketOrderElement.addElement("price");
				priceTicketElement.setText(ot.getTotal().toString());
				Element quantityTicketElement = ticketOrderElement.addElement("quantity"); // 票的数量
				quantityTicketElement.setText("1");
				Element totalPriceTicketElement = ticketOrderElement.addElement("totalPrice");
				totalPriceTicketElement.setText(ot.getTotal().toString());
				Element occDateTicketElement = ticketOrderElement.addElement("occDate");// 游玩日期
				occDateTicketElement.setText(ot.getTicket().getStart_date());
				Element goodsCodeTicketElement = ticketOrderElement.addElement("goodsCode");// 商品编码
//				goodsCodeTicketElement.setText(ot.getTicket().getZyb_code());
				Element goodsNameTicketElement = ticketOrderElement.addElement("goodsName");
				goodsNameTicketElement.setText(ot.getTicket().getTicket_name());
//				Element remarkTicketElement = ticketOrderElement.addElement("remark");
//				remarkTicketElement.setText("测试数据1");
			}
			
		}
		
		
		System.out.println(document.getRootElement().asXML().toString()); // 将document文档对象直接转换成字符串输出
		return document.getRootElement().asXML();
	}
	
	// 提交订单接口xml生成
		public static String createUserXml(User user, String order_no, String ticket_order_no,Zyb zyb) {
			// DocumentHelper提供了创建Document对象的方法
			Document document = DocumentHelper.createDocument();
			// 添加节点信息
			Element rootElement = document.addElement("PWBRequest");
			Element transactionNameElement = rootElement.addElement("transactionName");
			transactionNameElement.setText("SEND_CODE_REQ");

			// header 信息
			Element headerElement = rootElement.addElement("header");
			Element applicationElement = headerElement.addElement("application");
			applicationElement.setText("SendCode");
			String date = getDateByFormat(new Date(),"yyyy-MM-dd");
			Element requestTimeElement = headerElement.addElement("requestTime");
			requestTimeElement.setText(date);

			// identityInfo 信息
			Element identityInfoElement = rootElement.addElement("identityInfo");
			Element corpCodeElement = identityInfoElement.addElement("corpCode");
			corpCodeElement.setText(zyb.getCorpCode());

			Element userNameElement = identityInfoElement.addElement("userName");
			userNameElement.setText(zyb.getUserName());

			// orderRequest 订单请求
			Element orderRequestElement = rootElement.addElement("orderRequest");
			Element orderElement = orderRequestElement.addElement("order");
			Element certificateNoElement = orderElement.addElement("certificateNo");
			certificateNoElement.setText(user.getIdentity_card());

			Element linkNameElement = orderElement.addElement("linkName");
			linkNameElement.setText(user.getUsername());

			Element linkMobileElement = orderElement.addElement("linkMobile");
			linkMobileElement.setText(user.getPhone());

			Element orderCodeElement = orderElement.addElement("orderCode");
			orderCodeElement.setText(order_no);
			Element orderPriceElement = orderElement.addElement("orderPrice");
			orderPriceElement.setText("0");

			// Element srcElement = orderElement.addElement("src");
			// srcElement.setText("weixin");

			// Element groupNoElement = orderElement.addElement("groupNo");
			// groupNoElement.setText("");
			Element payMethodElement = orderElement.addElement("payMethod");// 支付方式待定
			payMethodElement.setText("vm");
			Element ticketOrdersElement = orderElement.addElement("ticketOrders");


			Element ticketOrderElement = ticketOrdersElement.addElement("ticketOrder");

			Element orderCodeTicketElement = ticketOrderElement.addElement("orderCode");// 子订单号的编码
			orderCodeTicketElement.setText(ticket_order_no);
			//根据票是否是实名制买票
			 Element credentialsElement =
			 ticketOrderElement.addElement("credentials");
			 Element credentialElement =
			 credentialsElement.addElement("credential");
			 Element nameElement = credentialElement.addElement("name");
			 nameElement.setText(user.getUsername());
			 Element idElement = credentialElement.addElement("id");
			 idElement.setText(user.getIdentity_card());
			Element priceTicketElement = ticketOrderElement.addElement("price");
			priceTicketElement.setText("0");
			Element quantityTicketElement = ticketOrderElement.addElement("quantity"); // 票的数量
			quantityTicketElement.setText("1");
			Element totalPriceTicketElement = ticketOrderElement.addElement("totalPrice");
			totalPriceTicketElement.setText("0");
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Element occDateTicketElement = ticketOrderElement.addElement("occDate");// 游玩日期
			occDateTicketElement.setText(sdf.format(new Date()));
			Element goodsCodeTicketElement = ticketOrderElement.addElement("goodsCode");// 商品编码
			if(user.getLevel().equals("normal")){
				goodsCodeTicketElement.setText(zyb.getNoramlZybCode());
			}else if(user.getLevel().equals("vip")){
				goodsCodeTicketElement.setText(zyb.getVipZybCode());
			}
			
			Element goodsNameTicketElement = ticketOrderElement.addElement("goodsName");
			goodsNameTicketElement.setText("用户二维码");
//			Element remarkTicketElement = ticketOrderElement.addElement("remark");
//			remarkTicketElement.setText("测试数据1");
		
			
			
			System.out.println(document.getRootElement().asXML().toString()); // 将document文档对象直接转换成字符串输出
			return document.getRootElement().asXML();
		}

	// 获取二维码的参数
	public static String findImgXml(String orders_no,Zyb zyb) {
		// DocumentHelper提供了创建Document对象的方法
		Document document = DocumentHelper.createDocument();
		// 添加节点信息
		Element rootElement = document.addElement("PWBRequest");
		Element transactionNameElement = rootElement.addElement("transactionName");
		transactionNameElement.setText("QUERY_IMG_URL_REQ");

		// header 信息
		Element headerElement = rootElement.addElement("header");
		Element applicationElement = headerElement.addElement("application");
		applicationElement.setText("SendCode");

		String date = getDateByFormat(new Date(),"yyyy-MM-dd HH:mm:ss");
		Element requestTimeElement = headerElement.addElement("requestTime");
		requestTimeElement.setText(date);

		// identityInfo 信息
		Element identityInfoElement = rootElement.addElement("identityInfo");
		Element corpCodeElement = identityInfoElement.addElement("corpCode");
		corpCodeElement.setText(zyb.getCorpCode());

		Element userNameElement = identityInfoElement.addElement("userName");
		userNameElement.setText(zyb.getUserName());

		// orderRequest 订单请求
		Element orderRequestElement = rootElement.addElement("orderRequest");
		Element orderElement = orderRequestElement.addElement("order");

		Element orderCodeElement = orderElement.addElement("orderCode");
		orderCodeElement.setText(orders_no);

		//
		System.out.println(document.getRootElement().asXML().toString()); // 将document文档对象直接转换成字符串输出
		return document.getRootElement().asXML();
	}
	
	// 退票审核参数
		public static String returnOrdersXml(String orders_no,Zyb zyb,String thirdReturnCode) {
			// DocumentHelper提供了创建Document对象的方法
			Document document = DocumentHelper.createDocument();
			// 添加节点信息
			Element rootElement = document.addElement("PWBRequest");
			Element transactionNameElement = rootElement.addElement("transactionName");
			transactionNameElement.setText("RETURN_TICKET_NUM_NEW_REQ");

			// header 信息
			Element headerElement = rootElement.addElement("header");
			Element applicationElement = headerElement.addElement("application");
			applicationElement.setText("SendCode");

			String date = getDateByFormat(new Date(),"yyyy-MM-dd");
			Element requestTimeElement = headerElement.addElement("requestTime");
			requestTimeElement.setText(date);

			// identityInfo 信息
			Element identityInfoElement = rootElement.addElement("identityInfo");
			Element corpCodeElement = identityInfoElement.addElement("corpCode");
			corpCodeElement.setText(zyb.getCorpCode());

			Element userNameElement = identityInfoElement.addElement("userName");
			userNameElement.setText(zyb.getUserName());

			// orderRequest 订单请求
			Element orderRequestElement = rootElement.addElement("orderRequest");
			Element returnTicketElement = orderRequestElement.addElement("returnTicket");

			Element orderCodeElement = returnTicketElement.addElement("orderCode");
			orderCodeElement.setText(orders_no);
			
			Element returnNumElement = returnTicketElement.addElement("returnNum");
			returnNumElement.setText("1");
			Element thirdReturnCodeElement = returnTicketElement.addElement("thirdReturnCode");
			thirdReturnCodeElement.setText(thirdReturnCode);
			//
			System.out.println(document.getRootElement().asXML().toString()); // 将document文档对象直接转换成字符串输出
			return document.getRootElement().asXML();
		}
		
	// 保险下单失败退票
	public static String returnOrdersInsurantXml(String orders_no, Zyb zyb, String thirdReturnCode, int count) {
		// DocumentHelper提供了创建Document对象的方法
		Document document = DocumentHelper.createDocument();
		// 添加节点信息
		Element rootElement = document.addElement("PWBRequest");
		Element transactionNameElement = rootElement.addElement("transactionName");
		transactionNameElement.setText("RETURN_TICKET_NUM_NEW_REQ");

		// header 信息
		Element headerElement = rootElement.addElement("header");
		Element applicationElement = headerElement.addElement("application");
		applicationElement.setText("SendCode");

		String date = getDateByFormat(new Date(), "yyyy-MM-dd");
		Element requestTimeElement = headerElement.addElement("requestTime");
		requestTimeElement.setText(date);

		// identityInfo 信息
		Element identityInfoElement = rootElement.addElement("identityInfo");
		Element corpCodeElement = identityInfoElement.addElement("corpCode");
		corpCodeElement.setText(zyb.getCorpCode());

		Element userNameElement = identityInfoElement.addElement("userName");
		userNameElement.setText(zyb.getUserName());

		// orderRequest 订单请求
		Element orderRequestElement = rootElement.addElement("orderRequest");
		Element returnTicketElement = orderRequestElement.addElement("returnTicket");

		Element orderCodeElement = returnTicketElement.addElement("orderCode");
		orderCodeElement.setText(orders_no);

		Element returnNumElement = returnTicketElement.addElement("returnNum");
		returnNumElement.setText(count + "");
		Element thirdReturnCodeElement = returnTicketElement.addElement("thirdReturnCode");
		thirdReturnCodeElement.setText(thirdReturnCode);
		//
		System.out.println(document.getRootElement().asXML().toString()); // 将document文档对象直接转换成字符串输出
		return document.getRootElement().asXML();
	}

	// 获取订单的二维码路径
	public static String getImg(String imgXml) {  
	    try{
	    	
	    	Document document = DocumentHelper.parseText(imgXml);
	        Element rootElement = document.getRootElement(); 
	       String root = rootElement.getText();
	       System.out.println(root);
	        Iterator<Element> responseIterator = rootElement.elementIterator();  
	        int code = -1;
	        String img = "";
	        while(responseIterator.hasNext()){  
	            Element responseElement = responseIterator.next();
	            if(responseElement.getName().equals("code")){
	            	String codeVal = responseElement.getText();
	            	code = Integer.parseInt(codeVal);
	            }else if(code == 0){
	            	if(responseElement.getName().equals("img")){
	            		img = responseElement.getText();
	            	}
	            }
	            
	        }
	        return img;
	    } catch (Exception e) {    
           e.printStackTrace();    
        }
	    return null;
	}
	
	public static String getReturnCode(String returnXml) {  
	    try{
	    	
	    	Document document = DocumentHelper.parseText(returnXml);
	        Element rootElement = document.getRootElement(); 
	       String root = rootElement.getText();
	       System.out.println(root);
	        Iterator<Element> responseIterator = rootElement.elementIterator();  
	        String code = "-1";
	        while(responseIterator.hasNext()){  
	            Element responseElement = responseIterator.next();
	            if(responseElement.getName().equals("code")){
	            	code = responseElement.getText();
	            }
	            
	        }
	        return code;
	    } catch (Exception e) {    
           e.printStackTrace();    
        }
	    return null;
	}
	
	//退票申请 返回的数据
	public static Map<String,String> getReturnData(String returnXml) {  
	    try{
	    	
	    	Document document = DocumentHelper.parseText(returnXml);
	        Element rootElement = document.getRootElement(); 
	       String root = rootElement.getText();
	       System.out.println(root);
	        Iterator<Element> responseIterator = rootElement.elementIterator();  
	        Map<String,String> map = new HashMap<String,String>();
	        String code = "-1";
	        String retreatBatchNo = "";
	        while(responseIterator.hasNext()){  
	            Element responseElement = responseIterator.next();
	            if(responseElement.getName().equals("code")){
	            	code = responseElement.getText();
	            	map.put("code", code);
	            }else if(Integer.parseInt(code) == 0){
	            	if(responseElement.getName().equals("retreatBatchNo")){
	            		retreatBatchNo = responseElement.getText();
	            		map.put("retreatBatchNo", retreatBatchNo);
	            	}
	            }
	            
	        }
	        return map;
	    } catch (Exception e) {    
           e.printStackTrace();    
        }
	    return null;
	}
	
	public static String getDateByFormat(Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String d = sdf.format(date);
		return d;
	}

}
