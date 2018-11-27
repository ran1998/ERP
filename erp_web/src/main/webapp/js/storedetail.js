$(function () {
	$("#grid").datagrid({
		url: "storedetail_listByPage",
		columns: [[
  		    {field:'uuid',title:'编号',width:100},
  		    {field:'storeName',title:'仓库',width:100},
  		    {field:'goodsName',title:'商品',width:100},
  		    {field:'num',title:'数量',width:100},

		    {field:'-',title:'操作',width:200,formatter:function(value,row,index)
		    	{
		    		return "<a href='#' onclick='edit("+row.uuid+")'>修改</a> <a href='#' onclick='dele("+row.uuid+")'>删除</a>";
		    	}}		    
		 ]]
	})
})