package guru.springframework.sfgjms.listener;

import guru.springframework.sfgjms.config.JmsConfig;
import guru.springframework.sfgjms.model.HelloWorldMessage;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Component
public class HelloMessageListener {
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.QUEUE_NAME)
    public void listen(@Payload HelloWorldMessage helloWorldMessage,
                       @Headers MessageHeaders headers,
                       Message message) {

        log.info("RECEIVED Message {}\\n\\tWith Headers {}\\n\\tAnd JMS Message {}", helloWorldMessage, headers, message);
    }

    @JmsListener(destination = JmsConfig.QUEUE_SR_NAME)
    public void listenAndReply(@Payload HelloWorldMessage helloWorldMessage,
                       @Headers MessageHeaders headers,
                       Message message) throws JMSException {

        log.info("RECEIVED Message {}\\n\\tWith Headers {}\\n\\tAnd JMS Message {}", helloWorldMessage, headers, message);

        HelloWorldMessage payload = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("Good morning World! I'm Replying too")
                .build();

        jmsTemplate.convertAndSend(message.getJMSReplyTo(), payload);
        log.info("Replay Message with: {}", payload);
    }
}
