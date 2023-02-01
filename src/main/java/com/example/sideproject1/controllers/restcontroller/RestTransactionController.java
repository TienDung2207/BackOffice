package com.example.sideproject1.controllers.restcontroller;

import com.example.sideproject1.dto.request.PaymentRequest;
import com.example.sideproject1.dto.request.PaymentSearch;
import com.example.sideproject1.dto.request.PaymentUpdate;
import com.example.sideproject1.dto.response.PagingPayment;
import com.example.sideproject1.dto.response.ResponseMsg;
import com.example.sideproject1.entities.Payment;
import com.example.sideproject1.services.PaymentService;
import com.example.sideproject1.services.RoleService;
import com.example.sideproject1.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
public class RestTransactionController {
    private final PaymentService paymentService;

    @PostMapping("/transaction/payment/detail/{numberPage}")
    public PagingPayment changePageNumber2(@RequestBody PaymentSearch paymentSearch, @PathVariable("numberPage") int numberPage) {
        PagingPayment response = paymentService.changePaymentPage(paymentSearch, numberPage);

        return response;
    }

    @GetMapping("/transaction/payment")
    public Payment getPaymentById(@RequestParam(name = "id") int id) {
        return paymentService.getPaymentById(id);
    }


    @PostMapping("/transaction/update")
    public ResponseEntity<ResponseMsg> updatePayment(@RequestBody PaymentUpdate paymentUpdate) {
        ResponseEntity<ResponseMsg> responseMsg = paymentService.updatePayment(paymentUpdate);

        return responseMsg;
    }

    @PostMapping("/transaction/payment/create")
    public ResponseEntity<ResponseMsg> createOrder(@RequestBody PaymentRequest paymentRequest) {
        ResponseEntity<ResponseMsg> response = paymentService.createOrder(paymentRequest);

        return response;
    }
}
