package edu.vt.ece4564.wootparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.ParseException;

/**
 * JSON.simple {@link ContentHandler} to parse Woot API v2. Uses 'Keys' as a
 * term for any of the JSON keys, and special keys that I use to denote certain
 * places in the JSON, such as WOOT_ITEM is a key that denotes the current JSON
 * object we are parsing is an WootItem object.
 * 
 * The {@link WootEventParser} keeps a stack of currently active keys so that it
 * can understand it's place in the JSON, and any non-understood keys are
 * ignored.
 * 
 * @author Hamilton Turner
 */
public class WootEventParser implements ContentHandler {
	public enum Key {
		Type("Event Type"), Id("Id"), Title("Title"), Site("Event Site"), StartDate(
				"Event StartDate"), EndDate("Event EndDate"), Offers(
				"Event Offers"), Features("Offer Features"), Url("Offer URL"), PercentageRemaining(
				"Offer Percentage Remaining"), SoldOut("Offer sold out?"), Specs(
				"Offer Specs"), Items("Offer Items"), Subtitle("Offer subtitle"), Teaser(
				"Offer teaser"), WriteUp("Offer writeup"), Stats("Offer stats"), ListPrice(
				"Item List price"), PurchaseLimit("Item Purchase Limit"), SalePrice(
				"Item Sale Price"), Attributes("Item Attributes"), Key(
				"Item Attribute Key"), Value("Item Attribute Value"), JSON_ARRAY(
				"JSON Array"), WOOT_EVENT("Woot Event"), WOOT_OFFER(
				"Woot Offer"), WOOT_ITEM("Woot Item"), WOOT_ITEM_ATTRIBUTE(
				"Woot Item Attribute"), UNKNOWN_KEY("Unknown_Key"), UNKNOWN_OBJECT(
				"Unknown_Object");

		private final String name;

		private Key(String s) {
			name = s;
		}

		public boolean equalsName(String otherName) {
			return (otherName == null) ? false : name.equals(otherName);
		}

		public String toString() {
			return name;
		}
	}

	/**
	 * Stores all events internally, and once parsing is complete they can be
	 * retrieved
	 */
	public WootEventParser() {
		listener_ = new StoringWootEventListener();
	}

	public WootEventParser(WootEventListener listener) {
		if (listener == null)
			throw new IllegalArgumentException(
					"A WootEventListener is required");

		listener_ = listener;
	}

	private WootEventListener listener_;
	private Stack<Key> currentKey = new Stack<WootEventParser.Key>();
	private WootEvent currentEvent = new WootEvent();
	private WootOffer currentOffer = new WootOffer();
	private WootItem currentItem = new WootItem();
	private WootAttribute currentAttribute = new WootAttribute();

	@Override
	public boolean startObjectEntry(String key) throws ParseException,
			IOException {
		try {
			WLog.d("Adding entry", Key.valueOf(key).name);
			currentKey.push(Key.valueOf(key));
		} catch (IllegalArgumentException iae) {
			WLog.e("Unknown JSON Key encountered:", key);
			currentKey.push(Key.UNKNOWN_KEY);
			WLog.d("Adding entry", Key.UNKNOWN_KEY.name);
		}
		return true;
	}

	/**
	 * Uses the current stack of keys to determine what type of JSON object is
	 * starting. We currently only know about {@link Key#WOOT_EVENT},
	 * {@link Key#WOOT_OFFER}, {@link Key#WOOT_ITEM} as possible JSON objects,
	 * others are tagged as {@link Key#UNKNOWN_OBJECT}
	 */
	@Override
	public boolean startObject() throws ParseException, IOException {
		// Note that the if-else must go in order or more precise to less
		// precise to work properly
		if (currentKey.search(Key.UNKNOWN_KEY) != -1) {
			// We are inside an unknown key and have encountered
			// an object
			currentKey.push(Key.UNKNOWN_OBJECT);
		} else if (currentKey.peek() == Key.JSON_ARRAY
				&& currentKey.search(Key.Attributes) != -1) {
			// We are starting an object inside of an array, and
			// we have seen the key for Item attribute, so we're looking
			// at an item attribute object
			currentKey.push(Key.WOOT_ITEM_ATTRIBUTE);
		} else if (currentKey.peek() == Key.JSON_ARRAY
				&& currentKey.search(Key.Items) != -1) {
			// We are starting an object inside of an array, and
			// we have seen the key for Items, so we're looking
			// at an item object
			currentKey.push(Key.WOOT_ITEM);
		} else if (currentKey.peek() == Key.JSON_ARRAY
				&& currentKey.search(Key.Offers) != -1) {
			// We are starting an object in an array, and we have seen the
			// Offers key, so we are inside the Offers array starting an offer
			currentKey.push(Key.WOOT_OFFER);
		} else if (currentKey.peek() == Key.JSON_ARRAY
				&& currentKey.size() == 1) {
			// We are starting an object in the root array, so it's an event
			currentKey.push(Key.WOOT_EVENT);
		} else {
			// This should never happen in normal operation, because we
			// are inside some key that we didn't push on the stack.
			// Use unknown object to avoid storing anything and log the
			// error
			WLog.e("Encountered an unexpected start of object!");
			currentKey.push(Key.UNKNOWN_OBJECT);
		}
		WLog.d("Adding", currentKey.peek().name);
		return true;
	}

	@Override
	public void startJSON() throws ParseException, IOException {
		WLog.d("Start JSON Parsing");
	}

