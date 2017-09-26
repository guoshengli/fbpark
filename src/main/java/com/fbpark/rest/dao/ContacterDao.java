package com.fbpark.rest.dao;

import java.util.List;

import com.fbpark.rest.model.Contacter;

public interface ContacterDao extends BaseDao<Contacter, Long> {
	public List<Contacter> getContacterList(Long userid);
	
	public List<Contacter> getContacterByIdentityCard(String identify_card,Long user_id);
}
