import logging
from time import sleep

from .retriever import Retriever
from .processor import Processor


class Poller():
    def __init__(self, retriever: Retriever, processor: Processor) -> None:
        self.retriever = retriever
        self.processor = processor

    def execute(self) -> None:
        while True:
            logging.info("Polling for requests")
            newest_request = self.retriever.get_newest()

            if newest_request is None:
                logging.info("No requests found, waiting")
                sleep(0.1)
            else:
                self.processor.process(newest_request)