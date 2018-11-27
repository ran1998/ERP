var existEditIndex = -1;
$(function () {
	// 初始化供应商列表
	$('#supplier').combogrid({
		url: 'supplier_list.action?type=1',
		panelWidth: 750,
		idField: 'name',
		textField: 'name',
		columns: [[
			{field: 'uuid', title: '编号', width: 60},
			{field: 'name', title: '宽度', width: 100},
			{field: 'address', title: '地址', width: 100},
			{field: 'contact', title: '联系人', width: 100},
			{field: 'tele', title: '联系地址', width: 100},
			{field: 'email', title: '邮箱地址', width: 100},
		]],
		mode: 'romote'
	})
	
	$("#ordersgrid").datagrid({
		singSelect: true,
		showFooter: true,
		columns: [[
			{field: 'goodsuuid', title: '商品编号', width: 100, editor: {type: 'numberbox', options: {disabled: true}}},
			{field: 'goodsname', title: '商品名称', width: 100, editor: {
				type: 'combobox',
				options: {
					url: 'goods_list.action',
					textField: 'name',
					valueField: 'name',
					onSelect: function (goods) {
						// 获取商品编号的编辑器
						var goodsUuidEditor = getEditor('goodsuuid');
						$(goodsUuidEditor.target).val(goods.uuid);
						// 获取商品价格的编辑器
						var priceEditor = getEditor('price');
						if (Request['type'] * 1 == 1) {							
							// 设置为进货价格
							$(priceEditor.target).val(goods.inprice);
						}
						if (Request['type'] * 1 == 2) {							
							// 设置为销售价格
							$(priceEditor.target).val(goods.outprice);
						}
						
						// 自动选中数量输入框
						var numEditor = getEditor('num');
						$(numEditor.target).select();
						//绑定事件
						bindGridEvent();
						// 计算金额
						cal();
						// 计算总金额
						sum();
					}
				},
			}},
			{field: 'price', title: '价格', width: 100, editor: 'numberbox', options: {precision: 2}},
			{field: 'num', title: '数量', width: 100, editor: 'numberbox'},
			{field: 'money', title: '金额', width: 100, editor: 'numberbox', options: {pricision: 2, disabled: true}},
			{field: '-', title: '操作', width: 100, formatter: function (value, row, rowIndex) {
				if (row.num != '合计') {
					return '<a href="javascript:void(0)" onClick="deleteRow('+ rowIndex +')">删除</a>';
				}
			}}
			
		]],
		onClickRow: function (rowIndex, rowData) {
			// 关闭当前行的编辑
			$("#ordersgrid").datagrid("endEdit", existEditIndex);
			existEditIndex = rowIndex;
			// 开启点击时的行编辑
			$("#ordersgrid").datagrid("beginEdit", existEditIndex);
			//绑定事件
			bindGridEvent();
			// 计算金额
			cal();
			// 计算总金额
			sum();
		},
		toolbar: [
			{
				iconCls: 'icon-add',
				text: '增加',
				handler: function () {
					// 如果存在编辑行就关闭掉
					if (existEditIndex > -1) {
						$("#ordersgrid").datagrid("endEdit", existEditIndex);
					}
					// 追加一行
					$("#ordersgrid").datagrid('appendRow', {num:0,money:0});
					// 获取最后一行的索引
					existEditIndex = $("#ordersgrid").datagrid("getRows").length - 1;
					// 最后一行开启编辑
					$("#ordersgrid").datagrid('beginEdit', existEditIndex);
				}
			},
			{
				iconCls: 'icon-save',
				text: '提交',
				handler: function () {
					if (existEditIndex > -1) {
						console.log(1);
						// 关闭编辑行
						$('#ordersgrid').datagrid('endEdit', existEditIndex);
						var formdata = $('#orderForm').serializeJSON();
						
						if(formdata['t.supplieruuid'] == '') {
							$.messager.alert('提示', '供应商不能为空', 'info');
							return;
						}
						
						var rows = $('#ordersgrid').datagrid('getRows');
						formdata.json = JSON.stringify(rows);
						
						// 提交到后台
						$.ajax({
							type: 'post',
							url: 'orders_add.action?t.type='+Request['type'],
							dataType: 'json',
							data: formdata,
							success: function (res) {
								if (res.success) {
									// 清空供应商输入框
									$('#supplier').combogrid('clear');
									// 重新加载数据
									$('#ordersgrid').datagrid('loadData',{total:0,rows:[],footer:[{num: '合计', money: 0}]});
									// 关闭订单窗口
									$('#addOrdersDlg').dialog('close');
									// 刷新表格
									$('#grid').datagrid('reload');
								}
							}
						})
					}
				}
			}
		]
	})
	// 加载页脚
	$('#ordersgrid').datagrid('reloadFooter', [{num: '合计', money: 0}]);
})
	/**
	 * 获取编辑器
	 */
	function getEditor(field) {
		return $("#ordersgrid").datagrid("getEditor", {index: existEditIndex, field: field});
	}
	/**
	 * 计算金额
	 */
	function cal() {
		var priceEditor = getEditor('price');
		var price = $(priceEditor.target).val();
		
		var numEditor = getEditor('num');
		var num = $(numEditor.target).val();
		// 计算金额保留两位小数
		var $money = (price * num).toFixed(2);
		// 显示金额
		var moneyEditor = getEditor('money');
		$(moneyEditor.target).val($money);
		
		// 更新编辑行表格数据，防止计算合计时无法计算编辑行的金额
		$('#ordersgrid').datagrid('getRows')[existEditIndex]['money'] = $money;
	}
	/**
	 * 自动计算金额绑定事件
	 */
	function bindGridEvent() {
		// 价格输入框绑定事件自动计算金额
		var priceEditor = getEditor('price');
		$(priceEditor.target).bind('keyup', function () {
			cal();
			sum();
		})
		// 数量输入框绑定事件自动计算金额
		var numEditor = getEditor('num');
		$(numEditor.target).bind('keyup', function () {
			cal();
			sum();
		})
	}
	/**
	 * 删除行
	 */
	function deleteRow(rowIndex) {
		// 关闭编辑行
		$('#ordersgrid').datagrid('endEdit', rowIndex);
		// 删除行
		$('#ordersgrid').datagrid('deleteRow', rowIndex);
		// 重新加载数据
		var data = $('#ordersgrid').datagrid('getData');
		$('#ordersgrid').datagrid('loadData', data);
		sum();
	}
	/**
	 * 计算总金额
	 * @returns
	 */
	function sum() {
		var totalPrice = 0;
		var rows = $('#ordersgrid').datagrid('getRows');
		// 循环累加
		$.each(rows, function (i, r) {
			totalPrice += parseFloat(r.money);
		})
		// 加载页脚
		$('#ordersgrid').datagrid('reloadFooter', [{num: '合计', money: totalPrice.toFixed(2)}]);
	}