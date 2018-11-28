$(function () {
	$("#grid").datagrid({
		url: 'storedetail_storealertList',
		columns: [[
			{field: 'uuid', title: '商品编号', width: 100},
			{field: 'name', title: '商品名称', width: 100},
			{field: 'storenum', title: '库存', width: 100},
			{field: 'outnum', title: '代发货', width: 100},
		]],
		singleSelect: true,
		toolbar: [
			{
				text: '发送预警邮件',
				iconCls: 'icon-add',
				handler: function () {
					$.ajax({
						type: 'post',
						url: 'storedetail_sendStorealertMail',
						dataType: 'json',
						success: function (res) {
							$.messager.alert("信息",res.message,"info");
						}
					})
				}
			}
		]
	})
})