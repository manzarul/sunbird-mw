package org.sunbird.learner;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

import org.apache.log4j.Logger;
import org.sunbird.learner.actors.RequestRouterActor;

/**
 * Created by arvind on 5/5/17.
 * Remote actor Application start point .
 */
public class Application {
   private static final Logger LOGGER = Logger.getLogger(Application.class); 
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
        LOGGER.info("ACTORS STARTED " + learnerActorSelectorRef);
    }
}
