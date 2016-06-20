import http.client
import json
import sys

from tests.contact_dto import ContactDTOFrom
from tests.utils.get_message import get_message, get_a_property
from tests.utils.get_properties import GetProperties


class TestContact():
    def setup_class(cls):
        contact = GetProperties()
        cls.url_host = contact.host
        cls.url_basepath = contact.url + 'contacts'

    def test_add_contact(cls):
        file_name = sys._getframe().f_code.co_name + '.properties'
        data = get_message(file_name)
        connection = http.client.HTTPSConnection(cls.url_host)
        headers = {'Content-Type': 'application/json'}
        connection.request('POST', cls.url_basepath, body=data, headers=headers)
        response = connection.getresponse()
        assert response.code == 200

        res_string = str(response.read(), 'utf-8')
        TestContact.compare_json(ContactDTOFrom().from_json(res_string), file_name)

    def test_add_contact_again(cls):
        file_name = sys._getframe().f_code.co_name + '.properties'
        data = get_message(file_name)
        connection = http.client.HTTPSConnection(cls.url_host)
        headers = {'Content-Type': 'application/json'}
        connection.request('POST', cls.url_basepath, body=data, headers=headers)
        response = connection.getresponse()
        assert response.code == 409

        message = json.loads(str(response.read(), 'utf-8'))
        assert message['errors'][0]['code'] == 101

    def test_get_contact_by_email(cls):
        connection = http.client.HTTPSConnection(cls.url_host)
        print('URL IS ', cls.url_host.__str__())
        connection.request('GET', cls.url_basepath + '?email=araza@gmail.com')
        response = connection.getresponse()
        assert response.code == 200

        res_string = str(response.read(), 'utf-8')
        file_name = 'test_add_contact.properties'
        x = [ContactDTOFrom()]
        TestContact.compare_json((x[0].from_json(res_string))[0], file_name)

    def test_get_contact_by_name(cls):
        connection = http.client.HTTPSConnection(cls.url_host)
        print('URL IS ', cls.url_host.__str__())
        connection.request('GET', cls.url_basepath + '?name=Altaf')
        response = connection.getresponse()
        assert response.code == 200

        res_string = str(response.read(), 'utf-8')
        file_name = 'test_add_contact.properties'
        x = [ContactDTOFrom()]
        TestContact.compare_json((x[0].from_json(res_string))[0], file_name)

    def test_get_contact_by_email_and_name(cls):
        connection = http.client.HTTPSConnection(cls.url_host)
        print('URL IS ', cls.url_host.__str__())
        connection.request('GET', cls.url_basepath + '?email=araza@gmail.com&name=altaf')
        response = connection.getresponse()
        assert response.code == 200

        res_string = str(response.read(), 'utf-8')
        file_name = 'test_add_contact.properties'
        x = [ContactDTOFrom()]
        TestContact.compare_json((x[0].from_json(res_string))[0], file_name)

    def test_get_contact_by_email_less_3_char(cls):
        connection = http.client.HTTPSConnection(cls.url_host)
        print('URL IS ', cls.url_host.__str__())
        connection.request('GET', cls.url_basepath + '?email=ar')
        response = connection.getresponse()
        assert response.code == 400

        message = json.loads(str(response.read(), 'utf-8'))
        assert message['errors'][0]['code'] == 112

    def test_get_contact_by_name_less_3_char(cls):
        connection = http.client.HTTPSConnection(cls.url_host)
        print('URL IS ', cls.url_host.__str__())
        connection.request('GET', cls.url_basepath + '?name=Al')
        response = connection.getresponse()
        assert response.code == 400

        message = json.loads(str(response.read(), 'utf-8'))
        assert message['errors'][0]['code'] == 112

    def test_get_contact_by_empty_email(cls):
        connection = http.client.HTTPSConnection(cls.url_host)
        print('URL IS ', cls.url_host.__str__())
        connection.request('GET', cls.url_basepath + '?email=')
        response = connection.getresponse()
        assert response.code == 400

        message = json.loads(str(response.read(), 'utf-8'))
        assert message['errors'][0]['code'] == 112

    def test_get_contact_by_no_param(cls):
        connection = http.client.HTTPSConnection(cls.url_host)
        print('URL IS ', cls.url_host.__str__())
        connection.request('GET', cls.url_basepath)
        response = connection.getresponse()
        assert response.code == 400

        message = json.loads(str(response.read(), 'utf-8'))
        assert message['errors'][0]['code'] == 111

    def test_update_contact(cls):
        file_name = sys._getframe().f_code.co_name + '.properties'
        data = get_message(file_name)
        connection = http.client.HTTPSConnection(cls.url_host)
        headers = {'Content-Type': 'application/json'}
        connection.request('PUT', cls.url_basepath + '/email/araza@gmail.com', body=data, headers=headers)
        response = connection.getresponse()
        assert response.code == 200

        res_string = str(response.read(), 'utf-8')
        TestContact.compare_json(ContactDTOFrom().from_json(res_string), file_name)

    def test_get_contact_after_update(cls):
        connection = http.client.HTTPSConnection(cls.url_host)
        print('URL IS ', cls.url_host.__str__())
        connection.request('GET', cls.url_basepath + '?email=araza@gmail.com')
        response = connection.getresponse()
        assert response.code == 200

        res_string = str(response.read(), 'utf-8')
        file_name = 'test_update_contact.properties'
        x = [ContactDTOFrom()]
        TestContact.compare_json((x[0].from_json(res_string))[0], file_name)

    def test_delete_contact(cls):
        connection = http.client.HTTPSConnection(cls.url_host)
        connection.request('DELETE', cls.url_basepath + '/email/araza@gmail.com')
        response = connection.getresponse()
        assert response.code == 200

    def test_delete_contact_same_again(cls):
        connection = http.client.HTTPSConnection(cls.url_host)
        connection.request('DELETE', cls.url_basepath + '/email/araza@gmail.com')
        response = connection.getresponse()
        assert response.code == 400

        message = json.loads(str(response.read(), 'utf-8'))
        assert message['errors'][0]['code'] == 102

    def test_delete_contact_nonexisting_contact(cls):
        connection = http.client.HTTPSConnection(cls.url_host)
        connection.request('DELETE', cls.url_basepath + '/email/noone@gmail.com')
        response = connection.getresponse()
        assert response.code == 400

        message = json.loads(str(response.read(), 'utf-8'))
        assert message['errors'][0]['code'] == 102

    def compare_json(contact_dto, file_name):
        assert contact_dto.name == get_a_property(file_name, 'name')
        assert contact_dto.email == get_a_property(file_name, 'email')
        assert contact_dto.phone == int(get_a_property(file_name, 'phone'))
        assert contact_dto.address == get_a_property(file_name, 'address')
