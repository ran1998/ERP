package cn.itcast.erp.biz.impl;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.itcast.erp.biz.ISupplierBiz;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Supplier;
/**
 * 供应商业务逻辑类
 * @author Administrator
 *
 */
public class SupplierBiz extends BaseBiz<Supplier> implements ISupplierBiz {

	private ISupplierDao supplierDao;
	
	public void setSupplierDao(ISupplierDao supplierDao) {
		this.supplierDao = supplierDao;
		setBaseDao(supplierDao);
	}

	/**
	 * 导出excel
	 */
	@Override
	public void export(OutputStream os, Supplier t1) {
		// 根据查询条件获取供应商列表
		List<Supplier> list = super.getList(t1, null, null);
		// 创建excel工作簿
		HSSFWorkbook wk = new HSSFWorkbook();
		// 创建工作表
		HSSFSheet sheet = null;
		if ("1".equals(t1.getType())) {
			sheet = wk.createSheet("供应商");
		}
		if ("2".equals(t1.getType())) {
			sheet = wk.createSheet("客户");
		}
		// 创建表头
		HSSFRow row = sheet.createRow(0);
		String[] headerName = {"名称","地址","联系人","电话","Email"};
		int[] columnWidth = {4000,8000,2000,3000,8000};
		HSSFCell cell = null;
		for (int i=0; i<headerName.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(headerName[i]);
			sheet.setColumnWidth(i, columnWidth[i]);
		}
		
		//写入内容
		int i = 1;
		for (Supplier supplier : list) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(supplier.getName());
			row.createCell(1).setCellValue(supplier.getAddress());
			row.createCell(2).setCellValue(supplier.getContact());
			row.createCell(3).setCellValue(supplier.getTele());
			row.createCell(4).setCellValue(supplier.getEmail());
			i++;
		}
		
		// 写入到输出流
		try {
			wk.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				wk.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
}
