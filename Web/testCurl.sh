curl -i -X GET http://localhost:3000/buildinginfo \
    -H "Content-Type: application/json" \
    --data '{"bid" : "0228777", "name" : "socc_building",
             "longitude": "123", "latitude": "11"}'

curl -i -X GET http://localhost:3000/indoorposition \
    -H "Content-Type: application/json" \
    --data '{"bid" : "228778", "rssi" : {"a" : -20, "b" : -10} }'
