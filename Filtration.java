package com.exercise3;

import akka.actor.ActorRef;
import akka.japi.Pair;

import java.util.Map;

public class Filtration extends WorkstationActor {

    public Filtration(ActorRef warehouse, ActorRef nextWorkstation, Map<Product, Double> requiredResources, Pair<Product, Double> outputResources, double failureProbability, int numSlots, int processingTime) {
        super(warehouse, nextWorkstation, requiredResources, outputResources, failureProbability, numSlots, processingTime);
    }
}
