layui.use(['layer', 'form', 'table', 'ztree', 'laydate', 'admin', 'ax', 'excel'], function () {
    var layer = layui.layer;
    var form = layui.form;
    var table = layui.table;
    var $ZTree = layui.ztree;
    var $ax = layui.ax;
    var laydate = layui.laydate;
    var admin = layui.admin;
    var $ = layui.$;

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
        condition: {}
    };

    /**
     * 初始化表格的列
     */
    MgrBillingInfo.initColumn = function () {
        return [[
            {field: 'member_id', sort: true, title: '用户ID', hide: true},
            {field: 'billing_type', sort: true, title: '账单状态'},
            {field: 'billing_memo', sort: true, title: '账单描述'},
            {field: 'billing_amount', sort: true, title: '账单总金额'},
            {field: 'create_date', sort: true, title: '创建时间'}
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
        cols: MgrBillingInfo.initColumn()
    });

});