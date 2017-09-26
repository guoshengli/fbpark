package com.fbpark.rest.model;

 import java.io.Serializable;
 import java.util.Date;
 import javax.persistence.AssociationOverrides;
 import javax.persistence.Column;
 import javax.persistence.EmbeddedId;
 import javax.persistence.Entity;
 import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

 
 @Entity
 @Table(name="recommandation")
 @AssociationOverrides({@javax.persistence.AssociationOverride(name="pk.user", joinColumns={@javax.persistence.JoinColumn(name="user_id")}), @javax.persistence.AssociationOverride(name="pk.follower", joinColumns={@javax.persistence.JoinColumn(name="follower_id")})})
 public class Recommandation
   implements Serializable
 {
   private static final long serialVersionUID = 2524659555729848644L;
 
   @EmbeddedId
   private RecommandationId pk = new RecommandationId();
 
   @Temporal(TemporalType.TIMESTAMP)
   @Generated(GenerationTime.ALWAYS)
   @Column(name="create_time", insertable=false, updatable=false)
   private Date create_time;
 
   public RecommandationId getPk() { return this.pk; }
 
   public void setPk(RecommandationId pk)
   {
     this.pk = pk;
   }
 
   public Long getCreateTime() {
     return create_time.getTime() / 1000L;
   }
 
   public void setCreateTime(Date create_time) {
     this.create_time = create_time;
   }
 }

