package smartspace.dao.rdb;

import smartspace.data.UserEntity;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface UserCrud extends PagingAndSortingRepository<UserEntity, String>{ 

//	List<UserEntity> findAllByNameLike(String string, PageRequest of);
	
	public List<UserEntity> findAllByUserNameLike(
			@Param("pattern") String pattern, 
			Pageable pageable);

}
