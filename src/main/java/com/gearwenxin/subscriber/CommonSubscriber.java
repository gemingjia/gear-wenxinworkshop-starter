package com.gearwenxin.subscriber;

import com.gearwenxin.common.WenXinUtils;
import com.gearwenxin.entity.Message;
import com.gearwenxin.entity.response.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.Disposable;
import reactor.core.publisher.FluxSink;

import java.util.Queue;

import static com.gearwenxin.common.WenXinUtils.buildAssistantMessage;

/**
 * @author Ge Mingjia
 * @date 2023/7/20
 */
@Slf4j
public class CommonSubscriber implements Subscriber<ChatResponse>, Disposable {

    private final FluxSink<ChatResponse> emitter;
    private Subscription subscription;
    private final Queue<Message> messagesHistory;

    private final StringBuffer stringBuffer = new StringBuffer();

    public CommonSubscriber(FluxSink<ChatResponse> emitter, Queue<Message> messagesHistory) {
        this.emitter = emitter;
        this.messagesHistory = messagesHistory;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
        log.info("onSubscribe ==========>");
    }

    @Override
    public void onNext(ChatResponse response) {
        log.info("onNext ==========>");
        String partResult = response.getResult();
        // 消费一条任务
        subscription.request(1);
        if (!StringUtils.isBlank(partResult)) {
            // 拼接每一部分的消息
            stringBuffer.append(partResult);
        }
        emitter.next(response);
    }

    @Override
    public void onError(Throwable throwable) {
        log.info("onError ==========>");
        // 如果出现错误，中断后立即保存当前已有部分
        String errPartResult = stringBuffer.toString();
        Message message = buildAssistantMessage(errPartResult);
        WenXinUtils.offerMessage(messagesHistory, message);
        emitter.error(throwable);
    }

    @Override
    public void onComplete() {
        log.info("onComplete ==========>");
        String allResult = stringBuffer.toString();
        Message message = buildAssistantMessage(allResult);
        WenXinUtils.offerMessage(messagesHistory, message);
        emitter.complete();
    }

    @Override
    public void dispose() {
        log.info("dispose ==========>");
        subscription.cancel();
    }

    @Override
    public boolean isDisposed() {
        return Disposable.super.isDisposed();
    }
}