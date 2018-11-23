package cn.itcast.erp.action;
import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IMenuBiz;
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
	
	public void getMenuTree() {
		Menu menu = menuBiz.get("0");
		String json = JSON.toJSONString(menu);
		write(json.toString());
	}
}
