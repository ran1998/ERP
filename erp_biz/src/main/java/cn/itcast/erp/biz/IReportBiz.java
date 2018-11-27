package cn.itcast.erp.biz;

import java.util.Date;
import java.util.List;

public interface IReportBiz {
	public List orderReport(Date startDate, Date endDate);
}
