package com.ecommerce.pinkbags.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.pinkbags.dto.CheckoutForm;
import com.ecommerce.pinkbags.entities.Customer;
import com.ecommerce.pinkbags.entities.Order;
import com.ecommerce.pinkbags.entities.OrderItem;
import com.ecommerce.pinkbags.entities.Product;
import com.ecommerce.pinkbags.entities.ShippingAddress;
import com.ecommerce.pinkbags.entities.User;
import com.ecommerce.pinkbags.repositories.CustomerRepository;
import com.ecommerce.pinkbags.repositories.OrderItemRepository;
import com.ecommerce.pinkbags.repositories.OrderRepository;
import com.ecommerce.pinkbags.repositories.ProductRepository;
import com.ecommerce.pinkbags.repositories.ShippingAddressRepository;
import com.ecommerce.pinkbags.repositories.UserRepository;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;


@Controller
public class HomeController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ShippingAddressRepository shippingAddressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

      
    // Helper method to get cart information
    private void addCartInfoToModel(@AuthenticationPrincipal UserDetails user, Model model) {
        Order order = new Order();
        List<OrderItem> items = List.of();
        int cartItems = 0;

        if (user != null) {
            Customer customer = customerRepository.findByUserUsername(user.getUsername());
            if (customer != null) {
                Optional<Order> optionalOrder = orderRepository.findByCustomerAndCompleteFalse(customer);
                
                if (optionalOrder.isPresent()) {
                    order = optionalOrder.get();
                    items = order.getOrderItems();
                    cartItems = order.getCartItems();
                }
                // Don't create a new order here - only create when adding items
            }
        }

        model.addAttribute("items", items);
        model.addAttribute("order", order);
        model.addAttribute("cartItems", cartItems);
    }

    @GetMapping("/about")
    public String about(@AuthenticationPrincipal UserDetails user, Model model) {
        addCartInfoToModel(user, model);
        return "about";
    }

     @GetMapping("/survey")
    public String survey(@AuthenticationPrincipal UserDetails user, Model model) {
        addCartInfoToModel(user, model);
        return "survey";
    }

    @GetMapping("/main")
    public String main(@AuthenticationPrincipal UserDetails user, Model model) {
        addCartInfoToModel(user, model);
        return "main";
    }

     @GetMapping("/terms")
    public String terms(@AuthenticationPrincipal UserDetails user, Model model) {
        addCartInfoToModel(user, model);
        return "terms";
    }

@GetMapping("/cart")
public String cart(@AuthenticationPrincipal UserDetails user, Model model) {
    if (user == null) {
        return "redirect:/login?redirect=cart";
    }

    Customer customer = customerRepository.findByUserUsername(user.getUsername());
    if (customer == null) {
        // Handle case where customer doesn't exist
        return "redirect:/error?message=Customer not found";
    }

    Optional<Order> optionalOrder = orderRepository.findByCustomerAndCompleteFalse(customer);

    Order order;
    List<OrderItem> items;
    int cartItems;
    BigDecimal subtotal = BigDecimal.ZERO;
    BigDecimal tax = BigDecimal.ZERO;
    BigDecimal grandTotal = BigDecimal.ZERO;

    if (optionalOrder.isPresent()) {
        order = optionalOrder.get();
        items = order.getOrderItems();
        cartItems = order.getCartItems();

        // Calculate subtotal and totalPrice per item
        for (OrderItem item : items) {
            // Add null-safe check to prevent NullPointerException
            if (item.getProduct() != null) {
                BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
                BigDecimal totalPrice = item.getProduct().getPrice().multiply(quantity);
                totalPrice = totalPrice.setScale(2, RoundingMode.HALF_UP);
                subtotal = subtotal.add(totalPrice);
            }
        }

        tax = subtotal.multiply(BigDecimal.valueOf(0.05)).setScale(2, RoundingMode.HALF_UP);
        grandTotal = subtotal.add(tax);
    } else {
        order = new Order();
        order.setCustomer(customer);
        order.setComplete(false);
        orderRepository.save(order);
        items = List.of();
        cartItems = 0;
    }

    // Fetch 4 random products for display
    List<Product> randomProducts = productRepository.findRandomProducts(4);

    model.addAttribute("items", items);
    model.addAttribute("order", order);
    model.addAttribute("random_products", randomProducts);
    model.addAttribute("subtotal", subtotal);
    model.addAttribute("tax", tax);
    model.addAttribute("grand_total", grandTotal);
    model.addAttribute("cartItems", cartItems);
    model.addAttribute("shipping", true);

    return "cart";
}

