
import io.vertx.core.*;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.impl.MessageImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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

               List<Future> futureList = new ArrayList<>();

               for (String w : work){
                   Future<Message<Object>> f1 = Future.future();
                   futureList.add(f1);
                   vertx.eventBus().send("work",w + ":" + jobId,f1.completer());
               }
               StringBuffer resultSet = new StringBuffer();

               CompositeFuture.all(futureList).setHandler(ar -> {
                   if (ar.succeeded()) {
                       ar.result().list().forEach((result) -> resultSet.append(((MessageImpl) result).body().toString()));
                       System.out.println(resultSet.toString());
                       // All succeeded
                   } else {
                       // All completed and at least one failed
                   }
               });
           }
       });
    }
}
