package cn.itcast.erp.biz.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.itcast.erp.biz.IStoredetailBiz;
import cn.itcast.erp.dao.IGoodsDao;
import cn.itcast.erp.dao.IStoreDao;
import cn.itcast.erp.dao.IStoredetailDao;
import cn.itcast.erp.dao.impl.GoodsDao;
import cn.itcast.erp.entity.Storedetail;
/**
 * 仓库库存业务逻辑类
 * @author Administrator
 *
 */
public class StoredetailBiz extends BaseBiz<Storedetail> implements IStoredetailBiz {

	private IStoredetailDao storedetailDao;
	
	public void setStoredetailDao(IStoredetailDao storedetailDao) {
		this.storedetailDao = storedetailDao;
		setBaseDao(storedetailDao);
	}
	
	private IGoodsDao goodsDao;
	private IStoreDao storeDao;
	

	public void setGoodsDao(IGoodsDao goodsDao) {
		this.goodsDao = goodsDao;
	}


	public void setStoreDao(IStoreDao storeDao) {
		this.storeDao = storeDao;
	}

	@Override
	public List<Storedetail> getListByPage(Storedetail t1, Storedetail t2, Object param, int firstResult, int maxResults) {
		List<Storedetail> list = super.getListByPage(t1, t2, param, firstResult, maxResults);
		Map<Long, String> goodsNameMap = new HashMap<Long, String>();
		Map<Long, String> storeNameMap = new HashMap<Long, String>();
		
		for (Storedetail sd : list) {
			sd.setGoodsName(getGoodsName(sd.getGoodsuuid(), goodsNameMap));
			sd.setStoreName(getStoreName(sd.getStoreuuid(), storeNameMap));
		}
		return list;
	}

	/**
	 * 获取商品·名称
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
	 * 获取仓库名
	 * @param uuid
	 * @param storeNameMap
	 * @return
	 */
	private String getStoreName(long uuid, Map<Long, String> storeNameMap) {
		String name  = storeNameMap.get(uuid);
		if (null == name) {
			name = storeDao.get(uuid).getName();
			storeNameMap.put(uuid, name);
		}
		return name;
	}
}
