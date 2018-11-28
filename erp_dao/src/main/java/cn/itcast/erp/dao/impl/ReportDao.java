package cn.itcast.erp.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import cn.itcast.erp.dao.IReportDao;

public class ReportDao extends HibernateDaoSupport implements IReportDao {

	@Override
	public List orderReport(Date startDate, Date endDate) {
		String hql = "select new Map(gt.name as name, sum(od.money) as y) " +
				"from Goodstype gt,Goods g,Orders o,Orderdetail od "+
				"where g.goodstypeuuid=gt and od.orders=o and od.goodsuuid=g.uuid "+
				"and o.type='2' ";				
		List<Date> queryParams = new ArrayList<Date>();
		if (null != startDate) {
			hql += "and o.createtime>=? ";
			queryParams.add(startDate);
		}
		if (null != endDate) {
			hql += "and o.createtime<=? ";
			queryParams.add(endDate);
		}
		hql += "group by gt.name";
		if (queryParams.size() > 0) {
			return this.getHibernateTemplate().find(hql, queryParams.toArray(new Object[] {}));
		}
		return this.getHibernateTemplate().find(hql);
	}

	@Override
	public List<Map<String, Object>> getSumMoney(int year) {
		String hql = "select new Map(month(o.createtime) || '月' as month,sum(od.money) as y) "+
				"from Orders o,Orderdetail od "+
				"where od.orders=o and o.type='2' and year(o.createtime)=? "+
				"group by month(o.createtime)";
		return (List<Map<String, Object>>) this.getHibernateTemplate().find(hql, year);
	}
	
}
