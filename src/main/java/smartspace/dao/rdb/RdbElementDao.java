package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import smartspace.dao.EnhancedElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;

@Repository
public class RdbElementDao implements EnhancedElementDao<String> {
	private String smartspace;
	private ElementCrud elementCrud;
	private GenericIdGeneratorCrud generatorCrud;
	
	@Value("${smartspace.name}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}

	@Autowired
	public RdbElementDao(ElementCrud eleCrud, GenericIdGeneratorCrud generatorCrud) {
		this.elementCrud = eleCrud;
		this.generatorCrud = generatorCrud;
	}

	@Override
	@Transactional
	public ElementEntity create(ElementEntity elementEntity) {
		// create and enter id into db
		GenericIdGenerator nextIdNum = this.generatorCrud.save(new GenericIdGenerator());

		// set element key and destroy the row in db
		elementEntity.setKey(this.smartspace + "=" + nextIdNum.getId());
		this.generatorCrud.delete(nextIdNum);

		// if element doesn't exists then add it else throw RuntimeException
		if (!this.elementCrud.existsById(elementEntity.getKey())) {
			ElementEntity returnedVal = this.elementCrud.save(elementEntity);
			return returnedVal;
		} else
			throw new RuntimeException("element with the same key " + elementEntity.getKey() + "already exists!");
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ElementEntity> readById(String elementKey) {
		return this.elementCrud.findById(elementKey);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementEntity> readAll() {
		List<ElementEntity> lst = new ArrayList<>();
		this.elementCrud.findAll().forEach(lst::add);
		return lst;
	}

	@Override
	@Transactional
	public void update(ElementEntity elemntEnt) {
		
		ElementEntity existing = this.readById(elemntEnt.getKey())
				.orElseThrow(() -> new RuntimeException("no element to update"));

		if (elemntEnt.getCreatorEmail() != null)
			existing.setCreatorEmail(elemntEnt.getCreatorEmail());

		if (elemntEnt.getCreatorSmartspace() != null)
			existing.setCreatorSmartspace(elemntEnt.getCreatorSmartspace());

		if (elemntEnt.getLocation() != null)
			existing.setLocation(elemntEnt.getLocation());

		if (elemntEnt.getName() != null)
			existing.setName(elemntEnt.getName());

		if (elemntEnt.getType() != null)
			existing.setType(elemntEnt.getType());

		if (elemntEnt.getMoreAttributes() != null)
			existing.setMoreAttributes(elemntEnt.getMoreAttributes());
		
		existing.setExpired(elemntEnt.isExpired());
		
		this.elementCrud.save(existing);
	}

	@Override
	@Transactional
	public void deleteByKey(String elementKey) {
		this.elementCrud.deleteById(elementKey);
	}

	@Override
	@Transactional
	public void delete(ElementEntity elementEntity) {
		this.elementCrud.delete(elementEntity);
	}

	@Override
	@Transactional
	public void deleteAll() {
		this.elementCrud.deleteAll();
	}

	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readAll(int size, int page) {
		return this.elementCrud.findAll(PageRequest.of(page, size)).getContent();
	}

	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readAll(String sortBy, int size, int page) {
		return this.elementCrud
				.findAll(PageRequest.of(page, size, Direction.ASC, sortBy))
				.getContent();
	}

	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readElementWithSmartspaceContaining(String smartspace, int size, int page) {
		return this.elementCrud
				.findAllByNameLike("%" + smartspace + "%", PageRequest.of(page, size));
	}

	@Override
	public ElementEntity importElement(ElementEntity elementEntity) {
		// import elementEntity to our database
		return this.elementCrud.save(elementEntity);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readAllByDistanceFromLocation(
			Location l,double distance, int size, int page){
		return this.elementCrud
				.findAllByLocation_xBetweenAndLocation_yBetween(
						l.getX()-distance, l.getX()+distance,
						l.getY()-distance, l.getY()+distance);
	}
	
	@Override
	public List<ElementEntity> readAllUsingName(String name, int size, int page) {
		return this.elementCrud.findAllByNameLike("%"+name+"%", PageRequest.of(page, size));
	}

	@Override
	public List<ElementEntity> readAllUsingNameNotExpired(String name, int size, int page) {
		return this.elementCrud.findAllByNameAndExpired(name, false, PageRequest.of(page, size));
	}

	@Override
	public List<ElementEntity> readAllUsingType(String type, int size, int page) {
		return this.elementCrud.findAllByTypeLike("%"+type+"%", PageRequest.of(page, size));
	}

	@Override
	public List<ElementEntity> readAllUsingTypeNotExpired(String type, int size, int page) {
		return this.elementCrud.findAllByExpiredAndType(false, type, PageRequest.of(page, size));
	}
	
}

