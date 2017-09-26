package com.fbpark.rest.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name = "orders_ticket")
@Entity
public class OrdersTicket extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 42338459710627963L;
	@Column(name = "num")
	private int num;

	@Column(name = "barcode", columnDefinition = "TEXT")
	private String barcode;

	// 是否使用 enable 未使用 disable 已使用
	@Column(name = "status")
	private int status;

	@Column(name = "total")
	private BigDecimal total;

	@Column(name = "update_time")
	private Date update_time;

	@Column(name = "ticket_order_no")
	private String ticket_order_no;

	// ping ++ 退款Id
	@Column(name = "refund_id")
	private String refund_id;

	// 智游宝 退单号
	@Column(name = "returnCode")
	private String returnCode;

	// 智游宝 退单批次号
	@Column(name = "retreatBatchNo")
	private String retreatBatchNo;

	// 保单号
	@Column(name = "transNo")
	private String transNo;

	// 开发者身份标识
	@Column(name = "partnerId")
	private int partnerId;

	// 投保单号
	@Column(name = "insureNum")
	private String insureNum;

	// 安全码
	@Column(name = "verification")
	private String verification;

	// 投保开始日期
	@Column(name = "startDate")
	private String startDate;

	// 投保结束日期
	@Column(name = "endDate")
	private String endDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Orders orders;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ticket_id")
	private Ticket ticket;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contacter_id")
	private Contacter contacter;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public Orders getOrders() {
		return orders;
	}

	public void setOrders(Orders orders) {
		this.orders = orders;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public long getUpdate_time() {
		return update_time.getTime() / 1000;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public String getTicket_order_no() {
		return ticket_order_no;
	}

	public void setTicket_order_no(String ticket_order_no) {
		this.ticket_order_no = ticket_order_no;
	}

	public String getRefund_id() {
		return refund_id;
	}

	public void setRefund_id(String refund_id) {
		this.refund_id = refund_id;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public Contacter getContacter() {
		return contacter;
	}

	public void setContacter(Contacter contacter) {
		this.contacter = contacter;
	}

	public String getRetreatBatchNo() {
		return retreatBatchNo;
	}

	public void setRetreatBatchNo(String retreatBatchNo) {
		this.retreatBatchNo = retreatBatchNo;
	}

	public String getTransNo() {
		return transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public int getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(int partnerId) {
		this.partnerId = partnerId;
	}

	public String getInsureNum() {
		return insureNum;
	}

	public void setInsureNum(String insureNum) {
		this.insureNum = insureNum;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getVerification() {
		return verification;
	}

	public void setVerification(String verification) {
		this.verification = verification;
	}

}
