package data;

/**
 * Created by joyeongje on 2016. 9. 4..
 */
public class DataSource {

    public enum DATAFORMAT {
        BuildingInfo, IndoorPosition, RSSIDSET
    }

    public DataSource(){}

    public static String createRequestURL(DATAFORMAT dataformat, double lat,double lon, double alt, double radius, String name)
    {
        String selectedURL = "";

        switch (dataformat) {
            case BuildingInfo: // 건물 정보 요청 다음지도 url : 경도, 위도, 반경, 건물이름
               // selectedURL = "http://beaver.hp100.net:4000/test" + "?lat=" + Double.toString(lat) + "&lon=" + Double.toString(lon) +
                 //               "&radius=" + Double.toString(radius) + "&buildName=" + name;

                selectedURL = "http://beaver.hp100.net:4000/buildinginfo" + "?buildName=" + name;
                System.out.println(selectedURL);
                //

                // name 은 requirement
                // 경도랑 위도는 내 위치 정보를 받아올 수 있는 경우에만 받아올 에정

                break;

            case IndoorPosition: // 건물내에서 나의 위치를 요청한다
                // TODO: 2016. 9. 4. 이것도 해야됨 
                selectedURL = "여기에 서버주소 " ;
                break;

            case RSSIDSET: // rssid 셋을 서버로 보낸다
                selectedURL = "";
                break;
        }

        return selectedURL;
    }


}
