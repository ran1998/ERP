package cn.itcast.erp.action;
import cn.itcast.erp.biz.IRole_menuBiz;
import cn.itcast.erp.entity.Role_menu;

/**
 * 角色菜单Action 
 * @author Administrator
 *
 */
public class Role_menuAction extends BaseAction<Role_menu> {

	private IRole_menuBiz role_menuBiz;
	
	public void setRole_menuBiz(IRole_menuBiz role_menuBiz) {
		this.role_menuBiz = role_menuBiz;
		setBaseBiz(role_menuBiz);
	}
	
	
}
