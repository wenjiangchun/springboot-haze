package com.haze.demo.designpattern.template;

public class PostgreSQLCon extends AbstractConnectTemplate{
    @Override
    public void setDBDriver() {
        System.out.println("Setting PostgreSQL DB Driver...");
    }

    @Override
    public void setCredentials() {
        System.out.println("Setting Credentials for PostgreSQL DB...");
    }

    @Override
    public void setData() {
        System.out.println("Setting up data for PostgreSQL DB...");
    }
}
