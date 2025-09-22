package br.org.oficinadasmeninas.domain.admin.service;

import java.util.List;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.admin.dto.AdminDto;
import br.org.oficinadasmeninas.domain.admin.dto.CreateAdminDto;
import br.org.oficinadasmeninas.domain.admin.dto.UpdateAdminDto;

public interface IAdminService {

	public List<AdminDto> getAllAdmin();
	
	public AdminDto getAdminById(UUID id);
	
	public AdminDto getAdminByEmail(String email); 
	
	public UUID createAdmin(CreateAdminDto admin);
	
	public void updateAdmin(UUID id, UpdateAdminDto admin);
	
}
