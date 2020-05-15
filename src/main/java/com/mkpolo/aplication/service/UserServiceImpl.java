package com.mkpolo.aplication.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mkpolo.aplication.dto.ChangePasswordForm;
import com.mkpolo.aplication.entity.User;
import com.mkpolo.aplication.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository repository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public Iterable<User> getAllUsers() {
		return repository.findAll();
	}

	private boolean checkUsernameAvailable(User user) throws Exception {
		Optional<User> userFound = repository.findByUsername(user.getUsername());

		if (userFound.isPresent()) {
			throw new Exception("Username no disponible");
		}
		return true;
	}

	private boolean checkPasswordValid(User user) throws Exception {
		if (!user.getPassword().equals(user.getConfirmPassword())) {
			throw new Exception("Las contraseñas no coinciden");
		}
		return true;
	}

	@Override
	public User createUser(User user) throws Exception {
		if (checkUsernameAvailable(user) && checkPasswordValid(user)) {
			
			String encodePass = bCryptPasswordEncoder.encode(user.getPassword());
			user.setPassword(encodePass);
			
			repository.save(user);
		}
		return user;
	}

	@Override
	public User getUserById(Long id) throws Exception {
		return repository.findById(id).orElseThrow(() -> new Exception("El usuario no existe."));
	}

	@Override
	public User updateUser(User fromUser) throws Exception {
		User toUser = getUserById(fromUser.getId());
		mapUser(fromUser, toUser);
		return repository.save(toUser);
	}

	protected void mapUser(User from, User to) {
		to.setUsername(from.getUsername());
		to.setFirstName(from.getFirstName());
		to.setLastName(from.getLastName());
		to.setEmail(from.getEmail());
		to.setRoles(from.getRoles());
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public void deleteUser(Long id) throws Exception {
		User user = getUserById(id);

		repository.delete(user);
	}

	@Override
	public User changePassword(ChangePasswordForm form) throws Exception {

		User user = getUserById(form.getId());
		if (!isLoggedUserADMIN() && !user.getPassword().equals(form.getCurrentPassword())) {
			throw new Exception("Current Password invalid");
		}

		if (user.getPassword().equals(form.getNewPassword())) {
			throw new Exception("La nueva contraseña tiene que ser diferente a la anterior.");
		}

		if (!form.getNewPassword().equals(form.getConfirmPassword())) {
			throw new Exception("Las contraseñas no coinciden");
		}
		
		String encodePass = bCryptPasswordEncoder.encode(form.getNewPassword());
		user.setPassword(encodePass);
		return repository.save(user);
	}

	public boolean isLoggedUserADMIN() {
		return loggedUserHasRole("ROLE_ADMIN");
	}

	public boolean loggedUserHasRole(String role) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails loggedUser = null;
		Object roles = null;
		if (principal instanceof UserDetails) {
			loggedUser = (UserDetails) principal;

			roles = loggedUser.getAuthorities().stream().filter(x -> role.equals(x.getAuthority())).findFirst()
					.orElse(null); // loggedUser = null;
		}
		return roles != null ? true : false;
	}

}
