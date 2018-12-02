package cn.itcast.erp.action;
import cn.itcast.erp.biz.IOrderdetailBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.exception.ERPException;

/**
 * 订单明细Action 
 * @author Administrator
 *
 */
public class OrderdetailAction extends BaseAction<Orderdetail> {

	private IOrderdetailBiz orderdetailBiz;
	
	public void setOrderdetailBiz(IOrderdetailBiz orderdetailBiz) {
		this.orderdetailBiz = orderdetailBiz;
		setBaseBiz(orderdetailBiz);
	}
	
	private Long storeuuid;

	public Long getStoreuuid() {
		return storeuuid;
	}

	public void setStoreuuid(Long storeuuid) {
		this.storeuuid = storeuuid;
	}
	
	public void doInStore() {
		Emp loginUser = this.getLoginUser();
		if (null == loginUser) {
			ajaxReturn(false, "您还没有登陆呢");
			return;
		}
		try {
			orderdetailBiz.doInStore(getId(), loginUser.getUuid(), storeuuid);
			ajaxReturn(true, "入库成功");
		} catch (ERPException e) {
			e.printStackTrace();
			ajaxReturn(false, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "入库失败");
		}
	}
	
	public void doOutStore() {
		Emp loginUser = this.getLoginUser();
		if (null == loginUser) {
			ajaxReturn(false, "您还没有登陆呢");
			return;
		}
		try {
			orderdetailBiz.doOutStore(loginUser.getUuid(), getId(), storeuuid);
			ajaxReturn(true, "出库成功");
		} catch (ERPException e) {
			e.printStackTrace();
			ajaxReturn(false, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "出库失败");
		}
	}
}
