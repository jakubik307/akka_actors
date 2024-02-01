package com.exercise3;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class Main {
    public static final int TIME_SCALE = 1000;
    public static void main(String[] args) {
        ActorSystem production = ActorSystem.create("production");

        ActorRef warehouse = production.actorOf(WorkstationActor.props(WorkstationActor.class), "warehouse");
        ActorRef press = production.actorOf(WorkstationActor.props(Press.class), "press");
        ActorRef fermentation = production.actorOf(WorkstationActor.props(Fermentation.class), "fermentation");
        ActorRef filtration = production.actorOf(WorkstationActor.props(Filtration.class), "filtration");
        ActorRef bottling = production.actorOf(WorkstationActor.props(Bottling.class), "bottling");

        //If possible sent
    }
}

