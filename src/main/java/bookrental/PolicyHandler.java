package bookrental;

import bookrental.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRequested_Checkstock(@Payload Requested requested){

        if(requested.isMe()){
            System.out.println("##### listener Checkstock : " + requested.toJson());
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCanceled_Cancelstock(@Payload Canceled canceled){

        if(canceled.isMe()){
            System.out.println("##### listener Cancelstock : " + canceled.toJson());
        }
    }

}
