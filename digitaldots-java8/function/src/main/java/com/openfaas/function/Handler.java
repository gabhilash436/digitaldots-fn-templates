package com.openfaas.function;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openfaas.model.IRequest;
import com.openfaas.model.IResponse;
import com.openfaas.model.Response;
import org.apache.commons.lang3.StringUtils;

import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class Handler extends BeverHandler {

    private static final String PARAM_USER_NAME = "name";


    private static final String RESPONSE_TEMPLETE = "Hello %s, response from [%s], PID [%s], %s";

    private ObjectMapper mapper = new ObjectMapper();


    public static String getIpAddress() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                } else {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (ip != null && ip instanceof Inet4Address) {
                            return ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("IP地址获取失败" + e.toString());
        }
        return "";
    }

    private static String getPID() {
        return ManagementFactory
                .getRuntimeMXBean()
                .getName()
                .split("@")[0];
    }


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

        System.out.println("1. ---" + userName);


        String message = String.format(RESPONSE_TEMPLETE,
                userName,
                getIpAddress(),
                getPID(),
                new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" ).format(new Date()));

        System.out.println("2. ---" + message);


        Map<String, Object> rlt = new HashMap<>();
        rlt.put("success", true);
        rlt.put("message", message);

        String rltStr = null;

        try {
            rltStr = mapper.writeValueAsString(rlt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        BeverResponse res = new BeverResponse();
        res.setContentType("application/json;charset=utf-8");
        res.setBody(rltStr);

	    return res;
    }
}
