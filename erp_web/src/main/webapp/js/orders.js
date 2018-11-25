function formatDate(dataVaule) {
	return new Date(dataVaule).Format('yyyy-MM-dd');
}

function getState(state) {
	switch (state * 1) {
		case 0:return '未审核';
		case 1:return '已审核';
		case 2:return '已确认';
		case 3:return '已入库';
		default: return '';
	}
}

$(function () {
	var url = 'orders_listByPage.action?t1.type=1'
	if (Request['oper'] == 'doCheck') {
		url += '&t1.state=0';
	}
	if (Request['oper'] == 'doStart') {
		url += '&t1.state=1';
	}
	if (Request['oper'] == 'doCheck') {
		$('#ordersDlg').dialog({
			toolbar: [{
				text: '审核',
				iconCls: 'icon-search',
				handler: doCheck
			}]
		})
	}
	if (Request['oper'] == 'doStart') {
		$('#ordersDlg').dialog({
			toolbar: [{
				text: '确认',
				iconCls: 'icon-search',
				handler: doStart
			}]
		})
	}
	// 加载表格
	$('#grid').datagrid({
		url: url,
		singleSelect:true,
		pagination:true,
		fitColumns:true,
		columns: [[
  		    {field:'uuid',title:'编号',width:100},
  		    {field:'createtime',title:'生成日期',width:100, formatter: formatDate},
  		    {field:'checktime',title:'审核日期',width:100, formatter: formatDate},
  		    {field:'starttime',title:'确认日期',width:100, formatter: formatDate},
  		    {field:'endtime',title:'入库或出库日期',width:100, formatter: formatDate},
  		    {field:'type',title:'1:采购 2:销售',width:100},
  		    {field:'creater',title:'下单员',width:100},
  		    {field:'checker',title:'审核员',width:100},
  		    {field:'starter',title:'采购员',width:100},
  		    {field:'ender',title:'库管员',width:100},
  		    {field:'supplieruuid',title:'供应商或客户',width:100},
  		    {field:'totalmoney',title:'合计金额',width:100},
  		    {field:'state',title:'采购: 0:未审核 1:已审核, 2:已确认, 3:已入库；销售：0:未出库 1:已出库',width:100},
  		    {field:'waybillsn',title:'运单号',width:100},

		    {field:'-',title:'操作',width:200,formatter:function(value,row,index)
		    	{
		    		return "<a href='#' onclick='edit("+row.uuid+")'>修改</a> <a href='#' onclick='dele("+row.uuid+")'>删除</a>";
		    	}}		    
		   ]],
		   onDblClickRow: function(rowIndex, rowData) {
			   $('#uuid').html(rowData.uuid);
			   $('#suppliername').html(rowData.supplierName);
			   $('#creater').html(rowData.createrName);
			   $('#checker').html(rowData.checkerName);
			   $('#starter').html(rowData.starterName);
			   $('#ender').html(rowData.enderName);
			   $('#createtime').html(new Date(rowData.createtime).Format('yyyy-MM-dd'));
			   $('#checktime').html(new Date(rowData.checktime).Format('yyyy-MM-dd'));
			   $('#starttime').html(new Date(rowData.starttime).Format('yyyy-MM-dd'));
			   $('#endtime').html(new Date(rowData.endtime).Format('yyyy-MM-dd'));
			   $('#state').html(getState(rowData.state));
			   $('#ordersDlg').dialog("open");
			   $('#itemgrid').datagrid('loadData', rowData.orderDetails);
		   }
	})
	
	// 加载明细表格
	$('#itemgrid').datagrid({
		columns: [[
			{field: 'uuid', title: '编号', width: 100},
			{field: 'goodsuuid', title: '商品编号', width: 100},
			{field: 'goodsname', title: '商品名称', width: 100},
			{field: 'price', title: '价格', width: 100},
			{field: 'num', title: '数量', width: 100},
			{field: 'money', title: '金额', width: 100},
			{field: 'state', title: '状态', width: 100, formatter: getDetailState},
		]]
	})
	/**
	 * 转化明细的状态
	 */
	function getDetailState(value) {
		switch (value * 1) {
			case 1: return "未入库";
			case 2: return "已入库";
			default: return "";
		}
	}
	/**
	 * 审核
	 */
	function doCheck() {
		$.messager.confirm('审核', '确定要审核吗', function (yes) {
			if (yes) {
				$.ajax({
					type: 'post',
					url: 'orders_doCheck.action?id='+$('#uuid').html(),
					dataType: 'json',
					success: function (res) {
						$.messager.alert('提示', res.message, 'info', function () {
							if (res.success) {
								$('#ordersDlg').dialog('close');
								$('#grid').datagrid('reload');
							}
						})
					}
				})
			}
		})
	}
	/**
	 * 确认
	 */
	function doStart() {
		$.messager.confirm('确认', '确定要确认订单吗', function (yes) {
			if (yes) {
				$.ajax({
					type: 'post',
					url: 'orders_doStart.action?id='+$('#uuid').html(),
					dataType: 'json',
					success: function (res) {
						$.messager.alert('提示', res.message, 'info', function () {
							if (res.success) {
								$('#ordersDlg').dialog('close');
								$('#grid').datagrid('reload');
							}
						})
					}
				})
			}
		})
	}

})