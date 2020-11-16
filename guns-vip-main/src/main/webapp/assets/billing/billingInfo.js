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
            {field: 'billing_memo', sort: true, title: '账单描述', width: 500},
            {field: 'billing_amount', sort: true, title: '账单总金额'},
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
                            name: '金额',
                            type: 'bar', //柱状
                            data: data.data.billingAmountList,
                            itemStyle: {
                                normal: { //柱子颜色
                                    color: 'green'
                                }
                            },
                        }]
                    };


                    var option = {
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                type: 'cross',
                                crossStyle: {
                                    color: '#999'
                                }
                            }
                        },
                        toolbox: {
                            feature: {
                                dataView: {show: true, readOnly: false},
                                magicType: {show: true, type: ['line', 'bar']},
                                restore: {show: true},
                                saveAsImage: {show: true}
                            }
                        },
                        legend: {
                            data: ['蒸发量', '降水量', '平均温度']
                        },
                        xAxis: [
                            {
                                type: 'category',
                                data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
                                axisPointer: {
                                    type: 'shadow'
                                }
                            }
                        ],
                        yAxis: [
                            {
                                type: 'value',
                                name: '水量',
                                min: 0,
                                max: 250,
                                interval: 50,
                                axisLabel: {
                                    formatter: '{value} ml'
                                }
                            },
                            {
                                type: 'value',
                                name: '温度',
                                min: 0,
                                max: 25,
                                interval: 5,
                                axisLabel: {
                                    formatter: '{value} °C'
                                }
                            }
                        ],
                        series: [
                            {
                                name: '蒸发量',
                                type: 'bar',
                                data: [2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3]
                            },
                            {
                                name: '降水量',
                                type: 'bar',
                                data: [2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3]
                            },
                            {
                                name: '平均温度',
                                type: 'line',
                                yAxisIndex: 1,
                                data: [2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2]
                            }
                        ]
                    };


                    chartZhu.setOption(optionchart, true);
                }
            },
            error: function (data) {
                layer.msg('错误', data.msg);
            }
        });
    }
});