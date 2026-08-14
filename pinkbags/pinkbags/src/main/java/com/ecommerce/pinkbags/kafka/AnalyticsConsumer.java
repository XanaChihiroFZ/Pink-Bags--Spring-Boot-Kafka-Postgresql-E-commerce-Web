// package com.ecommerce.pinkbags.kafka;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.kafka.annotation.KafkaListener;
// import org.springframework.stereotype.Service;

// import com.ecommerce.pinkbags.kafka.dto.OrderDTO;

// @Service
// public class AnalyticsConsumer {

//     private static final Logger logger = LoggerFactory.getLogger(AnalyticsConsumer.class);

//     // Example: listen on the same topic as Inventory & Notification
//     @KafkaListener(topics = "orders", groupId = "analytics-group")
//     public void consumeOrderAnalytics(OrderDTO order) {
//         logger.info("📊 [AnalyticsConsumer] Processing analytics for Order: {}", order.getId());

//         int totalItems = order.getItems() != null ? order.getItems().size() : 0;

//         double totalAmount = order.getItems() != null
//                 ? order.getItems().stream()
//                     .mapToDouble(item -> item.getPrice().doubleValue() * item.getQuantity())
//                     .sum()
//                 : 0.0;

//         logger.info("   🛒 Customer ID: {}", order.getCustomerId());
//         logger.info("   📦 Number of items: {}", totalItems);
//         logger.info("   💰 Total Order Value: {}", totalAmount);

//         // later: push to a DB or analytics tool (Postgres, Elastic, MongoDB, etc.)
//     }
// }
