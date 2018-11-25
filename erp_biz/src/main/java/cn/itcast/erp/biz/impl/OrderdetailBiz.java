package cn.itcast.erp.biz.impl;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.itcast.erp.biz.IOrderdetailBiz;
import cn.itcast.erp.dao.IOrderdetailDao;
import cn.itcast.erp.dao.impl.StoredetailDao;
import cn.itcast.erp.dao.impl.StoreoperDao;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;
import cn.itcast.erp.entity.Storedetail;
import cn.itcast.erp.entity.Storeoper;
import cn.itcast.erp.exception.ERPException;
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
	
	
}
