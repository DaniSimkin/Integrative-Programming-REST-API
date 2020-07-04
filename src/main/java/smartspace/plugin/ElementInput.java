package smartspace.plugin;

public class ElementInput {
	
    private int roomNumber;
    private int numberOfBeds;
    private int maximumGuests;
    private int roomSize;
    private String roomType;
    private int roomPrice;
    private String ElementKey;
    private String imageURL;
	private int roomServicePrice;
	
    public int getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	public int getNumberOfBeds() {
		return numberOfBeds;
	}

	public void setNumberOfBeds(int numberOfBeds) {
		this.numberOfBeds = numberOfBeds;
	}

	public int getMaximumGuests() {
		return maximumGuests;
	}

	public void setMaximumGuests(int maximumGuests) {
		this.maximumGuests = maximumGuests;
	}

	public int getRoomSize() {
		return roomSize;
	}

	public void setRoomSize(int roomSize) {
		this.roomSize = roomSize;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public String getElementKey() {
		return ElementKey;
	}

	public void setElementKey(String elementKey) {
		ElementKey = elementKey;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
    	
	public ElementInput(){
	}
	
	public int getRoomPrice() {
		return roomPrice;
	}
	
	public void setRoomPrice(int roomPrice) {
		this.roomPrice = roomPrice;
	}

	public int getRoomServicePrice() {
		return roomServicePrice;
	}

	public void setRoomServicePrice(int roomServicePrice) {
		this.roomServicePrice = roomServicePrice;
	}
}
