package prj.jersey.simonExp.datas;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CurrencyRate")
public class CurrencyData implements Serializable {

    private static final long serialVersionUID = 1L;

    final static public String EntityName = "CurrencyRate";

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    @Column(name = "currCode")
    private Integer currCode;
    @Column(name = "rate")
    private String rate;
    @Column(name = "cashspot")
    private String cashspot;
    @Column(name = "rateDate")
    private Timestamp rateDate;
    @Column(name = "currName")
    private String currName;
    @Column(name = "price")
    private Float price;
    @Column(name = "fetchUrl")
    private String fetchUrl;

    public CurrencyData() {
    }

    public CurrencyData(Integer id, Integer currCode, String rate, String cashspot, Timestamp rateDate, String currName, Float price, String fetchUrl) {
        super();
        this.id = id;
        this.currCode = currCode;
        this.rate = rate;
        this.cashspot = cashspot;
        this.rateDate = rateDate;
        this.currName = currName;
        this.price = price;
        this.fetchUrl = fetchUrl;
    }

    public CurrencyData(Integer currCode, String rate, String cashspot, Timestamp rateDate, String currName, Float price, String fetchUrl) {
        super();
        this.currCode = currCode;
        this.rate = rate;
        this.cashspot = cashspot;
        this.rateDate = rateDate;
        this.currName = currName;
        this.price = price;
        this.fetchUrl = fetchUrl;
    }

    public Integer getCurrCode() {
        return currCode;
    }

    public void setCurrCode(Integer currCode) {
        this.currCode = currCode;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getCashspot() {
        return cashspot;
    }

    public void setCashspot(String cashspot) {
        this.cashspot = cashspot;
    }

    public Timestamp getRateDate() {
        return rateDate;
    }

    public void setRateDate(Timestamp rateDate) {
        this.rateDate = rateDate;
    }

    public String getCurrName() {
        return currName;
    }

    public void setCurrName(String currName) {
        this.currName = currName;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getFetchUrl() {
        return fetchUrl;
    }

    public void setFetchUrl(String fetchUrl) {
        this.fetchUrl = fetchUrl;
    }
}
