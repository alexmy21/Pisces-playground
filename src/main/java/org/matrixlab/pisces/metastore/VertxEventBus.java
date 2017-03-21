/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.matrixlab.pisces.metastore;

/**
 *
 * @author alexmy
 */
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import org.matrixlab.pisces.metastore.core.CustomMessage;
import org.matrixlab.pisces.metastore.core.CustomMessageCodec;
import org.matrixlab.pisces.metastore.core.Runner;
//import io.vertx.example.util.Runner;

/**
 * Publisher
 *
 * @author Junbong
 */
public class VertxEventBus extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runVerticle(VertxEventBus.class);
    }

    @Override
    public void start() throws Exception {
        EventBus eventBus = getVertx().eventBus();

        // Register codec for custom message
        eventBus.registerDefaultCodec(CustomMessage.class, new CustomMessageCodec());

        // Custom message
//        CustomMessage clusterWideMessage = new CustomMessage(200, "a00000001", "Message sent from publisher!");
        CustomMessage localMessage = new CustomMessage(200, "a0000001", "Local message!");

        // Send a message to [cluster receiver] every second
//        getVertx().setPeriodic(1000, _id -> {
//            eventBus.send("cluster-message-receiver", clusterWideMessage, reply -> {
//                if (reply.succeeded()) {
//                    CustomMessage replyMessage = (CustomMessage) reply.result().body();
//                    System.out.println("Received reply: " + replyMessage.getSummary());
//                } else {
//                    System.out.println("No reply from cluster receiver");
//                }
//            });
//        });

        // Deploy local receiver
        getVertx().deployVerticle(LocalReceiver.class.getName(), deployResult -> {
            // Deploy succeed
            if (deployResult.succeeded()) {
                // Send a message to [local receiver] every 2 second
                getVertx().setPeriodic(2000, _id -> {
                    eventBus.send("local-message-receiver", localMessage, reply -> {
                        if (reply.succeeded()) {
                            CustomMessage replyMessage = (CustomMessage) reply.result().body();
                            System.out.println("Received local reply: " + replyMessage.getSummary());
                        } else {
                            System.out.println("No reply from local receiver");
                        }
                    });
                });

                // Deploy failed
            } else {

            }
        });
    }
}
