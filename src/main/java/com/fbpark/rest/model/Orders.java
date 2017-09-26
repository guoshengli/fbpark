package com.fbpark.rest.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@Table(name="orders")
@Entity
public class Orders extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1437480705058032464L;
	
	@Column(name="order_no")
	private String order_no;
	
	//订单状态：0 未付款、1 已付款、2 已取消、3 退款中、4 退款失败、5 退票成功
	@Column(name="status")
	private int status;
	
	@Column(name="description", columnDefinition="TEXT")
	private String description;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Generated(GenerationTime.ALWAYS)
    @Column(name="create_time", insertable=false, updatable=false)
    private Date create_time;
	
	//订单总金额
	@Column(name="amount")
	private BigDecimal amount;
	
	//支付类型 wechat alipay
	@Column(name="pay_type")
	private String pay_type;
	
	//ping++ 支付id
	@Column(name="charge_id")
	private String charge_id;
	
	//ping++ 退款id
	@Column(name="refund_id")
	private String refund_id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", updatable = false)
	private User user;
	
	
	 @ManyToMany(mappedBy="orders", fetch=FetchType.LAZY)
	 private Set<Ticket> ticket;
	 
	 @OneToMany(mappedBy = "orders", cascade = { javax.persistence.CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<OrdersTicket> ordersTickets = new ArrayList<OrdersTicket>();

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getCreate_time() {
		return create_time.getTime() / 1000;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public Set<Ticket> getTicket() {
		return ticket;
	}

	public void setTicket(Set<Ticket> ticket) {
		this.ticket = ticket;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCharge_id() {
		return charge_id;
	}

	public void setCharge_id(String charge_id) {
		this.charge_id = charge_id;
	}

	public String getRefund_id() {
		return refund_id;
	}

	public void setRefund_id(String refund_id) {
		this.refund_id = refund_id;
	}

	public List<OrdersTicket> getOrdersTickets() {
		return ordersTickets;
	}

	public void setOrdersTickets(List<OrdersTicket> ordersTickets) {
		this.ordersTickets = ordersTickets;
	}

	

}
