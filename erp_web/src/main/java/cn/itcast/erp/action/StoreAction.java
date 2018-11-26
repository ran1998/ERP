package cn.itcast.erp.action;
import cn.itcast.erp.biz.IStoreBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Store;

/**
 * 仓库Action 
 * @author Administrator
 *
 */
public class StoreAction extends BaseAction<Store> {

	private IStoreBiz storeBiz;
	
	public void setStoreBiz(IStoreBiz storeBiz) {
		this.storeBiz = storeBiz;
		setBaseBiz(storeBiz);
	}
	
	/**
	 * 当前用户下的仓库
	 */
	public void myList() {
		Emp loginUser = this.getLoginUser();
		if (null == loginUser) {
			ajaxReturn(false, "您还没有登陆呢");
			return;
		}
		if (null == getT1()) {
			this.setT1(new Store());
		}
		this.getT1().setEmpuuid(loginUser.getUuid());
		super.list();
	}
}
