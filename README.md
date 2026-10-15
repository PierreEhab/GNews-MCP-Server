# GNews MCP Server

A Java MCP server that exposes the GNews search and top-headlines endpoints as tools over stdio.
It uses the official MCP Java SDK and requires Java 17 or later and Maven.

The implementation separates environment configuration, GNews HTTP access, MCP tool definitions,
and server startup under `src/main/java/com/example/gnews`.

## Configure and run

Set `GNEWS_API_KEY` to an API key from [GNews](https://gnews.io/dashboard), then start the server:

```powershell
$env:GNEWS_API_KEY = "your-api-key"
mvn compile exec:java
```

The server communicates over stdin/stdout, so configure your MCP client to launch Maven in this
project directory with `compile exec:java` as its arguments and `GNEWS_API_KEY` in its environment.
Do not print logs to stdout when using the stdio transport.

## Debug with MCP Inspector

Install Node.js 22.19 or later and build the runnable jar:

```powershell
mvn package
```

Then set your GNews key and start the Inspector with the Java server command:

```powershell
$env:GNEWS_API_KEY = "your-api-key"
npx -y @modelcontextprotocol/inspector -e "GNEWS_API_KEY=$env:GNEWS_API_KEY" java -jar target/gnews-mcp-server-1.0.0.jar
```

The Inspector opens its web UI and starts the MCP server over stdio. Use **Tools** to list
`search_news` and `get_top_headlines`, then call either tool and inspect its result. Keep the API
key in the environment; pass it to Inspector with `-e`, because Inspector does not automatically
forward the parent shell's environment variables to the server process. Inspector 2.x requires
Node.js 22.19 or later.

## Tools

- `search_news`: required `q`; supports `lang`, `country`, `max`, `in`, `nullable`, `from`, `to`,
  `sortby`, `page`, and `truncate`.
- `get_top_headlines`: supports `category`, `lang`, `country`, `max`, `q`, `nullable`, `from`, `to`,
  `page`, and `truncate`.

Successful tool calls return GNews's JSON response. API failures and invalid date-time arguments are
reported as MCP tool errors. Article limits and date ranges remain subject to the configured GNews
subscription plan.
