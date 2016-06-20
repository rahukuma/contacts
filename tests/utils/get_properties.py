import configparser
import os

from tests.utils import get_message

class GetProperties:
    def __init__(self):
        path = self.file_path('url.properties')
        config = configparser.ConfigParser()
        config.read(path)
        self.url = config.get('url', 'url')
        self.host = config.get('url', 'host')

    def file_path(self, path):
        return os.path.join(get_message._DIR, 'data', path)