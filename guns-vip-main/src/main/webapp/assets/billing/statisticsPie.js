layui.use(['layer', 'form', 'table', 'admin', 'ax', 'laydate', 'element', 'upload', 'tree', 'echarts'], function () {
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var laydate = layui.laydate;
    var upload = layui.upload;
    var tree = layui.tree;
    var echarts = layui.echarts;

    /**
     * 绑定日期
     */
    laydate.render({
        elem: '#date1'
        , type: 'year'
    });

    $('#btnSearch').click(function () {
        var dateYear = $("#date1").val();
        getData(dateYear);
    });

    $(document).ready(function () {
        getData("");
    });

    function getData(param) {
        var loading = layer.msg('正在生成报表', {icon: 16, shade: 0.3, time: 0});
        $.ajax({
            url: Feng.ctxPath + "/statistics/getPieMap",
            data: {
                dateStr: param
            },
            type: "Post",
            dataType: "json",
            success: function (data) {
                if (data.code == '0000') {
                    var chartZhu = echarts.init(document.getElementById('pieMap'));
                    var options = {
                        tooltip: {
                            trigger: 'item',
                            formatter: '{a} <br/>{b}: {c} ({d}%)'
                        },
                        legend: {
                            orient: 'vertical',
                            left: 10,
                            data: data.data.nameList
                        },
                        series: [
                            {
                                name: '年度消费',
                                type: 'pie',
                                radius: ['50%', '70%'],
                                avoidLabelOverlap: false,
                                label: {
                                    show: false,
                                    position: 'center'
                                },
                                emphasis: {
                                    label: {
                                        show: true,
                                        fontSize: '30',
                                        fontWeight: 'bold'
                                    }
                                },
                                labelLine: {
                                    show: false
                                },
                                data: data.data.dataList
                            }
                        ]
                    };
                    chartZhu.setOption(options, true);
                    if (data.data.nameList.length <= 0) {
                        layer.msg("未查询到数据！");
                    }
                    layer.close(loading);
                }
            },
            error: function (data) {
                layer.msg('错误', data.msg);
            }
        });

    }

});