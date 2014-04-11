package cn.skyliuyang;

import cn.skyliuyang.entity.Code;
import cn.skyliuyang.entity.Session;
import cn.skyliuyang.entity.User;
import cn.skyliuyang.service.AppService;
import cn.skyliuyang.service.CodeService;
import cn.skyliuyang.service.SessionService;
import cn.skyliuyang.service.UserService;
import cn.skyliuyang.service.impl.AppServiceImpl;
import cn.skyliuyang.service.impl.CodeServiceImpl;
import cn.skyliuyang.service.impl.SessionServiceImpl;
import cn.skyliuyang.service.impl.UserServiceImpl;
import cn.skyliuyang.util.MongoDBUtil;
import cn.skyliuyang.util.StringUtil;
import cn.skyliuyang.util.URLUtil;
import cn.skyliuyang.util.UUIDUtil;
import org.vertx.java.busmods.BusModBase;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

/**
 * Created by liuyang on 14-3-26.
 */
public class OAuth2Verticle extends BusModBase implements Handler<Message<JsonObject>> {

    private AppService appService = new AppServiceImpl();
    private CodeService codeService = new CodeServiceImpl();
    private UserService userService = new UserServiceImpl();
    private SessionService sessionService = new SessionServiceImpl();

    private String address;

    @Override
    public void start() {
        super.start();

        MongoDBUtil.getInstance().connect();

        address = getOptionalStringConfig("oauth2.address", "oauth2.address");

        eb.registerHandler(address, this, new AsyncResultHandler<Void>() {
            @Override
            public void handle(AsyncResult<Void> voidAsyncResult) {

                logger.info(" >>> OAuth2 is started");
            }
        });
    }

    @Override
    public void stop() {
        MongoDBUtil.getInstance().disconnect();
    }

    @Override
    public void handle(Message<JsonObject> jsonObjectMessage) {
        String action = jsonObjectMessage.body().getString("action");
        JsonObject params = jsonObjectMessage.body().getObject("params");
        String appKey = URLUtil.decode(params.getString("app_key"));//TODO app验证

        if ("authorize".equals(action)) {
            //用户同意授权，生成code并重定向到redirect_uri
            String userId = URLUtil.decode(params.getString("user_id"));
            container.logger().info(userId);
            String password = URLUtil.decode(params.getString("password"));
            String sessionId = userService.login(new User(userId, password));
            if ("".equals(sessionId)) {
                sendError(jsonObjectMessage, "user_id or password is wrong.");
            } else {
                sessionService.create(new Session(sessionId, 3600 * 24 * 30, userId));
            }

            String code = generateAuthorizeCode();
            codeService.create(new Code(code, appKey, sessionId));

            String redirectUri = URLUtil.decode(params.getString("redirect_uri"));

            JsonObject data = new JsonObject();
            data.putString("location", "http://" + redirectUri + "/?code=" + code);

            if (StringUtil.isEmpty(userId) || StringUtil.isEmpty(appKey)) {
                sendError(jsonObjectMessage, "user or app not exited");
            } else {
                sendOK(jsonObjectMessage, data);
            }


        } else if ("access_token".equals(action)) {
            //app凭code换取access_token
            String code = URLUtil.decode(params.getString("code"));
            if (!appService.checkSign(params.toMap())) {
                sendError(jsonObjectMessage, "app not exited");
            } else if (!codeService.checkCode(code, appKey)) {
                sendError(jsonObjectMessage, "code not exited");
            } else {
                //发放令牌
                String accessToken = codeService.getAccessToken(code);
                JsonObject data = new JsonObject();
                data.putString("access_token", accessToken);
                sendOK(jsonObjectMessage, data);
            }

        }
    }


    private String generateAuthorizeCode() {
        return UUIDUtil.generateUUID();
    }
}
