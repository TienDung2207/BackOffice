package com.example.sideproject1.controllers.controller;


import com.example.sideproject1.dto.response.LayoutResponse;
import com.example.sideproject1.entities.Payment;
import com.example.sideproject1.services.LayoutService;
import com.example.sideproject1.services.PaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@Slf4j
public class TransactionController {
    private final LayoutService layoutService;
    private final PaymentService paymentService;

    @GetMapping("/transaction")
    public String transaction(Authentication authentication, Model model) {
        LayoutResponse layoutResponse = layoutService.handleLayout(authentication);
        Page<Payment> page = paymentService.pagingPayment(1, null, "", "", "");

        model.addAttribute("roleDesc", layoutResponse.getRoleDesc());
        model.addAttribute("roleName", layoutResponse.getRoleName());
        model.addAttribute("actionsByRole", layoutResponse.getActionsByRole());

        model.addAttribute("currentPage", 1);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("payments", page.getContent());


        return "transaction";
    }

    @GetMapping("/transaction/update")
    public String updateTransaction(Authentication authentication, Model model, @RequestParam("id") int id) {
        LayoutResponse layoutResponse = layoutService.handleLayout(authentication);
        Payment payment = paymentService.getPaymentById(id);

        model.addAttribute("payment", payment);
        model.addAttribute("roleName", layoutResponse.getRoleName());
        model.addAttribute("roleDesc", layoutResponse.getRoleDesc());
        model.addAttribute("actionsByRole", layoutResponse.getActionsByRole());

        return "update-transaction";
    }
}
