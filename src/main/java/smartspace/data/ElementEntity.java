package smartspace.data;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import smartspace.dao.rdb.MapToJsonConverter;
import smartspace.data.Location;

@Entity
@Table(name="ELEMENTS")
public class ElementEntity implements SmartspaceEntity<String> {

	private String elementSmartspace;
	private String elementId;
	private Location location;
	private String name;
	private String type;
	private Date creationTimestamp;
	private boolean expired;
	private String creatorSmartspace;
	private String creatorEmail;
	private Map<String, Object> moreAttributes;

	public ElementEntity() {

	}

	public ElementEntity(String elementSmartspace, String elementId, Location location, String name, String type,
			Date creationTimestamp, boolean expired, String creatorSmartspace, String creatorEmail,
			Map<String, Object> moreAttributes) {
		super();
		this.elementSmartspace = elementSmartspace;
		this.elementId = elementId;
		this.location = location;
		this.name = name;
		this.type = type;
		this.creationTimestamp = creationTimestamp;
		this.expired = expired;
		this.creatorSmartspace = creatorSmartspace;
		this.creatorEmail = creatorEmail;
		this.moreAttributes = moreAttributes;
	}


	public ElementEntity(String name, String type, Location location, Date creationTimeStamp, String creatorEmail,
			String creatorSmartSpace, boolean expired, Map<String, Object> moreAttributes) {
		this.location = location;
		this.name = name;
		this.type = type;
		this.creationTimestamp = creationTimeStamp;
		this.expired = expired;
		this.creatorSmartspace = creatorSmartSpace;
		this.creatorEmail = creatorEmail;
		this.moreAttributes = moreAttributes;
	}

	@Transient
	public String getElementSmartspace() {
		return elementSmartspace;
	}
	@Transient
	public String getElementId() {
		return elementId;
	}

	@Embedded
	public Location getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public boolean isExpired() {
		return expired;
	}

	public String getCreatorSmartspace() {
		return creatorSmartspace;
	}

	public String getCreatorEmail() {
		return creatorEmail;
	}

	
	@Lob
	@Convert(converter=MapToJsonConverter.class)
	public Map<String, Object> getMoreAttributes() {
		return moreAttributes;
	}

	public void setElementSmartspace(String elementSmartspace) {
		this.elementSmartspace = elementSmartspace;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public void setCreatorSmartspace(String creatorSmartspace) {
		this.creatorSmartspace = creatorSmartspace;
	}

	public void setCreatorEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
	}

	public void setMoreAttributes(Map<String, Object> moreAttributes) {
		this.moreAttributes = moreAttributes;
	}
	
	@Override
	@Id
	@Column(name="ID")
	public String getKey() {
		return this.elementSmartspace + "=" +  this.elementId;
	}

	@Override
	public void setKey(String key) {
		String[] parts = key.split("=");
		this.elementSmartspace = parts[0];
		this.elementId = parts[1];
	}

}