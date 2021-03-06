package cn.itcast.erp.biz.impl;
import java.util.ArrayList;
import java.util.List;

import cn.itcast.erp.biz.IRoleBiz;
import cn.itcast.erp.dao.IRoleDao;
import cn.itcast.erp.dao.impl.MenuDao;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Menu;
import cn.itcast.erp.entity.Role;
import cn.itcast.erp.entity.Tree;
import redis.clients.jedis.Jedis;
/**
 * 角色业务逻辑类
 * @author Administrator
 *
 */
public class RoleBiz extends BaseBiz<Role> implements IRoleBiz {

	private IRoleDao roleDao;
	private Jedis jedis;
	
	
	
	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
	}
	public void setRoleDao(IRoleDao roleDao) {
		this.roleDao = roleDao;
		setBaseDao(roleDao);
	}
	private MenuDao menuDao;
	
	public MenuDao getMenuDao() {
		return menuDao;
	}
	public void setMenuDao(MenuDao menuDao) {
		this.menuDao = menuDao;
	}


	@Override
	public List<Tree> readRoleMenus(Long uuid) {
		List<Tree> treeList = new ArrayList<Tree>();
		// 获取根节点
		Menu root = menuDao.get("0");
		//获取角色信息
		Role role = roleDao.get(uuid);
		//获取角色菜单
		List<Menu> roleMenus = role.getMenus();
		Tree t1 = null;
		Tree t2 = null;
		for (Menu menu1 : root.getMenus()) {
			t1 = new Tree();
			t1.setId(menu1.getMenuid());
			t1.setText(menu1.getMenuname());
			for (Menu menu2 : menu1.getMenus()) {
				t2 = new Tree();
				t2.setId(menu2.getMenuid());
				t2.setText(menu2.getMenuname());
				if (roleMenus.contains(menu2)) {
					t2.setChecked(true);
				}
				t1.getChildren().add(t2);
			}
			treeList.add(t1);
		}
		return treeList;
	}
	/**
	 * 修改权限
	 */
	@Override
	public void updateRoleMenus(Long uuid, String checkedStr) {
		Role role = roleDao.get(uuid);
		// 清空用户下所有权限
		role.setMenus(new ArrayList<Menu>());
		// 获取选中menuid的数组
		String[] split = checkedStr.split(",");
		Menu menu = null;
		for (String m : split) {
			menu = menuDao.get(m);
			role.getMenus().add(menu);
		}
		// 菜单清缓存
		try {			
			for (Emp emp : role.getEmpList()) {
				jedis.del("menuList_"+emp.getUuid());
				System.out.println("clear redis");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
