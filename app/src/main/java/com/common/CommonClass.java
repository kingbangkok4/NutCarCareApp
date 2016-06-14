package com.common;

/**
 * Created by nuttapong_i on 14/06/2559.
 */
public class CommonClass {
    public String ConvertCodeToStatus(String codeStatus){
        String status = "";
        switch (codeStatus){
            case "1" :
                status = "ดำเนินการอยู่";
                break;
            case "2" :
                status = "ดำเนินการเสร็จ";
                break;
            case "3" :
                status = "ลูกค้ารับรถแล้ว";
                break;
            case "-1" :
                status = "ยกเลิก";
                break;
            default:
                status = "ไม่รู้จักสถานะ";
        }


        return status;
    }
    public String ConvertStatusToCode(String codeStatus){
        String status = "";
        switch (codeStatus){
            case "ดำเนินการอยู่" :
                status = "1";
                break;
            case "ดำเนินการเสร็จ" :
                status = "2";
                break;
            case "ลูกค้ารับรถแล้ว" :
                status = "3";
                break;
            case "ยกเลิก" :
                status = "-1";
                break;
            default:
                status = "0";
        }


        return status;
    }
}
