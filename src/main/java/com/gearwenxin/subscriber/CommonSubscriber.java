package com.gearwenxin.subscriber;

import com.gearwenxin.core.ChatUtils;
import com.gearwenxin.entity.Message;
import com.gearwenxin.entity.response.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.Disposable;
import reactor.core.publisher.FluxSink;

import java.util.Deque;
import java.util.Optional;
import java.util.StringJoiner;

import static com.gearwenxin.common.WenXinUtils.assertNotNull;
import static com.gearwenxin.common.WenXinUtils.buildAssistantMessage;

/**
 * @author Ge Mingjia
 * {@code @date} 2023/7/20
 */
@Slf4j
public class CommonSubscriber implements Subscriber<ChatResponse>, Disposable {

    private final FluxSink<ChatResponse> emitter;
    private Subscription subscription;
    private final Deque<Message> messagesHistory;
    private final StringJoiner joiner = new StringJoiner("");

    public CommonSubscriber(FluxSink<ChatResponse> emitter, Deque<Message> messagesHistory) {
        this.emitter = emitter;
        this.messagesHistory = messagesHistory;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(15);
        log.debug("onSubscribe");
    }

    @Override
    public void onNext(ChatResponse response) {
        if (isDisposed()) {
            return;
        }

        assertNotNull(response, "ChatResponse is null");

        log.debug("CommonSubscriber.onNext");

        Optional.ofNullable(response.getResult()).ifPresent(joiner::add);
        subscription.request(15);
        emitter.next(response);
    }

    @Override
    public void onError(Throwable throwable) {
        if (isDisposed()) {
            return;
        }

        log.error("onError");
        emitter.error(throwable);
    }

    @Override
    public void onComplete() {
        if (isDisposed()) {
            return;
        }
        log.debug("onComplete");
        String result = joiner.toString();
        Optional.ofNullable(result).filter(StringUtils::isNotBlank).ifPresent(r -> {
            Message message = buildAssistantMessage(r);
            ChatUtils.offerMessage(messagesHistory, message);
            log.debug("offerMessage onComplete");
        });
        emitter.complete();
    }

    @Override
    public void dispose() {
        log.debug("dispose");
        subscription.cancel();
    }

    @Override
    public boolean isDisposed() {
        return Disposable.super.isDisposed();
    }
}