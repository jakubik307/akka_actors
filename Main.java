package com.exercise3;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.japi.Pair;

import java.util.Map;

public class Main {
    public static final int TIME_SCALE = 1000;
    public static void main(String[] args) {
        ActorSystem production = ActorSystem.create("production");

        ActorRef warehouse = production.actorOf(Warehouse.props(Map.of(
                Product.grapes, 1000.0,
                Product.bottles, 1000.0,
                Product.sugar, 1000.0,
                Product.bottle_of_wine, 1000.0
        )), "warehouse");

        ActorRef bottling = production.actorOf(Bottling.props(warehouse, warehouse, Map.of(
                Product.filtered_wine, 0.75,
                Product.bottles, 1.0
        ), Pair.create(Product.bottle_of_wine, 1.0), 0.05, 1, 5), "bottling");

        ActorRef filtration = production.actorOf(Filtration.props(warehouse, bottling, Map.of(
                Product.unfiltered_wine, 25.0
        ), Pair.create(Product.filtered_wine, 24.0), 0.0, 10, 720), "filtration");

        ActorRef fermentation = production.actorOf(Fermentation.props(warehouse, filtration, Map.of(
                Product.grape_juice, 15.0,
                Product.water, 8.0,
                Product.sugar, 2.0
        ), Pair.create(Product.unfiltered_wine, 25.0), 0.05, 10, 20160), "fermentation");

        ActorRef press = production.actorOf(Press.props(warehouse, fermentation, Map.of(
                Product.grapes, 15.0
        ), Pair.create(Product.grape_juice, 10.0), 0.0, 1, 720), "press");

        while (true) {
            press.tell(new StartMessage(), ActorRef.noSender());
            try {
                Thread.sleep(TIME_SCALE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


