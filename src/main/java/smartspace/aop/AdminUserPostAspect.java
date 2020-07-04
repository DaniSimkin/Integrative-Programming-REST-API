package smartspace.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

@Component
@Aspect
public class AdminUserPostAspect {
	private Log log = LogFactory.getLog(AdminUserPostAspect.class);
	private EnhancedUserDao<String> userDao;
	
	@Autowired
	public void setUserDao(EnhancedUserDao<String> userDao) {
		this.userDao = userDao;
	}
	
	@Before("@annotation(smartspace.aop.AdminUserPostActions)")
	public void checkAdminPostPrivilages(JoinPoint jp) {
		Object[] param = jp.getArgs();
		log.debug("Parmameters 1 and 2 are: " + param[1] +", " + param[2]);
		String key = param[1] + "="+ param[2];
		UserEntity adminUserEntity = userDao.readById(key)
				.orElseThrow(() -> new RuntimeException("user doesn't exist"));
		log.trace("User found! " + adminUserEntity.getUserName());

		if (!adminUserEntity.getRole().equals(UserRole.ADMIN)) {
			throw new RuntimeException("you are not an ADMIN");
		}
		log.trace("Admin user validated successfully");
	}
	
	@Before("@annotation(smartspace.aop.AdminUserGetActions)")
	public void checkAdminGetPrivilages(JoinPoint jp) {
		Object[] param = jp.getArgs();
		log.debug("Parmameters 2 and 3 are: " + param[2] +", " + param[3]);
		String key = param[2] + "="+ param[3];
		UserEntity adminUserEntity = userDao.readById(key)
				.orElseThrow(() -> new RuntimeException("user doesn't exist"));
		log.trace("User found! " + adminUserEntity.getUserName());

		if (!adminUserEntity.getRole().equals(UserRole.ADMIN)) {
			throw new RuntimeException("you are not an ADMIN");
		}
	}
}
