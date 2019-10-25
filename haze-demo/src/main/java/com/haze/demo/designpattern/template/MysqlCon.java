package com.haze.demo.designpattern.template;

public class MysqlCon extends AbstractConnectTemplate{
    @Override
    public void setDBDriver() {
        System.out.println("Setting Mysql DB Driver...");
    }

    @Override
    public void setCredentials() {
        System.out.println("Setting Credentials for Mysql DB...");
    }

    @Override
    public void setData() {
        System.out.println("Setting up data for Mysql DB...");
    }

    @Override
    public boolean disableLogging() {
        return false;
    }
}
