import unittest
import os
import json

from jsonschema import ValidationError

from processor import Processor
from validate import Validator
from handler import RequestHandler

SCRIPT_DIR = os.path.dirname(os.path.realpath(__file__))
SAMPLES_DIR = os.path.join(SCRIPT_DIR, "samples")


def _get_sample(name):
    file_path = os.path.join(SAMPLES_DIR, name)
    with open(file_path) as f:
        return json.load(f)


class TestHandler(unittest.TestCase):
    def setUp(self):
        self.create_request = _get_sample("create.json")
        self.update_request = _get_sample("update.json")
        self.delete_request = _get_sample("delete.json")

    def test_invalid_request(self):
        invalid_request = {"foo": "bar"}

        self.assertRaises(
            ValidationError,
            lambda: RequestHandler(invalid_request, None),
        )

    def test_valid_create_request(self):
        self.assertIsNotNone(RequestHandler(self.create_request, None))


class TestValidator(unittest.TestCase):
    def setUp(self):
        self.create_request = _get_sample("create.json")
        self.update_request = _get_sample("update.json")
        self.delete_request = _get_sample("delete.json")

    def test_invalid_request(self):
        invalid_request = {"foo": "bar"}

        self.assertRaises(
            ValidationError,
            lambda: Validator(invalid_request).validate(),
        )

    def test_valid_request(self):
        self.assertTrue(Validator(self.create_request).validate())


class TestProcessor(unittest.TestCase):
    def setUp(self):
        self.create_request = _get_sample("create.json")
        self.update_request = _get_sample("update.json")
        self.delete_request = _get_sample("delete.json")

    def test_valid_process(self):
        processor = Processor(os.environ["WIDGET_QUEUE_NAME"])
        self.assertTrue(processor.process(self.create_request))


if __name__ == "__main__":
    unittest.main()
