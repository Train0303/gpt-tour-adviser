import json
import time
from datetime import datetime, timedelta

import pandas as pd
import requests

now = datetime.now()

class GetApiError(Exception):
    def __init__(self, msg):
        self.msg = msg

    def __str__(self):
        return self.msg


def get_servicekey() -> str:
    with open("secrets.json") as f:
        secrets = json.loads(f.read())
    return secrets.get('decoding_key')


def get_current_date() -> str:
    return now.strftime('%Y%m%d') + '00'


def get_tmFc() -> str: # 06시, 18시 가능
    if 0 <= now.hour < 6: # 전날 18시를 가져와야 함
        yesterday = (now + timedelta(days=-1))
        return yesterday.strftime('%Y%m%d1800')

    elif 6 <= now.hour < 18: # 당일 06시를 가져와야 함
        return now.strftime('%Y%m%d0600')

    elif 18 <= now.hour <= 24: # 당일 18시를 가져와야 함
        return now.strftime('%Y%m%d1800')


def get_temp_api(regId: str) -> list[dict]:
    temp_url = 'http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa'
    params = {'ServiceKey': get_servicekey(),
              'pageNo': '1',
              'numOfRows': '100',
              'dataType': 'JSON',
              'regId' : regId,
              'tmFc': get_tmFc()}

    while True:
        try:
            response = requests.get(url=temp_url, params=params, timeout=5).json()
            break
        except:
            print("get_temp_api error")
            time.sleep(3)

    if response['response']['header']['resultCode'] != '00':
        ErrorMessage = response['response']['header']['resultMsg']
        raise GetApiError(ErrorMessage)

    return response['response']['body']['items']['item']


def get_yooksang_api(regId: str) -> list[dict]:
    yooksang_url = 'http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst'
    params = {'ServiceKey': get_servicekey(),
              'pageNo': '1',
              'numOfRows': '100',
              'dataType': 'JSON',
              'regId' : regId,
              'tmFc': get_tmFc()}

    while True:
        try:
            response = requests.get(url=yooksang_url, params=params, timeout=5).json()
            break
        except:
            print("get_yooksang_api error")
            time.sleep(3)

    if response['response']['header']['resultCode'] != '00':
        ErrorMessage = response['response']['header']['resultMsg']
        raise GetApiError(ErrorMessage)

    return response['response']['body']['items']['item']


def get_tour_api(city_area_id: str) -> list[dict]:
    url = 'http://apis.data.go.kr/1360000/TourStnInfoService1/getCityTourClmIdx1'
    params = {'ServiceKey': get_servicekey(),
              'pageNo': '1',
              'numOfRows': '100',
              'dataType': 'JSON',
              'CURRENT_DATE': get_current_date(),
              'DAY': '14',
              'CITY_AREA_ID': city_area_id}

    while True:
        try:
            response = requests.get(url=url, params=params, timeout=5).json()
            break
        except:
            print("get_tour_api error")
            time.sleep(3)


    if response['response']['header']['resultCode'] != '00':
        ErrorMessage = response['response']['header']['resultMsg']
        raise GetApiError(ErrorMessage)

    return response['response']['body']['items']['item']


def get_temp_dataframe(regId: str) -> pd.DataFrame:

    temp_data = []
    temp_api_data = get_temp_api(regId=regId)[0]

    # 관광지수는 9일까지만 제공하므로, 통일해준다. (max: 10이지만, 9까지만 돌림)
    for i in range(3, 10):
        day = now + timedelta(days=i-1)
        taMin = temp_api_data[f'taMin{i}']
        taMax = temp_api_data[f'taMax{i}']
        temp_data.append({'날짜': day.strftime('%Y-%m-%d'), '기온코드': regId, '최소기온': str(taMin), '최대기온': str(taMax)})

    return pd.DataFrame(temp_data)



def get_yooksang_dataframe(regId: str) -> pd.DataFrame:

    yooksang_data = []
    yooksang_api_data = get_yooksang_api(regId)[0]

    # am, pm은 7일까지만 제공, 데이터의 통일성을 위해, am, pm을 설정해준다.
    # 관광지수는 9일까지만 제공하므로, 통일해준다. (max: 10이지만, 9까지만 돌림)
    for i in range(3, 10):
        day = now + timedelta(days=i-1)
        if i <= 7:
            rain_am = yooksang_api_data[f'rnSt{i}Am']
            rain_pm = yooksang_api_data[f'rnSt{i}Pm']
            forecast_am = yooksang_api_data[f'wf{i}Am']
            forecast_pm = yooksang_api_data[f'wf{i}Pm']
        else:
            rain_am = yooksang_api_data[f'rnSt{i}']
            rain_pm = yooksang_api_data[f'rnSt{i}']
            forecast_am = yooksang_api_data[f'wf{i}']
            forecast_pm = yooksang_api_data[f'wf{i}']
        yooksang_data.append({'날짜': day.strftime('%Y-%m-%d'), '육상코드': regId,
                              '오전비확률': f'{rain_am}%', '오후비확률': f'{rain_pm}%',
                              '오전날씨': forecast_am, '오후날씨': forecast_pm})

    return pd.DataFrame(yooksang_data)


def get_tour_dataframe(city_area_id: str) -> pd.DataFrame:

    tour_data = []
    tour_api_data = get_tour_api(city_area_id=city_area_id)

    for items in tour_api_data:
        day = items['tm'].split()[0]
        kmaTci = items['kmaTci']
        TCI_GRADE = items['TCI_GRADE']
        tour_data.append({'날짜': day, '투어코드': city_area_id, '관광지수': kmaTci, '관광지표': TCI_GRADE})

    return pd.DataFrame(tour_data)


def get_total_dataframe():
    df_total = pd.DataFrame()
    df_merged = pd.read_csv('code_merged.csv', dtype={'투어코드': str})

    for row in df_merged.iterrows(): # row : [idx, {column1 : value1, .. }]
        code_tour = row[1]['투어코드']
        code_temp = row[1]['기온코드']
        code_yooksang = row[1]['육상코드']
        df = pd.DataFrame(row[1]).T

        if code_tour != '':
            df_tour = get_tour_dataframe(code_tour)
            df = pd.merge(left=df, right=df_tour, on='투어코드', how='left')

        if code_temp != '':
            df_temp = get_temp_dataframe(code_temp)
            df = pd.merge(left=df, right=df_temp, on=['기온코드', '날짜'], how='left')

        if code_yooksang != '':
            df_yooksang = get_yooksang_dataframe(code_yooksang)
            df = pd.merge(left=df, right=df_yooksang, on=['육상코드', '날짜'], how='left')

        df_total = pd.concat([df_total, df])

    return df_total


if __name__ == "__main__":
    columns = ['날짜', '전체도시이름', '도단위이름', '시군구이름', '최소기온', '최대기온', '오전날씨', '오전비확률', '오후날씨', '오후비확률', '관광지수', '관광지표']
    df_total = get_total_dataframe()
    df_total = df_total[columns]
    df_total.dropna(inplace=True)
    df_total.set_index('날짜', inplace=True)
    df_total.sort_values('날짜', inplace=True)
    df_total.to_csv('city_with_weather.csv', encoding='utf-8')