from abc import ABC, abstractmethod
import json
import logging

from .retriever import Retriever


class Processor(ABC):
    def process(self, request):
        (delete_request, request) = request

        match request["type"]:
            case "create":
                self._process_create(request)
            case "update":
                self._process_update(request)
            case "delete":
                self._process_delete(request)

        delete_request()

    @abstractmethod
    def _process_create(self, request):
        pass

    @abstractmethod
    def _process_update(self, request):
        pass

    @abstractmethod
    def _process_delete(self, request):
        pass


class S3Processor(Processor):
    def __init__(self, response_bucket) -> None:
        self.response_bucket = response_bucket

    def _process_create(self, request):
        logging.info(f'Processing a CREATE request into S3 for {request["widgetId"]}')
        owner = request["owner"].replace(" ", "-").lower()
        self.response_bucket.put_object(
            Key=f"widgets/{owner}/{request['widgetId']}",
            Body=json.dumps(request),
        )

    def _process_update(self, request):
        logging.info(f'Processing an UPDATE request into S3 for {request["widgetId"]}')

    def _process_delete(self, request):
        logging.info(f'Processing a DELETE request into S3 for {request["widgetId"]}')


class DynamoDBProcessor(Processor):
    def __init__(self, response_table) -> None:
        self.response_table = response_table

    def _process_create(self, request):
        logging.info(
            f'Processing a CREATE request into DynamoDB for {request["widgetId"]}'
        )
        self.response_table.put_item(
            Item={
                "id": request["widgetId"],
                "owner": request["owner"],
                "label": request["label"],
                "description": request["description"],
                **{a["name"]: a["value"] for a in request["otherAttributes"]},
            }
        )

    def _process_update(self, request):
        logging.info(
            f'Processing an UPDATE request into DynamoDB for {request["widgetId"]}'
        )

    def _process_delete(self, request):
        logging.info(
            f'Processing a DELETE request into DynamoDB for {request["widgetId"]}'
        )
