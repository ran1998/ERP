package cn.itcast.erp.biz.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.itcast.erp.biz.IReportBiz;
import cn.itcast.erp.dao.IReportDao;

public class ReportBiz implements IReportBiz {

	private IReportDao reportDao;
	
	public void setReportDao(IReportDao reportDao) {
		this.reportDao = reportDao;
	}

	@Override
	public List orderReport(Date startDate, Date endDate) {
		return reportDao.orderReport(startDate, endDate);
	}

	@Override
	public List<Map<String, Object>> trendReport(int year) {
		// 保存每个月的销售额
		ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>(12);
		// 获取那一年的销售额
		List<Map<String, Object>> yearMoney = reportDao.getSumMoney(year);
		HashMap<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
		for (Map<String, Object> m : yearMoney) {
			map.put((String) m.get("month"), m);
		}
		Map<String, Object> data = null;
		for (int i=1; i<=12; i++) {
			data = map.get(i+"月");
			if (null == data) {
				data = new HashMap<String,Object>();
				data.put("month", i+"月");
				data.put("y", 0);
			}
			result.add(data);
		}
		return result;
	}

}
