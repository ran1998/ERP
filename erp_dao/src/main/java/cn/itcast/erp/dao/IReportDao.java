package cn.itcast.erp.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IReportDao {
	public List orderReport(Date startDate, Date endDate);
	
	public List<Map<String, Object>> getSumMoney(int year);
 }
