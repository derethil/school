from abc import ABC, abstractmethod


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
            return sorted(objects, key=lambda x: int(x.key))[0]


class SQSRetriever(S3Retriever):
    def __init__(self, request_queue):
        self.request_queue = request_queue
        self.message_cache = []

    def get_one(self):
        if len(self.message_cache) == 0:
            self.message_cache = self.request_queue.receive_messages(
                MaxNumberOfMessages=10
            )

        return self.message_cache.pop(0) if len(self.message_cache) > 0 else None
