package cn.itcast.erp.action;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IOrdersBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;
import cn.itcast.erp.exception.ERPException;
import cn.itcast.redsun.bos.ws.Waybilldetail;
import cn.itcast.redsun.bos.ws.impl.IWaybillWs;

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
	/**
	 * 我的订单
	 */
	public void myListByPage() {
		if (null == this.getT1()) {
			this.setT1(new Orders());
		}
		Emp loginUser = this.getLoginUser();
		this.getT1().setCreater(loginUser.getUuid());
		super.listByPage();
	}
	
	/**
	 * 根据订单导出excel
	 */
 	public void exportById() {
 		String filename = "orders_"+getId()+".xls";
 		HttpServletResponse response = ServletActionContext.getResponse();
 		try {
 			response.setHeader("Content-Disposition", "attachment;filename="+new String(filename.getBytes(), "ISO-8859-1"));
			ordersBiz.exportById(response.getOutputStream(), getId());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
 	}
 	private IWaybillWs waybillWs;
 	
 	public void setWaybillWs(IWaybillWs waybillWs) {
		this.waybillWs = waybillWs;
	}
 	
 	private Long waybillSn;
 	

	public Long getWaybillSn() {
		return waybillSn;
	}


	public void setWaybillSn(Long waybillSn) {
		this.waybillSn = waybillSn;
	}


	/**
 	 * 查询运单详情
 	 */
 	public void waybilldetailList() {
 		List<Waybilldetail> waybilldetailList = waybillWs.waybilldetailList(waybillSn);
 		write(JSON.toJSONString(waybilldetailList));
 	}
}
