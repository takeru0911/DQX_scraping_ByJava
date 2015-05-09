package com.dqx.scrape;

public abstract class MarketData {
	protected int _id = -1;
	protected long _price = -1;
	protected String _date = null;
	protected int _exhibits = -1;
	protected String _itemName = null;

	public MarketData(int id, int price, String date, int exhibits, String itemName){
		this._id = id;
		this._price = price;
		this._date = date;
		this._exhibits = exhibits;
		this._itemName = itemName;
	}

	public boolean checkRequire(){
		boolean satisfy = true;

		if(this._id == -1){
			satisfy = false;
		}
		if(this._price == -1){
			satisfy = false;
		}
		if(this._exhibits == -1){
			satisfy = false;
		}
		if(this._date == null){
			satisfy = false;
		}
		if(this._itemName == null){
			satisfy = false;
		}

		return satisfy;
	}

	@Override
	public String toString() {
		return "MarketData [_id=" + _id + ", _price=" + _price + ", _date="
				+ _date + ", _exhibits=" + _exhibits + ", _itemName="
				+ _itemName + "]";
	}

	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public long get_price() {
		return _price;
	}
	public void set_price(long _price) {
		this._price = _price;
	}
	public String get_date() {
		return _date;
	}
	public void set_date(String _date) {
		this._date = _date;
	}
	public int get_exhibits() {
		return _exhibits;
	}
	public void set_exhibits(int _exhibits) {
		this._exhibits = _exhibits;
	}
	public String get_itemName() {
		return _itemName;
	}
	public void set_itemName(String _itemName) {
		this._itemName = _itemName;
	}


}
