package com.dream.rest;

import java.util.HashMap;
import java.util.Map;

import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.APIConnectionException;
import com.pingplusplus.exception.APIException;
import com.pingplusplus.exception.AuthenticationException;
import com.pingplusplus.exception.ChannelException;
import com.pingplusplus.exception.InvalidRequestException;
import com.pingplusplus.exception.RateLimitException;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.ChargeCollection;
import com.pingplusplus.model.Refund;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }
    
    static String charge = null;
    
    public Refund refund(Integer amount) {
    	  if (charge == null) {
    	      return null;
    	  }
    	  Refund refund = null;
    	  Map<String, Object> params = new HashMap<String, Object>();
    	  params.put("description", "苹果被咬了一口。");
    	  params.put("amount", amount);// 退款的金额, 单位为对应币种的最小货币单位，例如：人民币为分（如退款金额为 1 元，此处请填 100）。必须小于等于可退款金额，默认为全额退款

    	  try {
    	      refund = Refund.create(this.charge, params);
    	      System.out.println(refund);
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
    	    return refund;
    	  }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
    
    
    @SuppressWarnings("deprecation")
	public static void main(String[] args)throws Exception {
//		Pingpp.apiKey="sk_test_ibbTe5jLGCi5rzfH4OqPW9KC";
		Charge c = null;
//		try {
//			c = Charge.retrieve("ch_L8qn10mLmr1GS8e5OODmHaL4");
//		} catch (AuthenticationException e) {
//			e.printStackTrace();
//		} catch (InvalidRequestException e) {
//			e.printStackTrace();
//		} catch (APIConnectionException e) {
//			e.printStackTrace();
//		} catch (APIException e) {
//			e.printStackTrace();
//		} catch (ChannelException e) {
//			e.printStackTrace();
//		} catch (RateLimitException e) {
//			e.printStackTrace();
//		}
//		System.out.println(c.getApp().toString());
    	
    	Pingpp.apiKey = "sk_test_ibbTe5jLGCi5rzfH4OqPW9KC";

    	Map<String, Object> chargeParams = new HashMap<String, Object>();
    	chargeParams.put("order_no",  "123456789");
    	chargeParams.put("amount", 100);
    	Map<String, String> app = new HashMap<String, String>();
    	app.put("id", "app_1Gqj58ynP0mHeX1q");
    	chargeParams.put("app", app);
    	chargeParams.put("channel",  "upacp");
    	chargeParams.put("currency", "cny");
    	chargeParams.put("client_ip",  "127.0.0.1");
    	chargeParams.put("subject",  "Your Subject");
    	chargeParams.put("body",  "Your Body");

    	try {
			c = Charge.create(chargeParams);
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
//    	System.out.println(c.toString());
//    	Charge c1 = Charge.retrieve(c.getId());
//    	System.out.println(c1.toString());
    	Map<String,Object> chargeParams1 = new HashMap<String,Object>();
    	chargeParams1.put("limit", 3);
    	ChargeCollection cc = Charge.list(chargeParams1);
    	System.out.println(cc.toString());
	}
}
