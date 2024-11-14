FROM nginx:alpine

COPY build /usr/share/nginx/html

# Izlozi port
EXPOSE 80

COPY nginx.conf /etc/nginx/conf.d/default.template

ENTRYPOINT ["/bin/sh", "-c", "envsubst '${BACKEND_URL}' < /etc/nginx/conf.d/default.template > /etc/nginx/conf.d/default.conf && nginx -g 'daemon off;'"]