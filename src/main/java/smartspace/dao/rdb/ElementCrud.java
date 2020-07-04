package smartspace.dao.rdb;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import smartspace.data.ElementEntity;

public interface ElementCrud extends
//CrudRepository<ElementEntity, String>
		PagingAndSortingRepository<ElementEntity, String> {
	
	public List<ElementEntity> findAllByNameLike(
			@Param("pattern") String pattern,
			Pageable pageable);
	
	public List<ElementEntity> 
		findAllByLocation_xBetweenAndLocation_yBetween(double startX, double endX,
				double startY, double endY);
	
	public List<ElementEntity> findAllByName(
			@Param("name") String name,
			Pageable pageable);
	
	public List<ElementEntity> findAllByNameAndExpired(
			@Param("name") String name, 
			@Param("expired") boolean expired,
			Pageable pageable);
	
	public List<ElementEntity> findAllByTypeLike(
			@Param("type") String type, 
			Pageable pageable);
	
	public List<ElementEntity> findAllByExpiredAndType(
			@Param("expired") boolean expired,
			@Param("type") String type, 
			Pageable pageable);	
	
}
