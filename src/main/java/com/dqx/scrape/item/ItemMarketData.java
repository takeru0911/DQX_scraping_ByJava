package com.dqx.scrape.item;

import com.dqx.scrape.MarketData;

public class ItemMarketData extends MarketData{
	private int _star;

	public ItemMarketData(int id, int price, String date, int exhibits, String itemName, int star){
		super(id, price, date, exhibits, itemName);
		this._star = star;
	}

	@Override
	public String toString() {
		return "ItemMarketData [_star=" + _star + ", _id=" + _id + ", _price="
				+ _price + ", _date=" + _date + ", _exhibits=" + _exhibits
				+ ", _itemName=" + _itemName + "]";
	}

	@Override
	public boolean checkRequire(){
		boolean result = super.checkRequire();
		if(_star == -1){
			result = false;
		}

		return result;
	}

	public int get_Star() {
		return _star;
	}

	public void setStar(int star) {
		this._star = star;
	}


}
