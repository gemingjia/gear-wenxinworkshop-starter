package com.gearwenxin.config;

import com.gearwenxin.service.AccessTokenPool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class AccessTokenConfig {

    @Bean
    @ConditionalOnMissingBean(AccessTokenPool.class)
    public AccessTokenPool defaultAccessTokenService() {
//        return new DefaultAccessTokenService();
        return null;
    }
}
