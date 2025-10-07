package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.mappers;

import br.org.oficinadasmeninas.domain.donation.DonationStatusEnum;
import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;

public class RequestNotifyPaymentDonationStatusMapper {
    public static DonationStatusEnum fromPaymentStatus(PaymentStatusEnum paymentStatus) {
        if (paymentStatus == null) return DonationStatusEnum.PENDING;

        return switch (paymentStatus) {
            case PAID -> DonationStatusEnum.PAID;
            case ACTIVE, IN_ANALYSIS, WAITING -> DonationStatusEnum.PENDING;
            case DECLINED, CANCELED -> DonationStatusEnum.CANCELED;
        };
    }

    public static PaymentStatusEnum fromDonationStatus(DonationStatusEnum donationStatus) {
        if (donationStatus == null) return PaymentStatusEnum.WAITING;

        return switch (donationStatus) {
            case PAID -> PaymentStatusEnum.PAID;
            case PENDING -> PaymentStatusEnum.WAITING;
            case CANCELED -> PaymentStatusEnum.CANCELED;
        };
    }
}
