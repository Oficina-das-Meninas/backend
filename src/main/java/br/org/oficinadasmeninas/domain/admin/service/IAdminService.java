package br.org.oficinadasmeninas.domain.admin.service;

import java.util.UUID;

import br.org.oficinadasmeninas.domain.admin.Admin;
import br.org.oficinadasmeninas.domain.admin.dto.AdminDto;

public interface IAdminService {

	public AdminDto getAdminById(UUID id);
	
	public UUID createAdmin(Admin admin);
	
	public void updateAdmin(UUID id, Admin admin);
	
}
