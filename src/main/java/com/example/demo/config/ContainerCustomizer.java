package com.example.demo.config;

import org.apache.catalina.Context;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Ray on 2017/7/11.
 */
@Configuration
@EnableAutoConfiguration
public class ContainerCustomizer implements EmbeddedServletContainerCustomizer {
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        ((TomcatEmbeddedServletContainerFactory) container).addContextCustomizers(new TomcatContextCustomizer()
        {
            @Override
            public void customize(Context context)
            {
                context.setUseHttpOnly(false);
            }
        });
    }
}
