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
})