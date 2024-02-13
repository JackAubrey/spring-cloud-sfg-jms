package guru.springframework.sfgjms.sender;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.sfgjms.config.JmsConfig;
import guru.springframework.sfgjms.model.HelloWorldMessage;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class HelloSender {
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    //@Scheduled(fixedRate = 5000)
    public void sendMessage() {
        HelloWorldMessage message = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("Hello World!")
                .build();

        jmsTemplate.convertAndSend(JmsConfig.QUEUE_NAME, message);
        log.info("Sent message: {}", message);
    }

    @Scheduled(fixedRate = 2000)
    public void sendAndReceiveMessage() throws JMSException {
        HelloWorldMessage message = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("Hello World!")
                .build();

        Message receivedMessage = jmsTemplate.sendAndReceive(JmsConfig.QUEUE_SR_NAME, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    Message helloMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
                    helloMessage.setStringProperty("_type", message.getClass().getName());
                    log.info("SENDING and waiting for response message: {}", message);
                    return helloMessage;
                } catch (Exception e) {
                    throw new JMSException("creating message error", "500", e);
                }
            }
        });

        log.info("RETURNED Message REPLY {}", receivedMessage.getBody(String.class));
    }
}
