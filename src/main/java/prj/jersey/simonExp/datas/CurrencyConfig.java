package prj.jersey.simonExp.datas;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CurrencyConfig")
public class CurrencyConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    final static public String EntityName = "CurrencyConfig";

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    @Column(name = "updateDate")
    private Timestamp updateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public CurrencyConfig() {
    }

}
