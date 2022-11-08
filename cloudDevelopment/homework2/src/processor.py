from abc import ABC, abstractmethod
import json
import logging


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

    def _create_key(self, request):
        owner = request["owner"].replace(" ", "-").lower()
        return f"widgets/{owner}/{request['widgetId']}"

    def _process_create(self, request):
        logging.info(f'Processing a CREATE request into S3 for {request["widgetId"]}')

        key = self._create_key(request)
        self.response_bucket.put_object(Key=key, Body=json.dumps(request))

    def _process_update(self, request):
        logging.info(f'Processing an UPDATE request into S3 for {request["widgetId"]}')

        key = self._create_key(request)

        try:
            widget = json.loads(self.response_bucket.Object(key).get()["Body"].read())
        except Exception:
            logging.warning(f"Could not find widget {request['widgetId']} to update")
            return

        new_widget = self.update_widget_json(widget, request)
        self.response_bucket.put_object(Key=key, Body=json.dumps(new_widget))

    def _process_delete(self, request):
        logging.info(f'Processing a DELETE request into S3 for {request["widgetId"]}')

        key = self._create_key(request)

        try:
            self.response_bucket.Object(key).delete()
        except Exception:
            logging.warning(f"Could not find widget {request['widgetId']} to delete")
            return

    def update_widget_json(self, widget_old, widget_update):
        def can_update(key):
            return key not in ["widgetId", "owner"]

        new_widget = widget_old.copy()
        to_update = {k: v for k, v in widget_update.items() if can_update(k)}

        for key, value in to_update.items():
            if value is None:
                break

            if value == "":
                new_widget.pop(key)

            new_widget[key] = value

        return new_widget


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

        try:
            widget = self.response_table.get_item(Key={"id": request["widgetId"]})
        except Exception:
            logging.warning(f"Could not find widget {request['widgetId']} to update")
            return

        new_widget = self.update_widget_json(widget["Item"], request)
        self.response_table.put_item(Item=new_widget)

    def _process_delete(self, request):
        logging.info(
            f'Processing a DELETE request into DynamoDB for {request["widgetId"]}'
        )

        try:
            self.response_table.delete_item(Key={"id": request["widgetId"]})
        except Exception:
            logging.warning(f"Could not find widget {request['widgetId']} to delete")
            return

    def update_widget_json(self, widget_old, widget_update):
        def can_update(key):
            return key not in ["widgetId", "owner"]

        new_widget = widget_old.copy()
        to_update = {k: v for k, v in widget_update.items() if can_update(k)}

        for key, value in to_update.items():
            if value is None or key == "otherAttributes":
                break

            if value == "":
                new_widget.pop(key)

            new_widget[key] = value

        return {
            **new_widget,
            **{a["name"]: a["value"] for a in widget_update["otherAttributes"]},
        }
