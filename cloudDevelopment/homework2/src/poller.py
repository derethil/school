from time import sleep

from .retriever import Retriever
from .processor import Processor


class Poller():
    def __init__(self, retriever: Retriever, processor: Processor) -> None:
        self.retriever = retriever
        self.processor = processor

    def execute(self) -> None:
        while True:
            newest_request = self.retriever.get_newest()

            if newest_request is None:
                print("No requests found")
                sleep(0.1)
            else:
                self.processor.process(newest_request)