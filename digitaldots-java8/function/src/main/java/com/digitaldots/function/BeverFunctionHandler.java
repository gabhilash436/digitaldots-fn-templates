package com.digitaldots.function;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openfaas.model.IRequest;
import com.openfaas.model.IResponse;
import com.openfaas.model.Response;

import org.apache.commons.lang3.StringUtils;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.HashMap;
import java.util.Map;


public class BeverFunctionHandler{

    private static final String PARAM_USER_NAME = "name";
    private static final String RESPONSE_TEMPLETE = "Hello %s";

    private ObjectMapper mapper = new ObjectMapper();

    private String getUserName(IRequest req) {

        String userName = null;

        try {
            Map<String, Object> mapFromStr = mapper.readValue(req.getBody(),
                    new TypeReference<Map<String, Object>>() {});

            if(null!=mapFromStr && mapFromStr.containsKey(PARAM_USER_NAME)) {
                userName = String.valueOf(mapFromStr.get(PARAM_USER_NAME));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        if(StringUtils.isBlank(userName)) {
            userName = "anonymous";
        }

        return userName;
    }

    
    public IResponse Handle(IRequest req) {

        String userName = getUserName(req);
        String message = String.format(RESPONSE_TEMPLETE,
                userName,
                new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" ).format(new Date()));
        Map<String, Object> rlt = new HashMap<>();
        rlt.put("success", true);
        rlt.put("message", message);

        String rltStr = null;

        try {
            rltStr = mapper.writeValueAsString(rlt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Response res = new Response();
        res.setContentType("application/json;charset=utf-8");
        res.setBody(rltStr);

	    return res;
    }
    
}
