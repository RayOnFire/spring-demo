package com.example.demo.service;

import com.example.demo.exception.InventoryNotEnoughException;
import com.example.demo.exception.ObjectNotFoundException;
import com.example.demo.exception.UserNotMatchException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ray on 2017/7/5.
 */

@Service
public class OrderServiceImpl implements OrderService {

    private CartRepository cartRepository;

    private CartItemRepository cartItemRepository;

    private ShippingInfoRepository shippingInfoRepository;

    private MergedOrderRepository mergedOrderRepository;

    private MerchandiseRepository merchandiseRepository;

    private UnifiedOrderRepository unifiedOrderRepository;

    private MerchandiseService merchandiseService;

    @Autowired
    public OrderServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository, ShippingInfoRepository shippingInfoRepository, MergedOrderRepository mergedOrderRepository, MerchandiseRepository merchandiseRepository, UnifiedOrderRepository unifiedOrderRepository, MerchandiseService merchandiseService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.shippingInfoRepository = shippingInfoRepository;
        this.mergedOrderRepository = mergedOrderRepository;
        this.merchandiseRepository = merchandiseRepository;
        this.unifiedOrderRepository = unifiedOrderRepository;
        this.merchandiseService = merchandiseService;
    }

    @Override
    @Transactional
    public MergedOrder makeOrder(User buyer, long cartId, long userShippingInfoId, String paymentMethod)
            throws InventoryNotEnoughException, UserNotMatchException, ObjectNotFoundException {

        ShoppingCart cart = cartRepository.findOne(cartId);

        // 生成合并订单
        UserShippingInfo userShippingInfo = shippingInfoRepository.findOne(userShippingInfoId);
        MergedOrder mergedOrder = new MergedOrder(buyer, cart, cart.getTotal(), userShippingInfo, PaymentMethod.valueOf(paymentMethod));
        mergedOrderRepository.save(mergedOrder);

        // 减库存
        for (CartItem item: cart.getCartItems()) {
            merchandiseService.setInventory(item.getMerchandise().getId(), item.getAmount());
            //Merchandise merchandise = item.getMerchandise();
                /*
                session.createSQLQuery("UPDATE merchandise SET inventory = inventory - ? WHERE id = ?")
                        .setParameter(0, item.getAmount())
                        .setParameter(1, merchandise.getId())
                        .executeUpdate();
                */
            //entityManager.lock(merchandise, LockModeType.PESSIMISTIC_WRITE);
            //merchandise.setInventory(merchandise.getInventory() - item.getAmount());
            //merchandiseRepository.save(merchandise);
            // Lock will be released here? To avoid using raw EntityManager, business code above should to move to DAO
            // and perform locking(with @Transactional?)
        }

        // 分割订单，相同vendor一个shopping cart
        Map<User, ShoppingCart> shoppingCartMap = new HashMap<>();
        for (CartItem item : cart.getCartItems()) {
            User publisher = item.getMerchandise().getPublisher();
            ShoppingCart unifiedCart = shoppingCartMap.get(publisher);
            if (unifiedCart != null) {
                CartItem copiedItem = new CartItem(item.getMerchandise(), item.getAmount(), item.getDate(), unifiedCart);
                unifiedCart.getCartItems().add(item);
                unifiedCart.setSubmit(true);
                cartItemRepository.save(copiedItem);
            } else {
                ShoppingCart newCart = new ShoppingCart(cart.getUser());
                newCart.setSubmit(true);
                cartRepository.save(newCart);
                CartItem copiedItem = new CartItem(item.getMerchandise(), item.getAmount(), item.getDate(), newCart);
                cartItemRepository.save(copiedItem);
                shoppingCartMap.put(publisher, newCart);
            }
        }

        // 对每一个cart生成unified order
        List<UnifiedOrder> unifiedOrders = new ArrayList<>();
        shoppingCartMap.forEach((user, savedCart) -> {
            UnifiedOrder unifiedOrder = new UnifiedOrder(buyer, savedCart.getTotal(), userShippingInfo, PaymentMethod.valueOf(paymentMethod), user, mergedOrder, savedCart);
            unifiedOrderRepository.save(unifiedOrder);
            unifiedOrders.add(unifiedOrder);
        });

        mergedOrder.getUnifiedOrders().addAll(unifiedOrders);

        cart.setSubmit(true);
        cartRepository.save(cart);

        return mergedOrder;
    }
}
