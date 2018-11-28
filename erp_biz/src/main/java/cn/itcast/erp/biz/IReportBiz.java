package cn.itcast.erp.biz;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IReportBiz {
	public List orderReport(Date startDate, Date endDate);
	
	public List<Map<String, Object>> trendReport(int year);
}
