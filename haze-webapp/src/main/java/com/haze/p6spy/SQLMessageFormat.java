package com.haze.p6spy;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLMessageFormat implements MessageFormattingStrategy {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        if (!sql.trim().equals("")) {
            return format.format(new Date()) + " | took " + elapsed + "ms | " + category + " | connection " + connectionId + "\n "
                    + sql + ";";
        }
        return "";
    }
}
