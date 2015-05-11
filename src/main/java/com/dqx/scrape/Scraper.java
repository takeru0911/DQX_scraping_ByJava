package com.dqx.scrape;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.dqx.scrape.material.ScrapingForMaterials;

public class Scraper {
	public static void main(String[] args) {
		ScrapingDQX scraper = new ScrapingForMaterials("どうのこうせき", 1);
		List<MarketData> d = new ArrayList<MarketData>();
		try {
			for(int i = 0; i < 3; i++){
				List<MarketData> datas = scraper.scrape(new URL("http://grooowl.com/r/dqx/bazaar_list_histories/history2/2395/2015-04/" + i), "0", "999999999999999999");
				d.addAll(datas);
			}
		} catch (MalformedURLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		d.forEach(data -> System.out.println(data.toString()));
	}
}
