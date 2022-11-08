from abc import ABC, abstractmethod
from enum import Enum
import json
import logging

from botocore.exceptions import ClientError


class Retriever(ABC):
    @abstractmethod
    def get_one(self):
        pass


class S3Retriever(Retriever):
    def __init__(self, request_bucket):
        self.request_bucket = request_bucket

    def _get_all(self) -> list:
        objects = self.request_bucket.objects.all()
        return list(objects)

    def get_one(self):
        objects = self._get_all()
        if len(objects) == 0:
            return None
        else:
            request = sorted(objects, key=lambda x: int(x.key))[0]

            try:
                request_content = json.loads(request.get()["Body"].read())

            except ClientError:
                logging.warning(f"Could not find request {request.key} to retrieve")
                return

            return (request.delete, request_content)


class SQSRetriever(S3Retriever):
    def __init__(self, request_queue):
        self.request_queue = request_queue
        self.request_cache = []

    def get_one(self):
        if len(self.request_cache) == 0:
            self.request_cache = self.request_queue.receive_messages(
                MaxNumberOfMessages=10
            )

        if len(self.request_cache) == 0:
            return None

        request = self.request_cache.pop()
        return (request.delete, json.loads(request.body))
