package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.mappers;

import br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout.RequestCreateCheckoutCustomerDto;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout.RequestCreateCheckoutDonationDto;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout.RequestCreateCheckoutDto;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.*;
import br.org.oficinadasmeninas.presentation.shared.utils.MoneyConverter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestCreateCheckoutPagbankMapper {

    public RequestCreateCheckoutPagbank toGateway(RequestCreateCheckoutDto domain, RequestCreateCheckoutConfig config) {
        RequestCreateCheckoutCustomerDto oldCustomer = domain.customerDto();
        RequestCreateCheckoutDonationDto oldDonation = domain.donation();
        RequestCreateCheckoutCustomer customer = new RequestCreateCheckoutCustomer(
                oldCustomer.name(),
                oldCustomer.email(),
                oldCustomer.document(),
                toCustomerPhone(oldCustomer.phone())
        );

        RequestCreateCheckoutItem item = new RequestCreateCheckoutItem(
                config.itemName(),
                config.quantity(),
                MoneyConverter.doubleToLong(oldDonation.value()),
                config.itemImage()
        );


        RequestCreateCheckoutRecurrence recurrence = null;

        if (domain.signatureDto().isRecurrence()) {
            recurrence = new RequestCreateCheckoutRecurrence(
                    config.itemName(),
                    config.interval(),
                    config.cycles()
            );

        }

        return new RequestCreateCheckoutPagbank(
                domain.internalId(),
                config.expirationDate(),
                customer,
                List.of(item),
                recurrence,
                toPaymentMethods(config.paymentMethods()),
                config.redirectUrl(),
                config.notificationUrls(),
                config.paymentNotificationUrls(),
                false
        );
    }

    private RequestCreateCheckoutCustomerPhone toCustomerPhone(String phone) {
        String digits = phone.replaceAll("\\D", "");

        String country = digits.substring(0, 2);
        String area = digits.substring(2, 4);
        String number = digits.substring(4);

        return new RequestCreateCheckoutCustomerPhone(country, area, number);
    }

    private List<RequestCreateCheckoutPaymentMethod> toPaymentMethods(List<PaymentMethodEnum> methods) {
        return methods.stream()
                .map(RequestCreateCheckoutPaymentMethod::new)
                .collect(Collectors.toList());
    }
}
