layui.use(['layer', 'form', 'table', 'admin', 'ax', 'laydate', 'element', 'upload', 'tree'], function () {
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var laydate = layui.laydate;
    var upload = layui.upload;
    var couponId = Feng.getUrlParam("couponId");
    var view = Feng.getUrlParam("view");
    var tree = layui.tree;

    /**
     * 绑定日期
     */
    laydate.render({
        elem: '#date1'
        , type: 'datetime'
        , trigger: 'click'
    });

    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + '/billing/info/saveBilling', function (data) {
            if (data.code == '0000') {
                Feng.success("保存成功！");
                //传给上个页面，刷新table用
                var frameIndex = parent.layer.getFrameIndex(window.name);
                parent.layer.close(frameIndex);
            } else {
                Feng.error("保存失败！" + data.message);
                //关掉对话框
                admin.closeThisDialog();
            }
        }, function (data) {
            Feng.error("保存失败！" + data.message);
            //关掉对话框
            admin.closeThisDialog();
        });
        ajax.set(data.field);
        ajax.start();
    });





    form.verify({
        clength: function (value) {
            var i, sum;
            sum = 0;
            for (i = 0; i < value.length; i++) {
                if ((value.charCodeAt(i) >= 0) && (value.charCodeAt(i) <= 255))
                    sum = sum + 1;
                else
                    sum = sum + 2;
            }
            if (sum > 10) {
                return '输入长度请小于15位';
            }
            if (value < 0) {
                return '请输入正整数';
            }
        },
        nlength: function (value) {
            var i, sum;
            sum = 0;
            for (i = 0; i < value.length; i++) {
                if ((value.charCodeAt(i) >= 0) && (value.charCodeAt(i) <= 255))
                    sum = sum + 1;
                else
                    sum = sum + 2;
            }
            if (sum > 128) {
                return '输入长度请小于128位';
            }
        },
        verifyProduct: function (value) {
            if (value == '' || value == ',') {
                return '请选择产品品类';
            }
        },
        verifyArea: function (value) {
            if (value != '-1') {
                var data = $("#departmentid").val();
                if (data == '' || data == ',') {
                    return '请选择区域';
                }
            }
        },
        otherReq: function (value, item) {
            var limitAmount = $("#limitAmount").val();
            if (limitAmount == "" || limitAmount == 0) {
                var verifyName = $("input[name='isCirculationUse']:checked").val();
                if (verifyName == 'yes') {
                    return '无门槛优惠券禁止设置为循环满减优惠券';
                }
            }
        }
    });

});