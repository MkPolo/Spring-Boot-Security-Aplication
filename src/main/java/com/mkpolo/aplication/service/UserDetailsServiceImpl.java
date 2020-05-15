package com.mkpolo.aplication.service;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mkpolo.aplication.entity.Role;
import com.mkpolo.aplication.repository.UserRepository;

@Service
@Transactional //Se bloquea la primera transaccion de busqueda de roles si es que se esta usando
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//Buscar nombre de usuario en nuestra base de datos
		com.mkpolo.aplication.entity.User appUser = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario Invalido."));
		
			Set grantList = new HashSet(); 
	    
	    //Crear la lista de los roles/accessos que tienen el usuarios
	    for (Role role: appUser.getRoles()) {
	        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getDescription());
	            grantList.add(grantedAuthority);
	    }
	    UserDetails user = (UserDetails) new User(username, appUser.getPassword(),grantList);
	  //Crear y retornar Objeto de usuario soportado por Spring Security
		return user;
	}

}
