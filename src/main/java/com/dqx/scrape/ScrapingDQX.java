package com.dqx.scrape;

import java.net.URL;
import java.util.List;

public interface ScrapingDQX {
	abstract public List<MarketData> scrape(URL fetchURL, String startDate, String endDate);
}
