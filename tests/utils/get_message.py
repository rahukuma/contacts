import configparser
import os

from pathlib import Path
from tests.contact_dto import ContactDTOTo

_ROOT = os.path.abspath(os.path.dirname(__file__))
_DIR = Path(_ROOT).parent.__str__()

def get_message(file_name):
    file_path = os.path.join(_DIR, 'data', file_name)
    config = configparser.ConfigParser()
    config.read(file_path)
    try:
        name = config.get('contact', 'name')
    except configparser.NoOptionError:
        name = None
    try:
        email = config.get('contact', 'email')
    except configparser.NoOptionError:
        email = None
    try:
        phone = config.get('contact', 'phone')
    except configparser.NoOptionError:
        phone = None
    try:
        address = config.get('contact', 'address')
    except configparser.NoOptionError:
        address = None

    m = ContactDTOTo(name, email, phone, address)
    return m.to_json(m)

def get_a_property(file_name, property):
    file_path = os.path.join(_DIR, 'data', file_name)
    config = configparser.ConfigParser()
    config.read(file_path)
    try:
        value = config.get('contact', property)
    except configparser.NoOptionError:
        value = None

    return value
