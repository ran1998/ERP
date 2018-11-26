package cn.itcast.erp.biz;
import cn.itcast.erp.entity.Orderdetail;
/**
 * 订单明细业务逻辑层接口
 * @author Administrator
 *
 */
public interface IOrderdetailBiz extends IBaseBiz<Orderdetail>{
	
	public void doInStore(Long uuid, Long empUuid, Long storeUuid);
	public void doOutStore(Long empuuid, Long uuid, Long storeuuid);
}

