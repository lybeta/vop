package cn.skyliuyang;

import org.junit.Test;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.testtools.TestVerticle;

import static org.vertx.testtools.VertxAssert.*;

/**
 * Created by liuyang on 14-3-26.
 */
public class OAuth2ModuleTest extends TestVerticle {
    @Override
    public void start() {
        initialize();

        container.logger().info("[OAuth2Verticle] module name : " + System.getProperty("vertx.modulename"));

        container.deployModule(System.getProperty("vertx.modulename"),
                new AsyncResultHandler<String>() {
                    @Override
                    public void handle(AsyncResult<String> asyncResult) {


                        if (!asyncResult.succeeded()) asyncResult.cause().printStackTrace();

                        assertTrue(asyncResult.succeeded());
                        assertNotNull("deploymentID should not be null",
                                asyncResult.result());
                        startTests();
                    }
                }
        );

    }

    @Test
    public void testAuthorize() {
        container.logger().info("[OAuth2Verticle] authorize");

        JsonObject data = new JsonObject();
        data.putString("action", "authorize");
        JsonObject params = new JsonObject();
        params.putString("user_id", "lybeta");
        params.putString("password", "123456");
        params.putString("app_key", "appone");
        params.putString("redirect_uri", "http://www.baidu.com");

        data.putObject("params", params);

        vertx.eventBus().send("oauth2.address", data, new Handler<Message<JsonObject>>() {

            @Override
            public void handle(Message<JsonObject> reply) {

                String location = reply.body().getString("location");
                container.logger().info(reply.body());
                assertEquals("the status of message replied must be 'ok'. ", "ok", reply.body().getString("status"));

                testComplete();
            }
        });
    }

    @Test
    public void testAccessToken() {
        container.logger().info("[OAuth2Verticle] accessToken");

        JsonObject data = new JsonObject();
        data.putString("action", "access_token");
        JsonObject params = new JsonObject();
        params.putString("app_key", "apptwo");
        params.putString("code", "952864b5cc7e4a35befb87832c6978e1");
        params.putString("sign", "c58c66eb19b1db5e256e7380703021b6");

        data.putObject("params", params);

        vertx.eventBus().send("oauth2.address", data, new Handler<Message<JsonObject>>() {

            @Override
            public void handle(Message<JsonObject> reply) {

                String location = reply.body().getString("location");
                container.logger().info(reply.body());
                assertEquals("the status of message replied must be 'ok'. ", "ok", reply.body().getString("status"));

                testComplete();
            }
        });
    }
}
