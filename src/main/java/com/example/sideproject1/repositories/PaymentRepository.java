package com.example.sideproject1.repositories;

//import com.example.sideproject1.entities.Payment;
import com.example.sideproject1.entities.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    @Query("select p from Payment p where"
            + "((p.amount = :amount) or (:amount is null ))"
            + "and ((p.ipAddress like concat('%', :ipAddress,'%')) or (:ipAddress is null ) or (:ipAddress = ''))"
            + "and ((p.orderInfo like concat('%', :orderInfo, '%')) or (:orderInfo is null) or (:orderInfo = ''))"
            + "and ((p.status like concat('%', :status, '%')) or (:status is null) or (:status = ''))")
    Page<Payment> findAllPay(Double amount, String ipAddress, String orderInfo, String status, Pageable pageable);

    Payment getByPayId(int id);
}
