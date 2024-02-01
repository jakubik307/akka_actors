package com.exercise3;

import java.util.Map;

public class AskMessage {
    final Map<Product, Integer> resources;

    AskMessage(Map<Product, Integer> resources) {
        this.resources = resources;
    }
}
