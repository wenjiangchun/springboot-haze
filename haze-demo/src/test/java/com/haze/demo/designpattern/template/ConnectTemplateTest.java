package com.haze.demo.designpattern.template;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConnectTemplateTest {

    @Test
    public void run() {
        System.out.println("-------------for Mysql-------------");
        AbstractConnectTemplate template = new MysqlCon();
        template.run();
        System.out.println("------------for PostgreSQL-------------");
        AbstractConnectTemplate postgreSQLCon = new PostgreSQLCon();
        postgreSQLCon.run();
    }
}