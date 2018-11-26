package cn.itcast.erp.biz.impl;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.itcast.erp.biz.IOrdersBiz;
import cn.itcast.erp.dao.IOrdersDao;
import cn.itcast.erp.dao.impl.EmpDao;
import cn.itcast.erp.dao.impl.SupplierDao;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;
import cn.itcast.erp.exception.ERPException;
/**
 * 订单业务逻辑类
 * @author Administrator
 *
 */
public class OrdersBiz extends BaseBiz<Orders> implements IOrdersBiz {

	private IOrdersDao ordersDao;
	
	public void setOrdersDao(IOrdersDao ordersDao) {
		this.ordersDao = ordersDao;
		setBaseDao(ordersDao);
	}
	
	private EmpDao empDao;
	private SupplierDao supplierDao;
	
	public void setEmpDao(EmpDao empDao) {
		this.empDao = empDao;
	}

	public void setSupplierDao(SupplierDao supplierDao) {
		this.supplierDao = supplierDao;
	}

	public void add(Orders orders) {
		// 设置添加时状态为未审核
		orders.setState(Orders.STATE_CREATE);
		// 设置类型为采购
		// orders.setType(Orders.TYPE_IN);
		// 设置创建时间
		orders.setCreatetime(new Date());
		
		double totalMoney = 0;
		for (Orderdetail orderDetail : orders.getOrderDetails()) {
			totalMoney += orderDetail.getMoney();
			// 设置商品明细为未入库
			orderDetail.setState(orderDetail.STATE_NO_IN);
			// 设置明细对应的订单
			orderDetail.setOrders(orders);
		}
		orders.setTotalmoney(totalMoney);
		ordersDao.add(orders);
	}
	@Override
	public List<Orders> getListByPage(Orders t1,Orders t2,Object param,int firstResult,int maxResults) {
		// 获取分页数据
		List<Orders> listByPage = super.getListByPage(t1, t2, param, firstResult, maxResults);
		// 缓存员工的数据，key为员工编号,value为员工名
		Map<Long, String> empNameMap = new HashMap<Long, String>();
		// 缓存供应商的数据, key为供应商编号,value为供应商名
		Map<Long, String> supplierNameMap = new HashMap<Long, String>();
		for (Orders o : listByPage) {
			// 设置下单员名称
			o.setCreaterName(getEmpName(o.getCreater(), empNameMap));
			// 设置审核员名称
			o.setCheckerName(getEmpName(o.getCreater(), empNameMap));
			// 设置采购员名称
			o.setStarterName(getEmpName(o.getStarter(), empNameMap));
			// 设置库管员名称
			o.setEnderName(getEmpName(o.getEnder(), empNameMap));
			// 设置供应商名称
			o.setSupplierName(getSupplierName(o.getSupplieruuid(), supplierNameMap));
		}
		return super.getListByPage(t1, t2, param, firstResult, maxResults);
		
	}
	
	/**
	 * 通过id获取员工名
	 * @param uuid
	 * @param empNameMap
	 * @return
	 */
	private String getEmpName(Long uuid, Map<Long, String> empNameMap) {
		if (null == uuid) {
			return "";
		}
		String empName = empNameMap.get(uuid);
		if (null == empName) {
			// 从map缓存获取,没有则设置
			empName = empDao.get(uuid).getName();
			empNameMap.put(uuid, empName);
		}
		return empName;
	}
	
	/**
	 * 获取供应商名
	 * @param uuid
	 * @param supplierNameMap
	 * @return
	 */
	private String getSupplierName(Long uuid, Map<Long, String> supplierNameMap) {
		if (null == uuid) {
			return "";
		}
		String supplierName = supplierNameMap.get(uuid);
		if (null == supplierName) {
			supplierName = supplierDao.get(uuid).getName();
			supplierNameMap.put(uuid, supplierName);
		}
		return supplierName;
	}

	@Override
	public void doCheck(Long uuid, Long empUuid) {
		Orders order = ordersDao.get(uuid);
		if (!Orders.STATE_CREATE.equals(order.getState())) {
			throw new ERPException("该订单已经审核过了呢");
		}
		// 设置审核员
		order.setChecker(empUuid);
		// 设置审核时间
		order.setChecktime(new Date());
		// 更新审核状态
		order.setState(Orders.STATE_CHECK);
	}

	@Override
	public void doStart(Long uuid, Long empUuid) {
		Orders order = ordersDao.get(uuid);
		if (!Orders.STATE_CHECK.equals(order.getState())) {
			throw new ERPException("该订单已确认过了呢");
		}
		// 设置确定人
		order.setStarter(empUuid);
		// 设置确定时间
		order.setStarttime(new Date());
		// 更新确定状态
		order.setState(Orders.STATE_START);
	}
}
