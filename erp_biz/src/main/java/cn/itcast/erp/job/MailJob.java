package cn.itcast.erp.job;

import java.util.List;

import javax.mail.MessagingException;

import cn.itcast.erp.biz.IStoredetailBiz;
import cn.itcast.erp.entity.Storealert;

public class MailJob {
	private IStoredetailBiz storedetailBiz;
	
	public void setStoredetailBiz(IStoredetailBiz storedetailBiz) {
		this.storedetailBiz = storedetailBiz;
	}

	public void sendStorealertMail() {
		// 查询是否存在库存预警
		List<Storealert> storealertList = storedetailBiz.getStorealertList();
		if (storealertList.size() > 0) {
			try {
				// 发送预警邮件
				storedetailBiz.sendStoreAlertMail();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}
}
