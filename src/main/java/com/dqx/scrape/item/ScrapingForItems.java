package com.dqx.scrape.item;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dqx.scrape.MarketData;
import com.dqx.scrape.ScrapingDQX;

public class ScrapingForItems implements ScrapingDQX{
	private List<MarketData> _scrapeData;
	private String _itemName;
	private String _date = null;
	private int _id;
	private ItemMarketData data;
	public ScrapingForItems(String itemName, int id) {
		this._itemName = itemName;
		this._id = id;
	}

	@Override
	public List<MarketData> scrape(URL fetchURL, String startDate, String endDate) {
		try {
			_scrapeData = new ArrayList<MarketData>();
			Document doc = Jsoup.connect(fetchURL.toString()).get();
			Elements trElements = doc.getElementsByTag("tr");
			//extract td Tag elements from tr Elements.
			List<Elements> tdElementsLst = trElements.stream()
					.map(tr -> tr.getElementsByTag("td"))
					.collect(Collectors.toList());

			for(Elements tdElements: tdElementsLst){
				tdElements.forEach(el -> {
					Elements lh1s = el.select(".lh1");
					if(lh1s.size() != 0){
						this.extractDate(lh1s.get(0));
						data = new ItemMarketData(this._id, -1, this._date, -1, this._itemName, -1);
					}
					boolean isStar = createItemMarketData(el, data);
					if(data != null){
						if(data.checkRequire()){
							this._scrapeData.add(data);
							data = new ItemMarketData(_id, -1, _date, -1, _itemName, -1);
						}
						if(isStar){
							int numOfStar = ((ItemMarketData)data).get_Star();
							data = new ItemMarketData(_id, -1, _date, -1, _itemName, numOfStar);
						}
					}
				});
			}

		}catch(IOException e){
			e.printStackTrace();

		}

		return _scrapeData;
	}
	private void extractDate(Element el){
		String text = el.text();
		_date = text.split("（")[1].split("）")[0].replaceAll(" ", "").replaceAll("時", "").replaceAll("/", "") + "0000";
	}

	private boolean createItemMarketData(Element el, ItemMarketData data){
		int numOfStar = extractNumOfStar(el);
		int price = extractPrice(el);
		int exhibits = extractExhibits(el);

		if(numOfStar != -1){
			data.setStar(numOfStar);
		}
		if(price != -1){
			data.set_price(price);
		}
		if(exhibits != -1){
			data.set_exhibits(exhibits);
		}
		return numOfStar == -1 ? false : true;
	}

	private int extractNumOfStar(Element el){
		int numOfStar = -1;

		if(el.getElementsByTag("span").size() != 0){
			Elements els = el.select(".star");
			if(els.size() != 0){
				String starString = els.text();
				//★の数をカウント
				numOfStar = Math.abs(3 - (starString.length() - starString.replace("★", "").length()));
			}
		}else {
			Elements n2s = el.select(".n2");
			if(n2s.size() == 0){

			}else {
				Element n2 = n2s.get(0);
				if(n2.text().equals("★★★")){
					numOfStar = 3;
				}
			}
		}

		return numOfStar;
	}

	private int extractPrice(Element el){
		int price = -1;

		if(el.select(".d1").size() == 0){

		}else{
			try{
				price = Integer.parseInt(el.text().split(" ")[0].replaceAll(",", ""));
			}catch(Exception e){
				price = -1;
			}
		}

		return price;
	}

	private int extractExhibits(Element el){
		int exhibits = -1;
		Elements n2s = el.select(".n2");
		if(n2s.size() == 0){

		}else{
			Element n2 = n2s.get(0);
			String exhibitStr = n2.text().indexOf(" ") == -1 ? "-1": n2.text().split(" ")[0];
			exhibits = Integer.parseInt(exhibitStr);
		}

		return exhibits;
	}
	public static void main(String[] args){
		try {
			List<MarketData> result = new ScrapingForItems("takeru", 11).scrape(new URL("http://grooowl.com/r/dqx/bazaar_list_histories/history2/596/2015-05"), null, null);
			result.forEach(data -> System.out.println(((ItemMarketData)data).toString()));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}


}
