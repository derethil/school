import boto3
from logger import logger


class Processor:
    def __init__(self, widget_queue_name) -> None:
        sqs = boto3.resource("sqs")
        self.queue_name = widget_queue_name
        self.queue = sqs.get_queue_by_name(QueueName=widget_queue_name)
        logger.info("Connected to SQS queue: %s", widget_queue_name)

    def process(self, request):
        res = self.queue.send_message(MessageBody=str(request))
        logger.info(
            f"Sent {request['type']} widget ({request['widgetId']}) to queue: {self.queue_name}"
        )
        return res
