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

/**
 * Local receiver
 *
 * @author Junbong
 */
public class LocalReceiver extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        EventBus eventBus = getVertx().eventBus();

        // Does not have to register codec because sender already registered
        /*eventBus.registerDefaultCodec(CustomMessage.class, new CustomMessageCodec());*/
        // Receive message
        eventBus.consumer("local-message-receiver", message -> {
            CustomMessage customMessage = (CustomMessage) message.body();

            System.out.println("Custom message received: " + customMessage.getSummary());

            // Replying is same as publishing
            CustomMessage replyMessage = new CustomMessage(200, "a00000002", "Message sent from local receiver!");
            message.reply(replyMessage);
        });
    }
}
