package com.fbpark.rest.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.Cart;
@Repository("cartDao")
@SuppressWarnings("unchecked")
public class CartDaoImpl extends BaseDaoImpl<Cart, Long> implements CartDao {

	public CartDaoImpl() {
		super(Cart.class);
	}

	
	@Override
	public List<Cart> getCartList(Long userid,int count) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Cart where user_id=? order by create_time desc";
		Query query = session.createQuery(hql).setLong(0, userid)
				.setMaxResults(count);
		List<Cart> cartList = query.list();
		return cartList;
	}

	@Override
	public List<Cart> getCartList(Long userid, int count, Long cartid) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Cart where user_id=? and id < ? order by create_time desc";
		Query query = session.createQuery(hql).setLong(0, userid).setLong(1,cartid)
				.setMaxResults(count);
		List<Cart> cartList = query.list();
		return cartList;
	}

}
