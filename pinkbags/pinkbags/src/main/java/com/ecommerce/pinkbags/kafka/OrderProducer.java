// package com.ecommerce.pinkbags.kafka;

// import java.util.List;
// import java.util.Random;
// import java.util.UUID;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.kafka.core.KafkaTemplate;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Service;

// import com.ecommerce.pinkbags.entities.Customer;
// import com.ecommerce.pinkbags.entities.Order;
// import com.ecommerce.pinkbags.entities.Product;
// import com.ecommerce.pinkbags.kafka.dto.OrderDTO;
// import com.ecommerce.pinkbags.kafka.util.RandomOrderGenerator;
// import com.ecommerce.pinkbags.repositories.CustomerRepository;
// import com.ecommerce.pinkbags.repositories.OrderRepository;
// import com.ecommerce.pinkbags.repositories.ProductRepository;

// @Service
// public class OrderProducer {

//     @Autowired
//     private KafkaTemplate<String, Object> kafkaTemplate;

//     @Autowired
//     private CustomerRepository customerRepository;

//     @Autowired
//     private OrderRepository orderRepository;

//     @Autowired
//     private ProductRepository productRepository;

//     private static final String TOPIC = "orders";
//     private static final Random RANDOM = new Random();
//     private static final Logger logger = LoggerFactory.getLogger(OrderProducer.class);

//     @Scheduled(fixedRate = 5000) // every 5 seconds
//     public void produceOrder() {
//         List<Customer> customers = customerRepository.findAll();
//         List<Product> products = productRepository.findAll();

      
// if (products.isEmpty()) {
//     logger.info("⚠️ No products available to create orders.");
//     return;
// }

// Customer customer;
// if (customers.isEmpty() || RANDOM.nextBoolean()) {
//     customer = createRandomCustomer();
//     customerRepository.save(customer);
//     logger.info("🆕 Created new customer: {}", customer.getId());
// } else {
//     customer = RandomOrderGenerator.getRandomCustomer(customers);
//     if (orderRepository.existsByCustomerAndCompleteFalse(customer)) {
//         logger.info("⏭️ Customer {} already has an incomplete order. Skipping.", customer.getId());
//         return;
//     }
// }

//         // ✅ Generate order with random order items
//         Order order = RandomOrderGenerator.generateOrder(customer, products);
//         orderRepository.save(order);

//         OrderDTO dto = RandomOrderGenerator.toDTO(order);
//         kafkaTemplate.send(TOPIC, customer.getId().toString(), dto);

//         logger.info("✅ Produced order " + order.getTransactionId() +
//                 " with " + order.getOrderItems().size() +
//                 " items for customer " + customer.getId());
//     }

//     // Helper to create random customer
//     private Customer createRandomCustomer() {
//         Customer customer = new Customer();
//         customer.setName("Customer " + UUID.randomUUID().toString().substring(0, 5));
//         customer.setEmail("user" + RANDOM.nextInt(1000) + "@example.com");
//         return customer;
//     }
// }
