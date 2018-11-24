package cn.itcast.erp.biz;
import cn.itcast.erp.entity.Emp;
/**
 * 员工业务逻辑层接口
 * @author Administrator
 *
 */
public interface IEmpBiz extends IBaseBiz<Emp>{
	
	public Emp findByUsernameAndPwd(String username, String pwd);

	public void updatePwd(Long uuid, String oldPwd, String newPwd);
	
	public void updatePwd_reset(Long uuid, String newPwd);
}

