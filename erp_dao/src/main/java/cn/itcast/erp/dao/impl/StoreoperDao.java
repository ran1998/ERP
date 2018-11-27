package cn.itcast.erp.dao.impl;
import java.util.Calendar;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import cn.itcast.erp.dao.IStoreoperDao;
import cn.itcast.erp.entity.Storeoper;
/**
 * 仓库操作记录数据访问类
 * @author Administrator
 *
 */
public class StoreoperDao extends BaseDao<Storeoper> implements IStoreoperDao {

	
	/**
	 * 构建查询条件
	 * @param dep1
	 * @param dep2
	 * @param param
	 * @return
	 */
	public DetachedCriteria getDetachedCriteria(Storeoper storeoper1,Storeoper storeoper2,Object param){
		DetachedCriteria dc=DetachedCriteria.forClass(Storeoper.class);
		Calendar car = Calendar.getInstance();
		if(storeoper1!=null){
			if(storeoper1.getType()!=null &&  storeoper1.getType().trim().length()>0)
			{
				dc.add(Restrictions.eq("type", storeoper1.getType()));			
			}
			// 操作员查询
			if (storeoper1.getEmpuuid() != null) {
				dc.add(Restrictions.eq("empuuid", storeoper1.getEmpuuid()));
			}
			// 商品查询
			if (storeoper1.getGoodsuuid() != null) {
				dc.add(Restrictions.eq("goodsuuid", storeoper1.getGoodsuuid()));
			}
			// 仓库查询
			if (storeoper1.getStoreuuid() != null) {
				dc.add(Restrictions.eq("storeuuid", storeoper1.getStoreuuid()));
			}
			if (storeoper1.getOpertime() != null) {
				car.setTime(storeoper1.getOpertime());
				car.set(Calendar.HOUR, 0);
				car.set(Calendar.MINUTE, 0);
				car.set(Calendar.SECOND, 0);
				car.set(Calendar.MILLISECOND, 0);
				dc.add(Restrictions.ge("opertime", car.getTime()));
			}
		}	
		if (storeoper2!=null) {
			if (storeoper2.getOpertime() != null) {
				car.setTime(storeoper2.getOpertime());
				car.set(Calendar.HOUR, 59);
				car.set(Calendar.MINUTE, 59);
				car.set(Calendar.SECOND, 59);
				car.set(Calendar.MILLISECOND, 59);
				dc.add(Restrictions.le("opertime", car.getTime()));
			}
		}
		return dc;
	}
	
	
}

