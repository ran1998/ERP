package cn.itcast.erp.biz;
import cn.itcast.erp.entity.Orders;
/**
 * 订单业务逻辑层接口
 * @author Administrator
 *
 */
public interface IOrdersBiz extends IBaseBiz<Orders>{
	
	void doCheck(Long uuid, Long empUuid);
	
	void doStart(Long uuid, Long empUuid);
}

