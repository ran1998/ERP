package cn.itcast.erp.action;
import java.util.List;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IMenuBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Menu;

/**
 * 菜单Action 
 * @author Administrator
 *
 */
public class MenuAction extends BaseAction<Menu> {

	private IMenuBiz menuBiz;
	
	public void setMenuBiz(IMenuBiz menuBiz) {
		this.menuBiz = menuBiz;
		setBaseBiz(menuBiz);
	}
	
	/**
	 * 获取菜单数据
	 */
	public void getMenuTree() {
		Menu readMenusByEmpuuid = menuBiz.readMenusByEmpuuid(getLoginUser().getUuid());
		write(JSON.toJSONString(readMenusByEmpuuid));
	}
	
	/**
	 * 根据员工编号查菜单
	 */
	public void getMenusByEmpuuid() {
		Emp loginUser = this.getLoginUser();
		List<Menu> menusByEmpuuid = menuBiz.getMenusByEmpuuid(loginUser.getUuid());
		write(JSON.toJSONString(menusByEmpuuid));
	}
}
