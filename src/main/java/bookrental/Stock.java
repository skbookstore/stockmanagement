package bookrental;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
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
        BeanUtils.copyProperties(this, incomed);
        incomed.publishAfterCommit();


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
