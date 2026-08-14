// package com.ecommerce.pinkbags.kafka;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.kafka.annotation.KafkaListener;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import com.ecommerce.pinkbags.entities.Product;
// import com.ecommerce.pinkbags.kafka.dto.OrderDTO;
// import com.ecommerce.pinkbags.kafka.dto.OrderItemDTO;
// import com.ecommerce.pinkbags.repositories.ProductRepository;

// @Service
// public class InventoryConsumer {

//     private final ProductRepository productRepository;
//     private static final Logger logger = LoggerFactory.getLogger(InventoryConsumer.class);


//     public InventoryConsumer(ProductRepository productRepository) {
//         this.productRepository = productRepository;
//     }

//     @KafkaListener(
//         topics = "orders",
//         groupId = "inventory-service",
//         containerFactory = "kafkaListenerContainerFactory"
//     )
//     @Transactional
//    public void consumeOrder(OrderDTO order) {
//     logger.info("📦 [InventoryConsumer] Received Order: {} from customer {}", order.getId(), order.getCustomerId());

//     if (order.getItems() != null) {
//         for (OrderItemDTO item : order.getItems()) {
//             Product product = productRepository.findById(item.getProductId()).orElse(null);

//             if (product != null) {
//                 int newStock = product.getStock() - item.getQuantity();
//                 product.setStock(Math.max(newStock, 0)); // prevent negative stock
//                 productRepository.save(product);

//                 logger.info("   ➖ Reduced stock for {} by {} (Remaining: {})",
//                         product.getName(),
//                         item.getQuantity(),
//                         product.getStock());
//             } else {
//                 logger.info("   ⚠ Product not found: {}", item.getProductId());
//             }
//         }
//     }
//    }
// }
