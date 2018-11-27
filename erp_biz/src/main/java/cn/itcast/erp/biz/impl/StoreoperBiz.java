package cn.itcast.erp.biz.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.itcast.erp.biz.IStoreoperBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IGoodsDao;
import cn.itcast.erp.dao.IStoreDao;
import cn.itcast.erp.dao.IStoreoperDao;
import cn.itcast.erp.entity.Storeoper;
/**
 * 仓库操作记录业务逻辑类
 * @author Administrator
 *
 */
public class StoreoperBiz extends BaseBiz<Storeoper> implements IStoreoperBiz {

	private IStoreoperDao storeoperDao;
	
	public void setStoreoperDao(IStoreoperDao storeoperDao) {
		this.storeoperDao = storeoperDao;
		setBaseDao(storeoperDao);
	}

	private IGoodsDao goodsDao;
	private IStoreDao storeDao;
	private IEmpDao empDao;

	public void setGoodsDao(IGoodsDao goodsDao) {
		this.goodsDao = goodsDao;
	}
	public void setStoreDao(IStoreDao storeDao) {
		this.storeDao = storeDao;
	}
	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
	}
	
	@Override
	public List<Storeoper> getListByPage(Storeoper t1, Storeoper t2, Object param, int firstResult, int maxResults) {
		List<Storeoper> list = super.getListByPage(t1, t2, param, firstResult, maxResults);
		Map<Long, String> goodsNameMap = new HashMap<Long, String>();
		Map<Long, String> storeNameMap = new HashMap<Long, String>();
		Map<Long, String> empNameMap = new HashMap<Long, String>();
		for (Storeoper so : list) {
			so.setEmpName(getEmpName(so.getEmpuuid(), empNameMap));
			so.setGoodsName(getGoodsName(so.getGoodsuuid(), goodsNameMap));
			so.setStoreName(getStoreName(so.getStoreuuid(), storeNameMap));
		}
		return list;
	}
	
	/**
	 * 获取·仓库名
	 * @param uuid
	 * @param goodsNameMap
	 * @return
	 */
	private String getStoreName(long uuid, Map<Long, String> storeNameMap) {
		String name = storeNameMap.get(uuid);
		if (null == name) {
			name = storeDao.get(uuid).getName();
			storeNameMap.put(uuid, name);
		}
		return name;
	}
	/**
	 * 获取·商品名
	 * @param uuid
	 * @param goodsNameMap
	 * @return
	 */
	private String getGoodsName(long uuid, Map<Long, String> goodsNameMap) {
		String name = goodsNameMap.get(uuid);
		if (null == name) {
			name = goodsDao.get(uuid).getName();
			goodsNameMap.put(uuid, name);
		}
		return name;
	}
	/**
	 * 获取·操作员名
	 * @param uuid
	 * @param goodsNameMap
	 * @return
	 */
	private String getEmpName(long uuid, Map<Long, String> empNameMap) {
		String name = empNameMap.get(uuid);
		if (null == name) {
			name = empDao.get(uuid).getName();
			empNameMap.put(uuid, name);
		}
		return name;
	}
	
}
