import io.vertx.core.*;
import io.vertx.core.eventbus.Message;

import java.util.UUID;

/**
 * Created by marutsingh on 2/20/17
 * This class divides the chunk of work in multiple tasks and sends these tasks to verticles
 */
public class MasterWorker extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut) {
       vertx.eventBus().localConsumer("vrp", new Handler<Message<Object>>() {
           @Override
           public void handle(Message<Object> objectMessage) {
               String data =  objectMessage.body().toString();
               String[] work = data.split(",");
               String jobId = UUID.randomUUID().toString();
               vertx.sharedData().getLocalMap("jobCount").putIfAbsent(jobId,work.length);
               for (String w : work){
                   vertx.eventBus().send("work",w + ":" + jobId);
               }
           }
       });
    }
}
