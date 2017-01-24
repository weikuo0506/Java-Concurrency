package com.walker.concurrency.inpractice.chapter2;

import javax.servlet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by walker on 2016/11/15.
 */
public class StatelessFactorizer implements Servlet {
    public void init(ServletConfig config) throws ServletException {

    }

    public ServletConfig getServletConfig() {
        return null;
    }

    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        String key = req.getAttribute("key").toString();
        System.out.println(key);
        int value = Integer.parseInt(key);
        ArrayList<Integer> factors = factor(value);
        res.getWriter().write(factors.toString());
    }

    private ArrayList<Integer> factor(int a) {
        int upperlimit = (int)(Math.sqrt(a));
        ArrayList<Integer> factors = new ArrayList<Integer>();
        for(int i=1;i <= upperlimit; i+= 1){
            if(a%i == 0){
                factors.add(i);
                if(i != a/i){
                    factors.add(a/i);
                }
            }
        }
        Collections.sort(factors);
        return factors;
    }
    public String getServletInfo() {
        return null;
    }

    public void destroy() {

    }
}
