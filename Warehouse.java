package com.exercise3;

import akka.actor.AbstractActor;
import akka.actor.Props;

import java.util.HashMap;
import java.util.Map;

public class Warehouse extends AbstractActor {
    private final Map<Product, Double> resources;

    public Warehouse(Map<Product, Double> resources) {
        this.resources = resources;
    }

    static Props props(Map<Product, Double> resources) {
        return Props.create(Warehouse.class, () -> new Warehouse(resources));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(AskMessage.class, this::handleSendResources)
                .match(TransferMessage.class, this::handleReceiveResources)
                .build();
    }

    private void handleSendResources(AskMessage askMessage) {
        Map<Product, Double> resourcesToSend = new HashMap<>();

        for (Map.Entry<Product, Double> entry : askMessage.resources.entrySet()) {
            Product product = entry.getKey();
            Double amount = entry.getValue();
            // If there are enough resources, send them else send what you have
            if (resources.get(product) >= amount) {
                resourcesToSend.put(product, amount);
            } else {
                resourcesToSend.put(product, resources.get(product));
            }
        }

        getSender().tell(new TransferMessage(resourcesToSend), getSelf());

        for (Map.Entry<Product, Double> entry : resourcesToSend.entrySet()) {
            Product product = entry.getKey();
            Double amount = entry.getValue();
            resources.put(product, resources.get(product) - amount);
        }

        System.out.println("Sending resources from warehouse to " + getSender().path().name() + ": " + resourcesToSend + " at " + System.currentTimeMillis());
    }

    private void handleReceiveResources(TransferMessage transferMessage) {
        for (Map.Entry<Product, Double> entry : transferMessage.resources.entrySet()) {
            Product product = entry.getKey();
            Double amount = entry.getValue();
            resources.put(product, resources.get(product) + amount);
        }

        System.out.println("Receiving resources at warehouse from " + getSender().path().name() + ": " + transferMessage.resources + " at " + System.currentTimeMillis());
    }
}
