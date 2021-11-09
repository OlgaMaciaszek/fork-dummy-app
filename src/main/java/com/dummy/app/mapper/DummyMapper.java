package com.dummy.app.mapper;

import java.util.function.Function;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.dummy.app.api.InputClass;
import com.dummy.app.api.OutputClass;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DummyMapper implements Function<Message<InputClass>, Message<OutputClass>> {

    @Override
    public Message<OutputClass> apply(Message<InputClass> message) {
        String key = (String) message.getHeaders().get("kafka_receivedMessageKey");
        if (!StringUtils.hasLength(key)) {
            log.error("Message with empty key");
            return null;
        }

        return MessageBuilder
                .withPayload(OutputClass.builder().outputVariable("someInfo").build())
                .setHeader("kafka_messageKey", key)
                .build();
    }
}
