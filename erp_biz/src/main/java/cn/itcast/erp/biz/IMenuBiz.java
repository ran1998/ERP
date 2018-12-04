package cn.itcast.erp.biz;
import java.util.List;

import cn.itcast.erp.entity.Menu;
/**
 * 菜单业务逻辑层接口
 * @author Administrator
 *
 */
public interface IMenuBiz extends IBaseBiz<Menu>{
	List<Menu> getMenusByEmpuuid(Long uuid);
	Menu readMenusByEmpuuid(Long uuid);
}