	@Override
	public boolean startArray() throws ParseException, IOException {
		WLog.d("Adding", Key.JSON_ARRAY.name);
		currentKey.push(Key.JSON_ARRAY);
		return true;
	}

	@Override
	public boolean primitive(Object value) throws ParseException, IOException {
		if (value == null) {
			WLog.e("Received null value for key:",
					(currentKey.isEmpty() ? "No Key!" : currentKey.peek()));
			return true;
		}

		WLog.d("About to store value", value);

		if (currentKey.isEmpty()) {
			WLog.e("Received value, but key was null");
			return true;
		}

		Key key = currentKey.peek();
		switch (key) {
		/*
		 * =================================================================
		 * Keys for WOOT_EVENT
		 * =================================================================
		 */
		case Type:
			currentEvent.setType((String) value);
			break;
		case Site:
			currentEvent.setSite((String) value);
			break;
		case EndDate:
			currentEvent.setEndDate((String) value);
			break;
		case StartDate:
			currentEvent.setStartDate((String) value);
			break;
		/*
		 * =================================================================
		 * Keys shared between multiple JSON objects
		 * =================================================================
		 */
		case Title:
			String title = (String) value;
			if (currentKey.search(Key.Offers) != -1)
				currentOffer.setTitle(title);
			else if (currentKey.search(Key.WOOT_EVENT) != -1)
				currentEvent.setTitle((String) value);
			else
				WLog.e("Received an Id but it's unclear where to put it!");
			break;
		case Id:
			String id = (String) value;
			if (currentKey.search(Key.Items) != -1)
				currentItem.setId(id);
			else if (currentKey.search(Key.Offers) != -1)
				currentOffer.setId(id);
			else if (currentKey.search(Key.WOOT_EVENT) != -1)
				currentEvent.setID((String) value);
			else
				WLog.e("Received an Id but it's unclear where to put it!");
			break;
		/*
		 * =================================================================
		 * Keys for WOOT_OFFER
		 * =================================================================
		 */
		case Features:
			currentOffer.setFeatures((String) value);
			break;
		case Url: // TODO move to shared b/c photos uses
			currentOffer.setUrl((String) value);
			break;
		case PercentageRemaining:
			currentOffer.setPercentageRemaining((Long) value);
			break;
		case SoldOut:
			currentOffer.setSoldOut((Boolean) value);
			break;
		case Specs:
			currentOffer.setSpecs((String) value);
			break;
		case Subtitle:
			currentOffer.setSubtitle((String) value);
			break;
		case Teaser:
			currentOffer.setTeaser((String) value);
			break;
		case WriteUp:
			currentOffer.setWriteUp((String) value);
			break;
		case Stats:
			currentOffer.setStats((String) value);
			break;
		/*
		 * =================================================================
		 * Keys for WOOT_ITEM
		 * =================================================================
		 */
		case SalePrice:
			currentItem.setSalePrice((Double) value);
			break;
		case ListPrice:
			currentItem.setListPrice((Double) value);
			break;
		case PurchaseLimit:
			currentItem.setPurchaseLimit((Long) value);
			break;
		/*
		 * =================================================================
		 * Keys for WOOT_ITEM
		 * =================================================================
		 */
		case Key:
			currentAttribute.key = (String) value;
			break;
		case Value:
			currentAttribute.value = value;
			break;
		/*
		 * =================================================================
		 * Keys for array and special types
		 * =================================================================
		 */
		case Offers:
		case Items:
		case Attributes:
			WLog.i("Ignoring array key", key.name());
			break;
		case JSON_ARRAY:
		case WOOT_EVENT:
		case WOOT_OFFER:
		case WOOT_ITEM:
		case WOOT_ITEM_ATTRIBUTE:
			WLog.e("Received unexpected call to primitive");
		default:
			WLog.e("Ignoring value", value, "for key", key.name());
			break;
		}

		return true;
	}

	@Override
	public boolean endObjectEntry() throws ParseException, IOException {
		Key currentObject = currentKey.pop();
		WLog.d("Removing object entry", currentObject.name);
		return true;
	}

	@Override
	public boolean endObject() throws ParseException, IOException {
		Key currentObject = currentKey.pop();
		WLog.d("Removing Object", currentObject.name);

		switch (currentObject) {
		case WOOT_ITEM_ATTRIBUTE:
			currentItem.setAttribute(currentAttribute.key,
					currentAttribute.value.toString());
			currentAttribute = new WootAttribute();
			break;
		case WOOT_ITEM:
			currentOffer.getItems().add(currentItem);
			currentItem = new WootItem();
			break;
		case WOOT_OFFER:
			currentEvent.getOffers().add(currentOffer);
			currentOffer = new WootOffer();
			break;
		case WOOT_EVENT:
			listener_.onWootEvent(currentEvent);
			currentEvent = new WootEvent();
			break;
		case UNKNOWN_OBJECT:
			WLog.d("Received end of unknown object");
			break;
		default:
			WLog.e("Received unexpected end of object");
		}

		return true;
	}

	@Override
	public void endJSON() throws ParseException, IOException {
		WLog.d("End JSON");
	}

	@Override
	public boolean endArray() throws ParseException, IOException {
		Key current = currentKey.pop();
		WLog.d("Removing", current.name);
		return true;
	}

	public List<WootEvent> getEvents() {
		if (listener_ instanceof StoringWootEventListener)
			return ((StoringWootEventListener) listener_).events_;
		else
			return new ArrayList<WootEvent>();
	}

	private static class WootAttribute {
		public String key = "";
		public Object value = "";
	}
}
