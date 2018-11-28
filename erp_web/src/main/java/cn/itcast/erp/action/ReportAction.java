package cn.itcast.erp.action;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionContext;

import cn.itcast.erp.biz.IReportBiz;

public class ReportAction {
	private IReportBiz reportBiz;

	public void setReportBiz(IReportBiz reportBiz) {
		this.reportBiz = reportBiz;
	}
	
	private Date startDate;
	private Date endDate;

	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	private int year;
	
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	/**
	 * 销售统计报表
	 */
	public void orderReport() {
		List orderReport = reportBiz.orderReport(startDate, endDate);
		write(JSON.toJSONString(orderReport));
	}
	
	public void trendReport() {
		List<Map<String, Object>> trendReport = reportBiz.trendReport(year);
		write(JSON.toJSONString(trendReport));
	}
	public void write(String jsonString) {
		HttpServletResponse res = ServletActionContext.getResponse();
		res.setContentType("text/html;charset=utf-8");
		res.setCharacterEncoding("utf-8");
		try {
			res.getWriter().println(jsonString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
