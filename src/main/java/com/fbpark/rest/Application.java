 package com.fbpark.rest;
 
 import org.glassfish.jersey.server.ResourceConfig;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.fbpark.rest.service.AdminServiceImpl;
import com.fbpark.rest.service.BasicParamServiceImpl;
import com.fbpark.rest.service.CDNAccessServiceImpl;
import com.fbpark.rest.service.NotificationServiceImpl;
import com.fbpark.rest.service.PoiServiceImpl;
import com.fbpark.rest.service.TicketServiceImpl;
import com.fbpark.rest.service.UserServiceImpl;
 
 public class Application extends ResourceConfig
 {
   public Application()
   {
     register(JacksonJsonProvider.class);
     register(UserServiceImpl.class);
     register(CDNAccessServiceImpl.class);
     register(AdminServiceImpl.class);
     register(PoiServiceImpl.class);
     register(NotificationServiceImpl.class);
     register(TicketServiceImpl.class);
     register(BasicParamServiceImpl.class);
     
   }
 }

