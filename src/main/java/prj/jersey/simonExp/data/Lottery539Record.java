package prj.jersey.simonExp.data;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Lottery539Record implements Serializable {

    private static final long serialVersionUID = 1L;

    final static public String EntityName = "Lottery539Record";

    @Id
    @GeneratedValue
    private Integer id;

    private Timestamp date;

    private Integer serialNumber;

    private Integer winningNb1;

    private Integer winningNb2;

    private Integer winningNb3;

    private Integer winningNb4;

    private Integer winningNb5;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getWinningNb1() {
        return winningNb1;
    }

    public void setWinningNb1(Integer winningNb1) {
        this.winningNb1 = winningNb1;
    }

    public Integer getWinningNb2() {
        return winningNb2;
    }

    public void setWinningNb2(Integer winningNb2) {
        this.winningNb2 = winningNb2;
    }

    public Integer getWinningNb3() {
        return winningNb3;
    }

    public void setWinningNb3(Integer winningNb3) {
        this.winningNb3 = winningNb3;
    }

    public Integer getWinningNb4() {
        return winningNb4;
    }

    public void setWinningNb4(Integer winningNb4) {
        this.winningNb4 = winningNb4;
    }

    public Integer getWinningNb5() {
        return winningNb5;
    }

    public void setWinningNb5(Integer winningNb5) {
        this.winningNb5 = winningNb5;
    }
}
