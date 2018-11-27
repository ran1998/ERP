package cn.itcast.erp.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import cn.itcast.erp.dao.IReportDao;

public class ReportDao extends HibernateDaoSupport implements IReportDao {

	@Override
	public List orderReport(Date startDate, Date endDate) {
		String hql = "select new Map(gt.name as name, sum(od.money) as y) " +
				"from Goodstype gt,Goods g,Orders o,Orderdetail od "+
				"where g.goodstypeuuid=gt and od.orders=o and od.goodsuuid=g.uuid "+
				"and o.type='1' "+
				"group by gt.name";
				
		return this.getHibernateTemplate().find(hql);
	}
	
}
