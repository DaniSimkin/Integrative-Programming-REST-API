package smartspace.layout;

import java.util.Date;
import java.util.Map;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.layout.data.CreatorBoundary;
import smartspace.layout.data.Key;
import smartspace.layout.data.ElementLocationBoundary;

public class ElementBoundary {
	private Key key;
	private String name;
	private String elementType;
	private Date created;
	private boolean expired;
	private CreatorBoundary creator;
	private ElementLocationBoundary latlng;
	private Map<String, Object> elementProperties;

	public ElementBoundary() {
	}

	public ElementBoundary(ElementEntity entity) {
		if (key == null) {
			this.key = new Key();
		}
		if (entity.getKey() != null) {
			String[] args = entity.getKey().split("=");
			this.key.setSmartspace(args[0]);
			this.key.setId(args[1]);
		}
		this.elementType = entity.getType();
		this.name = entity.getName();
		this.created = entity.getCreationTimestamp();
		this.expired = entity.isExpired();
		if (creator == null) {
			this.creator = new CreatorBoundary();
		}
		this.creator.setEmail(entity.getCreatorEmail());
		this.creator.setSmartspace(entity.getCreatorSmartspace());
		if (latlng == null) {
			this.latlng = new ElementLocationBoundary();
		}
		this.latlng.setLat(entity.getLocation().getX());
		this.latlng.setLng(entity.getLocation().getY());
		this.elementProperties = entity.getMoreAttributes();
	}

	public ElementEntity convertToEntity() {
		ElementEntity entity = new ElementEntity();
		
		if(this.key != null) {
			if (this.key.getId() != null && this.key.getSmartspace() != null) {
				entity.setKey(this.key.getSmartspace() +"="+ this.key.getId());
			}
		}
		
		entity.setType(this.elementType);
		entity.setName(this.name);
		entity.setExpired(this.expired);
		entity.setCreationTimestamp(this.created);
		entity.setCreatorEmail(this.creator.getEmail());
		entity.setCreatorSmartspace(this.creator.getSmartspace());
		entity.setLocation(new Location(this.latlng.getLat(), this.latlng.getLng()));
		entity.setMoreAttributes(this.elementProperties);
		return entity;
	}
	
	public ElementEntity convertToEntityForUpdate() {
		
		ElementEntity entity = new ElementEntity();
		
//		if (this.key.getId() != null && this.key.getSmartspace() != null) {
//			entity.setKey(this.key.getSmartspace() +"="+ this.key.getId());
//		}
		
		if(this.getCreationTimestamp() != null) {
			entity.setCreationTimestamp(this.getCreationTimestamp());
		}
		
		if(this.getCreator() != null) {
			if(this.getCreator().getEmail() != null) {
				entity.setCreatorEmail(this.getCreator().getEmail());
			}
			if(this.getCreator().getSmartspace() != null) {
			entity.setCreatorSmartspace(this.getCreator().getSmartspace());
			}
		}
		
		if(this.getName() != null) {
			entity.setName(this.getName());
		}
		
		if(this.getElementType() != null) {
			entity.setType(this.getElementType());
		}
		
		if(this.isExpired()) {
			entity.setExpired(this.isExpired());
		}
		
		if(this.getLatlng() != null) {
			if(this.getLatlng().getLat() >= 0 && this.getLatlng().getLng() >= 0) {
				entity.setLocation(new Location(this.getLatlng().getLat(), this.getLatlng().getLng()));
			}
		}
		
		if(this.getElementProperties() != null) {
			entity.setMoreAttributes(this.getElementProperties());
		}
		
		return entity;
	}
	
	
	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getElementType() {
		return elementType;
	}

	public void setElementType(String type) {
		this.elementType = type;
	}

	public Date getCreationTimestamp() {
		return created;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.created = creationTimestamp;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public CreatorBoundary getCreator() {
		return creator;
	}

	public void setCreator(CreatorBoundary creator) {
		this.creator = creator;
	}

	public ElementLocationBoundary getLatlng() {
		return latlng;
	}

	public void setLatlng(ElementLocationBoundary latlng) {
		this.latlng = latlng;
	}

	public Map<String, Object> getElementProperties() {
		return elementProperties;
	}

	public void setElementProperties(Map<String, Object> elementProperties) {
		this.elementProperties = elementProperties;
	}

}
