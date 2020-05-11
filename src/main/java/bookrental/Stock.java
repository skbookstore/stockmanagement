package bookrental;

import javax.persistence.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

import java.util.List;

@Entity
@Table(name="Stock_table")
public class Stock {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String bookid;
    private Long qty;

    @PostPersist
    public void onPostPersist(){
        Incomed incomed = new Incomed();
        incomed.setId(this.getId());
        incomed.setBookid(this.getBookid());
        incomed.setQty(incomed.getQty() + this.getQty());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(incomed);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }


        Processor processor = Application.applicationContext.getBean(Processor.class);
        MessageChannel outputChannel = processor.output();

        outputChannel.send(MessageBuilder
                .withPayload(json)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());


        //BeanUtils.copyProperties(this, incomed);
        //incomed.publishAfterCommit();

    }

    @PostUpdate
    public void onPostUpdate(){
        Revsuccessed revsuccessed = new Revsuccessed();
        BeanUtils.copyProperties(this, revsuccessed);
        revsuccessed.publishAfterCommit();


        Revfailed revfailed = new Revfailed();
        BeanUtils.copyProperties(this, revfailed);
        revfailed.publishAfterCommit();


        Revcanceled revcanceled = new Revcanceled();
        BeanUtils.copyProperties(this, revcanceled);
        revcanceled.publishAfterCommit();


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }
    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }




}
