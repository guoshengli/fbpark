package com.fbpark.rest.dao;

import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.Template;
@Repository("templateDao")
public class TemplateDaoImpl extends BaseDaoImpl<Template, Long> implements TemplateDao {

	public TemplateDaoImpl() {
		super(Template.class);
		// TODO Auto-generated constructor stub
	}


}
