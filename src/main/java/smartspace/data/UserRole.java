package smartspace.data;


public enum UserRole {
	
	/** Can view all not-expired elements */
	PLAYER, 
	
	/** Can view, create, read, update all elements */
	MANAGER, 
	
	/** Can import and export data from smartspace */
	ADMIN 
}
