type Method = "get" | "post" | "put" | "del";

export class Api {
  private token = "";

  constructor(token: string = "") {
    this.token = token;
  }

  private async makeRequest(
    url: string,
    method: Method,
    body: Record<string, any> = {}
  ) {
    const authHeader = this.token != "" ? `Bearer ${this.token}` : "";

    const options: RequestInit = {
      method,
      headers: {
        "Content-Type": "application/json",
        Authorization: authHeader,
      },
    };

    if (method === "post" || method === "put") {
      options.body = JSON.stringify(body);
    }

    const result = await fetch(
      `${import.meta.env.VITE_SERVER_URL}${url}`,
      options
    );
    return result.json();
  }

  get(url: string) {
    return this.makeRequest(url, "get");
  }

  post(url: string, body: Record<string, any>) {
    return this.makeRequest(url, "post", body);
  }

  put(url: string, body: Record<string, any>) {
    return this.makeRequest(url, "put", body);
  }

  del(url: string) {
    return this.makeRequest(url, "del");
  }
}
