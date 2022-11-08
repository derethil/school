import click
import boto3
import logging

from .processor import Processor, S3Processor, DynamoDBProcessor
from .retriever import Retriever, S3Retriever, SQSRetriever
from .poller import Poller

logging.basicConfig(level=logging.INFO)


@click.command(help="Consume requests and store them in S3")
# Request Source Options
@click.option(
    "--request-bucket",
    "-rb",
    "rb_str",
    help="Name of bucket that will contain requests",
)
@click.option(
    "--request-queue",
    "-rq",
    "rq_str",
    help="Name of queue that will contain requests",
)
# Widget Storage Options
@click.option(
    "--widget-bucket",
    "-wb",
    "wb_str",
    help="Name of the S3 bucket that holds the widgets",
)
@click.option(
    "--dynamodb-widget-table",
    "-dwt",
    "dwt_str",
    help="Name of the DynamoDB table that holds the widgets",
)
def main(rb_str: str, rq_str: str, wb_str: str, dwt_str: str):
    s3 = boto3.resource("s3", region_name="us-east-1")
    sqs = boto3.resource("sqs", region_name="us-east-1")
    dynamodb = boto3.resource("dynamodb", region_name="us-east-1")
    logging.info("Connected to AWS")

    if rb_str and not rq_str:
        request_bucket = s3.Bucket(rb_str)
        retriever: Retriever = S3Retriever(request_bucket)

    elif rq_str and not rb_str:
        request_queue = sqs.get_queue_by_name(QueueName=rq_str)
        retriever = SQSRetriever(request_queue)

    else:
        raise ValueError("Must specify either a request bucket or queue, but not both")

    if wb_str and not dwt_str:
        widget_bucket = s3.Bucket(wb_str)
        processor: Processor = S3Processor(widget_bucket)

    elif dwt_str and not wb_str:
        widget_table = dynamodb.Table(dwt_str)
        processor = DynamoDBProcessor(widget_table)

    else:
        raise ValueError("Must provide either widget bucket or table, but not both")

    poller = Poller(retriever, processor)
    poller.execute()
