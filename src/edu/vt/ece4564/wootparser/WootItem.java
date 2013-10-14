package edu.vt.ece4564.wootparser;

import java.util.HashMap;

public class WootItem {
	private String id_;
	private Double salePrice_;
	private Double listPrice_;
	private HashMap<String, String> attributes_ = new HashMap<String, String>();
	private Long purchaseLimit_;
	
	public String getId() {
		return id_;
	}
	public void setId(String id_) {
		this.id_ = id_;
	}
	public Double getSalePrice() {
		return salePrice_;
	}
	public void setSalePrice(Double salePrice_) {
		this.salePrice_ = salePrice_;
	}
	public Double getListPrice() {
		return listPrice_;
	}
	public void setListPrice(Double listPrice_) {
		this.listPrice_ = listPrice_;
	}
	public HashMap<String, String> getAttributes() {
		return attributes_;
	}
	public void setAttributes(HashMap<String, String> attributes_) {
		this.attributes_ = attributes_;
	}
	public String getAttribute(String key) {
		return this.attributes_.get(key);
	}
	public void setAttribute(String key, String value) {
		this.attributes_.put(key, value);
	}
	public Long getPurchaseLimit() {
		return purchaseLimit_;
	}
	public void setPurchaseLimit(Long purchaseLimit_) {
		this.purchaseLimit_ = purchaseLimit_;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder("[WootItem: $");
		b.append(salePrice_).append("]");
		return b.toString();
	}
}
