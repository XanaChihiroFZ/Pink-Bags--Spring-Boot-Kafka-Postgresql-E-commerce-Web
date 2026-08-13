// package com.ecommerce.pinkbags.kafka;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.kafka.annotation.KafkaListener;
// import org.springframework.stereotype.Service;

// import com.ecommerce.pinkbags.kafka.dto.OrderDTO;

// @Service
// public class NotificationConsumer {

//     private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);

//     @KafkaListener(topics = "orders", groupId = "notification-service")
//     public void consumeOrder(OrderDTO order) {
//         if (order == null) return;

//         // Log the notification
//         logger.info("🔔 [NotificationConsumer] Order ID {} for Customer ID {} has been processed.",
//                 order.getId(),
//                 order.getCustomerId());

//         logger.info("Sending email to {} ...", order.getCustomerId());
//     }
// }
