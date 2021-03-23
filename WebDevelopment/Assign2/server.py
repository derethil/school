import mimetypes
import time
from pathlib import Path
from http.server import HTTPServer, BaseHTTPRequestHandler


class CS2610Assn1(BaseHTTPRequestHandler):

    def do_GET(self):
        resource_path = self.get_resource_path()

        if resource_path.startswith("bio"):
            self.redirect("http://localhost:8000/about.html")

        elif resource_path in ["tips", "help"]:
            self.redirect("http://localhost:8000/techtips+css.html")

        elif resource_path == "debugging.html":
            data = self.get_debugging_page()
            self.create_response(200, resource_path, data)

        elif resource_path == "teapot.html":
            data = self.get_data(resource_path)
            self.create_response(218, resource_path, data)

        elif resource_path == "forbidden.html":
            data = self.get_data(resource_path)
            self.create_response(403, resource_path, data)

        elif Path(resource_path).is_file():
            data = self.get_data(resource_path)
            self.create_response(200, resource_path, data)

        else:
            data = self.get_data("404.html")
            self.create_response(404, "404.html", data)

    def create_header(self, status):
        self.send_response(status) # send_response by default includes Server and Date headers
        self.send_header("Connection", "close")
        self.send_header("Cache-Control", f"max-age={5}")

    def create_response(self, status, resource_path, data):
        self.create_header(status)
        self.send_header("Content-type", mimetypes.guess_type(resource_path)[0])
        self.send_header("Content-length", len(data))
        self.end_headers()
        self.wfile.write(data)

    def redirect(self, location):
        self.create_header(301)
        self.send_header('Location', location)
        self.end_headers()

    def get_data(self, filepath):
        with open(filepath, "rb") as file:
            return file.read()

    def get_resource_path(self):
        HTML_PAGES = ["index", "about", "techtips-css", "techtips+css", "debugging", "teapot", "forbidden"]
        possible = sum(([page + ".html", page] for page in HTML_PAGES), [])
        filepath = self.path[1:]

        if filepath == "":
            filepath = "index.html"
        elif filepath in possible and not filepath.endswith(".html"):
            filepath += ".html"

        return filepath

    def get_debugging_page(self):
        return bytes(f"""
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="utf-8">
                <title>Debugging</title>
                <link rel="stylesheet" href="style.css"/>
            </head>
            <body>
                <h1>Server Debugging Page</h1>
                <p>You are visiting <b>{self.path}</b> from the IP address <b>{self.client_address[0]}</b>, port number <b>{self.client_address[1]}</b>.</p>
                <p>It is now {time.strftime(f"%c")}</p>

                <ul>
                    <li>Command: {self.command}</li>
                    <li>Path: {self.path}</li>
                    <li>Request Version: {self.request_version}</li>
                    <li>Version String: {self.version_string()}</li>
                </ul>

                <h3>Request headers</h3>

                <ol>
                    <li><b>Host</b>: {self.headers["Host"]}</li>
                    <li><b>User-Agent</b>: {self.headers["User-Agent"]}</li>
                    <li><b>Accept</b>: {self.headers["Accept"]}</li>
                    <li><b>Accept-Language</b>: {self.headers["Accept-Language"]}</li>
                    <li><b>Accept-Encoding</b>: {self.headers["Accept-Encoding"]}</li>
                    <li><b>Referer</b>: {self.headers["Referer"]}</li>
                    <li><b>DNT</b>: {self.headers["DNT"]}</li>
                    <li><b>Connection</b>: {self.headers["Connection"]}</li>
                </ol>

                <a href="index">Back to home page</a>
            </body>
            </html>
        """, encoding="utf-8")




if __name__ == '__main__':
    server_address = ('localhost', 8000)
    print(f"Serving from http://{server_address[0]}:{server_address[1]}")
    print("Press Ctrl-C to quit\n")
    try:
        HTTPServer(server_address, CS2610Assn1).serve_forever()
    except KeyboardInterrupt:
        print(" Exiting")
        exit(0)