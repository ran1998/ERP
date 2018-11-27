package cn.itcast.erp.action;
import cn.itcast.erp.biz.ISupplierBiz;
import cn.itcast.erp.entity.Supplier;

/**
 * 供应商Action 
 * @author Administrator
 *
 */
public class SupplierAction extends BaseAction<Supplier> {

	private ISupplierBiz supplierBiz;
	
	public void setSupplierBiz(ISupplierBiz supplierBiz) {
		this.supplierBiz = supplierBiz;
		setBaseBiz(supplierBiz);
	}
	/**romote传过来的参数*/
	private String q;

	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	
	@Override
	public void list() {
		if (null == getT1()) {
			this.setT1(new Supplier());
		}
		this.getT1().setName(q);
		super.list();
	}
	
}
