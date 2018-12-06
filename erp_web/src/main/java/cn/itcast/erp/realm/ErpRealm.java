package cn.itcast.erp.realm;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.biz.IMenuBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Menu;

public class ErpRealm extends AuthorizingRealm {

	private IEmpBiz empBiz;

	public void setEmpBiz(IEmpBiz empBiz) {
		this.empBiz = empBiz;
	}
	private IMenuBiz menuBiz;

	public void setMenuBiz(IMenuBiz menuBiz) {
		this.menuBiz = menuBiz;
	}

	@Override 
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("授权");
		// 得到当前登陆用户
		Emp emp = (Emp) principals.getPrimaryPrincipal();
		// 获取用户对应的权限
		List<Menu> menus = menuBiz.getMenusByEmpuuid(emp.getUuid());
		SimpleAuthorizationInfo sai = new SimpleAuthorizationInfo();
		for (Menu menu : menus) {
			sai.addStringPermission(menu.getMenuname());
		}
		return sai;
	}

	/**
	 * 认证方法
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("认证方法");
		// 转成实现类
		UsernamePasswordToken upt = (UsernamePasswordToken) token;
		String pwd = new String(upt.getPassword());
		Emp emp = empBiz.findByUsernameAndPwd(upt.getUsername(), pwd);
		if (null != emp) {
			// 1,主角 登录的用户
			// 2.证书凭证，这里我们用的密码
			// 3当前realm的名称
			return new SimpleAuthenticationInfo(emp, pwd, getName());
		}
		return null;
	}

}
