$(function () {
	$("#grid").datagrid({
		url: 'storeoper_listByPage',
		columns: [[
  		    {field:'uuid',title:'编号',width:100},
  		    {field:'empName',title:'员工',width:100},
  		    {field:'opertime',title:'操作日期',width:100, formatter: function (value) {
  		    	return new Date(value).Format('yyyy-MM-dd');
  		    }},
  		    {field:'storeName',title:'仓库',width:100},
  		    {field:'goodsName',title:'商品',width:100},
  		    {field:'num',title:'数量',width:100},
  		    {field:'type',title:'出入库',width:100, formatter: function (value) {
  		    	switch (value*1) {
  		    		case 1: return "入库";
  		    		case 2: return "出库";
  		    		default: return "";
				}
  		    }},
		]],
		pagination: true,
		singleSelect: true,
//		onLoadSuccess:function (data) {
//			$.each(data.rows, function (i, o) {
//				DisplayName.showName('emp_get?id='+o.empuuid, "empuuid"+i, row.empuuid, "t.name");
//				DisplayName.showName('store_get?id='+o.storeuuid, "storeuuid"+i, row.storeuuid, "t.name");
//				DisplayName.showName('goods_get?id='+o.goodsuuid, "goodsuuid"+i, row.goodsuuid, "t.name");
//			})
//		}

	})
	$("#btnSearch").bind('click', function () {
		var submitData = $("#searchForm").serializeJSON();
		$("#grid").datagrid("load", submitData);
	})
})