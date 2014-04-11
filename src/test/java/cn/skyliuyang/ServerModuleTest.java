package cn.skyliuyang;

import org.junit.After;
import org.junit.Test;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.testtools.TestVerticle;

import static org.junit.Assert.assertTrue;

/**
 * Created by liuyang on 14-3-25.
 */
public class ServerModuleTest extends TestVerticle {

    @Override
    public void start() {
        initialize();
        container.deployModule(System.getProperty("vertx.modulename"), new AsyncResultHandler<String>() {
            @Override
            public void handle(AsyncResult<String> asyncResult) {

                if (!asyncResult.succeeded()) {
                    asyncResult.cause().printStackTrace();
                }
                assertTrue(asyncResult.succeeded());
            }
        });
        startTests();

    }

    @Test
    public void testSome() throws Exception {


    }

    @After
    public void tearDown() throws Exception {


    }


}
