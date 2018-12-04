package cn.itcast.erp.biz;
import java.util.List;

import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Tree;
/**
 * 员工业务逻辑层接口
 * @author Administrator
 *
 */
public interface IEmpBiz extends IBaseBiz<Emp>{
	
	public Emp findByUsernameAndPwd(String username, String pwd);

	public void updatePwd(Long uuid, String oldPwd, String newPwd);
	
	public void updatePwd_reset(Long uuid, String newPwd);
	
	public List<Tree> readEmpRoles(Long uuid);
	
	public void updateEmpRoles(Long uuid, String checkedStr);
}

