package cn.skyliuyang;

import cn.skyliuyang.entity.User;
import cn.skyliuyang.service.UserService;
import cn.skyliuyang.service.impl.UserServiceImpl;
import cn.skyliuyang.util.MongoDBUtil;
import org.vertx.java.busmods.BusModBase;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Future;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

/**
 * Created by liuyang on 14-4-2.
 */
public class UserVerticle extends BusModBase implements Handler<Message<JsonObject>> {

    private UserService userService = new UserServiceImpl();
    private String address;

    @Override
    public void start(Future<Void> startedResult) {
        super.start(startedResult);

        MongoDBUtil.getInstance().connect();

        address = getOptionalStringConfig("user.address", "user.address");

        eb.registerHandler(address, this, new AsyncResultHandler<Void>() {
            @Override
            public void handle(AsyncResult<Void> voidAsyncResult) {

                logger.info(" >>> User is started");
            }
        });
    }

    @Override
    public void stop() {
        MongoDBUtil.getInstance().disconnect();
    }

    @Override
    public void handle(Message<JsonObject> jsonObjectMessage) {
        JsonObject params = jsonObjectMessage.body().getObject("params");
        String method;
        if (params.containsField("method")) {
            method = params.getString("method");
        } else {
            method = jsonObjectMessage.body().getString("method");
        }
        String sessionId = "";
        switch (method) {
            case "user.login":
                sessionId = login(params);
                if ("".equals(sessionId)) {
                    sendError(jsonObjectMessage, "login failed");
                } else {
                    sendOK(jsonObjectMessage, new JsonObject().putString("session_id", sessionId));
                }
                break;
            case "user.logout":
                logout(params);
                sendOK(jsonObjectMessage);
                break;
            case "user.register":
                sessionId = register(params);
                if ("".equals(sessionId)) {
                    sendError(jsonObjectMessage, "register failed");
                } else {
                    sendOK(jsonObjectMessage, new JsonObject().putString("sessionId", sessionId));
                }
                break;
            case "user.update":
                update(params);
                sendOK(jsonObjectMessage);
                break;
            case "user.get":
                JsonObject data = get(params);
                if (data != null) {
                    sendOK(jsonObjectMessage, data);
                } else {
                    sendError(jsonObjectMessage, "user not exit");
                }
                break;
            default:
                sendError(jsonObjectMessage, "method can not be valid");
                break;

        }

    }

    private String login(JsonObject params) {
        return userService.login(User.toObject(params.toMap()));
    }

    private void logout(JsonObject params) {
        userService.logout(params.getString("session_id"));
    }

    private String register(JsonObject params) {
        userService.create(User.toObject(params.toMap()));
        return login(params);
    }

    private void update(JsonObject params) {
        userService.update(User.toObject(params.toMap()));
    }

    private JsonObject get(JsonObject params) {
        User user = userService.get(params.getString("user_id"));
        JsonObject data = new JsonObject();
        data.putString("user_id", user.getUserId());
        data.putString("about", user.getAbout());
        return data;
    }
}
