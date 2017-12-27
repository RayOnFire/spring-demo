package com.example.demo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by Ray on 2017/7/2.
 */

@Component
public class SessionListener implements HttpSessionListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${com.ray.max-inactive-interval}")
    private int maxInactiveInterval;

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        logger.info("session created " + event.getSession().getId());
        event.getSession().setMaxInactiveInterval(maxInactiveInterval);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        logger.info("session destroyed " + event.getSession().getId());
    }
}
