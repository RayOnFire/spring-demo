package com.example.demo.controller;

import com.example.demo.service.OrderService;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

//@RestController
//@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MerchandiseRepository merchandiseRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShippingInfoRepository shippingInfoRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private EntityManager entityManager;



    /*
    @GetMapping("/transactions")
    public List<Transaction> getTransactions(@AuthenticationPrincipal UserDetails user,
                                            @RequestParam(value = "limit", defaultValue = "20") int limit,
                                            @RequestParam(value = "page", defaultValue = "1") int page) {
        List<Transaction> transactionList = new ArrayList<>();
        User target = userRepository.findByUsername(user.getUsername());
        List<UserOrder> userOrderList = orderRepository.findAllByMerchandise_Publisher(target);
        userOrderList.forEach(userOrder -> {
            List<Transaction> t = transactionRepository.findAllByRelatedUserOrder(userOrder);
            if (t != null) transactionList.addAll(t);
        });
        return transactionList;
    }

    @GetMapping("/transaction")
    public Transaction getTransaction(@AuthenticationPrincipal UserDetails userDetails,
                                      @RequestParam("transaction_id") Long transactionId)
            throws EntityNotFoundException {
        User user = userRepository.findByUsername(userDetails.getUsername());
        Transaction transaction = transactionRepository.findOne(transactionId);
        if (transaction == null) throw new EntityNotFoundException("No such a transaction");
        return transaction;
    }

    @PostMapping("/transaction")
    public Transaction addTransaction(@AuthenticationPrincipal UserDetails user,
                                 @RequestParam("order_id") long orderId,
                                 @RequestParam("payment_method") String paymentMethod)
            throws EntityNotFoundException, UserNotMatchException, PaymentNotSupportException {
        UserOrder userOrder = orderRepository.findOne(orderId);
        if (userOrder == null) throw new EntityNotFoundException("No such order exist");
        if (userOrder.getBuyer() != userRepository.findByUsername(user.getUsername()))
            throw new UserNotMatchException("Only buyer of the order can make transaction");
        PaymentMethod method;
        try {
            method = PaymentMethod.valueOf(paymentMethod);
        } catch (IllegalArgumentException e) {
            throw new PaymentNotSupportException("Payment method not support");
        }
        Transaction transaction = new Transaction(userOrder);
        transaction.setPaymentMethod(method);
        transactionRepository.save(transaction);
        // 自动支付
        paymentService.pay(transaction);
        return transaction;
    }

    @PutMapping("/transaction")
    public Transaction withdrawTransaction(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestParam("transaction_id") Long transactionId,
                                           @RequestParam(value = "withdraw_reason", required = false) String withdrawReason)
            throws Exception {
        User user = userRepository.findByUsername(userDetails.getUsername());
        Transaction transaction = transactionRepository.findOne(transactionId);
        UserOrder userOrder = orderRepository.findOne(transaction.getRelatedUserOrder().getId());
        if (userOrder.getBuyer() != user) throw new UserNotMatchException("Only buyer can cancel transaction");
        if (transaction.isCanceled()) throw new Exception("Cannot withdraw canceled transaction");
        transaction.setRequestWithdraw(true);
        transactionRepository.save(transaction);
        transactionService.notifyWithdraw(transaction);
        return transaction;
    }

    @DeleteMapping("/transaction")
    public Transaction cancelTransaction(@AuthenticationPrincipal UserDetails userDetails,
                                         @RequestParam("transaction_id") Long transactionId,
                                         @RequestParam(value = "cancel_reason", required = false) String cancelReason)
            throws UserNotMatchException, TransactionExistsException {
        User user = userRepository.findByUsername(userDetails.getUsername());
        Transaction transaction = transactionRepository.findOne(transactionId);
        UserOrder userOrder = orderRepository.findOne(transaction.getRelatedUserOrder().getId());
        if (userOrder.getBuyer() != user) throw new UserNotMatchException("Only buyer can cancel transaction");
        if (transaction.isPaid() && !transaction.isWithdrawn()) throw new TransactionExistsException("Cannot cancel paid transaction");
        transaction.setCanceled(true);
        transaction.setCancelledReason(cancelReason);
        return transaction;
    }
    */
}
