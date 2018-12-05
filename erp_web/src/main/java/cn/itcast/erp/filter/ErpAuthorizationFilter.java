package cn.itcast.erp.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

public class ErpAuthorizationFilter extends AuthorizationFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		// 获取主题
		Subject subject = getSubject(request, response);
		// 得到配置文件的权限列表
		String[] perms = (String[]) mappedValue;
		// 如果为空，则放行
		boolean isPermitted = true;
        if(null == perms || perms.length == 0){
        	return isPermitted;
        }
		// 权限检查
		for (String p : perms) {
			if (subject.isPermitted(p)) {
				return true;
			}
		}
		return false;
	}

}
