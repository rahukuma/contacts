import json


class ContactDTOTo(object):
    name = ''
    id = ''
    email = ''
    phone = ''
    address = ''

    def __init__(self, name, email, phone, address):
        self.name = name
        self.email = email
        self.phone = phone
        self.address = address

    def to_json(self, contact):
        message = json.dumps(contact.__dict__)
        return message


class ContactDTOFrom():
    def as_contact(self):
        c = ContactDTOFrom()
        c.__dict__.update(self)
        return c

    def from_json(self, message):
        o = json.loads(message, object_hook=ContactDTOFrom.as_contact)
        return o