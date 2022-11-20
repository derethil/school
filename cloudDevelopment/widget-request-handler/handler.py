from validate import Validator
from processor import Processor


def widget_request_handler(event, context):
    RequestHandler(event, context)


class RequestHandler:
    def __init__(self, event, context):
        self.event = event
        self.context = context

        self.handle_request()

    def handle_request(self):
        request = Validator(self.event)

        if request.validate():
            processor = Processor("widgets")
            processor.process(self.event)
