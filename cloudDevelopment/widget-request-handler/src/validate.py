from logger import logger
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
        logger.info("Validating incoming request")

        try:
            validate(instance=self.request, schema=self.schema)

        except ValidationError as error:
            logger.error("Invalid Request: %s", error)
            raise error
        return True
