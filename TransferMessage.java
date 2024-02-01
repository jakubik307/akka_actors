package com.exercise3;

import java.util.Map;

public class TransferMessage {
    final Map<Product, Double> resources;

    TransferMessage(Map<Product, Double> resources) {
        this.resources = resources;
    }
}
