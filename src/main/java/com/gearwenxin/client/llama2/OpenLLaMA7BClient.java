package com.gearwenxin.client.llama2;

import com.gearwenxin.client.base.FullClient;
import com.gearwenxin.config.WenXinProperties;
import com.gearwenxin.entity.Message;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ge Mingjia
 * @date 2023/7/24
 */
@Slf4j
@Lazy
@Service
public class OpenLLaMA7BClient extends FullClient {

    @Resource
    private WenXinProperties wenXinProperties;

    private String accessToken = null;
    private static final String TAG = "Open-LLaMA-7B-Client";
    private static Map<String, Deque<Message>> OPEN_LLAMA_7B_MESSAGES_HISTORY_MAP = new ConcurrentHashMap<>();

    private String getAccessToken() {
        return wenXinProperties.getAccessToken();
    }

    private String getCustomURL() {
        return wenXinProperties.getOpen_LLaMA_7B_URL();
    }

    @Override
    public String getCustomAccessToken() {
        return accessToken != null ? accessToken : getAccessToken();
    }

    @Override
    public Map<String, Deque<Message>> getMessageHistoryMap() {
        return OPEN_LLAMA_7B_MESSAGES_HISTORY_MAP;
    }

    @Override
    public void initMessageHistoryMap(Map<String, Deque<Message>> map) {
        OPEN_LLAMA_7B_MESSAGES_HISTORY_MAP = map;
    }

    @Override
    public String getURL() {
        return getCustomURL();
    }

    @Override
    public void setCustomAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String getTag() {
        return TAG;
    }
}
