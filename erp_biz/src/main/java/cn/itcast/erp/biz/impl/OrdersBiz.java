package cn.itcast.erp.biz.impl;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;

import cn.itcast.erp.biz.IOrdersBiz;
import cn.itcast.erp.dao.IOrdersDao;
import cn.itcast.erp.dao.impl.EmpDao;
import cn.itcast.erp.dao.impl.SupplierDao;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;
import cn.itcast.erp.exception.ERPException;
/**
 * 订单业务逻辑类
 * @author Administrator
 *
 */
public class OrdersBiz extends BaseBiz<Orders> implements IOrdersBiz {

	private IOrdersDao ordersDao;
	
	public void setOrdersDao(IOrdersDao ordersDao) {
		this.ordersDao = ordersDao;
		setBaseDao(ordersDao);
	}
	
	private EmpDao empDao;
	private SupplierDao supplierDao;
	
	public void setEmpDao(EmpDao empDao) {
		this.empDao = empDao;
	}

	public void setSupplierDao(SupplierDao supplierDao) {
		this.supplierDao = supplierDao;
	}

	public void add(Orders orders) {
		// 设置添加时状态为未审核
		orders.setState(Orders.STATE_CREATE);
		// 设置类型为采购
		// orders.setType(Orders.TYPE_IN);
		// 设置创建时间
		orders.setCreatetime(new Date());
		
		double totalMoney = 0;
		for (Orderdetail orderDetail : orders.getOrderDetails()) {
			totalMoney += orderDetail.getMoney();
			// 设置商品明细为未入库
			orderDetail.setState(orderDetail.STATE_NO_IN);
			// 设置明细对应的订单
			orderDetail.setOrders(orders);
		}
		orders.setTotalmoney(totalMoney);
		ordersDao.add(orders);
	}
	@Override
	public List<Orders> getListByPage(Orders t1,Orders t2,Object param,int firstResult,int maxResults) {
		// 获取分页数据
		List<Orders> listByPage = super.getListByPage(t1, t2, param, firstResult, maxResults);
		// 缓存员工的数据，key为员工编号,value为员工名
		Map<Long, String> empNameMap = new HashMap<Long, String>();
		// 缓存供应商的数据, key为供应商编号,value为供应商名
		Map<Long, String> supplierNameMap = new HashMap<Long, String>();
		for (Orders o : listByPage) {
			// 设置下单员名称
			o.setCreaterName(getEmpName(o.getCreater(), empNameMap));
			// 设置审核员名称
			o.setCheckerName(getEmpName(o.getCreater(), empNameMap));
			// 设置采购员名称
			o.setStarterName(getEmpName(o.getStarter(), empNameMap));
			// 设置库管员名称
			o.setEnderName(getEmpName(o.getEnder(), empNameMap));
			// 设置供应商名称
			o.setSupplierName(getSupplierName(o.getSupplieruuid(), supplierNameMap));
		}
		return super.getListByPage(t1, t2, param, firstResult, maxResults);
		
	}
	
	/**
	 * 通过id获取员工名
	 * @param uuid
	 * @param empNameMap
	 * @return
	 */
	private String getEmpName(Long uuid, Map<Long, String> empNameMap) {
		if (null == uuid) {
			return "";
		}
		String empName = empNameMap.get(uuid);
		if (null == empName) {
			// 从map缓存获取,没有则设置
			empName = empDao.get(uuid).getName();
			empNameMap.put(uuid, empName);
		}
		return empName;
	}
	
	/**
	 * 获取供应商名
	 * @param uuid
	 * @param supplierNameMap
	 * @return
	 */
	private String getSupplierName(Long uuid, Map<Long, String> supplierNameMap) {
		if (null == uuid) {
			return "";
		}
		String supplierName = supplierNameMap.get(uuid);
		if (null == supplierName) {
			supplierName = supplierDao.get(uuid).getName();
			supplierNameMap.put(uuid, supplierName);
		}
		return supplierName;
	}

	@Override
	public void doCheck(Long uuid, Long empUuid) {
		Orders order = ordersDao.get(uuid);
		if (!Orders.STATE_CREATE.equals(order.getState())) {
			throw new ERPException("该订单已经审核过了呢");
		}
		// 设置审核员
		order.setChecker(empUuid);
		// 设置审核时间
		order.setChecktime(new Date());
		// 更新审核状态
		order.setState(Orders.STATE_CHECK);
	}

	@Override
	public void doStart(Long uuid, Long empUuid) {
		Orders order = ordersDao.get(uuid);
		if (!Orders.STATE_CHECK.equals(order.getState())) {
			throw new ERPException("该订单已确认过了呢");
		}
		// 设置确定人
		order.setStarter(empUuid);
		// 设置确定时间
		order.setStarttime(new Date());
		// 更新确定状态
		order.setState(Orders.STATE_START);
	}

