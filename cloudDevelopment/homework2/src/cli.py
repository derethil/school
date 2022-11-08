import click
import boto3
import logging

from .processor import Processor, S3Processor, DynamoDBProcessor
from .retriever import Retriever
from .poller import Poller


@click.group()
def cli():
    logging.basicConfig(level=logging.INFO)

@cli.command(help="Consume requests and store them in S3")
@click.option("--request-bucket", "-rb", "rb_str", help="Name of bucket that will contain requests")
@click.option("--widget-bucket", "-wb", "wb_str", help="Name of the S3 bucket that holds the widgets")
@click.option("--dynamodb-widget-table","-dwt", "dwt_str", help="Name of the DynamoDB table that holds the widgets")
def process_s3(rb_str: str, wb_str: str, dwt_str: str):
    s3 = boto3.resource("s3", region_name="us-east-1")
    dynamodb = boto3.resource("dynamodb", region_name='us-east-1')
    logging.info("Successfully connected to S3 and DynamoDB")

    request_bucket = s3.Bucket(rb_str)
    retriever = Retriever(request_bucket)

    if wb_str:
        widget_bucket = s3.Bucket(wb_str)
        processor: Processor = S3Processor(widget_bucket)
    elif dwt_str:
        widget_table = dynamodb.Table(dwt_str)
        processor = DynamoDBProcessor(widget_table)

    poller = Poller(retriever, processor)
    poller.execute()