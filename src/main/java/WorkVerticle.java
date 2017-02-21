import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

/**
 * Created by marutsingh on 2/20/17.
 */
public class WorkVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut) {
        final String verticleId = super.deploymentID();

        vertx.eventBus().localConsumer("work", new Handler<Message<Object>>() {
            @Override
            public void handle(Message<Object> objectMessage) {
                String[] data =  objectMessage.body().toString().split(":");
                String work = data[0];
                String jobId = data[1];

                String result = work + "Completed***";
                //System.out.println(result);
/*
                Integer jobCount =
                        Integer.parseInt(getVertx().sharedData().getLocalMap("jobCount").get(jobId).toString());
                String event = String.format("%s:%d:%s",jobId,jobCount,result);*/

                objectMessage.reply(result);
                //vertx.eventBus().publish("resultCombiner",event);
            }
        });
    }
}
