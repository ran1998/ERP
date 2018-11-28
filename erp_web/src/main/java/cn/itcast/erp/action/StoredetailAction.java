package cn.itcast.erp.action;
import java.util.List;

import javax.mail.MessagingException;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IStoredetailBiz;
import cn.itcast.erp.entity.Storealert;
import cn.itcast.erp.entity.Storedetail;
import cn.itcast.erp.exception.ERPException;

/**
 * 仓库库存Action 
 * @author Administrator
 *
 */
public class StoredetailAction extends BaseAction<Storedetail> {

	private IStoredetailBiz storedetailBiz;
	
	public void setStoredetailBiz(IStoredetailBiz storedetailBiz) {
		this.storedetailBiz = storedetailBiz;
		setBaseBiz(storedetailBiz);
	}
	
	/**
	 * 库存不足报警列表
	 */
	public void storealertList() {
		List<Storealert> storealertList = storedetailBiz.getStorealertList();
		write(JSON.toJSONString(storealertList));
	}
	
	/**
	 * 发送预警邮件
	 */
	public void sendStorealertMail() {
		try {
			storedetailBiz.sendStoreAlertMail();
			ajaxReturn(true, "发送邮件成功");
		} catch (MessagingException e) {
			ajaxReturn(false, "构建预警邮件失败");
			e.printStackTrace();
		} catch (ERPException e) {
			e.printStackTrace();
			ajaxReturn(false, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "邮件发送失败");
		}
	}
	
}
