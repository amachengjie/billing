layui.use(['layer', 'form', 'table', 'ztree', 'laydate', 'admin', 'ax', 'excel', 'echarts'], function () {
    var layer = layui.layer;
    var form = layui.form;
    var table = layui.table;
    var $ZTree = layui.ztree;
    var $ax = layui.ax;
    var laydate = layui.laydate;
    var admin = layui.admin;
    var $ = layui.$;
    var echarts = layui.echarts;

    /**
     * 绑定日期
     */
    laydate.render({
        elem: '#date1'
    });
    laydate.render({
        elem: '#date2'
    });


    var MgrBillingInfo = {
        tableId: "billingInfoTable",    //表格id
        queryData: {},
        condition: {
            billingType: "",
            startTime: "",
            endTime: ""
        }
    };

    /**
     * 初始化表格的列
     */
    MgrBillingInfo.initColumn = function () {
        return [[
            {field: 'member_id', sort: true, title: '用户ID', hide: true},
            {field: 'billing_type', sort: true, title: '账单类型'},
            {field: 'billing_memo', sort: true, title: '账单描述', width: 300},
            {field: 'billing_amount', sort: true, title: '账单总金额',},
            {field: 'billing_time', sort: true, title: '账单时间',width: 300},
            {field: 'create_date', sort: true, title: '创建时间'},
            {field: 'member_name', sort: true, title: '用户名称'}
        ]];
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + MgrBillingInfo.tableId,
        url: Feng.ctxPath + '/billing/info/list',
        page: true,
        height: "full-160",
        limit: 300,
        limits: [100, 200, 300, 400, 500],
        cols: MgrBillingInfo.initColumn(),
        end:  report()
    });


    MgrBillingInfo.getQueryCondition = function () {
        MgrBillingInfo.queryData['billingType'] = $("#billingType").val();
        MgrBillingInfo.queryData['startTime'] = $("#date1").val();
        MgrBillingInfo.queryData['endTime'] = $("#date2").val();
    }

    $('#saveBillingPage').click(function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '新增账单',
            closeBtn: 0,
            maxmin: true,
            shadeClose: true,
            area: ['550px', '500px'],
            content: Feng.ctxPath + '/billing/info/saveBillingPage',
            end: function () {
                MgrBillingInfo.search();
            }
        });
    });

    // 搜索按钮点击事件
    MgrBillingInfo.search = function () {
        MgrBillingInfo.getQueryCondition();
        //查询数据
        table.reload(MgrBillingInfo.tableId, {
            where: MgrBillingInfo.queryData
        });
        //统计图
        report();
    };


    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        MgrBillingInfo.search();
    });

    function  report(){
        $.ajax({
            url: Feng.ctxPath + "/billing/info/getReportListBySix",
            data: {},
            type: "Post",
            dataType: "json",
            success: function (data) {
                if (data.code == '0000') {
                    var chartZhu = echarts.init(document.getElementById('EchartBilling'));
                    var optionchart = {
                        title: {
                            text: '近一个月金额'
                        },
                        tooltip: {},
                        legend: {
                            data: ['金额']
                        },
                        xAxis: {
                            data: data.data.dateList
                        },
                        yAxis: {
                            type: 'value',
                            axisLabel: {
                                formatter: '{value} ￥'
                            }
                        },
                        series: [{
                            name: ' 日期:金额',
                            type: 'bar', //柱状
                            data: data.data.billingAmountList,
                            itemStyle: {
                                normal: { //柱子颜色
                                    color: 'green'
                                }
                            },
                        }]
                    };

                   var  option = {
                       title: {
                           text: '近三十天开销'
                       },
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        legend: {
                            data: ['餐饮', '交通', '投资', '还款', '网上购物','线下购物']
                        },
                        grid: {
                            left: '5%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis: {
                            type: 'value'
                        },
                        yAxis: {
                            type: 'category',
                            data: data.data.dateList
                        },
                        series: [
                            {
                                name: '餐饮',
                                type: 'bar',
                                stack: '总量',
                                label: {
                                    show: false
                                },
                                data: data.data.repastCollect
                            },
                            {
                                name: '交通',
                                type: 'bar',
                                stack: '总量',
                                label: {
                                    show: false
                                },
                                data: data.data.trafficCollect
                            },
                            {
                                name: '投资',
                                type: 'bar',
                                stack: '总量',
                                label: {
                                    show: false
                                },
                                data: data.data.investCollect
                            },
                            {
                                name: '还款',
                                type: 'bar',
                                stack: '总量',
                                label: {
                                    show: false
                                },
                                data: data.data.repaymentCollect
                            },
                            {
                                name: '网上购物',
                                type: 'bar',
                                stack: '总量',
                                label: {
                                    show: false
                                },
                                data: data.data.online_shoppingCollect
                            },
                            {
                                name: '线下购物',
                                type: 'bar',
                                stack: '总量',
                                label: {
                                    show: false
                                },
                                data: data.data.shoppingCollect
                            }
                        ]
                    };
                    chartZhu.setOption(option, true);
                }
            },
            error: function (data) {
                layer.msg('错误', data.msg);
            }
        });
    }
});