@PostMapping("/update-item")
@ResponseBody
@PreAuthorize("isAuthenticated()")
@Transactional
public String updateItem(@AuthenticationPrincipal UserDetails user,
                         @RequestBody CartUpdateRequest request) {
    Customer customer = customerRepository.findByUserUsername(user.getUsername());
    Product product = productRepository.findById(request.getProductId())
                                       .orElseThrow();

    Order order = orderRepository.findByCustomerAndCompleteFalse(customer)
            .orElseGet(() -> {
                Order newOrder = new Order();
                newOrder.setCustomer(customer);
                newOrder.setComplete(false);
                return orderRepository.save(newOrder);
            });

    OrderItem orderItem = orderItemRepository.findByOrderAndProduct(order, product)
            .orElseGet(() -> {
                OrderItem newItem = new OrderItem();
                newItem.setOrder(order);
                newItem.setProduct(product);
                newItem.setQuantity(0);
                return newItem;
            });

    if ("add".equals(request.getAction())) {
        orderItem.setQuantity(orderItem.getQuantity() + 1);
    } else if ("remove".equals(request.getAction())) {
        orderItem.setQuantity(orderItem.getQuantity() - 1);
    }

    if (orderItem.getQuantity() <= 0) {
        orderItemRepository.delete(orderItem);
    } else {
        orderItemRepository.save(orderItem);
    }

    return "Item was updated";
}

@GetMapping("/get-cart-count")
@ResponseBody
public Map<String, Integer> getCartCount(@AuthenticationPrincipal UserDetails user) {
    Map<String, Integer> response = new HashMap<>();
    if (user != null) {
        Customer customer = customerRepository.findByUserUsername(user.getUsername());
        Optional<Order> optionalOrder = orderRepository.findByCustomerAndCompleteFalse(customer);
        response.put("count", optionalOrder.map(Order::getCartItems).orElse(0));
    } else {
        response.put("count", 0);
    }
    return response;
}

@GetMapping("/remove-from-cart/{itemId}")
public String removeFromCart(@PathVariable Long itemId, @AuthenticationPrincipal UserDetails user) {
    if (user == null) {
        return "redirect:/login?redirect=cart" + itemId;
    }

    Customer customer = customerRepository.findByUserUsername(user.getUsername());
    Optional<OrderItem> optionalItem = orderItemRepository.findByIdAndOrderCustomerAndOrderCompleteFalse(itemId, customer);
    optionalItem.ifPresent(item -> {
        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
            orderItemRepository.save(item);
        } else {
            orderItemRepository.delete(item);
        }
    });
    return "redirect:/cart";
}


    @GetMapping({"/", "/index"})
    public String index(@AuthenticationPrincipal UserDetails user, Model model) {
        addCartInfoToModel(user, model);
        
        List<Product> trendingProducts = productRepository.findByIdIn(List.of(5L,6L,7L,8L,9L,10L,11L,12L, 13L, 14L, 15L, 16L));
        List<Product> onSaleProducts = productRepository.findTop4ByDiscountGreaterThan(0.0);

        model.addAttribute("trending_products", trendingProducts);
        model.addAttribute("on_sale_products", onSaleProducts);

        return "index";
    }

    @GetMapping("/product")
    public String productPage(@AuthenticationPrincipal UserDetails user, Model model) {
        addCartInfoToModel(user, model);
        
        List<Product> products = productRepository.findByIdIn(List.of(1L,2L,3L,4L,5L,6L, 7L,8L,9L,10L));
        model.addAttribute("products", products);

        return "product";
    }

    @GetMapping("/product2")
    public String product2Page(@AuthenticationPrincipal UserDetails user, Model model) {
        addCartInfoToModel(user, model);
        
        List<Product> products = productRepository.findByIdIn(List.of(11L,12L, 13L,14L,15L,16L,17L,18L,19L,20L));
        model.addAttribute("products", products);

        return "product2";
    }

    @GetMapping("/login")
