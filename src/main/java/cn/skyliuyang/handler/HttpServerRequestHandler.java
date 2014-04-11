package cn.skyliuyang.handler;

import org.vertx.java.core.Handler;
import org.vertx.java.core.MultiMap;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by liuyang on 14-3-25.
 */
public class HttpServerRequestHandler implements Handler<HttpServerRequest> {


    private final EventBus eventBus;

    public HttpServerRequestHandler(EventBus eventBus) {
        this.eventBus = eventBus;
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
                // TODO 处理method异常
            }

            String eventBusAddress = getEventBusAddress(method);
            JsonObject data = new JsonObject();
            data.putValue("paramsMap", paramsMap);

            eventBus.send(eventBusAddress, data, new Handler<Message<JsonObject>>() {
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

            if ("/oauth2/authorize".equals(path)) {
                data.putString("action", "authorize");
                data.putObject("params", params);
                eventBus.send("oauth2.address", data, new Handler<Message<JsonObject>>() {
                    @Override
                    public void handle(Message<JsonObject> message) {
                        httpServerRequest.response().setStatusCode(301);
                        httpServerRequest.response().putHeader("Location", message.body().getString("location"));
                        httpServerRequest.response().end();
                    }
                });
            } else if ("/oauth2/access_token".equals(path)) {
                data.putString("action", "access_token");
                eventBus.send("oauth2.address", data, new Handler<Message<JsonObject>>() {
                    @Override
                    public void handle(Message<JsonObject> message) {
                        httpServerRequest.response().putHeader("content-type", "application.json; charset=UTF-8");
                        httpServerRequest.response().end(message.body().encode());
                    }
                });
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
        if (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            data.putString(entry.getKey(), entry.getValue());
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
