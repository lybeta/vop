package cn.skyliuyang;

import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.CookieDecoder;
import io.netty.handler.codec.http.ServerCookieEncoder;
import org.vertx.java.busmods.BusModBase;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.MultiMap;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by liuyang on 14-3-25.
 */
public class ServerVerticle extends BusModBase implements Handler<HttpServerRequest> {

    public static final int DEFAULT_PORT = 8888;
    public static final String DEFAULT_HOST = "127.0.0.1";

    @Override
    public void start() {
        super.start();

        int port = getOptionalIntConfig("port", DEFAULT_PORT);
        String host = getOptionalStringConfig("host", DEFAULT_HOST);

        HttpServer server = vertx.createHttpServer();

        server.requestHandler(this);

        server.listen(port, host, new AsyncResultHandler<HttpServer>() {
            @Override
            public void handle(AsyncResult<HttpServer> httpServerAsyncResult) {

                logger.info(" >>> server is started");
            }
        });

    }

    @Override
    public void handle(final HttpServerRequest httpServerRequest) {

        String path = httpServerRequest.path();
        String query = httpServerRequest.query();

        if (path.startsWith("/router")) {
            //路由到各个Verticle处理
            MultiMap paramsMap = httpServerRequest.params();

            String method = paramsMap.get("method");

            if (!method.contains(".")) {
                httpServerRequest.response().sendFile("web/404.html");
            }

            String eventBusAddress = getEventBusAddress(method);
            JsonObject data = new JsonObject();
            data.putValue("paramsMap", paramsMap);

            eb.send(eventBusAddress, data, new Handler<Message<JsonObject>>() {
                @Override
                public void handle(Message<JsonObject> message) {
                    httpServerRequest.response().putHeader("content-type", "application.json; charset=UTF-8");
                    httpServerRequest.response().end(message.body().encode());
                }
            });

        } else if (path.startsWith("/oauth2/")) {
            //路由到应用授权处理
            JsonObject data = new JsonObject();
            JsonObject params = getHttpRequestParams(httpServerRequest.params());
            data.putObject("params", params);

            if ("/oauth2/authorize".equals(path)) {
                data.putString("action", "authorize");
                eb.send("oauth2.address", data, new Handler<Message<JsonObject>>() {
                    @Override
                    public void handle(Message<JsonObject> message) {
                        httpServerRequest.response().setStatusCode(301);
                        httpServerRequest.response().putHeader("Location", message.body().getString("location"));
                        httpServerRequest.response().end();
                    }
                });
            } else if ("/oauth2/access_token".equals(path)) {
                data.putString("action", "access_token");
                eb.send("oauth2.address", data, new Handler<Message<JsonObject>>() {
                    @Override
                    public void handle(Message<JsonObject> message) {
                        httpServerRequest.response().putHeader("content-type", "application.json; charset=UTF-8");
                        httpServerRequest.response().end(message.body().encode());
                    }
                });
            }

        } else if ("/login".equals(path)) {
            JsonObject data = new JsonObject();
            JsonObject params = getHttpRequestParams(httpServerRequest.params());
            data.putObject("params", params);
            data.putString("method", "user.login");
            eb.send("user.address", data, new Handler<Message<JsonObject>>() {
                @Override
                public void handle(Message<JsonObject> message) {
                    String sessionId = message.body().getString("session_id");
                    httpServerRequest.response().putHeader("Set-Cookie", ServerCookieEncoder.encode("VOPSESSIONID", sessionId));
                    httpServerRequest.response().end();

                }
            });
        } else if ("/logout".equals(path)) {
            JsonObject params = getHttpRequestParams(httpServerRequest.params());
            String value = httpServerRequest.headers().get("Cookie");
            Set<Cookie> cookies = CookieDecoder.decode(value);
            for (final Cookie cookie : cookies) {
                if ("VOPSESSIONID".equals(cookie.getName())) {
                    String sessionId = cookie.getValue();
                    JsonObject data = new JsonObject();
                    params.putString("session_id", sessionId);
                    data.putString("method", "user.logout");
                    data.putObject("params", params);
                    eb.send("user.address", data, new Handler<Message<JsonObject>>() {
                        @Override
                        public void handle(Message<JsonObject> message) {
                            httpServerRequest.response().end();

                        }
                    });
                    return;
                }
            }
        } else if (!path.contains("src/test")) {
            //路由到页面等
            String file = "web" + path;
            httpServerRequest.response().sendFile(file, "web/404.html");
        }
    }

    private JsonObject getHttpRequestParams(MultiMap params) {
        JsonObject data = new JsonObject();

        Iterator<Map.Entry<String, String>> iter = params.entries().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            data.putString(entry.getKey(), entry.getValue());
            iter.remove();
        }

        return data;
    }

    private String getEventBusAddress(String method) {
        String address = "";

        String methodGroup = method.split(".")[0];
        switch (methodGroup) {
            case "user":
                address = "user.address";
                break;
            case "app":
                address = "app.address";
                break;
            case ""://TODO 完善各个address
                address = "";
                break;
            default:
                address = "";
                break;

        }

        return address;
    }


}
