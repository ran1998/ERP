$(function () {
	var url = 'orders_listByPage.action';
	// 我的订单
	if (Request['oper'] == 'myorders') {
		url = 'orders_myListByPage.action?t1.type='+Request['type'];
	}
	// 采购订单查询
	if (Request['oper'] == 'orders' && Request['type']*1 == 1) {
		url += '?t1.type=1';
		document.title = "采购订单";
	}
	// 销售订单查询
	if (Request['oper'] == 'orders' && Request['type']*1 == 2) {
		url += '?t1.type=2';
		document.title = "销售订单";
	}
	// 审核业务,查询state=0的未审核订单
	if (Request['oper'] == 'doCheck') {
		url += '?t1.type=1&t1.state=0';
	}
	// 确认业务,查询state=1的已审核的订单
	if (Request['oper'] == 'doStart') {
		url += '?t1.type=1&t1.state=1';
	}
	// 入库业务,查询state=2的已确认的订单
	if (Request['oper'] == 'doInStore') {
		url += '?t1.type=1&t1.state=2';
	}
	// 出入库标题
	var inoutTitle = "";
	// 出入库按钮
	var inoutBtn = "";
	if (Request['oper'] == 'doInStore') {
		url += '?t1.type=1&t1.state=2';
		inoutTitle = "采购入库";
		inoutBtn = "入库";
	}
	if (Request['oper'] == 'doOutStore') {
		url += '?t1.type=2&t1.state=0';
		inoutTitle = '销售出库';
		inoutBtn = '出库';
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
	//添加双击事件
	if(Request['oper'] == 'doInStore' || Request['oper'] == 'doOutStore'){
		$('#itemgrid').datagrid({
			onDblClickRow:function(rowIndex, rowData){
				//显示数据
				$('#itemuuid').val(rowData.uuid);
				$('#goodsuuid').html(rowData.goodsuuid);
				$('#goodsname').html(rowData.goodsname);
				$('#goodsnum').html(rowData.num);
				//打开入库窗口
				$('#itemDlg').dialog('open');
			}
		});
	}
	// 添加采购申请
	if (Request['oper'] == 'myorders') {
		var btnText = "";
		if (Request['type'] == 1) {
			$("#ordersupplier").html("供应商");
			btnText = "采购申请";
		}
		if (Request['type'] == 2) {
			$('#ordersupplier').html("客户");
			btnText = "销售订单录入";
		}
		$('#grid').datagrid({
			toolbar:[{
				text: btnText,
				iconCls: 'icon-add',
				handler: function () {
					$("#addOrdersDlg").dialog("open");
				}
			}]
		})
	}
	// 加载表格
	$('#grid').datagrid({
		url: url,
		singleSelect:true,
		pagination:true,
		fitColumns:true,
		columns: getColumns,
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
	//入库窗口
	$('#itemDlg').dialog({
		width:300,
		height:200,
		title:inoutTitle,
		modal:true,
		closed:true,
		buttons:[
		   {
			   text:inoutBtn,
			   iconCls:'icon-save',
			   handler:doInOutStore
		   }
		]
	});
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
	// 增加订单窗口
	$("#addOrdersDlg").dialog({
		title: '增加订单',
		width: 700,
		height: 400,
		modal: true,
		closed: true
	})
	/**
	 * 格式化日期
	 */
	function formatDate(dataVaule) {
		return new Date(dataVaule).Format('yyyy-MM-dd');
	}
	/**
	 * 格式化订单状态
	 */
	function getState(state) {
		if (Request['type'] * 1 == 1) {			
			switch (state * 1) {
				case 0:return '未审核';
				case 1:return '已审核';
				case 2:return '已确认';
				case 3:return '已入库';
				default: return '';
			}
		}
		if (Request['type'] * 1 == 2) {			
			switch (state * 1) {
				case 0:return '未出库';
				case 1:return '已出库';
				default: return '';
			}
		}
	}
	/**
	 * 转化明细的状态
	 */
	function getDetailState(value) {
		if (Request['type'] * 1 == 1) {			
			switch (value * 1) {
				case 0: return "未入库";
				case 1: return "已入库";
				default: return "";
			}
		}
		if (Request['type'] * 1 == 2) {			
			switch (value * 1) {
				case 0: return "未入库";
				case 1: return "已入库";
				default: return "";
			}
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
	/**
	 * 入库
	 */
	function doInOutStore(){
		var formdata = $('#itemForm').serializeJSON();
		if(formdata.storeuuid == ''){
			$.messager.alert('提示','请选择仓库!','info');
			return;
		}
		var message = ""; var url = "";
		if (Request['type'] * 1 == 1) {
			message = '确认要入库吗？';
			url = 'orderdetail_doInStore';
		}
		if (Request['type'] * 1 == 2) {
			message = '确认要出库吗？';
			url = 'orderdetail_doOutStore';
		}
		$.messager.confirm("确认",message,function(yes){
			if(yes){
				$.ajax({
					url: url,
					data: formdata,
					dataType: 'json',
					type: 'post',
					success:function(rtn){
						$.messager.alert('提示',rtn.message,'info',function(){
							if(rtn.success){
								//关闭入库窗口
								$('#itemDlg').dialog('close');
								//设置明细的状态
								$('#itemgrid').datagrid('getSelected').state = "1";
								//刷新明细列
								var data = $('#itemgrid').datagrid('getData');
								$('#itemgrid').datagrid('loadData',data);
								//如果所有明细都 入库了，应该关闭订单详情，并且刷新订单列表
								var allIn = true;
								$.each(data.rows,function(i,row){
									if(row.state * 1 == 0){
										allIn = false;
										//跳出循环
										return false;
									}
								});
								if(allIn == true){
									//关闭详情窗口
									$('#ordersDlg').dialog('close');
									//刷新订单列表
									$('#grid').datagrid('reload');
								}
							}
						});
					}
				});
			}
		});
	}
	
	/**
	 * 根据类型获取列
	 */
	function getColumns() {
		if (Request['type']*1 == 1) {
			return [[
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
			   ]];
		}
		if (Request['type']*1 == 2) {
			return [[
	            {field:'uuid',title:'编号',width:100},
	  		    {field:'createtime',title:'生成日期',width:100,formatter:formatDate},
	  		    {field:'endtime',title:'出库日期',width:100,formatter:formatDate},
	  		    {field:'createrName',title:'下单员',width:100},
	  		    {field:'enderName',title:'库管员',width:100},
	  		    {field:'supplierName',title:'客户',width:100},
	  		    {field:'totalmoney',title:'合计金额',width:100},
	  		    {field:'state',title:'状态',width:100,formatter:getState},
	  		    {field:'waybillsn',title:'运单号',width:100}
			]];
		}
	}

})