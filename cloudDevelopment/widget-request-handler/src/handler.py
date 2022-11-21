import os
import json
from validate import Validator
from processor import Processor
from logger import logger


def widget_request_handler(event, context):
    logger.info("Received event")
    RequestHandler(event, context)


class RequestHandler:
    def __init__(self, event, context):
        self.event = event
        self.context = context

        self.handle_request()

    def handle_request(self):
        request = Validator(self.event)

        if request.validate():
            processor = Processor(os.environ["WIDGET_QUEUE_NAME"])
            processor.process(self.event)
