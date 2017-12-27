package com.example.demo;

import com.example.demo.model.CoverImage;
import com.example.demo.model.Merchandise;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by Ray on 2017/7/11.
 */
public class JsonLoadTest {

    @Test
    public void testGson() {
        InputStream in = this.getClass().getResourceAsStream("/output.txt");
        String json = new Scanner(in).useDelimiter("\\Z").next();
        //System.out.println(scanner);
        Type fooType = new TypeToken<List<Merchandise>>() {}.getType();
        GsonBuilder builder = new GsonBuilder();
        ArrayList<LinkedTreeMap> o = (ArrayList<LinkedTreeMap>) builder.create().fromJson(json, Object.class);
        System.out.println(o);
        System.out.println(o.get(0).keySet());
        List<Merchandise> merchandiseList = new ArrayList<>();
        o.forEach(item -> {
            Merchandise merchandise = new Merchandise();
            item.keySet().forEach(key -> {
                switch ((String) key) {
                    case "id": merchandise.setJdId(Long.valueOf((String) item.get(key)));
                        break;
                    case "price": merchandise.setPrice(Double.valueOf((String) item.get(key)));
                        break;
                    case "name": merchandise.setName((String) item.get(key));
                        break;
                    case "description": merchandise.setDescription(builder.create().toJson(item.get(key)));
                        break;
                    case "img": CoverImage coverImage = new CoverImage("https:" + item.get(key));
                        coverImage.setMerchandise(merchandise);
                        merchandise.getCoverImages().add(coverImage);
                        break;
                    default:
                        break;
                }
            });
            merchandiseList.add(merchandise);
        });
        merchandiseList.forEach(i -> {
            i.getCoverImages().forEach(System.out::println);
        });
        // System.out.println(merchandiseList);
        /*
        System.out.println(o.size());
        LinkedTreeMap linkedTreeMap = (LinkedTreeMap) o.get(0);
        System.out.println(linkedTreeMap);
        Set set = linkedTreeMap.entrySet();
        System.out.println(set);
        set.forEach(i -> {
            //System.out.println(i);
            Map.Entry entry = (Map.Entry) i;
            System.out.println(entry.getKey().toString() + " " + entry.getValue().toString());
        });
        */
    }

