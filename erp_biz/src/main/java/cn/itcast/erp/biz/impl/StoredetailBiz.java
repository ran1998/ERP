package cn.itcast.erp.biz.impl;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import cn.itcast.erp.biz.IStoredetailBiz;
import cn.itcast.erp.dao.IGoodsDao;
import cn.itcast.erp.dao.IStoreDao;
import cn.itcast.erp.dao.IStoredetailDao;
import cn.itcast.erp.dao.impl.GoodsDao;
import cn.itcast.erp.entity.Storealert;
import cn.itcast.erp.entity.Storedetail;
import cn.itcast.erp.exception.ERPException;
import cn.itcast.erp.util.MailUtil;
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


	@Override
	public List<Storealert> getStorealertList() {
		return storedetailDao.getStorealertList();
	}

	private MailUtil mailUtil;
	
	public void setMailUtil(MailUtil mailUtil) {
		this.mailUtil = mailUtil;
	}

	// 邮件接收者
	private String to;
	// 邮件主题
	private String subject;
	// 邮件正文
	private String text;
	

	public void setTo(String to) {
		this.to = to;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}


	public void setText(String text) {
		this.text = text;
	}


	/**
	 * 发送警告邮件
	 */
	@Override
	public void sendStoreAlertMail() throws MessagingException {
		
		List<Storealert> storealertList = storedetailDao.getStorealertList();
		int count = storealertList==null ? 0 : storealertList.size();
		if (count > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			// 发送邮件
			mailUtil.sendMail(to, subject.replace("[time]", sdf.format(new Date())), text.replace("[count]", String.valueOf(count)));
		} else {
			throw new ERPException("没有需要预警的商品");
		}
	}
}
