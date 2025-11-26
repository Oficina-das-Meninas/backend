package br.org.oficinadasmeninas.domain.donation.repository;

import br.org.oficinadasmeninas.domain.donation.Donation;
import br.org.oficinadasmeninas.domain.donation.dto.DonationWithDonorDto;
import br.org.oficinadasmeninas.domain.donation.dto.GetDonationDto;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IDonationRepository {

    Donation insert(Donation donation);

    Donation updateMethod(Donation donation);

    Donation updateFeeAndLiquidValue(Donation donation);

    Donation updateCardBrand(Donation donation);

	List<Donation> findAll();

    PageDTO<DonationWithDonorDto> findByFilter(GetDonationDto getDonationDto);

	Optional<Donation> findById(UUID id);
}
