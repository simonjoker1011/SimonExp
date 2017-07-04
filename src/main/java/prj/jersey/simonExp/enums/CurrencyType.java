package prj.jersey.simonExp.enums;

import java.util.Currency;

public enum CurrencyType {
    USD(Currency.getInstance("USD").getNumericCode(), Currency.getInstance("USD").getCurrencyCode()),
    HKD(Currency.getInstance("HKD").getNumericCode(), Currency.getInstance("HKD").getCurrencyCode()),
    GBP(Currency.getInstance("GBP").getNumericCode(), Currency.getInstance("GBP").getCurrencyCode()),
    AUD(Currency.getInstance("AUD").getNumericCode(), Currency.getInstance("AUD").getCurrencyCode()),
    CAD(Currency.getInstance("CAD").getNumericCode(), Currency.getInstance("CAD").getCurrencyCode()),
    SGD(Currency.getInstance("SGD").getNumericCode(), Currency.getInstance("SGD").getCurrencyCode()),
    CHF(Currency.getInstance("CHF").getNumericCode(), Currency.getInstance("CHF").getCurrencyCode()),
    JPY(Currency.getInstance("JPY").getNumericCode(), Currency.getInstance("JPY").getCurrencyCode()),
    ZAR(Currency.getInstance("ZAR").getNumericCode(), Currency.getInstance("ZAR").getCurrencyCode()),
    SEK(Currency.getInstance("SEK").getNumericCode(), Currency.getInstance("SEK").getCurrencyCode()),
    NZD(Currency.getInstance("NZD").getNumericCode(), Currency.getInstance("NZD").getCurrencyCode()),
    THB(Currency.getInstance("THB").getNumericCode(), Currency.getInstance("THB").getCurrencyCode()),
    PHP(Currency.getInstance("PHP").getNumericCode(), Currency.getInstance("PHP").getCurrencyCode()),
    IDR(Currency.getInstance("IDR").getNumericCode(), Currency.getInstance("IDR").getCurrencyCode()),
    EUR(Currency.getInstance("EUR").getNumericCode(), Currency.getInstance("EUR").getCurrencyCode()),
    MYR(Currency.getInstance("MYR").getNumericCode(), Currency.getInstance("MYR").getCurrencyCode()),
    KRW(Currency.getInstance("KRW").getNumericCode(), Currency.getInstance("KRW").getCurrencyCode()),
    TWD(Currency.getInstance("TWD").getNumericCode(), Currency.getInstance("TWD").getCurrencyCode());

    private final Integer currencyCode;
    private final String currencyName;

    private CurrencyType(Integer currencyCode, String currencyName) {
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
    }

    public Integer getCurrencyCode() {
        return currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public static Integer getCurrencyCodeByName(String name) throws Exception {
        for (CurrencyType c : CurrencyType.values()) {
            if (c.getCurrencyName().equals(name)) {
                return c.getCurrencyCode();
            }
        }
        throw new Exception();
    }
}
