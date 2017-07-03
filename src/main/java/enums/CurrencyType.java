package enums;

import java.util.Currency;

public enum CurrencyType {
    USD(Currency.getInstance("USD").getCurrencyCode(), Currency.getInstance("USD").getDisplayName()),
    HKD(Currency.getInstance("HKD").getCurrencyCode(), Currency.getInstance("HKD").getDisplayName()),
    GBP(Currency.getInstance("GBP").getCurrencyCode(), Currency.getInstance("GBP").getDisplayName()),
    AUD(Currency.getInstance("AUD").getCurrencyCode(), Currency.getInstance("AUD").getDisplayName()),
    CAD(Currency.getInstance("CAD").getCurrencyCode(), Currency.getInstance("CAD").getDisplayName()),
    SGD(Currency.getInstance("SGD").getCurrencyCode(), Currency.getInstance("SGD").getDisplayName()),
    CHF(Currency.getInstance("CHF").getCurrencyCode(), Currency.getInstance("CHF").getDisplayName()),
    JPY(Currency.getInstance("JPY").getCurrencyCode(), Currency.getInstance("JPY").getDisplayName()),
    ZAR(Currency.getInstance("ZAR").getCurrencyCode(), Currency.getInstance("ZAR").getDisplayName()),
    SEK(Currency.getInstance("SEK").getCurrencyCode(), Currency.getInstance("SEK").getDisplayName()),
    NZD(Currency.getInstance("NZD").getCurrencyCode(), Currency.getInstance("NZD").getDisplayName()),
    THB(Currency.getInstance("THB").getCurrencyCode(), Currency.getInstance("THB").getDisplayName()),
    PHP(Currency.getInstance("PHP").getCurrencyCode(), Currency.getInstance("PHP").getDisplayName()),
    IDR(Currency.getInstance("IDR").getCurrencyCode(), Currency.getInstance("IDR").getDisplayName()),
    EUR(Currency.getInstance("EUR").getCurrencyCode(), Currency.getInstance("EUR").getDisplayName()),
    MYR(Currency.getInstance("MYR").getCurrencyCode(), Currency.getInstance("MYR").getDisplayName()),
    KRW(Currency.getInstance("KRW").getCurrencyCode(), Currency.getInstance("KRW").getDisplayName()),
    TWD(Currency.getInstance("TWD").getCurrencyCode(), Currency.getInstance("TWD").getDisplayName());

    private final String currencyCode;
    private final String currencyName;

    private CurrencyType(String currencyCode, String currencyName) {
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }
}
