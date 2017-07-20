package prj.jersey.simonExp.datas;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CurrencyInfo")
public class CurrencyInfo implements Serializable {

    public CurrencyInfo(Integer currCode, String rate, String cashspot, String currName, Timestamp dateBase) {
        super();
        this.currCode = currCode;
        this.rate = rate;
        this.cashspot = cashspot;
        this.currName = currName;
        this.dateBase = dateBase;
    }

    private static final long serialVersionUID = 1L;

    final static public String EntityName = "CurrencyInfo";

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
    @Column(name = "currName")
    private String currName;

    @Column(name = "historyHigh")
    private Float historyHigh;
    @Column(name = "historyLow")
    private Float historyLow;
    @Column(name = "decadeHigh")
    private Float decadeHigh;
    @Column(name = "decadeLow")
    private Float decadeLow;
    @Column(name = "fourYearHigh")
    private Float fourYearHigh;
    @Column(name = "fourYearLow")
    private Float fourYearLow;
    @Column(name = "yearHigh")
    private Float yearHigh;
    @Column(name = "yearLow")
    private Float yearLow;
    @Column(name = "halfYearHigh")
    private Float halfYearHigh;
    @Column(name = "halfYearLow")
    private Float halfYearLow;
    @Column(name = "seasonHigh")
    private Float seasonHigh;
    @Column(name = "seasonLow")
    private Float seasonLow;
    @Column(name = "monthHigh")
    private Float monthHigh;
    @Column(name = "monthLow")
    private Float monthLow;
    @Column(name = "weekHigh")
    private Float weekHigh;
    @Column(name = "weekLow")
    private Float weekLow;

    @Column(name = "dateBase")
    private Timestamp dateBase;

    public Timestamp getDateBase() {
        return dateBase;
    }

    public void setDateBase(Timestamp dateBase) {
        this.dateBase = dateBase;
    }

    public CurrencyInfo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getCurrName() {
        return currName;
    }

    public void setCurrName(String currName) {
        this.currName = currName;
    }

    public Float getHistoryHigh() {
        return historyHigh;
    }

    public void setHistoryHigh(Float historyHigh) {
        this.historyHigh = historyHigh;
    }

    public Float getHistoryLow() {
        return historyLow;
    }

    public void setHistoryLow(Float historyLow) {
        this.historyLow = historyLow;
    }

    public Float getDecadeHigh() {
        return decadeHigh;
    }

    public void setDecadeHigh(Float decadeHigh) {
        this.decadeHigh = decadeHigh;
    }

    public Float getDecadeLow() {
        return decadeLow;
    }

    public void setDecadeLow(Float decadeLow) {
        this.decadeLow = decadeLow;
    }

    public Float getFourYearHigh() {
        return fourYearHigh;
    }

    public void setFourYearHigh(Float fourYearHigh) {
        this.fourYearHigh = fourYearHigh;
    }

    public Float getFourYearLow() {
        return fourYearLow;
    }

    public void setFourYearLow(Float fourYearLow) {
        this.fourYearLow = fourYearLow;
    }

    public Float getYearHigh() {
        return yearHigh;
    }

    public void setYearHigh(Float yearHigh) {
        this.yearHigh = yearHigh;
    }

    public Float getYearLow() {
        return yearLow;
    }

    public void setYearLow(Float yearLow) {
        this.yearLow = yearLow;
    }

    public Float getHalfYearHigh() {
        return halfYearHigh;
    }

    public void setHalfYearHigh(Float halfYearHigh) {
        this.halfYearHigh = halfYearHigh;
    }

    public Float getHalfYearLow() {
        return halfYearLow;
    }

    public void setHalfYearLow(Float halfYearLow) {
        this.halfYearLow = halfYearLow;
    }

    public Float getSeasonHigh() {
        return seasonHigh;
    }

    public void setSeasonHigh(Float seasonHigh) {
        this.seasonHigh = seasonHigh;
    }

    public Float getSeasonLow() {
        return seasonLow;
    }

    public void setSeasonLow(Float seasonLow) {
        this.seasonLow = seasonLow;
    }

    public Float getMonthHigh() {
        return monthHigh;
    }

    public void setMonthHigh(Float monthHigh) {
        this.monthHigh = monthHigh;
    }

    public Float getMonthLow() {
        return monthLow;
    }

    public void setMonthLow(Float monthLow) {
        this.monthLow = monthLow;
    }

    public Float getWeekHigh() {
        return weekHigh;
    }

    public void setWeekHigh(Float weekHigh) {
        this.weekHigh = weekHigh;
    }

    public Float getWeekLow() {
        return weekLow;
    }

    public void setWeekLow(Float weekLow) {
        this.weekLow = weekLow;
    }

}
