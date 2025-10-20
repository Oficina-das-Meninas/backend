package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.mappers;

import br.org.oficinadasmeninas.domain.payment.dto.CustomerDto;
import br.org.oficinadasmeninas.domain.payment.dto.ItemDto;
import br.org.oficinadasmeninas.domain.payment.dto.PaymentMethodDto;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout.RequestCreateCheckoutCustomerDto;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout.RequestCreateCheckoutDonationDto;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout.RequestCreateCheckoutDto;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout.ResponseCreateCheckoutDto;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.PaymentsMethodEnum;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.*;
import br.org.oficinadasmeninas.presentation.shared.utils.MoneyConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RequestCreateCheckoutPagbankMapper {

    public RequestCreateCheckoutPagbank toGateway(RequestCreateCheckoutDto domain, RequestCreateCheckoutConfig config){
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

       if (domain.signatureDto().isRecurrence()){
           recurrence = new RequestCreateCheckoutRecurrence(
                   config.itemName(),
                   config.interval(),
                   config.cycles()
           );

       }

        RequestCreateCheckoutPagbank pagbank = new RequestCreateCheckoutPagbank(
                domain.internalId(),
                config.expirationDate(),
                customer,
                List.of(item),
                recurrence,
                toPaymentMethods(config.paymentMethods()),
                config.redirectUrl(),
                config.notificationUrls(),
                config.paymentNotificationUrls()
        );

        return  pagbank;
    }

    private RequestCreateCheckoutCustomerPhone toCustomerPhone(String phone){
        String digits = phone.replaceAll("\\D", "");

        String country = digits.substring(0, 2);
        String area = digits.substring(2, 4);
        String number = digits.substring(4);

        return new RequestCreateCheckoutCustomerPhone(country, area, number);
    }
    private List<RequestCreateCheckoutPaymentMethod> toPaymentMethods(List<PaymentsMethodEnum> methods){
        return methods.stream()
                .map(RequestCreateCheckoutPaymentMethod::new)
                .collect(Collectors.toList());
    }
}
