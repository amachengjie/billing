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
        ,type: 'year'
    });

    $('#btnSearch').click(function () {
        var dateYear = $("#date1").val();
        getData(dateYear);
    });
    $(document).ready(function () {
        getData("");
    });

    function getData(param){
        $.ajax({
            url: Feng.ctxPath + "/statistics/getMasterMap",
            data: {
                dateStr:param
            },
            type: "Post",
            dataType: "json",
            success: function (data) {
                if (data.code == '0000') {
                    var loading = layer.msg('正在生成报表', {icon: 16, shade: 0.3, time:0});
                    var chartZhu = echarts.init(document.getElementById('masterMap'));
                    var option = {
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        legend: {
                            data: ['利润', '支出', '收入']
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis: [
                            {
                                type: 'value'
                            }
                        ],
                        yAxis: [
                            {
                                type: 'category',
                                axisTick: {
                                    show: false
                                },
                                data: data.data.dateList
                            }
                        ],
                        series: [
                            {
                                name: '利润',
                                type: 'bar',
                                label: {
                                    show: true,
                                    position: 'inside'
                                },
                                data: data.data.profitCollect
                            },
                            {
                                name: '收入',
                                type: 'bar',
                                stack: '总量',
                                label: {
                                    show: true
                                },
                                data: data.data.incomeCollect
                            },
                            {
                                name: '支出',
                                type: 'bar',
                                stack: '总量',
                                label: {
                                    show: true,
                                    position: 'left'
                                },
                                data: data.data.otherCollect
                            }
                        ]
                    };
                    chartZhu.setOption(option, true);
                    layer.close(loading);
                }
            },
            error: function (data) {
                layer.msg('错误', data.msg);
            }
        });

    }



    var  options = {
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
            orient: 'vertical',
            left: 10,
            data: ['直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎']
        },
        series: [
            {
                name: '访问来源',
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
                data: [
                    {value: 335, name: '直接访问'},
                    {value: 310, name: '邮件营销'},
                    {value: 234, name: '联盟广告'},
                    {value: 135, name: '视频广告'},
                    {value: 1548, name: '搜索引擎'}
                ]
            }
        ]
    };

});