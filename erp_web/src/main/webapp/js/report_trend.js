$(function(){
    $('#grid').datagrid({
        queryParams:{},
        columns:[[
                  {field:'month',title:'月份',width:100},
                  {field:'y',title:'销售额',width:100}
              ]],
        singleSelect:true,
        onLoadSuccess: function (data) {
        	showChart(data.rows);
        }
    });

    $('#btnSearch').bind('click',function(){
        var submitData = $('#searchForm').serializeJSON();
        //提交查询条件，远程重新加载数据，
        $('#grid').datagrid({
            url:'report_trendReport',
            queryParams:submitData
        });
    });
});
/**
 * 画销售趋势图
 */
function showChart(){
    var monthData = new Array();
    for(var i = 1; i <=12; i++){
        monthData.push(i+"月");
    }
    $('#trendChart').highcharts({
        title: {
            text: $('#year').combobox('getValue') + '年销售趋势分析',
            x: -20 //center
        },
        subtitle: {
            text: 'Source: www.itcast.com',
            x: -20
        },
        xAxis: {
            categories: monthData
        },
        yAxis: {
            title: {
                text: '销售额（元）'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            valueSuffix: '元'
        },
        legend: {
            layout: 'vertical',
            align: 'center',
            verticalAlign: 'bottom',
            borderWidth: 0
        },
        series: [{name:'全部商品',data:$('#grid').datagrid('getRows')}]
    });
}
