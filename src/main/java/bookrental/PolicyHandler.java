package bookrental;

import bookrental.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{

    @Autowired
    StockRepository stockRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRequested_Checkstock(@Payload Requested requested){

        if(requested.getEventType().equals("requested")) {
            System.out.println("=============================");
            System.out.println("requested");
            stockRepository.findById(requested.getBookid())
                    .ifPresent(
                            stock -> {
                                long qty = stock.getQty();

                                if(qty > 1){
                                    stock.setQty(qty-1);
                                    stockRepository.save(stock);
                                    System.out.println("set Stock -1");
                                } else {
                                    System.out.println("stock-out");
                                }
                            }
                    )
            ;

            System.out.println("=============================");
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCanceled_Cancelstock(@Payload Canceled canceled){

        if(canceled.getEventType().equals("canceled")) {
            System.out.println("=============================");
            System.out.println("canceled");

            stockRepository.findById(canceled.getBookid())
                    .ifPresent(
                            stock -> {
                                stock.setQty(stock.getQty() + 1);
                                stockRepository.save(stock);
                            }
                    )
            ;
            System.out.println("set Stock +1");
            System.out.println("=============================");
        }
    }
}