	/**
	 * 根据id生产excel
	 */
	@Override
	public void exportById(OutputStream os, Long uuid) throws Exception {
		
		Orders orders = ordersDao.get(uuid);
		List<Orderdetail> orderDetails = orders.getOrderDetails();
		
		// 工作簿
		HSSFWorkbook book = new HSSFWorkbook();
		// 工作表
		HSSFSheet sheet = book.createSheet();
		// 内容样式
		HSSFCellStyle style_content = book.createCellStyle();
		style_content.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框
		style_content.setBorderTop(HSSFCellStyle.BORDER_THIN);//下边框
		style_content.setBorderLeft(HSSFCellStyle.BORDER_THIN);//下边框
		style_content.setBorderRight(HSSFCellStyle.BORDER_THIN);//下边框
		// 根据导出的订单样本创建10行4列
		int rowCount = orderDetails.size() + 9;
		for (int i=2; i<=rowCount; i++) {
			HSSFRow row = sheet.createRow(i);
			for (int j=0; j<4; j++) {
				HSSFCell cell = row.createCell(j);
				cell.setCellStyle(style_content);
			}
		}
		
		// 合并单元格
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));// 标题
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 3));//供应商
		sheet.addMergedRegion(new CellRangeAddress(7, 7, 0, 3));// 订单明细
		
		// 设置固定文本内容
		//****** 设置固定文本内容 ******//
		//设置标题内容, 注意：单元格必须创建后才能设置。
		sheet.createRow(0).createCell(0).setCellValue("采购单");//设置标题内容
		sheet.getRow(2).getCell(0).setCellValue("供应商");//设置供应商文本
		//已经创建过的row/cell，则通过sheet.getRow.getCell方式获取
		sheet.getRow(3).getCell(0).setCellValue("下单日期");
		sheet.getRow(3).getCell(2).setCellValue("经办人");
		sheet.getRow(4).getCell(0).setCellValue("审核日期");
		sheet.getRow(4).getCell(2).setCellValue("经办人");
		sheet.getRow(5).getCell(0).setCellValue("采购日期");
		sheet.getRow(5).getCell(2).setCellValue("经办人");
		sheet.getRow(6).getCell(0).setCellValue("入库日期");
		sheet.getRow(6).getCell(2).setCellValue("经办人");
		sheet.getRow(7).getCell(0).setCellValue("订单明细");
		sheet.getRow(8).getCell(0).setCellValue("商品名称");
		sheet.getRow(8).getCell(1).setCellValue("数量");
		sheet.getRow(8).getCell(2).setCellValue("价格");
		sheet.getRow(8).getCell(3).setCellValue("金额");

		//****** 设置行高和列宽 ******//
		sheet.getRow(0).setHeight((short)1000);//设置标题行高
		// 设置内容部分的行高
		for(int i = 2; i <= rowCount; i++){
		    sheet.getRow(i).setHeight((short)500);
		}
		//设置列宽
		for(int i = 0; i < 4; i++){
		    sheet.setColumnWidth(i, 5000);
		}

		//****** 设置对齐方式和字体 ******//
		// 内容部分的对齐设置
		style_content.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
		style_content.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
		// 设置内容部分的字体
		HSSFFont font_content = book.createFont();//创建字体
		font_content.setFontName("宋体");//设置字体名称
		font_content.setFontHeightInPoints((short)11);//设置字体大小
		style_content.setFont(font_content);//设置样式的字体

		// 标题样式
		HSSFCellStyle style_title = book.createCellStyle();
		style_title.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
		style_title.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
		HSSFFont font_title = book.createFont();//创建字体
		font_title.setFontName("黑体");//设置字体名称
		font_title.setBold(true);//加粗
		font_title.setFontHeightInPoints((short)18);//设置字体大小
		style_title.setFont(font_title);//设置样式的字体
		sheet.getRow(0).getCell(0).setCellStyle(style_title);//设置标题样式

		//****** 设置日期格式 ******//
		// 日期格式
		HSSFCellStyle style_date = book.createCellStyle();
		style_date.cloneStyleFrom(style_content);//日期格式基本上跟内容的格式一样，可以clone过来
		HSSFDataFormat dataFormat = book.createDataFormat();
		style_date.setDataFormat(dataFormat.getFormat("yyyy-MM-dd hh:mm"));
		// 设置日期 的日期格式
		for(int i = 3; i < 7; i++){
		    sheet.getRow(i).getCell(1).setCellStyle(style_date);
		}
		
		// 缓存供应商名称
		HashMap<Long, String> supplierNameMap = new HashMap<Long, String>();
		//设置供应商的值
		sheet.getRow(2).getCell(1).setCellValue(this.getSupplierName(orders.getSupplieruuid(), supplierNameMap));
		if (null != orders.getCreatetime()) {
			sheet.getRow(3).getCell(1).setCellValue(orders.getCreatetime());
		}
		if (null != orders.getChecktime()) {
			sheet.getRow(4).getCell(1).setCellValue(orders.getChecktime());
		}
		if (null != orders.getStarttime()) {
			sheet.getRow(5).getCell(1).setCellValue(orders.getStarttime());
		}
		if (null != orders.getEndtime()) {
			sheet.getRow(6).getCell(1).setCellValue(orders.getEndtime());
		}
		
		// 设置经办人的值
		if(null != orders.getCreater()){//下单员
			sheet.getRow(3).getCell(3).setCellValue(empDao.get(orders.getCreater()).getName());
		}
		if(null != orders.getChecker()){//审核员
			sheet.getRow(4).getCell(3).setCellValue(empDao.get(orders.getChecker()).getName());
		}
		if(null != orders.getStarter()){//采购员
			sheet.getRow(5).getCell(3).setCellValue(empDao.get(orders.getStarter()).getName());
		}
		if(null != orders.getEnder()){  //库管员
			sheet.getRow(6).getCell(3).setCellValue(empDao.get(orders.getEnder()).getName());
		}
		
		// 订单明细
		int rowIndex = 9;
		HSSFRow row = null;
		for (Orderdetail od : orderDetails) {
			row = sheet.getRow(rowIndex);
			row.getCell(0).setCellValue(od.getGoodsname());
			row.getCell(1).setCellValue(od.getNum());
			row.getCell(2).setCellValue(od.getPrice());
			row.getCell(3).setCellValue(od.getMoney());
			rowIndex++;
		}
		sheet.getRow(rowIndex).getCell(0).setCellValue("合计");
		sheet.getRow(rowIndex).getCell(3).setCellValue(orders.getTotalmoney());

		//保存工作簿到本地目录
		book.write(os);
		book.close();

		
	}
}
