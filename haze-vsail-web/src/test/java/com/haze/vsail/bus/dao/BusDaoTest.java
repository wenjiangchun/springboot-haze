package com.haze.vsail.bus.dao;

import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class BusDaoTest {

    @Test
    public void testCopy() throws Exception {
        try (InputStream in = new FileInputStream("/home/sofar/下载/12306/mytest.dbf");
             OutputStream out = new FileOutputStream("/home/sofar/mytest.dbf");
             ){
            Streams.copy(in, out, true);
            //Streams.copy()
        }

    }

}