package cn.itcast.erp.biz.impl;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authc.credential.HashingPasswordService;
import org.apache.shiro.crypto.hash.Md5Hash;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.impl.RoleDao;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Role;
import cn.itcast.erp.entity.Tree;
import cn.itcast.erp.exception.ERPException;
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
		System.out.println(encrypt(pwd, username));
		return empDao.findByUsernameAndPwd(username, encrypt(pwd, username));
	}
	
	@Override
	public void add(Emp emp) {
		// String encrypted = encrypt(emp.getPwd(), emp.getUsername());
		//emp.setPwd(encrypted);
		emp.setPwd(encrypt(emp.getUsername(), emp.getUsername()));
		empDao.add(emp);
	}
	
	@Override
	public void updatePwd(Long uuid, String oldPwd, String newPwd) throws ERPException {
		Emp emp = empDao.get(uuid);
		String oldEncrypt = encrypt(oldPwd, emp.getUsername());
		if (!emp.getPwd().equals(oldEncrypt)) {
			throw new ERPException("原密码错误");
		}
		// 设置持久化对象自动更新
		emp.setPwd(encrypt(newPwd, emp.getUsername()));
	}

	public String encrypt(String src, String salt) {
		Md5Hash md5 = new Md5Hash(src, salt, hashIterations);
		return md5.toString();
	}

	@Override
	public void updatePwd_reset(Long uuid, String newPwd) {
		Emp emp = empDao.get(uuid);
		if (null != emp) {
			emp.setPwd(encrypt(newPwd, emp.getUsername()));
		}
	}
	
	private RoleDao roleDao;
	

	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	/**
	 * 获取用户角色
	 */
	@Override
	public List<Tree> readEmpRoles(Long uuid) {
		ArrayList<Tree> arrayList = new ArrayList<Tree>();
		// 获取用户的所有角色
		Emp emp = empDao.get(uuid);
		List<Role> roles = emp.getRoles();
		// 获取角色列表
		List<Role> roleList = roleDao.getList(null, null, null);
		Tree t1 = null;
		for (Role role : roleList) {
			t1 = new Tree();
			t1.setId(String.valueOf(role.getUuid()));
			t1.setText(role.getName());
			if (roles.contains(role)) {
				t1.setChecked(true);
			}
			arrayList.add(t1);
		}
		return arrayList;
	}

	@Override
	public void updateEmpRoles(Long uuid, String checkedStr) {
		Emp emp = empDao.get(uuid);
		emp.setRoles(new ArrayList<Role>());
		String[] split = checkedStr.split(",");
		Role role = null;
		for (String s : split) {
			role = roleDao.get(Long.valueOf(s));
			emp.getRoles().add(role);
		}
	}
	
}
