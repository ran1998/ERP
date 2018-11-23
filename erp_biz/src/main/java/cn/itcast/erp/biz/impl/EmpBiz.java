package cn.itcast.erp.biz.impl;
import org.apache.shiro.authc.credential.HashingPasswordService;
import org.apache.shiro.crypto.hash.Md5Hash;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.entity.Emp;
/**
 * 员工业务逻辑类
 * @author Administrator
 *
 */
public class EmpBiz extends BaseBiz<Emp> implements IEmpBiz {

	private int hashIterations = 2;
	private IEmpDao empDao;
	
	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
		setBaseDao(empDao);
	}

	@Override
	public Emp findByUsernameAndPwd(String username, String pwd) {
		Md5Hash md5 = new Md5Hash(username, pwd, hashIterations);
		System.out.println(md5.toString());
		return empDao.findByUsernameAndPwd(username, md5.toString());
	}

	
}
