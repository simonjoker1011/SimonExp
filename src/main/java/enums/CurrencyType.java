package enums;

import java.util.Currency;
import java.util.Locale;

public enum CurrencyType {
    USD(Currency.getInstance(Locale.US).getCurrencyCode(), Locale.US.getDisplayName(), Currency.getInstance(Locale.US).getDisplayName()),
    TWD(Currency.getInstance(Locale.TAIWAN).getCurrencyCode(), Locale.TAIWAN.getDisplayName(), Currency.getInstance(Locale.TAIWAN).getDisplayName()),
    JPY(Currency.getInstance(Locale.JAPAN).getCurrencyCode(), Locale.JAPAN.getDisplayName(), Currency.getInstance(Locale.JAPAN).getDisplayName()),
    THB(Currency.getInstance(new Locale("th", "TH")).getCurrencyCode(), new Locale("th", "TH").getDisplayName(), Currency.getInstance(new Locale("th", "TH")).getDisplayName()),
    KPW(Currency.getInstance(Locale.KOREA).getCurrencyCode(), Locale.KOREA.getDisplayName(), Currency.getInstance(Locale.KOREA).getDisplayName()),
    GBP(Currency.getInstance(Locale.UK).getCurrencyCode(), Locale.UK.getDisplayName(), Currency.getInstance(Locale.UK).getDisplayName());

    private final String currencyCode;
    private final String country;
    private final String currencyName;

    private CurrencyType(String currencyCode, String country, String currencyName) {
        this.currencyCode = currencyCode;
        this.country = country;
        this.currencyName = currencyName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getCountry() {
        return country;
    }

    public String getCurrencyName() {
        return currencyName;
    }
}
