package com.common;

/**
 * Created by nuttapong_i on 14/06/2559.
 */
public class CommonClass {
    public static String ConvertStatus(String codeStatus){
        String status = "";
        switch (codeStatus){
            case "1" :
                status = "";
                break;
            case "2" :
                status = "";
                break;
            case "3" :
                status = "";
                break;
            default:
                status = "ไม่รู้จักสถานะ";
        }


        return status;
    }
}
