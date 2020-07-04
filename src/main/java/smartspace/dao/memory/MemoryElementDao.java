package smartspace.dao.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import smartspace.dao.ElementDao;
import smartspace.data.ElementEntity;

//@Repository
public class MemoryElementDao implements ElementDao<String> {
	private List<ElementEntity> elements;
	private AtomicLong nextId;
	
	public MemoryElementDao() {
		this.elements = Collections.synchronizedList(new ArrayList<ElementEntity>());
		this.nextId = new AtomicLong(1);
	}
	
	protected List<ElementEntity> getElements(){
		return this.elements;
	}
	
	@Override
	public ElementEntity create(ElementEntity elementEntity) {
		long tmpId = nextId.getAndIncrement();
		String theKey = 
				elementEntity.getElementSmartspace() + "=" + tmpId;
		elementEntity.setKey(theKey);
		this.elements.add(elementEntity);
		return elementEntity;
	}

	@Override
	public Optional<ElementEntity> readById(String elementKey) {
		ElementEntity target = null;
		for (ElementEntity current : this.elements) {
			if (current.getKey().equals(elementKey)) {
				target = current;
			}
		}
		if (target != null) {
			return Optional.of(target);
		}else {
			return Optional.empty();
		}
	}

	@Override
	public List<ElementEntity> readAll() {
		return this.elements;
	}

	@Override
	public void update(ElementEntity theElement) {
		synchronized (this.elements) {
			ElementEntity existing = this.readById(theElement.getKey())
					.orElseThrow(() -> new RuntimeException("no element to update"));
			
			if(theElement.getCreatorEmail()!= null)
				existing.setCreatorEmail(theElement.getCreatorEmail());
			
			if(theElement.getCreatorSmartspace()!= null)
				existing.setCreatorSmartspace(theElement.getCreatorSmartspace());
			
			if(theElement.getLocation()!= null)
				existing.setLocation(theElement.getLocation());
			
			if(theElement.getName()!= null)
				existing.setName(theElement.getName());
			
			if(theElement.getType()!= null)
				existing.setType(theElement.getType());
			
			if(theElement.getMoreAttributes()!= null)
				existing.setMoreAttributes(theElement.getMoreAttributes());
		}
	}

	@Override
	public void deleteByKey(String elementKey) {
		Iterator<ElementEntity> elementsItr = this.elements.listIterator();
		while (elementsItr.hasNext()) {
			ElementEntity elementEntity = (ElementEntity) elementsItr.next();
			if(elementEntity.getKey().equals(elementKey)) {
				elementsItr.remove();
				return ;
			}
		}
	}

	@Override
	public void delete(ElementEntity elementEntity) {
		deleteByKey(elementEntity.getKey());
	}

	@Override
	public void deleteAll() {
		this.elements.clear();
	}
}
