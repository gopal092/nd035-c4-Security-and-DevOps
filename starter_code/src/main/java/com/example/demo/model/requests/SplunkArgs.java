package com.example.demo.model.requests;

import com.splunk.Args;

public class SplunkArgs {
    public static void setSourceType(Args args, String sourceType){
        args.add("sourcetype", sourceType);
    }
}
