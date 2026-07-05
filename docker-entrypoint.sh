#!/bin/sh
set -e

# Cloud platforms set $PORT; default to Tomcat's standard 8080 for local `docker run`.
PORT="${PORT:-8080}"

sed -i "s/port=\"8080\"/port=\"${PORT}\"/" /usr/local/tomcat/conf/server.xml

exec "$@"
