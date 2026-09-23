package com.ead.payment.repositories;

import com.ead.payment.models.CreditCardModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CreditCardRepository extends JpaRepository<CreditCardModel, Integer>, JpaSpecificationExecutor<CreditCardModel> {
}
