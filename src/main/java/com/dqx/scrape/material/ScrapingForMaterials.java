package com.dqx.scrape.material;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dqx.scrape.MarketData;
import com.dqx.scrape.ScrapingDQX;

public class ScrapingForMaterials implements ScrapingDQX{
	private List<MarketData> _scrapeData;
	private String _materialName;
	private String _date = null;
	private int _id;
	private MaterialMarketData _data;

	public ScrapingForMaterials(String name, int id) {
		this._id = id;
		this._materialName = name;
	}

	@Override
	public List<MarketData> scrape(URL fetchURL, String startDate,
			String endDate) {
		try{
			_scrapeData = new ArrayList<MarketData>();
			Document doc = Jsoup.connect(fetchURL.toString()).get();
			Elements trElements = doc.getElementsByTag("tr");

			//extract td Tag elements from tr Elements.
			List<Elements> tdElementsLst = trElements.stream()
					.map(tr -> tr.getElementsByTag("td"))
					.collect(Collectors.toList());

			for(Elements tdElements: tdElementsLst){
				tdElements.forEach(el -> {
					extractDate(el);
					if(_data != null){
						createMaterialMarketData(el);

						if(_data.checkRequire()){
							if(Long.parseLong(_data.get_date()) > Long.parseLong(startDate) && Long.parseLong(_data.get_date()) < Long.parseLong(endDate)){
								this._scrapeData.add(_data);
								_data = null;
							}
						}
					}
				});
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return _scrapeData;
	}

	private void createMaterialMarketData(Element el) {
		int price = extractPrice(el);
		int exhibits = extractExhibits(el);

		if(price != -1){
			_data.set_price(price);
		}
		if(exhibits != -1){
			_data.set_exhibits(exhibits);
		}
	}

	private int extractExhibits(Element el){
		int exhibits = -1;
		Elements n2s = el.select(".n2").select(".r");
		if(n2s.size() == 0){

		}else{
			try{
				String text = n2s.get(0).text();
				exhibits = Integer.parseInt(text.replaceAll(" 件", ""));
			}catch(Exception e){
				exhibits = -1;
			}
		}
		return exhibits;
	}

	private int extractPrice(Element el){
		int price = -1;

		Elements prices = el.select(".d1");
		if(prices.size() == 0){

		}else{
			String text = prices.get(0).text();
			try{
				price = Integer.parseInt(text.replaceAll(",", "").replaceAll(" G", ""));
			}catch(Exception e){
				price = -1;
			}
		}

		return price;
	}

	private void extractDate(Element el){
		Elements trs = el.getElementsByTag("tr");
		trs.forEach(tr -> {
			String text = tr.text();
			if(text.contains("時")){
				_date = text.split("（")[1].split("）")[0].replaceAll(" ", "").replaceAll("時", "").replaceAll("/", "") + "0000";
				_data = new MaterialMarketData(_id, -1, _date, -1, _materialName);
			}
		});

	}

	public static void main(String[] args) throws MalformedURLException {
		List<MarketData> datas = new ScrapingForMaterials("takeru", 3).scrape(new URL("http://grooowl.com/r/dqx/bazaar_list_histories/history2/2395/2015-04/0"), "150421000000", "150428000000");
		datas.forEach(System.out::println);
	}
}
