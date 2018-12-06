package cn.itcast.erp.biz.impl;
import java.util.ArrayList;
import cn.itcast.erp.entity.Menu;
import redis.clients.jedis.Jedis;

import java.util.List;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IMenuBiz;
import cn.itcast.erp.dao.IMenuDao;
import cn.itcast.erp.entity.Menu;
/**
 * 菜单业务逻辑类
 * @author Administrator
 *
 */
public class MenuBiz extends BaseBiz<Menu> implements IMenuBiz {

	private IMenuDao menuDao;
	private Jedis jedis;
	
	
	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
	}

	public void setMenuDao(IMenuDao menuDao) {
		this.menuDao = menuDao;
		setBaseDao(menuDao);
	}

	@Override
	public List<Menu> getMenusByEmpuuid(Long uuid) {
		String menuListJson = jedis.get("menuList_"+uuid);
		List<Menu> menuList = null;
		if (menuListJson == null) {
			menuList = menuDao.getMenusByEmpuuid(uuid);
			jedis.set("menuList_"+uuid, JSON.toJSONString(menuList));
			System.out.println("database");
		} else {
			menuList = JSON.parseArray(menuListJson, Menu.class);
			System.out.println("redis");
		}
		return menuList;
	}
	
	/**
	 * 克隆menu
	 * @param src
	 * @return
	 */
	private Menu cloneMenu(Menu src) {
		Menu _new = new Menu();
		_new.setIcon(src.getIcon());
		_new.setUrl(src.getUrl());
		_new.setMenuid(src.getMenuid());
		_new.setMenuname(src.getMenuname());
		_new.setMenus(new ArrayList<Menu>());
		return _new;
	}
	
	@Override
	public Menu readMenusByEmpuuid(Long uuid) {
		Menu root = menuDao.get("0");
		Menu cloneMenu = this.cloneMenu(root);
		List<Menu> menusByEmpuuid = menuDao.getMenusByEmpuuid(uuid);
		Menu _m1 = null;
		Menu _m2 = null;
		for (Menu m1 : root.getMenus()) {
			_m1 = cloneMenu(m1);
			for (Menu m2 : m1.getMenus()) {
				if (menusByEmpuuid.contains(m2)) {
					_m2 = cloneMenu(m2);
					_m1.getMenus().add(_m2);
				}
			}
			if (_m1.getMenus().size()>0) {
				// 如果一级菜单下有二级菜单就添加进去
				cloneMenu.getMenus().add(_m1);
			}
		}
		return cloneMenu;
	}
	
}
