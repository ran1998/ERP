$(function () {
	$('#tree').tree({
		url: 'role_readRoleMenus?id='+2,
		animate: true,
		checkbox: true
	})
	$('#grid').datagrid({
		url: 'role_list',
		columns: [[
			{field: 'uuid', title: '编号', width: 100},
			{field: 'name', title: '名称', width: 100},
		]],
		singleSelect: true,
		onClickRow: function (rowIndex, rowData) {
			$('#tree').tree({
				url: 'role_readRoleMenus?id='+rowData.uuid,
				animate: true,
				checkbox: true
			})
		}
	})
	$('#btnSave').bind('click', function () {
		var nodes = $('#tree').tree('getChecked');
		var checkedStr = new Array();
		$.each(nodes, function (i, node) {
			checkedStr.push(node.id);
		})
		checkedStr = checkedStr.join(",");
		var formdata = {};
		formdata.id = $('#grid').datagrid('getSelected').uuid;
		formdata.checkedStr = checkedStr;
		
		$.ajax({
			type: 'post',
			url: 'role_updateRoleMenus',
			data: formdata,
			dataType: 'json',
			success: function (res) {
				$.messager.alert('提示', res.message, 'info');
			}
		})
		
	})
})