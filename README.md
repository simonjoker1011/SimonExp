TODO:


0.
strategies:

1) mean
2) variance
3) standard deviation


add curr type: gold
gold csv url: http://rate.bot.com.tw/gold/csv/2005-01/TWD/0

1.
Recommand IDEs:
VSCode
Atom

2.
strategy api

3.
lottery api


ISSUES to fix:


APIs:


update util today:
http://localhost:8080/simonExp/currency/CurrencyResource/updateTilToday

query currency:
http://localhost:8080/simonExp/currency/CurrencyResource?CurrencyName=USD&Rate=Buying&CashSpot=Spot&StartDate=2001-01-01&EndDate=2001-02-01

import someday currency:
http://localhost:8080/simonExp/currency/CurrencyResource?urlString=http://rate.bot.com.tw/xrt/flcsv/0/2001-01-05?Lang=en-US

import period currency:
http://localhost:8080/simonExp/currency/CurrencyResource/savePeriodCurrency?startdate=2001-01-01&enddate=2001-02-01

examples:
http://localhost:8080/simonExp/webapi/jerseyExample/getfile?urlString=http://rate.bot.com.tw/xrt/flcsv/0/2001-01-12?Lang=en-US

lotteries:
http://localhost:8080/simonExp/lottery/LotteryResource
