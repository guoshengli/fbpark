package com.fbpark.rest.dao;

import java.util.List;

import com.fbpark.rest.model.Insurant;

public interface InsurantDao extends BaseDao<Insurant, Long> {
	public List<Insurant> getInsurantByIdentityCard(String identity_card);
	
	
	public List<Insurant> getInsurantByIdentityCards(List<String> identity_cards);
}
