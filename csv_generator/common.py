import json
from datetime import datetime, timedelta

now = datetime.now()

def get_servicekey() -> str:
    with open("secrets.json") as f:
        secrets = json.loads(f.read())
    return secrets.get('decoding_key')


def get_current_date() -> str:
    return now.strftime('%Y%m%d') + '00'


class GetApiError(Exception):
    def __init__(self, msg):
        self.msg = msg

    def __str__(self):
        return self.msg