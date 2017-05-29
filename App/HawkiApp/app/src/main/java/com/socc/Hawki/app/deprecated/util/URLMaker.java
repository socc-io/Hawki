package com.socc.Hawki.app.deprecated.util;

/**
 * Created by joyeongje on 2016. 9. 4..
 */
public class URLMaker {

    public enum DATAFORMAT {
        BuildingInfo, IndoorPosition, RSSIDSET
    }

    public URLMaker(){}

    public static String createRequestURL(DATAFORMAT dataformat, double lat,double lon, double alt, double radius, String name)
    {
        String selectedURL = "";

        switch (dataformat) {
            case BuildingInfo: // 건물 정보 요청 다음지도 url : 경도, 위도, 반경, 건물이름
                selectedURL = "http://beaver.hp100.net:4000/buildinginfo" + "?buildName=" + name;
                break;

            case IndoorPosition: // 건물내에서 나의 위치를 요청한다
                selectedURL = "http://beaver.hp100.net:4000/getposition";
                break;

            case RSSIDSET: // rssid 셋을 서버로 보낸다
                selectedURL = "http://beaver.hp100.net:4000/collectrssi";
                break;
        }

        return selectedURL;
    }


}
