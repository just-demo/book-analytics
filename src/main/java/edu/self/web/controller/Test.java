package edu.self.web.controller;

import org.apache.commons.lang.StringUtils;

import java.util.*;

public class Test {
    public static void main(String[] args) {

        String text = "sdf kl;kkl  sdfs   d";
        System.out.println(text.replaceAll("(\\s)(?=\\s)", "&nbsp;"));

    }
}
