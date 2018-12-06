package cn.itcast.erp.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionContext;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.entity.Emp;
import freemarker.template.utility.SecurityUtilities;

public class LoginAction{
	public IEmpBiz empBiz;
	
	public void setEmpBiz(IEmpBiz empBiz) {
		this.empBiz = empBiz;
	}


	public String username;
	public String pwd;
	

	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPwd() {
		return pwd;
	}


	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	/*
	 * 登录验证
	 */
	public void checkUser() {
		try {			
//			Emp user = empBiz.findByUsernameAndPwd(username, pwd);
//			if (null == user) {
//				ajaxReturn(false, "账号或密码错误");
//				return;
//			}
//			ServletActionContext.getRequest().getSession().setAttribute("loginUser", user);
			// 1.创建令牌
			UsernamePasswordToken upt = new  UsernamePasswordToken(username, pwd);
			// 2.获得主题subject
			Subject subject = SecurityUtils.getSubject();
			// 3.执行login方法
			subject.login(upt);
			ajaxReturn(true, "");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "登录失败");
		}
	}
	/**
	 * 显示登录名
	 */
	public void showName() {
		// Emp loginUser = (Emp) ActionContext.getContext().getSession().get("loginUser");
		Emp loginUser = (Emp) SecurityUtils.getSubject().getPrincipal();
		if (null != loginUser) {
			ajaxReturn(true, loginUser.getName());
		} else {
			ajaxReturn(false, "");
		}
	}
	
	/**
	 * 登出
	 */
	public void logOut() {
		// ActionContext.getContext().getSession().remove("loginUser");
		SecurityUtils.getSubject().logout();
	}
	

	public void ajaxReturn(boolean success,String message){
		
		Map map=new HashMap();
		map.put("success", success);
		map.put("message", message);
		write(JSON.toJSONString(map));		
	}
	
	public void write(String jsonString){

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");		
		try {
			response.getWriter().print(jsonString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
