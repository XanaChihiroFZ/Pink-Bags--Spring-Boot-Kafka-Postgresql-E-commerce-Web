// package com.ecommerce.pinkbags.kafka.util;

// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Random;
// import java.util.Set;
// import java.util.UUID;

// import com.ecommerce.pinkbags.entities.Customer;
// import com.ecommerce.pinkbags.entities.Order;
// import com.ecommerce.pinkbags.entities.OrderItem;
// import com.ecommerce.pinkbags.entities.Product;
// import com.ecommerce.pinkbags.kafka.dto.OrderDTO;
// import com.ecommerce.pinkbags.kafka.dto.OrderItemDTO;

// public class RandomOrderGenerator {

//     private static final Random RANDOM = new Random();

//     // Generate a random order for a given customer
//     public static Order generateOrder(Customer customer, List<Product> products) {
//         Order order = new Order(customer);
//         order.setDateOrdered(LocalDateTime.now());
//         order.setTransactionId(UUID.randomUUID().toString());
//         order.setComplete(false);

//         // Pick random number of order items (between 1 and total number of products)
//         int numberOfItems = 1 + RANDOM.nextInt(products.size());

//         Set<Integer> chosenIndexes = new HashSet<>();
//         List<OrderItem> orderItems = new ArrayList<>();

//         for (int i = 0; i < numberOfItems; i++) {
//             // Pick a random product (avoid duplicates)
//             int index;
//             do {
//                 index = RANDOM.nextInt(products.size());
//             } while (!chosenIndexes.add(index));

//             Product product = products.get(index);

//             // Random quantity between 1 and 5
//             int quantity = 1 + RANDOM.nextInt(5);

//             OrderItem orderItem = new OrderItem();
//             orderItem.setOrder(order);
//             orderItem.setProduct(product);
//             orderItem.setQuantity(quantity);
//             orderItems.add(orderItem);
//         }

//         order.setOrderItems(orderItems);
//         return order;
//     }

//     // Convert Order -> DTO for Kafka
//     public static OrderDTO toDTO(Order order) {
//         OrderDTO dto = new OrderDTO();
//         dto.setId(order.getId());
//         dto.setCustomerId(order.getCustomer().getId());
//         dto.setTransactionId(order.getTransactionId());
//         dto.setDateOrdered(order.getDateOrdered());
//         dto.setComplete(order.isComplete());

//         List<OrderItemDTO> itemDTOs = new ArrayList<>();
//         if (order.getOrderItems() != null) {
//             for (OrderItem item : order.getOrderItems()) {
//                 OrderItemDTO itemDTO = new OrderItemDTO();
//                 itemDTO.setProductId(item.getProduct().getId());
//                 itemDTO.setProductName(item.getProduct().getName());
//                 itemDTO.setQuantity(item.getQuantity());
//                 itemDTO.setTotal(item.getTotal());
//                 itemDTOs.add(itemDTO);
//             }
//         }
//         dto.setItems(itemDTOs);

//         return dto;
//     }

//     // Helper to pick a random existing customer
//     public static Customer getRandomCustomer(List<Customer> customers) {
//         return customers.get(RANDOM.nextInt(customers.size()));
//     }
// }
