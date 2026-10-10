package com.ead.payment.services.impl;

import com.ead.payment.models.CreditCardModel;
import com.ead.payment.models.PaymentModel;
import com.ead.payment.services.PaymentStripeService;
import com.stripe.StripeClient;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Log4j2
@Service
public class PaymentStripeServiceImpl implements PaymentStripeService {

    @Value(value = "${ead.stripe.secretKey}")
    private String secretKeyStripe;

    @Override
    public PaymentModel processStripePayment(PaymentModel paymentModel, CreditCardModel creditCardModel) {
        StripeClient client = new StripeClient(secretKeyStripe);
        String paymentIntentId = null;

        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(paymentModel.getValuePaid().multiply(new BigDecimal("100")).longValue())
                    .setCurrency("brl")
                    .setPaymentMethod("pm_card_visa") // ID do cartão de teste
                    .setConfirm(true) // Força o processamento imediato do pagamento
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .setAllowRedirects(
                                            PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER
                                    )
                                    .build()
                    )
                    .build();

            PaymentIntent paymentIntent = client.v1().paymentIntents().create(params);

            log.info("paymentIntentId: " + paymentIntent.getId());
            log.info("Status: " + paymentIntent.getStatus());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return paymentModel;
    }
}
