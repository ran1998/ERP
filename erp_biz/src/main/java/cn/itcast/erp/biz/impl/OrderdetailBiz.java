package cn.itcast.erp.biz.impl;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.itcast.erp.biz.IOrderdetailBiz;
import cn.itcast.erp.dao.IOrderdetailDao;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.dao.impl.StoredetailDao;
import cn.itcast.erp.dao.impl.StoreoperDao;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;
import cn.itcast.erp.entity.Storedetail;
import cn.itcast.erp.entity.Storeoper;
import cn.itcast.erp.entity.Supplier;
import cn.itcast.erp.exception.ERPException;
import cn.itcast.redsun.bos.ws.impl.IWaybillWs;
/**
 * 订单明细业务逻辑类
 * @author Administrator
 *
 */
public class OrderdetailBiz extends BaseBiz<Orderdetail> implements IOrderdetailBiz {

	private IOrderdetailDao orderdetailDao;
	
	public void setOrderdetailDao(IOrderdetailDao orderdetailDao) {
		this.orderdetailDao = orderdetailDao;
		setBaseDao(orderdetailDao);
	}

	private StoredetailDao storeDetailDao;
	private StoreoperDao storeOperDao;

	public void setStoreDetailDao(StoredetailDao storeDetailDao) {
		this.storeDetailDao = storeDetailDao;
	}
	public void setStoreOperDao(StoreoperDao storeOperDao) {
		this.storeOperDao = storeOperDao;
	}
	
	private IWaybillWs waybillWs;
	private ISupplierDao supplierDao;

	public void setWaybillWs(IWaybillWs waybillWs) {
		this.waybillWs = waybillWs;
	}
	public void setSupplierDao(ISupplierDao supplierDao) {
		this.supplierDao = supplierDao;
	}
	/**
	 * 入库
	 */
	@Override
	public void doInStore(Long uuid, Long empUuid, Long storeUuid) {
		// 更新商品明细
		Orderdetail orderdetail = orderdetailDao.get(uuid);
		if (!Orderdetail.STATE_NO_IN.equals(orderdetail.getState())) {
			throw new ERPException("该商品已经入库了呢");
		}
		// 设置状态
		orderdetail.setState(Orderdetail.STATE_IN);
		// 设置入库操作员
		orderdetail.setEnder(empUuid);
		// 设置放入哪个仓库
		orderdetail.setStoreuuid(storeUuid);
		// 设置确定时间
		orderdetail.setEndtime(Calendar.getInstance().getTime());
		
		// 查询同一仓库是否有相同商品明细
		Storedetail storeDetail = new Storedetail();
		storeDetail.setGoodsuuid(orderdetail.getGoodsuuid());
		storeDetail.setStoreuuid(storeUuid);
		List<Storedetail> list = storeDetailDao.getList(storeDetail, null, null);
		
		if (list != null && list.size() > 0) {
			list.get(0).setNum(list.get(0).getNum() + orderdetail.getNum());
		} else {
			storeDetail.setNum(orderdetail.getNum());
			storeDetailDao.add(storeDetail);
		}
		
		// 更新入库记录
		Storeoper storeOper = new Storeoper();
		// 设置入库操作员
		storeOper.setEmpuuid(empUuid);
		// 设置入库的商品
		storeOper.setGoodsuuid(orderdetail.getGoodsuuid());
		// 设置入库的数量
		storeOper.setNum(orderdetail.getNum());
		// 设置入库时间
		storeOper.setOpertime(new Date());
		// 设置入库仓库
		storeOper.setStoreuuid(storeUuid);
		// 设置入库类型
		storeOper.setType(Storeoper.TYPE_IN);
		storeOperDao.add(storeOper);
		
		// 是否需要更新订单状态
		Orders orders = orderdetail.getOrders();
		Orderdetail orderdetail2 = new Orderdetail();
		orderdetail2.setState(Orderdetail.STATE_NO_IN);
		orderdetail2.setOrders(orders);
		// 查询同一订单下还有没有没有入库的明细
		long count = orderdetailDao.getCount(orderdetail2, null, null);
		if (count == 0) {
			// 设置订单为已入库
			orders.setState(Orders.STATE_END);
			// 设置操作人
			orders.setEnder(empUuid);
			// 设置操作时间
			orders.setEndtime(orderdetail.getEndtime());
		}
	}
	/**
	 * 出库
	 * @param empuuid
	 * @param uuid
	 * @param storeuuid
	 */
	public void doOutStore(Long empuuid, Long uuid, Long storeuuid) {
		// 获取订单明细
		Orderdetail orderdetail = orderdetailDao.get(uuid);
		if (!Orderdetail.STATE_NOT_OUT.equals(orderdetail.getState())) {
			throw new ERPException("该明细已经出库了哟");
		}
		
		// 更新订单明细
		orderdetail.setEnder(empuuid);
		orderdetail.setEndtime(new Date());
		orderdetail.setState(Orderdetail.STATE_OUT);
		orderdetail.setStoreuuid(storeuuid);
		
		// 查询库存
		Storedetail storedetail = new Storedetail();
		storedetail.setGoodsuuid(orderdetail.getGoodsuuid());
		storedetail.setStoreuuid(storeuuid);
		
		List<Storedetail> list = storeDetailDao.getList(storedetail, null, null);
		// 库存数量
		long num = -1l;
		if (null != list && list.size() > 0) {
			storedetail = list.get(0);
			num = storedetail.getNum().longValue() - orderdetail.getNum();
		}
		if (num > 0) {
			// 库存充足
			storedetail.setNum(num);
		} else {
			// 库存不足
			throw new ERPException("库存不足");
		}
		
		// 添加库存变更操作
		Storeoper storeoper = new Storeoper();
		storeoper.setEmpuuid(empuuid);
		storeoper.setGoodsuuid(orderdetail.getGoodsuuid());
		storeoper.setNum(orderdetail.getNum());
		storeoper.setOpertime(orderdetail.getEndtime());
		storeoper.setStoreuuid(storeuuid);
		storeoper.setType("2");
		storeOperDao.add(storeoper);
		
		// 查询订单下是否所有明细都已出库
		Orderdetail orderdetail2 = new Orderdetail();
		Orders order = orderdetail.getOrders();
		orderdetail2.setOrders(order);
		orderdetail2.setState(Orderdetail.STATE_NOT_OUT);
		long count = orderdetailDao.getCount(orderdetail2, null, null);
		if (count == 0) {
			// 都已出库
			order.setState(Orders.STATE_OUT);
			order.setEnder(empuuid);
			order.setEndtime(orderdetail.getEndtime());
			// 获取客户信息
			Supplier supplier = supplierDao.get(order.getSupplieruuid());
			// 在线预约下单
			Long waybillSn = waybillWs.addWaybill(1l, supplier.getAddress(), supplier.getName(), supplier.getContact(), "--");
			// 设置运单编号
			order.setWaybillsn(waybillSn);
		}
	}
	
	
}
