package com.haze.demo.designpattern.template;

import java.util.Date;

public abstract class AbstractConnectTemplate {

    private boolean isLoggingEnable = true;

    public AbstractConnectTemplate() {
        this.isLoggingEnable = disableLogging();
    }

    public boolean disableLogging(){
        return true;
    }

    private void logging(String msg) {
        if (isLoggingEnable) {
            System.out.println("Logging..." + msg);
        }
    }


    public final void run() {
        setDBDriver();
        logging("Drivers set [" + new Date() + "]") ;
        setCredentials();
        logging("Credentials set [" + new Date() + "]") ;
        connect();
        logging("Connected") ;
        prepareStatement();
        setData();
        insert();
        close();
        destroy();
    }

    public abstract void setDBDriver();

    public abstract void setCredentials();

    public void connect() {
        System.out.println("DB connection...");
    }

    public void prepareStatement() {
        System.out.println("DB prepareStatement...");
    }

    public abstract void setData();

    public void insert() {
        System.out.println("DB insert...");
    }

    public void close() {
        System.out.println("DB close...");
    }

    public void destroy() {
        System.out.println("DB desctroy...");
    }
}
