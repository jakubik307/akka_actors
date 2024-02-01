package com.exercise3;

import akka.actor.ActorRef;
import akka.japi.Pair;

import java.util.Map;

public class Fermentation extends WorkstationActor {

    public Fermentation(ActorRef warehouse, ActorRef nextWorkstation, Map<Product, Double> requiredResources, Pair<Product, Double> outputResources, double failureProbability, int numSlots, int processingTime) {
        super(warehouse, nextWorkstation, requiredResources, outputResources, failureProbability, numSlots, processingTime);
    }
}
