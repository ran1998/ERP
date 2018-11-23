package cn.itcast.erp.action;
import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.entity.Emp;
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
			write(ajaxReturn(true, "修改密码成功"));
		} catch (ERPException e) {
			e.printStackTrace();
			write(ajaxReturn(false, e.getMessage()));
		} catch (Exception e) {
			e.printStackTrace();
			write(ajaxReturn(false, "修改失败"));
		}
	}
}