    // @Test
    public void testGson2() {
        String str = "{\"id\": \"3312381\", \"price\": \"4999.00\", \"img\": \"//img11.360buyimg.com/n1/s450x450_jfs/t6004/154/4087293344/138512/e3fd933e/595dd1e4N0ef1710f.jpg\", \"name\": \"\\u5c0f\\u7c73(MI)Air13.3\\u82f1\\u5bf8\\u5168\\u91d1\\u5c5e\\u8d85\\u8f7b\\u8584\\u7b14\\u8bb0\\u672c\\u7535\\u8111(i5-6200U8G256GPCIE\\u56fa\\u6001\\u786c\\u76d8940MX\\u72ec\\u663eFHDWIN10)\\u94f6\", \"description\": {\"\\u578b\\u53f7\": \"\\u5c0f\\u7c73\\u7b14\\u8bb0\\u672cAIR 13.3\\u82f1\\u5bf8\", \"\\u989c\\u8272\": \"\\u94f6\\u8272\", \"\\u5e73\\u53f0\": \"Intel\", \"\\u7ffb\\u65b0\\u7c7b\\u578b\": \"\\u5168\\u65b0\", \"CPU\\u7c7b\\u578b\": \"\\u9177\\u777f\\u53cc\\u6838i5\\u5904\\u7406\\u5668\", \"CPU\\u901f\\u5ea6\": \"\\u6700\\u9ad8\\u4e3b\\u9891 2.7GHz\", \"\\u4e09\\u7ea7\\u7f13\\u5b58\": \"3M\", \"\\u6838\\u5fc3\": \"\\u53cc\\u6838\", \"\\u5185\\u5b58\\u5bb9\\u91cf\": \"8GB\", \"\\u786c\\u76d8\\u5bb9\\u91cf\": \"\\u5176\\u5b83\", \"\\u63a5\\u53e3\\u7c7b\\u578b\": \"\\u5176\\u5b83\", \"\\u56fa\\u6001\\u786c\\u76d8\": \"256GB PCIe \\u00d7 4 NVMe SSD\", \"\\u7c7b\\u578b\": \"\\u72ec\\u7acb\\u663e\\u5361\", \"\\u663e\\u793a\\u82af\\u7247\": \"NVIDIA GeForce 940MX \\u72ec\\u7acb\\u663e\\u5361\", \"\\u663e\\u5b58\\u5bb9\\u91cf\": \"\\u72ec\\u7acb1GB\", \"\\u5149\\u9a71\\u7c7b\\u578b\": \"\\u65e0\\u5149\\u9a71\", \"\\u5c4f\\u5e55\\u5c3a\\u5bf8\": \"13\\u82f1\\u5bf8\", \"\\u5c4f\\u5e55\\u89c4\\u683c\": \"13.3\\u82f1\\u5bf8\", \"\\u663e\\u793a\\u6bd4\\u4f8b\": \"\\u5bbd\\u5c4f16\\uff1a9\", \"\\u7269\\u7406\\u5206\\u8fa8\\u7387\": \"1920\\u00d71080\", \"\\u5c4f\\u5e55\\u7c7b\\u578b\": \"LED\\u80cc\\u5149\", \"\\u5185\\u7f6e\\u84dd\\u7259\": \"\\u84dd\\u72594.0\", \"\\u5c40\\u57df\\u7f51\": \"\\u5176\\u5b83\", \"\\u65e0\\u7ebf\\u5c40\\u57df\\u7f51\": \"\\u6709\", \"\\u97f3\\u9891\\u7aef\\u53e3\": \"\\u8033\\u673a\\u3001\\u9ea6\\u514b\\u98ce\\u4e8c\\u5408\\u4e00\\u63a5\\u53e3\", \"\\u5176\\u4ed6\\u7aef\\u53e3\": \"HDMI*1 TYPE-C*1\", \"USB3.0\": \"1\\u4e2a\", \"\\u626c\\u58f0\\u5668\": \"\\u5185\\u7f6e\\u626c\\u58f0\\u5668\", \"\\u675c\\u6bd4\\u97f3\\u6548\": \"\\u652f\\u6301\", \"\\u5185\\u7f6e\\u9ea6\\u514b\\u98ce\": \"\\u6709\", \"\\u952e\\u76d8\": \"\\u80cc\\u5149\\u952e\\u76d8\", \"\\u89e6\\u6478\\u677f\": \"\\u6709\", \"\\u7f51\\u7edc\\u6444\\u50cf\\u5934\": \"\\u6709\", \"\\u6444\\u50cf\\u5934\\u50cf\\u7d20\": \"100\\u4e07\", \"\\u8bfb\\u5361\\u5668\": \"\\u65e0\", \"\\u7535\\u6c60\": \"4\\u82af \\u9502\\u79bb\\u5b50\\u7535\\u6c60\", \"\\u7eed\\u822a\\u65f6\\u95f4\": \">8\\u5c0f\\u65f6\", \"\\u7535\\u6e90\\u9002\\u914d\\u5668\": \"100-240V\\u81ea\\u9002\\u5e94\\u4ea4\\u6d41\\u7535\\u6e90\\u9002\\u914d\\u5668\", \"\\u5c3a\\u5bf8\": \"309.6*210.9*14.8MM\", \"\\u51c0\\u91cd\": \"<1.5kg\"}}";
        GsonBuilder builder = new GsonBuilder();
        Merchandise o = builder.create().fromJson(str, Merchandise.class);
        System.out.println(o);
    }
}
