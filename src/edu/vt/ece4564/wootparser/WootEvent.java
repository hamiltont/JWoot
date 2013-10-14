package edu.vt.ece4564.wootparser;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Randall Ferrance
 * @contributor Hamilton Turner
 */
public class WootEvent {
	private String endDate;
	private String ID;
	private ArrayList<WootOffer> offers = new ArrayList<WootOffer>(2);
	private String startDate;
	private String site;
	private String title;
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public List<WootOffer> getOffers() {
		return offers;
	}

	public void setOffers(ArrayList<WootOffer> offers) {
		this.offers = offers;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	/**
	 * Convenience method to get the Items from the first {@link WootOffer}
	 */
	public ArrayList<WootItem> getFirstItems() {
		return offers.get(0).getItems();
	}

	/**
	 * Convenience method to get the SalePrice of the first {@link WootItem} of
	 * the first {@link WootOffer}
	 */
	public Double getSalePrice() {
		return getFirstItems().get(0).getSalePrice();
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder("[WootEvent: ");
		b.append(title).append(", ").append(offers.size()).append(" offers]");
		return b.toString();
	}
	
}
