import logging
from jsonschema import validate, ValidationError

schema = {
    "type": "object",
    "properties": {
        "type": {"type": "string"},
        "requestId": {"type": "string"},
        "widgetId": {"type": "string"},
        "owner": {"type": "string"},
        "label": {"type": "string"},
        "description": {"type": "string"},
        "otherAttributes": {
            "type": "array",
            "items": {
                "type": "object",
                "properties": {"name": {"type": "string"}, "value": {"type": "string"}},
                "required": ["name", "value"],
            },
        },
    },
    "required": ["type", "requestId", "widgetId", "owner"],
}


class Validator:
    def __init__(self, request):
        self.schema = schema
        self.request = request

    def validate(self):
        logging.info("Validating: %s", self.request["requestId"])

        try:
            validate(instance=self.request, schema=self.schema)

        except ValidationError as error:
            logging.error("Invalid Request: %s", error)
            return False
        return True
