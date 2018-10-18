package com.platform.parent.util;

import com.alibaba.fastjson.JSONObject;

import java.text.MessageFormat;

/**
 * Created by tqyao.
 */
public class EnumUtil {
    public static Object errorToJson(ErrorCode errorCode) {
        return convert(errorCode.getStatus(),errorCode.getCode(),errorCode.getMessage(), null);
    }

    public static Object errorToJson(ErrorCode errorCode, String details) {
        return convert(errorCode.getStatus(),errorCode.getCode(),errorCode.getMessage(), details);
    }

    public static Object succToJson(SuccessCode successCode) {
        return convert(successCode.getStatus(),successCode.getCode(),successCode.getMessage(), null);
    }

    private static Object convert(int status, String code, String message, String details) {
        JSONObject result = new JSONObject();
        result.put("status", status);
        result.put("code", code);
        String finalMessage = message;
        if (details != null)
        {
            MessageFormat form = new MessageFormat(message);
            finalMessage = form.format(new Object[]{details});
        }
        result.put("message",finalMessage);
        return result;
    }

    public static String[] parseState(String url)
    {
        if (url == null || url.isEmpty())
        {
            return null;
        }

        String token = "state=";
        int index = url.lastIndexOf(token);
        if (index == -1)
        {
            return null;
        }

        String stateValue = url.substring(index + token.length(), url.length());

        System.out.println("parseState got stateValue from url:[" + stateValue + "]");

        String[] stateSplits = stateValue.split("_");

        return stateSplits;
    }

    public static final void main(String[] args)
    {
        String[] result = parseState("http://www.mxjzq.com/H5/?code=001mBC6X1K7HIS0m6q4X15Gz6X1mBC66&state=8_3_groupmessage");
        for (int i = 0; i < result.length; i++)
        {
            System.out.println(result[i]);
        }

        result = parseState("http://www.mxjzq.com/H5/?code=001mBC6X1K7HIS0m6q4X15Gz6X1mBC66&state=");
        for (int i = 0; i < result.length; i++)
        {
            System.out.println(result[i]);
        }
    }

}
