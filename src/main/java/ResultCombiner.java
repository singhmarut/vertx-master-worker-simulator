import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by marutsingh on 2/20/17.
 * Combines the result
 */
public class ResultCombiner extends AbstractVerticle {

    Map<String,Integer> jobCount = new HashMap<>();

    Map<String,StringBuffer> jobResult = new HashMap<>();

    @Override
    public void start(Future<Void> fut) {

        vertx.eventBus().consumer("resultCombiner", new Handler<Message<Object>>() {
            @Override
            public void handle(Message<Object> objectMessage) {

                String[] event = objectMessage.body().toString().split(":");
                String jobId = event[0];
                String jobCount = event[1];
                String result = event[2];


                if (!ResultCombiner.this.jobCount.containsKey(jobId)) {
                    ResultCombiner.this.jobCount.put(jobId, 1);
                } else {
                    //Increase the count
                    ResultCombiner.this.jobCount.put(jobId, ResultCombiner.this.jobCount.get(jobId) + 1);
                }

                if (!jobResult.containsKey(jobId)) {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append(result);
                    jobResult.put(jobId,stringBuffer);
                } else {
                    jobResult.get(jobId).append("\r\n" + result);
                }

                if (ResultCombiner.this.jobCount.get(jobId) == Integer.parseInt(jobCount)) {
                    System.out.println("Job processed are " + jobCount);
                   System.out.println(jobResult.get(jobId).toString());
                }
            }
        });
    }
}
