package com.mkpolo.aplication.Exception;

public class UsernameOrIdNotFound extends Exception{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 8189475690772548237L;	

	public UsernameOrIdNotFound() {
		super("Usuario o Id no encontrado");
	}
	
	public UsernameOrIdNotFound(String message) {
		super(message);
	}
	
}
