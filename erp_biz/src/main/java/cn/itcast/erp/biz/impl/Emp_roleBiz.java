package cn.itcast.erp.biz.impl;
import cn.itcast.erp.biz.IEmp_roleBiz;
import cn.itcast.erp.dao.IEmp_roleDao;
import cn.itcast.erp.entity.Emp_role;
/**
 * 员工角色业务逻辑类
 * @author Administrator
 *
 */
public class Emp_roleBiz extends BaseBiz<Emp_role> implements IEmp_roleBiz {

	private IEmp_roleDao emp_roleDao;
	
	public void setEmp_roleDao(IEmp_roleDao emp_roleDao) {
		this.emp_roleDao = emp_roleDao;
		setBaseDao(emp_roleDao);
	}

	
}
