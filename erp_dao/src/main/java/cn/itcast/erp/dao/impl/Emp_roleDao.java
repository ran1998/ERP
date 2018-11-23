package cn.itcast.erp.dao.impl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import cn.itcast.erp.dao.IEmp_roleDao;
import cn.itcast.erp.entity.Emp_role;
/**
 * 员工角色数据访问类
 * @author Administrator
 *
 */
public class Emp_roleDao extends BaseDao<Emp_role> implements IEmp_roleDao {

	
	/**
	 * 构建查询条件
	 * @param dep1
	 * @param dep2
	 * @param param
	 * @return
	 */
	public DetachedCriteria getDetachedCriteria(Emp_role emp_role1,Emp_role emp_role2,Object param){
		DetachedCriteria dc=DetachedCriteria.forClass(Emp_role.class);
		if(emp_role1!=null){
		
		}		
		return dc;
	}
	
	
}

