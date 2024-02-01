package com.exercise3;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.Pair;

import java.util.HashMap;
import java.util.Map;

public abstract class WorkstationActor extends AbstractActor {
    protected ActorRef warehouse;
    protected ActorRef nextWorkstation;
    protected Map<Product, Double> requiredResources;
    protected Map<Product, Double> currentResources;
    protected Pair<Product, Double> outputResources;
    protected double failureProbability;
    protected int currentSlots;
    protected int processingTime;

    public WorkstationActor(ActorRef warehouse, ActorRef nextWorkstation, Map<Product, Double> requiredResources, Pair<Product, Double> outputResources, double failureProbability, int numSlots, int processingTime) {
        this.warehouse = warehouse;
        this.nextWorkstation = nextWorkstation;
        this.requiredResources = requiredResources;
        this.currentResources = new HashMap<>();
        this.outputResources = outputResources;
        this.failureProbability = failureProbability;
        this.currentSlots = numSlots;
        this.processingTime = processingTime / Main.TIME_SCALE;
    }

    static Props props (ActorRef warehouse, ActorRef nextWorkstation, Map<Product, Double> requiredResources, Pair<Product, Double> outputResources, double failureProbability, int numSlots, int processingTime) {
        return Props.create(WorkstationActor.class, () -> new WorkstationActor(warehouse, nextWorkstation, requiredResources, outputResources, failureProbability, numSlots, processingTime) {
        });
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(Pair.class, this::handleTransfer)
                .match(StartMessage.class, this::handleStart)
                .build();
    }

    protected void handleTransfer(Pair<Product, Double> products) {
        if (currentResources.containsKey(products.first())) {
            currentResources.put(products.first(), currentResources.get(products.first()) + products.second());
        } else {
            currentResources.put(products.first(), products.second());
        }

        // If there are free slots and enough resources, start processing
        if (currentSlots > 0) {

            // if there are not enough resources, ask the warehouse for them and wait for the response then check again
            if (!hasEnoughResources()) {
                warehouse.tell(new AskMessage(requiredResources), getSelf());
            }

            // If there are enough resources, start processing
            if (hasEnoughResources()) {
                for (Map.Entry<Product, Double> entry : requiredResources.entrySet()) {
                    Product product = entry.getKey();
                    Double amount = entry.getValue();
                    currentResources.put(product, currentResources.get(product) - amount);
                }
            }

            simulateProcessing();

            if (Math.random() > failureProbability) {
                sendToNextWorkstation(outputResources);
            }

        } else {
            // If there are no free slots wait for the slot to be free
            System.out.println("No free slots at " + getClass() + " at " + System.currentTimeMillis() + " ms");
        }
    }

    private boolean hasEnoughResources() {
        for (Map.Entry<Product, Double> entry : requiredResources.entrySet()) {
            Product product = entry.getKey();
            Double amount = entry.getValue();
            if (!currentResources.containsKey(product) || currentResources.get(product) < amount) {
                return false;
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

        System.out.println("Processed " + requiredResources + " at " + getClass().getName() + " at " + System.currentTimeMillis() + " ms");

        currentSlots++;
    }

    private void sendToNextWorkstation(Pair<Product, Double> products) {
        nextWorkstation.tell(products, getSelf());
        System.out.println("Sent " + products.second() + " " + products.first() + " to " + nextWorkstation.getClass().getName() + "from" + getClass().getName() + " at " + System.currentTimeMillis() + " ms");
    }

    private void handleStart(StartMessage startMessage) {
        handleTransfer(new Pair<>(Product.grapes, 0.0));
    }
}
