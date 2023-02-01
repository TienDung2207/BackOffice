package com.example.sideproject1.services;

import com.example.sideproject1.dto.request.PaymentRequest;
import com.example.sideproject1.dto.request.PaymentSearch;
import com.example.sideproject1.dto.request.PaymentUpdate;
import com.example.sideproject1.dto.response.PagingPayment;
import com.example.sideproject1.dto.response.ResponseMsg;
import com.example.sideproject1.entities.Payment;
import com.example.sideproject1.repositories.PaymentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

@Service
@Slf4j
@AllArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public ResponseEntity<ResponseMsg> createOrder(PaymentRequest paymentRequest) {
        try {
            Calendar date = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            InetAddress myIP = InetAddress.getLocalHost();

            Double amount = Double.parseDouble(paymentRequest.getAmount());
            String orderInfo = "";

            if (paymentRequest.getOrderInfo() == "") {
                orderInfo = "Thanh toan don hang thoi gian: ".concat(format.format(date.getTime()));
            } else {
                orderInfo = paymentRequest.getOrderInfo();
            }

            Payment payment = new Payment();

            payment.setAmount(amount);
            payment.setOrderInfo(orderInfo);
            payment.setStatus("Pending");
            payment.setIpAddress(myIP.getHostAddress());
            payment.setPayDate(date.getTime());

            paymentRepository.save(payment);

            ResponseMsg responseMsg = new ResponseMsg();
            responseMsg.setRspCode("00");
            responseMsg.setRspMessage("Success");

            return new ResponseEntity<>(responseMsg, HttpStatus.OK);
        } catch (Exception ex) {
            log.info("create order ex: ", ex);
            return new ResponseEntity<>(new ResponseMsg("500", "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Page<Payment> pagingPayment(int pageNumber, Double amount, String ipAddress, String orderInfo, String status) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 3, Sort.by("payId").ascending());

        return paymentRepository.findAllPay(amount, ipAddress, orderInfo, status, pageable);
    }

    public PagingPayment changePaymentPage(PaymentSearch paymentSearch, int numberPage) {
        Page<Payment> page;
        if (paymentSearch.getAmount() == null && paymentSearch.getOrderInfo() == "" && paymentSearch.getStatus() == "" && paymentSearch.getIpAddress() == "") {
            page = this.pagingPayment(numberPage, null, "", "", "");
        } else {
            page = this.pagingPayment(numberPage, paymentSearch.getAmount(), paymentSearch.getOrderInfo(), paymentSearch.getStatus(), paymentSearch.getIpAddress());
        }
        List<Payment> payments = page.getContent();

        PagingPayment response = new PagingPayment();
        response.setCurrentPage(numberPage);
        response.setTotalPages(page.getTotalPages());
        response.setTotalItems(page.getTotalElements());
        response.setPayments(payments);

        return response;
    }

    public Payment getPaymentById(int id) {
        return paymentRepository.getByPayId(id);
    }

    public ResponseEntity<ResponseMsg> updatePayment(PaymentUpdate paymentUpdate) {
        try {
            String status = paymentUpdate.getStatus();
            log.info("status: " + status);

            Payment payment = this.getPaymentById(paymentUpdate.getId());
            payment.setStatus(status);

            paymentRepository.save(payment);

            return new ResponseEntity<>(new ResponseMsg("00", "Updated payment successfully!"), HttpStatus.OK);
        } catch (Exception ex) {
            log.error("update payment error: ", ex);
            return new ResponseEntity<>(new ResponseMsg("500", "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
