package prj.jersey.simonExp.datas;

import java.sql.Timestamp;

public class CurrencyData {

    private static final long serialVersionUID = 1L;

    final static public String EntityName = "CurrencyRate";

    private Integer currCode;
    private String rate;
    private String cashspot;
    private Timestamp rateDate;
    private String currName;
    private Float price;
    private String fetchUrl;

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
