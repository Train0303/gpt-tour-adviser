import openai
import json
import argparse
from argparse import Namespace
import datetime
import pandas
from pandas import DataFrame


def parse_arguments() -> Namespace:
    today = datetime.datetime.now().date()
    parser = argparse.ArgumentParser(description="""여행 추천 서비스입니다. 필요한 인자는 다음과 같습니다.\n
    --start: 여행을 시작하는 날짜\n
    --end: 여행을 끝내는 날짜\n
    --region: 여행하고자 하는 지역\n
    """)

    parser.add_argument('--start', help='여행을 시작하는 날짜', default=today, type=str)
    parser.add_argument('--end', help='여행을 끝내는 날짜', default=today, type=str)
    parser.add_argument('--region', help='여행하고자 하는 지역', type=str)

    return parser.parse_args()


def get_servicekey() -> str:
    with open("secrets.json") as f:
        secrets = json.loads(f.read())
    return secrets.get('openai_key')


def get_weather_data() -> str:
    df_weather: DataFrame = pandas.read_csv("city_with_weather.csv")
    df_weather = df_weather.loc[(df_weather['날짜'] >= parse_data.start) &
                                (df_weather['날짜'] <= parse_data.end) &
                                (df_weather['도단위이름'] == parse_data.region)]
    df_weather = df_weather.drop(['도단위이름', '시군구이름', '관광지수'], axis=1)
    df_weather.rename(columns={'전체도시이름': '지역이름'}, inplace=True)
    return df_weather.to_csv(index=False)


if __name__ == '__main__':
    parse_data: Namespace = parse_arguments()
    weather_string: str = get_weather_data()
    user_message: str =\
        f"{parse_data.start}부터 {parse_data.end}까지 {parse_data.region}을 여행할 거야. 날씨를 고려해 날짜별로 관광지를 추천해줘."

    openai.api_key = get_servicekey()
    completion = openai.ChatCompletion.create(
        model="gpt-3.5-turbo",
        messages=[
            {"role": "system", "content": "너는 이제부터 관광지를 추천해주는 여행 가이드야."},
            {"role": "system", "content": f"여행 가이드를 위한 {parse_data.region}에 대한 날짜 별 기상 자료는 다음과 같아.\n" + \
                                          weather_string},
            {"role": "user", "content": user_message}
        ]
    )
    print(completion)
    result = completion['choices'][0]['message']['content']
    print(result)

