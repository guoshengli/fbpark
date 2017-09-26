package com.fbpark.rest.dao;

import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.Menu;
@Repository("menuDao")
public class MenuDaoImpl extends BaseDaoImpl<Menu, Long> implements MenuDao {

	public MenuDaoImpl() {
		super(Menu.class);
		// TODO Auto-generated constructor stub
	}


}
