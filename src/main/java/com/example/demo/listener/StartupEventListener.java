package com.example.demo.listener;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Ray on 2017/6/29.
 */
@Component
public class StartupEventListener {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CoverImageRepository coverImageRepository;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UnifiedOrderRepository unifiedOrderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ShippingInfoRepository shippingInfoRepository;

    @Autowired
    private MerchandiseRepository merchandiseRepository;

    @Autowired
    private MerchandiseRecommendRepository merchandiseRecommendRepository;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent () {
        User admin = new User();
        Random random = new Random();
        admin.setUsername("zl2003cn");
        admin.setPassword("pwd");
        userRepository.save(admin);

        User sonyVendor = new User("Sony", "pwd");
        userRepository.save(sonyVendor);

        InputStream in = this.getClass().getResourceAsStream("/mock-ps4-detail.html");
        String html = new Scanner(in).useDelimiter("\\Z").next();

        Merchandise merchandise = new Merchandise();
        merchandise.setName("PlayStation 4 Pro");
        merchandise.setPrice(499);
        merchandise.setSlogan("主机核心升级，支持原生4K游戏，支持HDR，1TB大容量，为追求高品质玩家设计！");
        merchandise.setPageHtml(html);
        merchandise.setInventory(10000);
        merchandise.setPublisher(admin);
        merchandiseRepository.save(merchandise);

        List<CoverImage> urls = new ArrayList<>();
        urls.add(new CoverImage("https://img10.360buyimg.com/n1/s450x450_jfs/t4465/236/2280808090/166997/f23d9205/591a952cN653e6e72.jpg"));
        urls.add(new CoverImage("https://img10.360buyimg.com/n1/s450x450_jfs/t5104/347/2424625446/173178/a5e58519/591a9518N36395f74.jpg"));
        urls.add(new CoverImage("https://img10.360buyimg.com/n1/s450x450_jfs/t5113/104/2424570402/91071/2676fe12/591a9524N7b9942eb.jpg"));
        urls.forEach(item -> {
            item.setMerchandise(merchandise);
        });
        merchandise.getCoverImages().addAll(urls);
        merchandiseRepository.save(merchandise);

        Merchandise relatedMerchandise = new Merchandise();
        relatedMerchandise.setName("索尼（SONY）【国行PS】PlayStation VR 虚拟现实头戴设备");
        relatedMerchandise.setPrice(2988);
        relatedMerchandise.setInventory(10000);
        relatedMerchandise.setPublisher(admin);
        merchandiseRepository.save(relatedMerchandise);

        List<CoverImage> covers = new ArrayList<>();
        covers.add(new CoverImage("https://img10.360buyimg.com/n1/s450x450_jfs/t2641/296/3658957863/66063/85279aec/57958959Nd80c4fc3.jpg"));
        covers.forEach(item -> {
            item.setMerchandise((relatedMerchandise));
        });
        relatedMerchandise.getCoverImages().addAll(covers);
        merchandiseRepository.save(relatedMerchandise);

        MerchandiseRecommend recommend = new MerchandiseRecommend(merchandise, relatedMerchandise);
        merchandiseRecommendRepository.save(recommend);

        Merchandise relatedVendorMerchandise = new Merchandise();
        relatedVendorMerchandise.setName("索尼（SONY）【PS4国行游戏】最终幻想 15 Final Fantasy XV");
        relatedVendorMerchandise.setPrice(339);
        relatedVendorMerchandise.setInventory(10000);
        relatedVendorMerchandise.setPublisher(sonyVendor);
        merchandiseRepository.save(relatedVendorMerchandise);

        CoverImage coverImage = new CoverImage("https://img10.360buyimg.com/n1/s450x450_jfs/t3493/315/1585880820/181696/61ccb55c/582c1423N2d2394ef.jpg");
        coverImage.setMerchandise(relatedVendorMerchandise);
        relatedVendorMerchandise.getCoverImages().add(coverImage);
        merchandiseRepository.save(relatedVendorMerchandise);

        MerchandiseRecommend recommend1 = new MerchandiseRecommend(merchandise, relatedVendorMerchandise);
        merchandiseRecommendRepository.save(recommend1);

        User buyer = new User("buyer", "pwd");
        userRepository.save(buyer);

        UserShippingInfo shippingInfo = new UserShippingInfo(buyer, "广东省广州市白云区人和镇北后西街8号", "12345678", "LeBron James");
        shippingInfoRepository.save(shippingInfo);
        UserShippingInfo shippingInfo1 = new UserShippingInfo(buyer, "上海市浦东新区", "87654321", "Kobe Bryant");
        shippingInfoRepository.save(shippingInfo1);

        InputStream inputStream = this.getClass().getResourceAsStream("/output.txt");
        String json = new Scanner(inputStream).useDelimiter("\\Z").next();
        GsonBuilder builder = new GsonBuilder();
        ArrayList<LinkedTreeMap> o = (ArrayList<LinkedTreeMap>) builder.create().fromJson(json, Object.class);

        List<Merchandise> merchandiseList = new ArrayList<>();
        o.forEach(item -> {
            Merchandise merchandiseLocal = new Merchandise();
            merchandiseLocal.setInventory(10000);
            merchandiseLocal.setPublisher(admin);
            item.keySet().forEach(key -> {
                switch ((String) key) {
                    case "id": merchandiseLocal.setJdId(Long.valueOf((String) item.get(key)));
                        break;
                    case "price": merchandiseLocal.setPrice(Double.valueOf((String) item.get(key)));
                        break;
                    case "name": merchandiseLocal.setName((String) item.get(key));
                        break;
                    case "description": merchandiseLocal.setDescription(builder.create().toJson(item.get(key)));
                        break;
                    case "img": CoverImage coverImageLocal = new CoverImage("https:" + item.get(key));
                        coverImageLocal.setMerchandise(merchandiseLocal);
                        merchandiseLocal.getCoverImages().add(coverImageLocal);
                        break;
                    case "html": merchandiseLocal.setPageHtml((String)item.get(key));
                        break;
                    default:
                        break;
                }
            });
            merchandiseRepository.save(merchandiseLocal);
            merchandiseList.add(merchandiseLocal);
        });
        /*
        InputStream stream = this.getClass().getResourceAsStream("/output.txt");
        String json2 = new Scanner(stream).useDelimiter("\\Z").next();
        GsonBuilder builder2 = new GsonBuilder();
        ArrayList<LinkedTreeMap> categories = (ArrayList<LinkedTreeMap>) builder2.create().fromJson(json2, Object.class);
        o.forEach(mainCategory -> {
            Category category = new Category();
            mainCategory.keySet().forEach(key -> {
                if (key.equals("name")) {
                    String name = String.join("|", (List)mainCategory.get(key));
                    category.setName(name);
                } else if (key.equals("subCategories")) {
                    LinkedTreeMap<String, List> subCategories = (LinkedTreeMap<String, List>)mainCategory.get(key);
                    subCategories.forEach((subName, subList) -> {
                        Category subCategory = new Category();
                        subCategory.setName(subName);
                        subList.forEach();
                    });


                }
            });
        });
        */
        /*
        for (int i = 0; i < 100; i++) {
            Merchandise merchandise = new Merchandise("Merchandise #" + String.valueOf(i), 499, admin, new Timestamp(System.currentTimeMillis()), 10000);
            merchandiseRepository.save(merchandise);
        }
        List<User> mockBuyers = new ArrayList<>();
        List<UserShippingInfo> shippingInfos = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            User buyer = new User("Buyer" + String.valueOf(i), "pwd");
            mockBuyers.add(buyer);
            UserShippingInfo shippingInfo = new UserShippingInfo(buyer, "Mars", "1234567");
            shippingInfos.add(shippingInfo);
        }
        userRepository.save(mockBuyers);
        shippingInfoRepository.save(shippingInfos);

        for (int i = 0; i < 100; i++) {
            User buyer = userRepository.findOne(Long.valueOf(random.nextInt(100) + 2));
            Merchandise merchandise = merchandiseRepository.findOne(Long.valueOf(random.nextInt(100) + 1));
            ShoppingCart cart = new ShoppingCart(buyer);
            cart = cartRepository.save(cart);
            CartItem cartItem = new CartItem(merchandise, Long.valueOf(random.nextInt(100)), new Timestamp(System.currentTimeMillis()), cart);
            cart.getCartItems().add(cartItem);
            cartRepository.save(cart);
            UserShippingInfo shippingInfo = shippingInfoRepository.findByUser(buyer);
            UnifiedOrder order = new UnifiedOrder(buyer, cart, merchandise.getPrice(), shippingInfo, admin);
            unifiedOrderRepository.save(order);
        }
        */
    }
}
