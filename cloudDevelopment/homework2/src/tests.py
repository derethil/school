import json
import os
import unittest

import boto3

from processor import S3Processor, DynamoDBProcessor
from retriever import S3Retriever, SQSRetriever

REQUEST_BUCKET = "usu-cs5260-push-requests"
REQUEST_QUEUE = "cs5260-requests"
WIDGET_BUCKET = "usu-cs5260-push-web"
WIDGET_TABLE = "widgets"

s3 = boto3.resource("s3", region_name="us-east-1")
ddb = boto3.resource("dynamodb", region_name="us-east-1")
sqs = boto3.resource("sqs", region_name="us-east-1")

SCRIPT_DIR = os.path.dirname(os.path.realpath(__file__))
SAMPLES_DIR = os.path.join(SCRIPT_DIR, "samples")


def _get_sample(name):
    file_path = os.path.join(SAMPLES_DIR, name)
    with open(file_path) as f:
        return json.load(f)


class TestS3Processor(unittest.TestCase):
    def setUp(self):
        self.create_request = _get_sample("create.json")
        self.update_request = _get_sample("update.json")
        self.delete_request = _get_sample("delete.json")

        self.bucket = s3.Bucket(WIDGET_BUCKET)
        self.processor = S3Processor(self.bucket)

    def test_create(self):
        # Delete pre-existing widget
        owner = self.create_request["owner"].replace(" ", "-").lower()
        key = f"widgets/{owner}/{self.create_request['widgetId']}"

        self.bucket.delete_objects(Delete={"Objects": [{"Key": key}]})

        widget_count = len(list(self.bucket.objects.all()))
        self.processor._process_create(self.create_request)
        self.bucket.load()

        widget_count_after = len(list(self.bucket.objects.all()))
        self.assertEqual(widget_count + 1, widget_count_after)

    def test_update(self):
        self.processor._process_create(self.create_request)
        self.bucket.load()

        widget_count = len(list(self.bucket.objects.all()))

        self.processor._process_update(self.update_request)
        self.bucket.load()

        widget_count_after = len(list(self.bucket.objects.all()))
        self.assertEqual(widget_count, widget_count_after)

    def test_delete(self):
        self.processor._process_create(self.create_request)
        self.bucket.load()

        widget_count = len(list(self.bucket.objects.all()))

        self.processor._process_delete(self.delete_request)
        self.bucket.load()

        widget_count_after = len(list(self.bucket.objects.all()))
        self.assertEqual(widget_count - 1, widget_count_after)


class TestDynamoDBProcessor(unittest.TestCase):
    def setUp(self):
        self.create_request = _get_sample("create.json")
        self.update_request = _get_sample("update.json")
        self.delete_request = _get_sample("delete.json")

        self.table = ddb.Table(WIDGET_TABLE)
        self.processor = DynamoDBProcessor(self.table)

    def test_create(self):
        self.table.delete_item(Key={"id": self.create_request["widgetId"]})
        widget_count = len(self.table.scan()["Items"])

        self.processor._process_create(self.create_request)

        widget_count_after = len(self.table.scan()["Items"])
        self.assertEqual(widget_count + 1, widget_count_after)

    def test_update(self):
        self.processor._process_create(self.create_request)
        widget_count = len(self.table.scan()["Items"])

        self.processor._process_update(self.update_request)

        widget_count_after = len(self.table.scan()["Items"])
        self.assertEqual(widget_count, widget_count_after)

    def test_delete(self):
        self.processor._process_create(self.create_request)
        widget_count = len(self.table.scan()["Items"])

        self.processor._process_delete(self.delete_request)

        widget_count_after = len(self.table.scan()["Items"])
        self.assertEqual(widget_count - 1, widget_count_after)


class TestS3Retriever(unittest.TestCase):
    def setUp(self):
        self.request_bucket = s3.Bucket(REQUEST_BUCKET)
        self.create_request = _get_sample("create.json")

    def test_get_one(self):
        widget_count = len(list(self.request_bucket.objects.all()))

        if widget_count == 0:
            raise Exception("Please add a request to the request bucket before testing")

        retriever = S3Retriever(self.request_bucket)
        newest_request = retriever.get_one()
        self.assertIsNotNone(newest_request)

    def test_when_empty(self):
        widget_count = len(list(self.request_bucket.objects.all()))

        if widget_count > 0:
            self.request_bucket.delete_objects(
                Delete={
                    "Objects": [
                        {"Key": obj.key} for obj in self.request_bucket.objects.all()
                    ]
                }
            )

        retriever = S3Retriever(self.request_bucket)
        newest_request = retriever.get_one()
        self.assertIsNone(newest_request)


class TestSQSRetriever(unittest.TestCase):
    def setUp(self):
        self.request_queue = sqs.get_queue_by_name(QueueName=REQUEST_QUEUE)

    def test_get_one(self):
        retriever = SQSRetriever(self.request_queue)
        request = retriever.get_one()
        self.assertIsNotNone(request)


if __name__ == "__main__":
    unittest.main()
