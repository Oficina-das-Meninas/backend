package br.org.oficinadasmeninas.infra.donation.service;

import br.org.oficinadasmeninas.domain.donation.dto.CreateDonationDto;
import br.org.oficinadasmeninas.domain.donation.dto.DonationDto;
import br.org.oficinadasmeninas.domain.donation.dto.DonationWithDonorDto;
import br.org.oficinadasmeninas.domain.donation.dto.GetDonationDto;
import br.org.oficinadasmeninas.domain.donation.mapper.DonationMapper;
import br.org.oficinadasmeninas.domain.donation.repository.IDonationRepository;
import br.org.oficinadasmeninas.domain.donation.service.IDonationService;
import br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum;
import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static br.org.oficinadasmeninas.domain.donation.mapper.DonationMapper.toDto;
import static br.org.oficinadasmeninas.domain.donation.mapper.DonationMapper.toEntity;

@Service
public class DonationService implements IDonationService {

    private final IDonationRepository donationRepository;

    public DonationService(IDonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    @Override
    public List<DonationDto> findAll() {
        return donationRepository
                .findAll().stream()
                .map(DonationMapper::toDto)
                .toList();
    }

    public PageDTO<DonationWithDonorDto> findByFilter(GetDonationDto getDonationDto){
        return donationRepository.findByFilter(getDonationDto);
    }

    @Override
    public DonationDto findById(UUID id) {
        var donation = donationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.DONATION_NOT_FOUND + id));

        return toDto(donation);
    }

    @Override
    public UUID insert(CreateDonationDto request) {

        var donation = toEntity(request);
        donationRepository.insert(donation);

        return donation.getId();
    }


    @Override
    public void updateMethod(UUID id, PaymentMethodEnum method) {
        var donation = donationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.DONATION_NOT_FOUND + id));

        donation.setMethod(method);
        donationRepository.updateMethod(donation);
    }

    @Override
    public List<DonationDto> findPendingCheckoutsByUserId(UUID userId) {
        return donationRepository
                .findPendingCheckoutsByUserId(userId).stream()
                .map(DonationMapper::toDto)
                .toList();
    }

    @Override
    public void updateFeeAndLiquidValue(UUID id, Double fee, Double valueLiquid) {
        var donation = donationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.DONATION_NOT_FOUND + id));

        donation.setFee(fee);
        donation.setValueLiquid(valueLiquid);
        donationRepository.updateFeeAndLiquidValue(donation);
    }

    @Override
    public void updateCardBrand(UUID id, String cardBrand) {
        var donation = donationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.DONATION_NOT_FOUND + id));

        donation.setCardBrand(cardBrand);
        donationRepository.updateCardBrand(donation);
    }
}
