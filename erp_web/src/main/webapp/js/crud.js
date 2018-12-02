
var method="";//保存提交的方法名称 
var listParam='';
var saveParam='';
$(function(){
	if (Request['type'] == 1) {
		listParam += '?t1.type=1';
		saveParam += '?t.type=1';
	}
	if (Request['type'] == 2) {
		listParam += '?t1.type=2';
		saveParam += '?t.type=2';
	}
	//表格数据初始化
	$('#grid').datagrid({
		url:name+'_listByPage.action'+listParam,
		columns:columns,
		singleSelect:true,
		pagination:true,
		toolbar: [{
			iconCls: 'icon-add',
			text:'增加',
			handler: function(){				
				$('#editWindow').window('open');
				$('#editForm').form('clear');
				method="add";
			}
		},'-',
		{
			text: '导出',
			iconCls: 'icon-excel',
			handler: function () {
				var formdata = $("#searchForm").serializeJSON();
				$.download(name+"_export"+listParam, formdata);
			}
		},'-',
		{
			text: '导入',
			iconCls: 'icon-excel',
			handler: function () {
				var importDlg = document.getElementById('importDlg');
				if (importDlg) {
					$(importDlg).dialog("open");
				}
			}
		}
		]

	});
	var importDlg = document.getElementById('importDlg');
	if (importDlg) {
		$("#importDlg").dialog({
			title: '导入',
			width: 330,
			height: 106,
			closed: true,
			buttons: [{
				text: '导入',
				handler:function () {
					$.ajax({
						type: 'post',
						url: name+'_doImport',
						data: new FormData($('#importForm')[0]),
						dataType: 'json',
						processData: false,
						contentType: false,
						success: function (res) {
							$.messager.alert('信息', res.message, 'info',function () {
								if (res.success) {
									$("#importDlg").dialog("close");
									$("#grid").datagrid("reload");
								}
							})
						}
					})
				}
			}]
		})
	}
	
	//条件查询
	$('#btnSearch').bind('click',function(){
		var formdata= $('#searchForm').serializeJSON();
		$('#grid').datagrid('load',formdata);		
	});
	
	//保存
	$('#btnSave').bind('click',function(){
		var formdata= $('#editForm').serializeJSON();	
		
		$.ajax({
			url:name+'_'+method+'.action'+saveParam,
			data:formdata,
			dataType:'json',
			type:'post',
			success:function(value){
				
				if(value.success){
					$('#editWindow').window('close');
					$('#grid').datagrid('reload');
				}
				$.messager.alert('提示',value.message);				
			}
			
		});
		
		
	});
	
	
});

/**
 * 删除 
 */
function dele(id){
	
	$.messager.confirm('提示','确定要删除此记录吗？',function(r){
		if(r)
		{
			$.ajax({
				url:name+'_delete.action?id='+id,
				dataType:'json',
				success:function(value){
					if(value.success){
						$('#grid').datagrid('reload');
					}
					$.messager.alert('提示',value.message);
				}
			});		
		}	
	});	
}

/**
 * 编辑
 */
function edit(id){
	
	$('#editWindow').window('open');
	$('#editForm').form('clear');
	$('#editForm').form('load',name+'_get.action?id='+id);	
	method="update";
}