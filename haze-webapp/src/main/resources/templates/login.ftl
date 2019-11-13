<#assign ctx=ctx.contextPath/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <link rel="shortcut icon" href="favicon.ico">
    <link href="${ctx}/resources/vsail/css/bootstrap.min.css?v=3.3.5" rel="stylesheet">
    <link href="${ctx}/resources/vsail/css/font-awesome.min.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx}/resources/vsail/css/animate.min.css" rel="stylesheet">
    <link href="${ctx}/resources/vsail/css/style.min.css?v=4.0.0" rel="stylesheet">
    <link rel="stylesheet" href="${ctx}/resources/vsail/css/login.css" />
    <title>登录页</title>
    <meta charset="UTF-8">
    <script type="text/javascript">
        <!-- 解决登录页面嵌套问题 -->
        if (window !== top){
            top.location.href = location.href;
        }
    </script>
</head>

<body>
<div class="dl">
    <div class="dly" id="dly">
        <div class="dly-title">
            <img src="${ctx}/resources/vsail/img/logo.png">智能车联网大数据管理平台
        </div>
        <div class="dly-m">
            <div class="dly-dlk">
                <h5>用户登录</h5>
                <form action="${ctx}/login" method="post" onsubmit="return validator()">
                    <input id="username" type="text" class="itxt yhm" name="username" tabindex="1" autocomplete="off" placeholder="请输入账号" />
                    <input type="password" id="password" name="password" class="itxt itxt-error mm" tabindex="2" autocomplete="off" placeholder="请输入密码" />
                    <div class="clear"></div>
                    <span style="color: red;font-weight: bold;font-size: 14px" data-bind="text: errorMessage"></span>
                    <button type="submit" class="dlan">登录</button>
                </form>
            </div>
        </div>
    </div>
    <div class="footer">
        <!--<span>Copyright © 2010 北京xxxxxx股份有限公司 京ICP备 10030151 号 </span>-->
    </div>
</div>
</body>
<script src="${ctx}/resources/vsail/js/jquery.min.js?v=2.1.4"></script>
<script src="${ctx}/resources/vsail/js/bootstrap.min.js?v=3.3.5"></script>
<script src="${ctx}/resources/knockout/knockout-3.5.0.js"></script>
<script>
    let errorViewModel;
    $(function() {
        errorViewModel = {
            errorMessage: ko.observable('${error!}')
        };
        ko.applyBindings(errorViewModel);
        $(window).resize(function() {
            var $content = $('.dl');
            $content.height($(this).height());
        }).resize();
    });
    function validator() {
        var username = $("#username").val();
        var password = $("#password").val();
        if (username === '' || password === '') {
            errorViewModel.errorMessage("用户名或密码不能为空!");
            return false;
        }
        return true;
    }
</script>

</html>