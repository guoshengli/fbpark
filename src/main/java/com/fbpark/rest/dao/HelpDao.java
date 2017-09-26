package com.fbpark.rest.dao;

import java.util.List;

import com.fbpark.rest.model.Help;

public interface HelpDao extends BaseDao<Help, Long> {
	public List<Help> getHelpBySequence();
}
