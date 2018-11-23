package cn.itcast.erp.dao.impl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import cn.itcast.erp.dao.IRole_menuDao;
import cn.itcast.erp.entity.Role_menu;
/**
 * 角色菜单数据访问类
 * @author Administrator
 *
 */
public class Role_menuDao extends BaseDao<Role_menu> implements IRole_menuDao {

	
	/**
	 * 构建查询条件
	 * @param dep1
	 * @param dep2
	 * @param param
	 * @return
	 */
	public DetachedCriteria getDetachedCriteria(Role_menu role_menu1,Role_menu role_menu2,Object param){
		DetachedCriteria dc=DetachedCriteria.forClass(Role_menu.class);
		if(role_menu1!=null){
			if(role_menu1.getMenuuuid()!=null &&  role_menu1.getMenuuuid().trim().length()>0)
			{
				dc.add(Restrictions.like("menuuuid", role_menu1.getMenuuuid(), MatchMode.ANYWHERE));			
			}
		
		}		
		return dc;
	}
	
	
}

