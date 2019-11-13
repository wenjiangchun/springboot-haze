<nav class="navbar-default navbar-static-side" role="navigation">
    <div class="nav-close"><i class="fa fa-times-circle"></i>
    </div>
    <div class="sidebar-collapse">
        <ul class="nav" id="side-menu">
            <li class="nav-logo">
                <a href="javascript:void(0)"><img src="${ctx}/res/vsail/img/logo.png"/></a>
            </li>
            <li>
                <a href="#">
                    <i><img src="${ctx}/res/vsail/img/nav-icon1.png"/></i>
                    <span class="nav-label">车辆监控</span>
                    <span class="fa arrow"></span>
                </a>
                <ul class="nav nav-second-level">
                    <li>
                        <a class="J_menuItem" href="#" url="${ctx}/v/map"<#--data-bind="click: function(data, event) {activeMenu('${ctx}/v/map', data, event) }"--> data-index="0">消防安全监控</a>
                    </li>
                </ul>
            </li>
            <li>
                <a href="#">
                    <i><img src="${ctx}/res/vsail/img/nav-icon2.png"></i>
                    <span class="nav-label">信息查询</span>
                    <span class="fa arrow"></span>
                </a>
                <ul class="nav nav-second-level">
                    <li>
                        <a class="J_menuItem" href="javascript:void(0)" url="${ctx}/v/stat/onoff">车辆上线/下线记录</a>
                    </li>
                    <#--<li>
                        <a class="J_menuItem" href="javascript:void(0)" url="${ctx}/v/stat/fire">火警记录</a>
                    </li>
                    <li>
                        <a class="J_menuItem" href="javascript:void(0)" url="${ctx}/v/stat/breakdown">故障记录</a>
                    </li>-->
                </ul>
            </li>
            <li>
                <a href="#">
                    <i><img src="${ctx}/res/vsail/img/nav-icon4.png"></i>
                    <span class="nav-label">统计分析</span>
                    <span class="fa arrow"></span>
                </a>
                <ul class="nav nav-second-level">
                    <li>
                        <a class="J_menuItem" href="javascript:void(0)">统计分析</a>
                    </li>
                </ul>
            </li>
            <li>
                <a href="#">
                    <i class="fa fa-desktop"></i>
                    <span class="nav-label">信息管理</span>
                    <span class="fa arrow"></span>
                </a>
                <ul class="nav nav-second-level">
                    <#--<li><a class="J_menuItem" href="javascript:void(0)" url="${ctx}/v/bus/view" id="sys_user_menu"><i
                                    class="fa fa-wrench"></i>产品管理</a>
                    </li>
                    <li><a class="J_menuItem" href="javascript:void(0)" url="${ctx}/v/model/view" id="sys_group_menu"><i
                                    class="fa fa-users"></i>车辆配置管理</a>
                    </li>-->
                    <#--<li><a class="J_menuItem" href="javascript:void(0)" url="${ctx}/v/engine/view" id="sys_role_menu"><i class="fa fa-gears"></i>主机厂管理</a>
                    </li>
                    <li><a class="J_menuItem" href="javascript:void(0)" url="${ctx}/v/product/view" id="sys_res_menu"><i class="fa fa-database"></i>供应商管理</a>
                    </li>-->
                </ul>
            </li>
            <li>
                <a href="#">
                    <i class="fa fa-desktop"></i>
                    <span class="nav-label">系统管理</span>
                    <span class="fa arrow"></span>
                </a>
                <ul class="nav nav-second-level">
                    <li><a class="J_menuItem" href="javascript:void(0)" url="${ctx}/system/user/view" id="sys_user_menu"><i
                                    class="fa fa-user"></i>用户管理</a>
                    </li>
                    <li><a class="J_menuItem" href="javascript:void(0)" url="${ctx}/system/group/view" id="sys_group_menu"><i
                                    class="fa fa-users"></i>机构管理</a>
                    </li>
                    <li><a class="J_menuItem" href="javascript:void(0)" url="${ctx}/system/role/view" id="sys_role_menu"><i class="fa fa-gears"></i>角色管理</a>
                    </li>
                    <li><a class="J_menuItem" href="javascript:void(0)" url="${ctx}/system/resource/view" id="sys_res_menu"><i class="fa fa-database"></i>资源管理</a>
                    <li><a class="J_menuItem" href="javascript:void(0)" url="${ctx}/system/dict/view" id="sys_dict_menu"><i class="fa fa-book"></i>字典管理</a>
                    <li><a class="J_menuItem" href="javascript:void(0)" url="${ctx}/system/config/view" id="sys_config_menu"><i class="fa fa-wrench"></i>配置管理</a>
                    </li>
                </ul>
            </li>
            <#--<li>
                <a href="#">
                    <i class="fa fa-eye"></i>
                    <span class="nav-label">系统监控</span>
                    <span class="fa arrow"></span>
                </a>
                <ul class="nav nav-second-level">
                    <li><a class="J_menuItem" href="javascript:void(0)" url="${ctx}/shiro/online/view" id="monitor_online_menu"><i class="fa fa-user"></i>在线用户</a>
                    </li>
                    <li><a class="J_menuItem" href="javascript:void(0)" url="${ctx}/monitor/jvm/view" id="monitor_jvm_menu"><i class="fa fa-coffee"></i>JVM信息</a>
                    </li>
                    <li><a class="J_menuItem" href="javascript:void(0)" url="${ctx}/m/redis/view" id="monitor_redis_menu"><i class="fa fa-cube"></i>redis信息</a>
                    </li>
                </ul>
            </li>-->
        </ul>
    </div>
</nav>