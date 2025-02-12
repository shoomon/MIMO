FROM nginx:latest

RUN chmod 644 /etc/letsencrypt/live/i12a504.p.ssafy.io/fullchain.pem \
    && chmod 644 /etc/letsencrypt/live/i12a504.p.ssafy.io/privkey.pem \
    && chown root:root /etc/letsencrypt/live/i12a504.p.ssafy.io/*

CMD ["nginx", "-g", "daemon off;", "-c", "/etc/nginx/nginx.conf"]
