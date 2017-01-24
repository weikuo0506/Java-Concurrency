package com.walker.concurrency.inpractice.chapter2;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by walker on 2016/11/15.
 */
public class FactorizerDemo {
    public static void main(String[] args) throws IOException {
        StatelessFactorizer factorizer = new StatelessFactorizer();
        ServletRequest request = new MockHttpServletRequest();
        request.setAttribute("key","24");
        HttpServletResponse response = new MockHttpServletResponse();
        try {
            factorizer.service(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(response.toString());
    }
}
