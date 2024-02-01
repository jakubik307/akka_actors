package com.exercise3;

import java.util.Map;

public class TransferMessage {
    final Map<Product, Integer> resources;

    TransferMessage(Map<Product, Integer> resources) {
        this.resources = resources;
    }
}
