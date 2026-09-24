package com.ead.payment.repositories;

import com.ead.payment.models.CreditCardModel;
import com.ead.payment.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CreditCardRepository extends JpaRepository<CreditCardModel, Integer>, JpaSpecificationExecutor<CreditCardModel> {
    Optional<CreditCardModel> findByUser(UserModel userModel);
}
