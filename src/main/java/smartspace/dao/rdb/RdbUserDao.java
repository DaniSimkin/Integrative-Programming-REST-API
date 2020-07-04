package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.EnhancedUserDao;
import smartspace.dao.UserDao;
import smartspace.data.UserEntity;

@Repository
public class RdbUserDao implements EnhancedUserDao<String>{

	private UserCrud userCrud;
	private GenericIdGeneratorCrud genericIdGeneratorCrud;	
	
	@Autowired
	public RdbUserDao(UserCrud userCrud, GenericIdGeneratorCrud genericIdGeneratorCrud) {		
		super();
		this.genericIdGeneratorCrud = genericIdGeneratorCrud;
		this.userCrud = userCrud;
	}
	
	public RdbUserDao() {		
	
	}
	
	
	@Override
	@Transactional
	public UserEntity create(UserEntity userEntity) {
		//SQL: INSERT INTO USER_ENTITY_TABLE (USER_ID, USER_SMARTSPACE) VALUES (?,?);
		//create and enter id into db
		GenericIdGenerator nextId = 
				this.genericIdGeneratorCrud.save(new GenericIdGenerator());
		
		//set user key and destroy the row in db
		userEntity.setKey(userEntity.getUserSmartspace() + "=" + userEntity.getUserEmail());
		this.genericIdGeneratorCrud.delete(nextId);
		
		//if user doesn't exists then add it, else throw RuntimeException
		if(!this.userCrud.existsById(userEntity.getKey())) {
			UserEntity rv = this.userCrud.save(userEntity);
			return rv;
		}
		else {
			throw new RuntimeException("user entity already exist with key:" + userEntity.getKey());
		}
	}

	@Override
	@Transactional(readOnly=true)
	public Optional<UserEntity> readById(String userKey) {
		//SQL: SELECT
		return this.userCrud.findById(userKey);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<UserEntity> readAll() {
		List<UserEntity> rv = new ArrayList<>();
		
		// SQL: SELECT
		this.userCrud.findAll()
			.forEach(rv::add);
		
		return rv;
	}

	@Override
	@Transactional
	public void update(UserEntity userEntity) {
		
		UserEntity existing = this.readById(userEntity.getKey())
				.orElseThrow(() -> new RuntimeException("user to update is NOT in DB"));
		
//		if(userEntity.getPoints() == existing.getPoints()) {
//			System.err.println("bla1");
//		}
//		else {
//			existing.setPoints(userEntity.getPoints());
//		}
		
		if(userEntity.getAvatar() != null) {
			System.err.println("bla2");
			existing.setAvatar(userEntity.getAvatar());
		}
		
		if(userEntity.getRole() != null) {
			System.err.println("bla3");
			existing.setRole(userEntity.getRole());
		}
		
		if(userEntity.getUserEmail() != null) {
			System.err.println("bla4");
			existing.setUserEmail(userEntity.getUserEmail());
		}
		
		if(userEntity.getUserName() != null) {
			System.err.println("bla5");
			existing.setUserName(userEntity.getUserName());
		}
		
		if(userEntity.getUserSmartspace() != null) {
			System.err.println("bla6");
			existing.setUserSmartspace(userEntity.getUserSmartspace());
		}
		
		// SQL: UPDATE
		this.userCrud.save(existing);
	}

	@Override
	@Transactional
	public void deleteAll() {
		// SQL: DELETE
		this.userCrud.deleteAll();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<UserEntity> readAll(int size, int page) {
		return this.userCrud
			.findAll(PageRequest.of(page, size))
			.getContent();
	}


	@Override
	@Transactional(readOnly=true)
	public List<UserEntity> readMessageWithNameContaining(
			String text, 
			int size, 
			int page) {
		
		return this.userCrud
				.findAllByUserNameLike(
						"%" + text + "%",
						PageRequest.of(page, size));
	}

	@Override
	public List<UserEntity> readAll(String sortBy, int size, int page) {
		return this.userCrud
			.findAll(PageRequest.of(
					page, size, 
					Direction.ASC, sortBy))
			.getContent();
	}
	
	@Override
	public UserEntity importUser(UserEntity userEntity) {
		return this.userCrud.save(userEntity);
		
	}
	
}
