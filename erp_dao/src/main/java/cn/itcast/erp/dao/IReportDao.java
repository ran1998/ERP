package cn.itcast.erp.dao;

import java.util.Date;
import java.util.List;

public interface IReportDao {
	public List orderReport(Date startDate, Date endDate);
}
