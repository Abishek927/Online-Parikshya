package com.online.exam.helper;

import java.util.HashMap;
import java.util.Map;

public class MapMessage {

    public Map<String,Object> getMessage(Map<Integer,Object> map){
        Map<String,String> message=new HashMap<>();
        for (Map.Entry<Integer,Object> entry:map.entrySet()
             ) {
            if(entry.getKey()==500){
                message.put("status",entry.getKey().toString());
                message.put("data",entry.getValue().toString());
            }
            message.put("status",entry.getKey().toString());
            message.put("data",entry.getValue().toString());

        }
    }
}
