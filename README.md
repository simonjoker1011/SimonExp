TODO:

0.
timer task issues:

a. init 
b. timers management
c. timer binds to trigger and tasks
d. another thread to run the task
e. when destroy (or stop) the timer

1.
tune page behavior: 
	buying, selling

add curr type: gold
gold csv url: http://rate.bot.com.tw/gold/csv/2005-01/TWD/0


2.
strategy api

3.
lottery api


ISSUES to fix:
1. temporary bind all job to MasterTimer

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
