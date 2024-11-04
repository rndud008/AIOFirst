package hello.aiofirst.controller;

import hello.aiofirst.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/orders")
@Slf4j
public class OrderController {
    private final OrderService orderService;

    @GetMapping("")
    public String orderManagementPage(Model model){

        model.addAttribute("orderCheck",true);
        model.addAttribute("orders", orderService.getOrderDTOList());

        return "views/layout";
    }

    @GetMapping("/detail/{orderId}")
    public String orderDetailPage(Model model, @PathVariable("orderId") Long orderId){

        model.addAttribute("orderDetailCheck",true);
        model.addAttribute("orderDetail", orderService.getOrderDetailDTO(orderId));

        return "views/layout";
    }


}
