package cn.itcast.erp.biz.impl;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.itcast.erp.biz.ISupplierBiz;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Supplier;
import cn.itcast.erp.exception.ERPException;
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

	@Override
	public void doImport(InputStream is) throws IOException {
		HSSFWorkbook wk = null;
		try {
			wk = new HSSFWorkbook(is);
			HSSFSheet sheetAt = wk.getSheetAt(0);
			String type = "";
			if ("供应商".equals(sheetAt.getSheetName())) {
				type = Supplier.TYPE_SUPPLIER;
			} else if ("客户".equals(sheetAt.getSheetName())) {
				type = Supplier.TYPE_CUSTOMER;
			} else {
				throw new ERPException("工作名称不正确");
			}
			// 最后一行行号
			int lastRowNum = sheetAt.getLastRowNum();
			for (int i=1; i<=lastRowNum; i++) {
				Supplier supplier2 = new Supplier();
				supplier2.setName(sheetAt.getRow(i).getCell(0).getStringCellValue());
				// 判断名称是否存在
				List<Supplier> list = supplierDao.getList(null, supplier2, null);
				if (list.size() > 0) {
					supplier2 = list.get(0);
				}
				System.out.println(sheetAt.getRow(i).getCell(3).getStringCellValue());
				supplier2.setAddress(sheetAt.getRow(i).getCell(1).getStringCellValue());//地址
				supplier2.setContact(sheetAt.getRow(i).getCell(2).getStringCellValue());
				supplier2.setTele(sheetAt.getRow(i).getCell(3).getStringCellValue());
				supplier2.setEmail(sheetAt.getRow(i).getCell(4).getStringCellValue());
				
				
				if (list.size() == 0) {
					supplier2.setType(type);
					supplierDao.add(supplier2);
				}
				
			}
		} finally {
			if (null != wk) {
				wk.close();
			}
		}
	}

	
}