public String loginPage(@AuthenticationPrincipal UserDetails user, 
                        Model model, 
                        @RequestParam(value = "redirect", required = false) String redirect) {
    if (user != null) {
        if (redirect != null && !redirect.isEmpty()) {
            return "redirect:/" + redirect;
        }
        return "redirect:/"; // default
    }

    addCartInfoToModel(user, model);
    if (redirect != null) {
        model.addAttribute("redirect", redirect);
    }
    return "login";
}


    @GetMapping("/signup")
    public String signupPage(@AuthenticationPrincipal UserDetails user, Model model) {
        if (user != null) {
            return "redirect:/";
        }
        
        addCartInfoToModel(user, model);
        return "signup";
    }

    @PostMapping("/signup")
    public String signupAction(@RequestParam String email,
                           @RequestParam("psw") String password,
                           @RequestParam("psw-repeat") String passwordRepeat,
                           RedirectAttributes redirectAttributes) {
        if (!password.equals(passwordRepeat)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/signup";
        }

        if (userRepository.findByUsername(email).isPresent() || 
            userRepository.findByEmail(email).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Email is already registered.");
            return "redirect:/signup";
        }

        // Create and save user
        User user = new User();
        user.setUsername(email);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("CUSTOMER");
        userRepository.save(user);

        // Create and save customer
        Customer customer = new Customer();
        customer.setUser(user);
        customer.setEmail(email);
        customerRepository.save(customer);

        redirectAttributes.addFlashAttribute("success", "Account created successfully. Please login.");
        return "redirect:/login";
    }

     @GetMapping("/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/index?logout"; // Redirect after logout
    }

    @GetMapping("/checkout")
    public String checkoutPage(@AuthenticationPrincipal UserDetails user, Model model) {
        if (user == null) {
            return "redirect:/login?redirect=checkout";
        }
        
        addCartInfoToModel(user, model);
        model.addAttribute("checkoutForm", new CheckoutForm());
        
        return "checkout";
    }

    @PostMapping("/process-checkout")
    public String processCheckout(@AuthenticationPrincipal UserDetails user,
                              @RequestParam String name,
                              @RequestParam String email,
                              @RequestParam String address,
                              @RequestParam String state,
                              @RequestParam String city,
                              @RequestParam("zip") String zipCode,
                                @ModelAttribute CheckoutForm checkoutForm) {
        Customer customer = customerRepository.findByUserUsername(user.getUsername());
        Order order = orderRepository.findByCustomerAndCompleteFalse(customer)
                .orElse(null);
        if (order == null) {
            return "redirect:/cart";
        }

        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setCustomer(customer);
        shippingAddress.setOrder(order);
        shippingAddress.setAddress(address);
        shippingAddress.setCity(city);
        shippingAddress.setState(state);
        shippingAddress.setZipcode(zipCode);
        shippingAddressRepository.save(shippingAddress);

        order.setComplete(true);
        orderRepository.save(order);

        return "redirect:/payment_confirmation";
    }

@GetMapping("/payment_confirmation")
public String paymentConfirmation(@AuthenticationPrincipal UserDetails user, Model model) {

    if (user == null) {
        return "redirect:/login?redirect=payment_confirmation";
    }

    addCartInfoToModel(user, model);

    // Fetch customer
    Customer customer = customerRepository.findByUserUsername(user.getUsername());
    if (customer == null) {
        model.addAttribute("error", "Customer not found");
        return "error";
    }

    // Fetch latest completed order
    Optional<Order> orderOpt = orderRepository.findTopByCustomerAndCompleteTrueOrderByIdDesc(customer);
    if (orderOpt.isEmpty()) {
        model.addAttribute("error", "No completed orders found for this customer");
        return "error";
    }

    Order order = orderOpt.get();

    // Fetch shipping address
    Optional<ShippingAddress> shippingOpt = shippingAddressRepository.findByOrder(order);
    if (shippingOpt.isEmpty()) {
        model.addAttribute("error", "Shipping address not found for order " + order.getId());
        return "error";
    }

    ShippingAddress shippingAddress = shippingOpt.get();

    // Add attributes for Thymeleaf
    model.addAttribute("order", order);
    model.addAttribute("shippingAddress", shippingAddress);

    return "payment_confirmation";
}

    @GetMapping("/product/{id}")
    public String productDetails(@PathVariable Long id,
                             @AuthenticationPrincipal UserDetails user,
                             Model model) {
        addCartInfoToModel(user, model);
        
        Product product = productRepository.findById(id).orElseThrow();
        List<Product> randomProducts = productRepository.findRandomProducts(4);

        model.addAttribute("product", product);
        model.addAttribute("randomProducts", randomProducts);

        return "product_details";
    }

@GetMapping("/order/{id}/invoice")
public void generateInvoice(@PathVariable Long id, HttpServletResponse response) throws Exception {
    // Fetch the order
    Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    // Set response type
    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=invoice_" + id + ".pdf");

    // Create PDF document
    com.itextpdf.text.Document document = new com.itextpdf.text.Document();
    PdfWriter.getInstance(document, response.getOutputStream());
    document.open();

    // Add header info
    document.add(new Paragraph("Invoice for Order #" + order.getId()));
    document.add(new Paragraph("Customer: " + order.getCustomer().getName()));
    document.add(new Paragraph("Date: " + order.getDateOrdered()));
    document.add(new Paragraph(" "));

    // Create table for order items
    PdfPTable table = new PdfPTable(3);
    table.addCell("Product");
    table.addCell("Quantity");
    table.addCell("Price");

    for (OrderItem item : order.getOrderItems()) {
        table.addCell(item.getProduct().getName());
        table.addCell(String.valueOf(item.getQuantity()));
        table.addCell(item.getTotalPrice().toString());  // Use getTotalPrice()
    }
    document.add(table);

    // Add total
    document.add(new Paragraph("Total: $" + order.getCartTotal()));

    document.close();
}

@PostMapping("/order/{id}/status")
public String updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
    Order order = orderRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Order not found"));
    order.setStatus(status.toUpperCase());
    order.setUpdatedAt(LocalDateTime.now());
    orderRepository.save(order);
    return "redirect:/orders/" + id; // back to order details
}

    // DTO for AJAX request
    public static class CartUpdateRequest {
        private Long productId;
        private String action;

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
    }
}