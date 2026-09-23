package com.ead.payment.repositories;

import com.ead.payment.models.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PaymentRepository extends JpaRepository<PaymentModel, Integer>, JpaSpecificationExecutor<PaymentModel> {
}
