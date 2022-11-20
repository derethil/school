import boto3
import logging


class Processor:
    def __init__(self, widget_queue_name) -> None:
        sqs = boto3.resource("sqs")
        self.queue = sqs.get_queue_by_name(QueueName=widget_queue_name)
        logging.info("Connected to SQS queue: %s", widget_queue_name)

    def process(self, request):
        self.queue.send_message(MessageBody=str(request))
        logging.info("Sent widget to queue: %s", request)
