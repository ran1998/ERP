package cn.itcast.erp.action;
import java.util.List;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IOrdersBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;
import cn.itcast.erp.exception.ERPException;

/**
 * 订单Action 
 * @author Administrator
 *
 */
public class OrdersAction extends BaseAction<Orders> {

	private IOrdersBiz ordersBiz;
	
	public void setOrdersBiz(IOrdersBiz ordersBiz) {
		this.ordersBiz = ordersBiz;
		setBaseBiz(ordersBiz);
	}
	
	private String json;
	
	
	public String getJson() {
		return json;
	}


	public void setJson(String json) {
		this.json = json;
	}


	/**
	 * 保存订单
	 */
	public void add() {
		System.out.println(json);
		Emp loginUser = this.getLoginUser();
		if (null == loginUser) {
			ajaxReturn(false, "您还未登录呢");
			return;
		}
		try {			
			// 获取提交的订单
			Orders orders = getT();
			// 设置提交订单的人
			orders.setCreater(loginUser.getUuid());
			// 获取订单明细
			List<Orderdetail> orderDatailsList = JSON.parseArray(json, Orderdetail.class);
			// 设置订单明细
			orders.setOrderDetails(orderDatailsList);
			// 保存订单
			ordersBiz.add(orders);
			ajaxReturn(true, "保存订单成功");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "保存订单失败");
		}
	}
	/**
	 * 审核
	 */
	public void doCheck() {
		Emp loginUser = this.getLoginUser();
		if (null == loginUser) {
			ajaxReturn(false, "您还没登陆呢");
			return;
		}
		try {
			ordersBiz.doCheck(getId(), loginUser.getUuid());
			ajaxReturn(true, "审核成功");
		} catch (ERPException e) {
			e.printStackTrace();
			ajaxReturn(false, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "审核失败");
		}
	}
	/**
	 * 确认
	 */
	public void doStart() {
		Emp loginUser = this.getLoginUser();
		if (null == loginUser) {
			ajaxReturn(false, "您还没有登陆呢");
			return;
		}
		try {
			ordersBiz.doStart(getId(), loginUser.getUuid());
			ajaxReturn(true, "确认成功");
		} catch (ERPException e) {
			e.printStackTrace();
			ajaxReturn(false, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "确认失败");
		}
	}
 	
}
