FROM node:21-alpine AS build

WORKDIR /app
COPY package*.json ./
RUN npm install

COPY src /app/src
COPY public /app/public
COPY tsconfig.json /app/tsconfig.json
RUN npm run build

FROM nginx:1.25.4-alpine
COPY conf/nginx.conf /etc/nginx/conf.d/default.conf

RUN addgroup -S mygroup && adduser -S -G mygroup myuser
USER myuser

COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80/tcp
ENTRYPOINT ["nginx", "-g", "daemon off;"]