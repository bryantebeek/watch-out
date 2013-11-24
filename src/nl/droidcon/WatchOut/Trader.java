package nl.droidcon.WatchOut;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.btce.BTCEExchange;
import com.xeiam.xchange.service.polling.PollingAccountService;
import com.xeiam.xchange.service.polling.PollingMarketDataService;
import com.xeiam.xchange.service.polling.PollingTradeService;

public class Trader {
   
    Exchange exchange;
    PollingMarketDataService marketService;
    PollingAccountService accountService;
    PollingTradeService tradeService;
    
    public Trader() {
        exchange = BTCEExchange.newInstance();

        ExchangeSpecification specification = exchange.getDefaultExchangeSpecification();
        specification.setApiKey("API_KEY");
        specification.setSecretKey("API_SECRET");
        exchange.applySpecification(specification);

        marketService = exchange.getPollingMarketDataService();
        accountService = exchange.getPollingAccountService();
        tradeService = exchange.getPollingTradeService();
    }
    
}
