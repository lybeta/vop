package cn.skyliuyang;

import org.vertx.java.platform.Verticle;

/**
 * Created by liuyang on 14-4-8.
 */
public class MainVerticle extends Verticle {

    @Override
    public void start() {
        super.start();
        container.deployWorkerVerticle("cn.skyliuyang.ServerVerticle");
        container.deployWorkerVerticle("cn.skyliuyang.OAuth2Verticle");
        container.deployWorkerVerticle("cn.skyliuyang.UserVerticle");
    }
}
