package com.fbpark.rest.dao;

import java.util.List;

import com.fbpark.rest.model.Cart;

public interface CartDao extends BaseDao<Cart, Long> {
	public List<Cart> getCartList(Long userid,int count);
	
	public List<Cart> getCartList(Long userid,int count,Long cartid);
}
