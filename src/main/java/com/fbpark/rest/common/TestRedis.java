package com.fbpark.rest.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class TestRedis {
	public static final int TO_HEX = 32;
    public static final String AES_KEY = "godblessyou";
    public static final String AES_IV = "00000000000000000";
	public static void main(String[] args) throws Exception {
//		String str = "{\"transNo\":\"o201708081807709996\",\"partnerId\":1007424,\"caseCode\":\"QX000000100916\",\"startDate\":\"2017-10-01\",\"endDate\":\"2017-10-08\",\"applicant\":{\"cName\":\"房兴伟\",\"cardType\":1,\"cardCode\":\"370983199012045316\",\"sex\":1,\"birthday\":\"1990-12-04\",\"mobile\":\"15664460099\",\"email\":\"teambition.alashanbaoxian@163.com\"},\"insurants\":[{\"insurantId\":40,\"cName\":\"房兴伟\",\"cardType\":1,\"cardCode\":\"370983199012045316\",\"sex\":1,\"birthday\":\"1990-12-04\",\"relationId\":1,\"count\":1,\"singlePrice\":800},{\"insurantId\":41,\"cName\":\"徐玉林\",\"cardType\":1,\"cardCode\":\"411422199304281218\",\"sex\":1,\"birthday\":\"1993-04-28\",\"relationId\":0,\"count\":1,\"singlePrice\":800}]}";
//		
//		String sign = "5e167fd31bc7179e8614494f321fb213";
//		String result = HttpUtil.sendPostStr("http://tuneapi.qixin18.com/api/localInsure?sign="+sign, str);
//		System.out.println("result-->"+result);
		
//		String str = "a,b,c,d,";
//		String s = str.substring(0,str.length() -1);
//		System.out.println(s);
		
//		String s = CodeUtil.encode2hex("order_code=o201708181112442410E87AAC4C62916C7449D61CDC43312161");
//		System.out.println(s);
		
//		String json = "{\"code\":10000,\"msg\":\"查询成功\",\"data\":[]}";
//		
//		JSONObject jsonObject = JSONObject.fromObject(json);
//		Object o = jsonObject.get("data");
//		System.out.println(o.toString().length());
//		JsonConfig configs = new JsonConfig();
//		configs.setExcludes(new String[]{"prop1"});
//		configs.setIgnoreDefaultExcludes(false);
//		configs.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
//
//		JSONObject cJson = JSONObject.fromObject(jsonObject, configs);
//		System.out.println(cJson);
//		jsonObject.put("prop2","456");
//		System.out.println(jsonObject.toString());
//		HashMap<String, Object> map = (HashMap<String, Object>) JSONObject.toBean(jsonObject, HashMap.class);
//		System.out.println(map.toString());
//		Jedis jedis = new Jedis("r-2ze2ee3488993584.redis.rds.aliyuncs.com", 6379);
//		String result = jedis.get("homepage");
//		System.out.println(result);
//		read07();
//		AesEncryptionUtil.encrypt(content, password, iv)
//		String content = "2O1N0A0A0A0A3G79DTHMOA80901RBTQVT";
//		String s1 = content.substring(content.length()-6, content.length());
//		System.out.println(s1);
//		String de = AesEncryptionUtil.decrypt(content, AES_KEY, AES_IV);
//		System.out.println(de);
//		String one = content.substring(0,2);
//		System.out.println(one);
//		int a = Integer.parseInt(one, 32);
//		System.out.println(a);
//		String second = content.substring(2,4);
//		System.out.println(second);
//		int b = Integer.parseInt(second, 32);
//		System.out.println(b);
//		
//		String thrid = content.substring(4,6);
//		System.out.println(second);
//		int c = Integer.parseInt(thrid, 32);
//		System.out.println(c);
//		
//		String four = content.substring(6,8);
//		System.out.println(four);
//		int d = Integer.parseInt(four, 32);
//		System.out.println(d);
//		
//		String five = content.substring(8,10);
//		System.out.println(five);
//		int e = Integer.parseInt(five, 32);
//		System.out.println(e);
//		
//		String six = content.substring(10,12);
//		System.out.println(six);
//		int f = Integer.parseInt(six, 32);
//		System.out.println(f);
//		
//		String seven = content.substring(12,14);
//		System.out.println(seven);
//		int g = Integer.parseInt(seven, 32);
//		System.out.println(g);
		
//		String s = CodeUtil.Encrypt("12345678");
//		System.out.println(s);
//		String s1 = "ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f";
//		String s2 = "ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f";
//		if(s1.equals(s2)){
//			System.out.println("true");
//		}
		
//		boolean a = Validator.isMobile(idcard);
//		System.out.println(a);
		
//		read07();
		
		String s = QRCodeHelper.createQRString("34", "42", "10021004", "210211198910097012", "01");
		System.out.println(s);
//		System.out.println(s.substring(s.length()-6, s.length()));
		
//		BigDecimal bd = new BigDecimal("0");
//		Double d = 1200d;
//		BigDecimal bd1 = BigDecimal.valueOf(d); 
//		BigDecimal bd2 = bd.add(bd1);
//		System.out.println(bd2.toString());
//		String barcode = "060500000000BIUN670C1D31601993E7B";
//		String idcard = barcode.substring(12, barcode.length()-8);
//		System.out.println(idcard);
//		String verify = barcode.substring(barcode.length()-6, barcode.length());
//		String one = idcard.substring(0,2);
//		System.out.println(one);
//		String a = Integer.parseInt(one, 32)+"";
//		if(a.length() != 3){
//			a = "0"+a;
//		}
//		String second = idcard.substring(2,4);
//		String b = Integer.parseInt(second, 32)+"";
//		if(b.length() != 3){
//			b = "0"+b;
//		}
//		String thrid = idcard.substring(4,6);
//		String c = Integer.parseInt(thrid, 32)+"";
//		if(c.length() != 3){
//			c = "0"+c;
//		}
//		String four = idcard.substring(6,8);
//		String d = Integer.parseInt(four, 32)+"";
//		if(d.length() != 3){
//			d = "0"+d;
//		}
//		String five = idcard.substring(8,10);
//		String e = Integer.parseInt(five,32)+"";
//		if(e.length() != 3){
//			e = "0"+e;
//		}
//		String f = idcard.substring(10,13);
//		String identity_card = a+b+c+d+e+f;
//		System.out.println(identity_card);
		
//		String s = getUserInfoByIdCard("522126199305093011", "陈龙");
//		System.out.println();
//		String json="{\"poi\":1}";
//		String result = HttpUtil.sendPostStr("http://api-hotel.fblife.com/hotel/hotel/room-type/6", "");
//		System.out.println(result);
//		  List<String> phone = new ArrayList<String>();
//		  phone.add("15201132276");
//		  List<String> params = new ArrayList<String>();
//		  params.add("o201709111447105859");
//		  params.add("郭胜利");
//		  params.add("大脚怪门票");
//	        System.out.println(phone.toString()+"  "+params.toString());
//		String s = "2017-10-01";
//		String s3 = "2017-10-03";
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date d = sdf.parse(s);
//		Date d3 = sdf.parse(s3);
//		long sDay = (d.getTime() / 1000);
//		long s3Day = (d3.getTime() / 1000 );
//		System.out.println((s3Day-sDay)/(60*60*24));
//		String s = "{\"rspInfo\":{\"rspCode\":1000,\"rspDesc\":\"核销成功\"},\"rspData\":\"\"}";
//		JSONObject j = JSONObject.fromObject(s);
//		Object o = j.get("rspData");
//		if(o == null || o.equals("")){
//			j.discard("rspData");
//		}
//		System.out.println(j.toString());
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		long l = 1506744000;
//		Date d = new Date(l*1000);
//		String s = sdf.format(d);
//		System.out.println(s);
//		String s1 = "\"11\"218.20.118.133";
//		String regex = "\"(.*?)\"(\\d+\\.\\d+\\.\\d+\\.\\d+)";
//		Pattern p = Pattern.compile(regex);
//		boolean b = Pattern.matches(regex,s1);
//		System.out.println(b);
		
		System.out.println((new Date()).getTime() / 1000);
	}
	
	public static String getUserInfoByIdCard(String idcard,String username){
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

	/**
	 * 将源字符串使用MD5加密为字节数组
	 * 
	 * @param source
	 * @return
	 */
	public static byte[] encode2bytes(String source) {
		byte[] result = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(source.getBytes("UTF-8"));
			result = md.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	 public static void read07() throws Exception
	    {
	        //创建输入流
//	        FileInputStream fis = new FileInputStream(new File("/Users/shengliguo/Documents/会员信息.xls"));
	        //由输入流得到工作簿
	        Workbook workbook = WorkbookFactory.create(new File("/Users/shengliguo/Documents/会员信息.xls"));
	        //得到工作表
	        Sheet sheet = workbook.getSheet("人员信息");
	        //得到行,0表示第一行
	        Row row = sheet.getRow(0);
	        Iterator<Row> rowIter = sheet.rowIterator();
	        Iterator<Cell> cellIter = row.cellIterator();
	        
	        int lastRowNum = sheet.getLastRowNum();
	        int lastCellNum = row.getLastCellNum();
	        System.out.println("lastRowNum---->"+lastRowNum+"---->"+lastCellNum);
	        //创建单元格行号由row确定,列号作为参数传递给createCell;第一列从0开始计算
	        Cell cell = row.getCell(2);
	        //给单元格赋值
	        String cellValue = cell.getStringCellValue();
	        System.out.println("C1的值是"+cellValue);
	        int a[][] = new int[lastRowNum][lastCellNum];
	        for(int i=1;i<=lastRowNum;i++)
	        {
	        	Row rows = sheet.getRow(i);
	        	for(int j=0;j<lastCellNum;j++){
	        		Cell cells = rows.getCell(j);
	        		System.out.println(cells.getStringCellValue());
	        	}
	        }
	        workbook.close();
//	        fis.close();
	    }

	/**
	 * 处理Base64解码并写图片到指定位置
	 *
	 * @param base64
	 *            图片Base64数据
	 * @param path
	 *            图片保存路径
	 * @return
	 */
	public static boolean base64ToImageFile(String base64, String path) throws IOException {// 对字节数组字符串进行Base64解码并生成图片
		// 生成jpeg图片
		try {
			OutputStream out = new FileOutputStream(path);
			return base64ToImageOutput(base64, out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 处理Base64解码并输出流
	 *
	 * @param base64
	 * @param out
	 * @return
	 */
	public static boolean base64ToImageOutput(String base64, OutputStream out) throws IOException {
		if (base64 == null) { // 图像数据为空
			return false;
		}
		try {
			// Base64解码
			byte[] bytes = org.apache.commons.codec.binary.Base64.decodeBase64(base64);
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {// 调整异常数据
					bytes[i] += 256;
				}
			}
			// 生成jpeg图片
			out.write(bytes);
			out.flush();
			return true;
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将源字符串使用MD5加密为32位16进制数
	 * 
	 * @param source
	 * @return
	 */
	public static String encode2hex(String source) {
		byte[] data = encode2bytes(source);

		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			String hex = Integer.toHexString(0xff & data[i]);

			if (hex.length() == 1) {
				hexString.append('0');
			}

			hexString.append(hex);
		}

		return hexString.toString();
	}

	public static Document build01() {
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

		Element requestTimeElement = headerElement.addElement("requestTime");
		requestTimeElement.setText("2017-07-28");

		// identityInfo 信息
		Element identityInfoElement = rootElement.addElement("identityInfo");
		Element corpCodeElement = identityInfoElement.addElement("corpCode");
		corpCodeElement.setText("TESTFX");

		Element userNameElement = identityInfoElement.addElement("userName");
		userNameElement.setText("admin");

		// orderRequest 订单请求
		Element orderRequestElement = rootElement.addElement("orderRequest");
		Element orderElement = orderRequestElement.addElement("order");
		Element certificateNoElement = orderElement.addElement("certificateNo");
		certificateNoElement.setText("140826199001120012");

		Element linkNameElement = orderElement.addElement("linkName");
		linkNameElement.setText("郭胜利");

		Element linkMobileElement = orderElement.addElement("linkMobile");
		linkMobileElement.setText("15201132276");

		Element orderCodeElement = orderElement.addElement("orderCode");
		orderCodeElement.setText("t20170728162531");
		Element orderPriceElement = orderElement.addElement("orderPrice");
		orderPriceElement.setText("230.00");

		// Element srcElement = orderElement.addElement("src");
		// srcElement.setText("weixin");

		// Element groupNoElement = orderElement.addElement("groupNo");
		// groupNoElement.setText("");
		Element payMethodElement = orderElement.addElement("payMethod");// 支付方式待定
		payMethodElement.setText("wait_pay");
		Element ticketOrdersElement = orderElement.addElement("ticketOrders");

		Element ticketOrderElement = ticketOrdersElement.addElement("ticketOrder");

		Element orderCodeTicketElement = ticketOrderElement.addElement("orderCode");// 子订单号的编码
		orderCodeTicketElement.setText("sub20170728163921");
		// Element credentialsElement =
		// ticketOrderElement.addElement("credentials");
		// Element credentialElement =
		// credentialsElement.addElement("credential");
		// Element nameElement = credentialElement.addElement("name");
		// nameElement.setText("郭胜利");
		// Element idElement = credentialElement.addElement("id");
		// idElement.setText("140826199001120012");
		Element priceTicketElement = ticketOrderElement.addElement("price");
		priceTicketElement.setText("50.00");
		Element quantityTicketElement = ticketOrderElement.addElement("quantity"); // 票的数量
		quantityTicketElement.setText("1");
		Element totalPriceTicketElement = ticketOrderElement.addElement("totalPrice");
		totalPriceTicketElement.setText("50.00");
		Element occDateTicketElement = ticketOrderElement.addElement("occDate");// 游玩日期
		occDateTicketElement.setText("2017-08-02");
		Element goodsCodeTicketElement = ticketOrderElement.addElement("goodsCode");// 商品编码
		goodsCodeTicketElement.setText("PST20160918013085");
		Element goodsNameTicketElement = ticketOrderElement.addElement("goodsName");
		goodsNameTicketElement.setText("大脚怪表演1");
		Element remarkTicketElement = ticketOrderElement.addElement("remark");
		remarkTicketElement.setText("测试数据1");
		System.out.println(document.getRootElement().asXML().toString()); // 将document文档对象直接转换成字符串输出
		return document;
		// parseXml02(document);
		// Writer fileWriter = new FileWriter("c:\\module.xml");
		// //dom4j提供了专门写入文件的对象XMLWriter
		// XMLWriter xmlWriter = new XMLWriter(fileWriter);
		// xmlWriter.write(document);
		// xmlWriter.flush();
		// xmlWriter.close();
		// System.out.println("xml文档添加成功！");
	}

	public static Document findImgXML() {

		// DocumentHelper提供了创建Document对象的方法
		Document document = DocumentHelper.createDocument();
		// 添加节点信息
		Element rootElement = document.addElement("PWBRequest");
		Element transactionNameElement = rootElement.addElement("transactionName");
		transactionNameElement.setText("SEND_CODE_IMG_REQ");

		// header 信息
		Element headerElement = rootElement.addElement("header");
		Element applicationElement = headerElement.addElement("application");
		applicationElement.setText("SendCode");

		Element requestTimeElement = headerElement.addElement("requestTime");
		requestTimeElement.setText("2017-07-28");

		// identityInfo 信息
		Element identityInfoElement = rootElement.addElement("identityInfo");
		Element corpCodeElement = identityInfoElement.addElement("corpCode");
		corpCodeElement.setText("TESTFX");

		Element userNameElement = identityInfoElement.addElement("userName");
		userNameElement.setText("admin");

		// orderRequest 订单请求
		Element orderRequestElement = rootElement.addElement("orderRequest");
		Element orderElement = orderRequestElement.addElement("order");

		Element orderCodeElement = orderElement.addElement("orderCode"); // 该订单号为我们orders表中的orders_no
		orderCodeElement.setText("t20170728162531");

		//
		System.out.println(document.getRootElement().asXML().toString()); // 将document文档对象直接转换成字符串输出
		return document;

	}

	public static Document findErweiXML() {

		// DocumentHelper提供了创建Document对象的方法
		Document document = DocumentHelper.createDocument();
		// 添加节点信息
		Element rootElement = document.addElement("PWBRequest");
		Element transactionNameElement = rootElement.addElement("transactionName");
		transactionNameElement.setText("SEND_CODE_IMG_REQ");

		// header 信息
		Element headerElement = rootElement.addElement("header");
		Element applicationElement = headerElement.addElement("application");
		applicationElement.setText("SendCode");

		Element requestTimeElement = headerElement.addElement("requestTime");
		requestTimeElement.setText("2015-07-08 14:55:53");

		// identityInfo 信息
		Element identityInfoElement = rootElement.addElement("identityInfo");
		Element corpCodeElement = identityInfoElement.addElement("corpCode");
		corpCodeElement.setText("sdzfxmxqcfx");

		Element userNameElement = identityInfoElement.addElement("userName");
		userNameElement.setText("mxqcfx");

		// orderRequest 订单请求
		Element orderRequestElement = rootElement.addElement("orderRequest");
		Element orderElement = orderRequestElement.addElement("order");

		Element orderCodeElement = orderElement.addElement("orderCode"); // 该订单号为我们orders表中的orders_no
		orderCodeElement.setText("o201708071533952694");

		//
		System.out.println(document.getRootElement().asXML().toString()); // 将document文档对象直接转换成字符串输出
		return document;

	}

	public static String parseXml02(String xmlStr) {
		try {

			Document document = DocumentHelper.parseText(xmlStr);
			Element rootElement = document.getRootElement();
			String root = rootElement.getText();
			System.out.println(root);
			Iterator<Element> responseIterator = rootElement.elementIterator();
			int code = -1;
			String img = "";
			while (responseIterator.hasNext()) {
				Element responseElement = responseIterator.next();
				System.out.println(
						"name-->" + responseElement.getName() + " :" + " value-->" + responseElement.getText());
				if (responseElement.getName().equals("code")) {
					String codeVal = responseElement.getText();
					code = Integer.parseInt(codeVal);
				} else if (code == 0) {
					if (responseElement.getName().equals("img")) {
						img = responseElement.getText();
					}
				}

				// Element codeElement = responseElement.element("code");
				// String codeStr = codeElement.getText();
				// int code = Integer.parseInt(codeStr);
				// if(code == 0){
				// Element imgElement = responseElement.element("img");
				// String imgStr = imgElement.getText();
				// System.out.println(imgStr);
				// }
			}
			return img;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Jedis getJedis(){
//		String path = getClass().getResource("/../../META-INF/redis.json").getPath();
//		JSONObject json1 = ParseFile.parseJson(path);
//		String host = json1.getString("host");
//		int port = json1.getInt("port");
//		String password = json1.getString("password");
		Jedis jedis = null;
		JedisPoolConfig config = new JedisPoolConfig();
		//最大空闲连接数, 应用自己评估，不要超过ApsaraDB for Redis每个实例最大的连接数
		config.setMaxIdle(10000);
		//最大连接数, 应用自己评估，不要超过ApsaraDB for Redis每个实例最大的连接数
		config.setMaxTotal(10000);
		config.setTestOnBorrow(false);
		config.setTestOnReturn(false);
		JedisPool pool = new JedisPool(config, "r-2ze106bcaf9ce214.redis.rds.aliyuncs.com", 6379, 1000, "Shengli2017");
		jedis = pool.getResource();
		return jedis;
	}

}
