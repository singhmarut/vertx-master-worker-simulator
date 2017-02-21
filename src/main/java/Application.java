import io.vertx.core.Vertx;

/**
 * Created by marutsingh on 2/20/17.
 */
public class Application {

    public static void main(String[] args){

        final Vertx vertx = Vertx.vertx();
        //vertx.deployVerticle(new HttpVerticle());
        vertx.deployVerticle(new MasterWorker());

        //DeploymentOptions options = new DeploymentOptions().setInstances(10);
        //vertx.deployVerticle("WorkVerticle",options);
        for (int i = 0; i < 5; i++){
            vertx.deployVerticle("WorkVerticle");
        }

        vertx.deployVerticle(new ResultCombiner());
        vertx.eventBus().publish("vrp", "Job1,Job2,Job3,Job4,Job5,Job6,Job7,Job8,Job9,Job10");
        System.out.println("Deployment done");
    }
}
