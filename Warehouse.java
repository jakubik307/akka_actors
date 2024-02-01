package com.exercise3;

import akka.actor.AbstractActor;

import java.util.HashMap;
import java.util.Map;

public class Warehouse extends AbstractActor {
    private final Map<Product, Integer> resources;

    public Warehouse(Map<Product, Integer> resources) {
        this.resources = resources;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(AskMessage.class, this::handleSendResources)
                .match(TransferMessage.class, this::handleReceiveResources)
                .build();
    }

    private void handleSendResources(AskMessage askMessage) {
        Map<Product, Integer> resourcesToSend = new HashMap<>();

        for (Map.Entry<Product, Integer> entry : askMessage.resources.entrySet()) {
            Product product = entry.getKey();
            Integer amount = entry.getValue();
            // If there are enough resources, send them else send what you have
            if (resources.get(product) >= amount) {
                resourcesToSend.put(product, amount);
            } else {
                resourcesToSend.put(product, resources.get(product));
            }
        }

        getSender().tell(new TransferMessage(resourcesToSend), getSelf());

        for (Map.Entry<Product, Integer> entry : resourcesToSend.entrySet()) {
            Product product = entry.getKey();
            Integer amount = entry.getValue();
            resources.put(product, resources.get(product) - amount);
        }

        System.out.println("Sending resources from warehouse to " + getSender().path().name() + ": " + resourcesToSend + " at " + System.currentTimeMillis());
    }

    private void handleReceiveResources(TransferMessage transferMessage) {
        for (Map.Entry<Product, Integer> entry : transferMessage.resources.entrySet()) {
            Product product = entry.getKey();
            Integer amount = entry.getValue();
            resources.put(product, resources.get(product) + amount);
        }

        System.out.println("Receiving resources at warehouse from " + getSender().path().name() + ": " + transferMessage.resources + " at " + System.currentTimeMillis());
    }
}
