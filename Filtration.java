package com.exercise3;

import akka.actor.ActorRef;
import akka.japi.Pair;

import java.util.Map;

public class Filtration extends WorkstationActor {

    public Filtration(ActorRef warehouse, ActorRef nextWorkstation, Map<Product, Integer> requiredResources, Map<Product, Integer> currentResources, Pair<Product, Integer> outputResources, double failureProbability, int numSlots, int processingTime) {
        super(warehouse, nextWorkstation, requiredResources, currentResources, outputResources, failureProbability, numSlots, processingTime);
    }
}
