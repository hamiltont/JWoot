package edu.vt.ece4564.wootparser;

import java.util.ArrayList;

/**
 * A horrible amount of Java boilerplate code to represent a Woot.com Offer
 * 
 * @author hamiltont
 * 
 */
public class WootOffer {
	private String features_;
	private String id_;
	private String url_;
	private Long percentageRemaining_;
	private Boolean soldOut_;
	private String specs_;
	private String subtitle_;
	private String teaser_;
	private String title_;
	private String writeUp_;
	private String stats_;
	private ArrayList<WootItem> items_ = new ArrayList<WootItem>(2);

	public String getFeatures() {
		return features_;
	}

	public void setFeatures(String features_) {
		this.features_ = features_;
	}

	public String getId() {
		return id_;
	}

	public void setId(String id_) {
		this.id_ = id_;
	}

	public ArrayList<WootItem> getItems() {
		return items_;
	}

	public void setItems(ArrayList<WootItem> items_) {
		this.items_ = items_;
	}

	public String getUrl() {
		return url_;
	}

	public void setUrl(String url_) {
		this.url_ = url_;
	}

	public Long getPercentageRemaining() {
		return percentageRemaining_;
	}

	public void setPercentageRemaining(Long percentageRemaining_) {
		this.percentageRemaining_ = percentageRemaining_;
	}

	public Boolean getSoldOut() {
		return soldOut_;
	}

	public void setSoldOut(Boolean soldOut_) {
		this.soldOut_ = soldOut_;
	}

	public String getSpecs() {
		return specs_;
	}

	public void setSpecs(String specs_) {
		this.specs_ = specs_;
	}

	public String getSubtitle() {
		return subtitle_;
	}

	public void setSubtitle(String subtitle_) {
		this.subtitle_ = subtitle_;
	}

	public String getTeaser() {
		return teaser_;
	}

	public void setTeaser(String teaser_) {
		this.teaser_ = teaser_;
	}

	public String getTitle() {
		return title_;
	}

	public void setTitle(String title_) {
		this.title_ = title_;
	}

	public String getWriteUp() {
		return writeUp_;
	}

	public void setWriteUp(String writeUp_) {
		this.writeUp_ = writeUp_;
	}

	public String getStats() {
		return stats_;
	}

	public void setStats(String stats_) {
		this.stats_ = stats_;
	}

	public String toString() {
		StringBuilder b = new StringBuilder("[WootOffer: ");
		b.append(title_).append(", ").append(items_.size()).append(" items]");
		return b.toString();
	}

}
