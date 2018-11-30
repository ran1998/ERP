package cn.itcast.erp.action;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import cn.itcast.erp.biz.ISupplierBiz;
import cn.itcast.erp.entity.Supplier;
import cn.itcast.erp.exception.ERPException;

/**
 * 供应商Action 
 * @author Administrator
 *
 */
public class SupplierAction extends BaseAction<Supplier> {

	private ISupplierBiz supplierBiz;
	
	public void setSupplierBiz(ISupplierBiz supplierBiz) {
		this.supplierBiz = supplierBiz;
		setBaseBiz(supplierBiz);
	}
	/**romote传过来的参数*/
	private String q;

	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	
	@Override
	public void list() {
		if (null == getT1()) {
			this.setT1(new Supplier());
		}
		this.getT1().setName(q);
		super.list();
	}
	
	/**
	 * 导出excel文件
	 */
	public void export() {
		String filename = "";
		if ("1".equals(getT1().getType())) {
			filename = "供应商.xls";
		}
		if ("2".equals(getT1().getType())) {
			filename = "客户.xls";
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			
			response.setHeader("Content-Disposition", "attachment;filename="+new String(filename.getBytes(), "ISO-8859-1"));
			supplierBiz.export(response.getOutputStream(), getT1());
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public File file;
	public String fileFileName;
	public String fileContentType;
	
	
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getFileFileName() {
		return fileFileName;
	}
	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}
	public String getFileContentType() {
		return fileContentType;
	}
	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}
	
	public void doImport() {
		// 文件类型判断
		if (!"application/vnd.ms-excel".equals(fileContentType)) {
			ajaxReturn(false, "上传文件必须为excel文件");
			return;
		}
		try {
			supplierBiz.doImport(new FileInputStream(file));
			ajaxReturn(true, "上传文件成功");
		} catch (IOException e) {
			ajaxReturn(false, "上传文件失败");
			e.printStackTrace();
		} catch (ERPException e) {
			ajaxReturn(false, e.getMessage());
			e.printStackTrace();
		}
		
	}
	
}
