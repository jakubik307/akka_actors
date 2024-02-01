package com.exercise3;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.Pair;

import java.util.Map;

public abstract class WorkstationActor extends AbstractActor {
    protected ActorRef warehouse;
    protected ActorRef nextWorkstation;
    protected Map<Product, Integer> requiredResources;
    protected Map<Product, Integer> currentResources;
    protected Pair<Product, Integer> outputResources;
    protected double failureProbability;
    protected int currentSlots;
    protected int processingTime;

    public WorkstationActor(ActorRef warehouse, ActorRef nextWorkstation, Map<Product, Integer> requiredResources, Map<Product, Integer> currentResources, Pair<Product, Integer> outputResources, double failureProbability, int numSlots, int processingTime) {
        this.warehouse = warehouse;
        this.nextWorkstation = nextWorkstation;
        this.requiredResources = requiredResources;
        this.currentResources = currentResources;
        this.outputResources = outputResources;
        this.failureProbability = failureProbability;
        this.currentSlots = numSlots;
        this.processingTime = processingTime / Main.TIME_SCALE;
    }

    static Props props(Class<? extends WorkstationActor> workstationClass) {
        return Props.create(workstationClass);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(Pair.class, this::handleTransfer).build();
    }

    private void handleTransfer(Pair<Product, Integer> products) {
        currentResources.put(products.first(), currentResources.get(products.first()) + products.second());

        // If there are free slots and enough resources, start processing
        if (currentSlots > 0 && hasEnoughResources()) {

            simulateProcessing();

            if (Math.random() > failureProbability) {
                sendToNextWorkstation(outputResources);
            }

        } else {
            // If there are no free slots, send the resources to current resources and wait for the slot to be free

        }
    }

    private boolean hasEnoughResources() {
        for (Map.Entry<Product, Integer> entry : requiredResources.entrySet()) {
            if (currentResources.get(entry.getKey()) < entry.getValue()) {
                AskMessage askMessage = new AskMessage(requiredResources);
                warehouse.tell(askMessage, getSelf());
            }
        }
        return true;
    }

    private synchronized void simulateProcessing() {
        currentSlots--;

        try {
            Thread.sleep(processingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        currentSlots++;
    }

    private void sendToNextWorkstation(Pair<Product, Integer> products) {
        nextWorkstation.tell(products, getSelf());
        System.out.println("Sent " + products.second() + " " + products.first() + " to " + nextWorkstation + "from" + getSelf() + " at " + System.currentTimeMillis() + " ms");
    }
}
