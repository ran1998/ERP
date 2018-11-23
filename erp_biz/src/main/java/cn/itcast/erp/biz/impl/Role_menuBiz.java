package cn.itcast.erp.biz.impl;
import cn.itcast.erp.biz.IRole_menuBiz;
import cn.itcast.erp.dao.IRole_menuDao;
import cn.itcast.erp.entity.Role_menu;
/**
 * 角色菜单业务逻辑类
 * @author Administrator
 *
 */
public class Role_menuBiz extends BaseBiz<Role_menu> implements IRole_menuBiz {

	private IRole_menuDao role_menuDao;
	
	public void setRole_menuDao(IRole_menuDao role_menuDao) {
		this.role_menuDao = role_menuDao;
		setBaseDao(role_menuDao);
	}

	
}
