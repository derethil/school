class Retriever():
    def __init__(self, request_bucket):
        self.request_bucket = request_bucket

    def get_all(self) -> list:
        objects = self.request_bucket.objects.all()
        return list(objects)

    def get_newest(self):
        objects = self.get_all()
        if len(objects) == 0:
            return None
        else:
            return sorted(objects, key=lambda x: int(x.key))[0]