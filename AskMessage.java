package com.exercise3;

import java.util.Map;

public class AskMessage {
    final Map<Product, Double> resources;

    AskMessage(Map<Product, Double> resources) {
        this.resources = resources;
    }
}
