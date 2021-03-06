package cn.itcast.erp.action;
import java.util.List;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Tree;
import cn.itcast.erp.exception.ERPException;

/**
 * 员工Action 
 * @author Administrator
 *
 */
public class EmpAction extends BaseAction<Emp> {

	private IEmpBiz empBiz;
	
	public void setEmpBiz(IEmpBiz empBiz) {
		this.empBiz = empBiz;
		setBaseBiz(empBiz);
	}
	public String newPwd;
	public String oldPwd;
	
	public String getNewPwd() {
		return newPwd;
	}


	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}


	public String getOldPwd() {
		return oldPwd;
	}


	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	/*
	 * 修改用户密码
	 */
	public void updatePwd() {
		Emp loginUser = getLoginUser();
		if (null == loginUser) {
			ajaxReturn(false, "亲，您还没有登录呢");
			return;
		}
		try {			
			empBiz.updatePwd(loginUser.getUuid(), oldPwd, newPwd);
			ajaxReturn(true, "修改密码成功");
		} catch (ERPException e) {
			e.printStackTrace();
			ajaxReturn(false, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "修改失败");
		}
	}
	
	/**
	 * 重置密码
	 */
	public void updatePwd_reset() {
		try {			
			this.empBiz.updatePwd_reset(getId(), newPwd);
			ajaxReturn(true, "重置密码成功");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "重置密码失败");
		}
	}
	
	private String checkedStr;
	
	public String getCheckedStr() {
		return checkedStr;
	}


	public void setCheckedStr(String checkedStr) {
		this.checkedStr = checkedStr;
	}


	/**
	 * 获取用户角色
	 */
	public void readEmpRoles() {
		List<Tree> readEmpRoles = empBiz.readEmpRoles(getId());
		write(JSON.toJSONString(readEmpRoles));
	}
	
	public void updateEmpRoles() {
		try {			
			empBiz.updateEmpRoles(getId(), checkedStr);
			ajaxReturn(true, "更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "更新失败");
		}
	}
}
