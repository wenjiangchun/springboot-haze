package com.haze.demo.jdk8.base64;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Demo {
    public static void test() {
        String url = "subjects?mathematics";
        String encodeStr = Base64.getUrlEncoder().encodeToString(url.getBytes(StandardCharsets.UTF_8));
        System.out.println(encodeStr);
        System.out.println(new String(Base64.getUrlDecoder().decode(encodeStr)));
    }

}
