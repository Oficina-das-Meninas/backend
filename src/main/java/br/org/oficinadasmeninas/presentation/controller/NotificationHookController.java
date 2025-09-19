package br.org.oficinadasmeninas.presentation.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.oficinadasmeninas.infra.facade.NotificationFacade;
import br.org.oficinadasmeninas.presentation.dto.notification.RequestPaymentNotificationDTO;

@RestController
@RequestMapping("/notification")
public class NotificationHookController {
    private final NotificationFacade facade;
    
    public NotificationHookController(NotificationFacade facade) {
        this.facade = facade;
    }
    
    @PostMapping("/payment")
    public void notifyPayment(@RequestBody RequestPaymentNotificationDTO request) {
        facade.updatePaymentStatus(request.getReferenceId(), request.getCharges().getFirst().getStatus());
    }
}
