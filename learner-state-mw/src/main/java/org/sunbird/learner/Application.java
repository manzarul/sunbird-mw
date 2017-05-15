package org.sunbird.learner;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;
import org.sunbird.common.models.util.LogHelper;
import org.sunbird.learner.actors.RequestRouterActor;
import org.sunbird.learner.util.ActorUtility;

/**
 * Created by arvind on 5/5/17.
 * Remote actor Application start point .
 */
public class Application {
    private static LogHelper logger = LogHelper.getInstance(Application.class.getName());
    private static ActorSystem system;

    public static void main(String[] args) {
        startRemoteCreationSystem();
    }
   /**
    * This method will do the basic setup for actors.
    */
    public static void startRemoteCreationSystem(){
        system = ActorSystem.create("RemoteMiddlewareSystem", ConfigFactory.load()
                .getConfig("RemoteMWConfig"));
        ActorRef learnerActorSelectorRef = system.actorOf(Props.create(RequestRouterActor.class),
                "RequestRouterActor");
        logger.info("ACTORS STARTED " + learnerActorSelectorRef);
        checkCassandraConnection();
    }

    private static void checkCassandraConnection() {
        ActorUtility.checkCassandraDbConnections();
    }
}
