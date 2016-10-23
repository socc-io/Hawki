1. API 필요한 기능

    건물 id  알려주세요 ( 도면 주셈 )
        Request : {
            경도: ''
            위도: ''
            이름: ''
            건물ID: ''
        }
        Response : [{
            건물ID: ''
            주소: ''
            대표번호(전화): ''
            건물명(실제): ''
            층: [
                기준점: {
                    x:
                    y:
                }
                도면URL: ''
            ]
        }]
        
    건물 안에서 내 위치 알려주세요
        Request : {
            건물ID: ''
            RSSI SET: {key(BSSID) - value(RSSI)}
            BLE: {}
            GEOMETRIC: {}
            BARO: {}
            ...
        }
        Response : {
            POSITION: {
                x: ''
                y: ''
                z(floor) : ''
            }
            POI: ''
        }

