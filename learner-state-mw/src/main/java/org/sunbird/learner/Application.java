package org.sunbird.learner;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;
import org.sunbird.learner.actors.CourseEnrollmentActor;
import org.sunbird.learner.actors.LearnerStateActor;
import org.sunbird.learner.actors.LearnerStateUpdateActor;

/**
 * Created by arvind on 5/5/17.
 */
public class Application {

    private static ActorSystem system;

    public static void main(String[] args) {

        startRemoteCreationSystem();

    }

    public static void startRemoteCreationSystem(){

        system = ActorSystem.create("RemoteMiddlewareSystem", ConfigFactory.load()
                .getConfig("RemoteMWConfig"));
        ActorRef learnerStateActorRef = system.actorOf(Props.create(LearnerStateActor.class),
                "LearnerStateActor");
        ActorRef learnerStateUpdateActorRef = system.actorOf(Props.create(LearnerStateUpdateActor.class),
                "LearnerStateUpdateActor");
        ActorRef courseEnrollmentActorRef = system.actorOf(Props.create(CourseEnrollmentActor.class),
                "CourseEnrollmentActor");
        System.out.println("ACTORS STARTED");

    }
}
