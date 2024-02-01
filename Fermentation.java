package com.exercise3;

import akka.actor.ActorRef;
import akka.japi.Pair;

import java.util.Map;

public class Fermentation extends WorkstationActor {

    public Fermentation(ActorRef warehouse, ActorRef nextWorkstation, Map<Product, Integer> requiredResources, Map<Product, Integer> currentResources, Pair<Product, Integer> outputResources, double failureProbability, int numSlots, int processingTime) {
        super(warehouse, nextWorkstation, requiredResources, currentResources, outputResources, failureProbability, numSlots, processingTime);
    }
}
