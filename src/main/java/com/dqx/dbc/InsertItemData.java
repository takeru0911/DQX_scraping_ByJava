package com.dqx.dbc;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.dqx.scrape.MarketData;
import com.dqx.scrape.item.ItemMarketData;
import com.dqx.scrape.item.ScrapingForItems;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class InsertItemData {
	public static final String DB_URL = "jdbc:mysql://localhost:3306/dqx";
	public static final String DB_USER = "takeru";
	public static final String DB_PASS = "takeru911";

	public static void main(String[] args) throws SQLException {
		MapHandler mapHandler = new MapHandler();
		ResultSetHandler<?> rsh = new MapListHandler();
		MysqlDataSource dataSource = new MysqlDataSource();

		dataSource.setUrl(DB_URL);
		dataSource.setUser(DB_USER);
		dataSource.setPassword(DB_PASS);


		QueryRunner runner = new QueryRunner(dataSource);

		List<Map<String, Object>> itemList = (List<Map<String, Object>>)runner.query("select * from item", rsh);

		for(Map<String, Object> item : itemList){
			List<Map<String, Object>> dateList = (List<Map<String, Object>>)runner.query("select max(date) as date from market_item where item_id=?",new String[]{"" + item.get("id")},  rsh);
			Timestamp ts = (Timestamp) dateList.get(0).get("date");
			System.out.println(ts.toString());

			Date date = new Date(System.currentTimeMillis());
			String year = "" + (date.getYear() + 1900);
			String month = "" + (date.getMonth() + 1);
			month = month.length() == 1 ? "0" + month : month;
			String fetchDate = year + "-" + month;

			String time = ts.toString();
			String start = time.substring(2, time.length() - 2).replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
			String url = (String)item.get("url") + "/" + fetchDate;
			url = url.replaceAll("detail", "history2");
			ScrapingForItems scraper = new ScrapingForItems((String)item.get("name"), (((Integer)item.get("id")).intValue()));
			System.out.println(url);
			//
			List<MarketData> d = new ArrayList<MarketData>();
			try {
				for(int i = 0; i < 3; i++){
					List<MarketData> datas = scraper.scrape(new URL(url + "/" + i), start, "999999999999999999");
					System.out.println();
					d.addAll(datas);
				}
			} catch (MalformedURLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			d.forEach(data -> {
				try {
					runner.update(
							"INSERT INTO market_item "
									+ "(stars, date, price, exhibits, item_id) "
									+ "VALUES("
									+ "" + ((ItemMarketData)data).get_Star()
								    + "," + data.get_date()
								    + "," + data.get_price()
								    + "," +data.get_exhibits()
								    + "," +data.get_id()
								    + ")"
									);
				} catch (Exception e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}

			});

		}
	}


}
