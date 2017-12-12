# -*- coding: utf-8 -*-

import requests, CONFIG

class DaumSearchEngine:
    def getBuildInfoByName(self, name):
        daum_search_query = 'https://apis.daum.net/local/v1/search/keyword.json?apikey='
        daum_app_key = CONFIG.DAUM['DAUM_KEY']
        daum_search_keyword = '&query=' + name
        daum_search_full_query = daum_search_query + daum_app_key + daum_search_keyword;
        r = requests.get(daum_search_full_query)
        return self.parsingBuildInfoJsonData(json_data=r.json())

    # Parse json data from DAUM API
    def parsingBuildInfoJsonData(self, json_data):
        try:
            data_list = json_data['channel']['item']
            results = []
            for r in data_list:
                temp = {'id': r['id'],
                        'phone': r['phone'],
                        'title': r['title'],
                        'address': r['address']}
                results.append(temp)
            return { 'Build': results }
        except Exception as e:
            print(e)