package com.exercise3;

import akka.actor.ActorSystem;

public class Main {
    public static final int TIME_SCALE = 1000;

    ActorSystem production = ActorSystem.create("production");
}

