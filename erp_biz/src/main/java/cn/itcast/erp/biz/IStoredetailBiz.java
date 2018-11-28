package cn.itcast.erp.biz;
import java.util.List;

import javax.mail.MessagingException;

import cn.itcast.erp.entity.Storealert;
import cn.itcast.erp.entity.Storedetail;
/**
 * 仓库库存业务逻辑层接口
 * @author Administrator
 *
 */
public interface IStoredetailBiz extends IBaseBiz<Storedetail>{
	
	public List<Storealert> getStorealertList();
	
	public void sendStoreAlertMail() throws MessagingException;
}

