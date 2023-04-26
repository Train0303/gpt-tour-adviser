import openai

n = input("오늘부터 며칠간 여행을 가실건가요? : ")
region = input("어디를 여행갈 예정이신가요? : ")
openai.api_key = "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"
completion = openai.ChatCompletion.create(
    model="gpt-3.5-turbo",
    messages = [
        {"role" : "system", "content" : "너는 이제부터 관광지를 추천해주는 여행 가이드야."},
        {"role" : "user", "content": f"오늘부터 {n}일간 {region}을 여행할 예정이야. 해당 일자에 날씨를 기반으로 날짜별 관광 코스를 추천해줘."}
    ]
)
print(completion)
print(completion['choices'][0]['message']['content'])