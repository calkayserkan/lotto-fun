//package com.serkan.lottofun.drawservice.event;
//
//import com.serkan.lottofun.drawservice.config.RabbitMQConfig;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DrawExtractedListener {
//    @RabbitListener(queues = RabbitMQConfig.DRAW_EXTRACTED_QUEUE)
//    public void handle(DrawExtractedEvent event) {
//        System.out.println("Event received: " + event.getDrawId());
//    }
//}
