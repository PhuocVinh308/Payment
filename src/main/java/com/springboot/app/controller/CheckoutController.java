package com.springboot.app.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lib.payos.PayOS;
import com.lib.payos.type.ItemData;
import com.lib.payos.type.PaymentData;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CheckoutController {
    private final PayOS payOS;

    public CheckoutController(PayOS payOS) {
        this.payOS = payOS;
    }


    @PostMapping(value = "/payment", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void createPaymentLink(@RequestParam int tongTien,HttpServletResponse httpServletResponse) {
        try {
            final String productName = "Thanh toan hoa don";
            final String description = "Thanh toan VTCoffee";
            final String returnUrl = "localhost:3000/success";
            final String cancelUrl = "localhost:3000/novalue";
            final int price = tongTien;

            // Gen order code
            String currentTimeString = String.valueOf(new Date().getTime());
            int orderCode = Integer.parseInt(currentTimeString.substring(currentTimeString.length() - 6));

            ItemData item = new ItemData(productName, 1, price);
            List<ItemData> itemList = new ArrayList<>();
            itemList.add(item);

            PaymentData paymentData = new PaymentData(orderCode, price, description,
                    itemList, cancelUrl, returnUrl);
            JsonNode data = payOS.createPaymentLink(paymentData);

            String checkoutUrl = data.get("checkoutUrl").asText();
            httpServletResponse.sendRedirect(checkoutUrl);
  System.out.print(checkoutUrl);
            httpServletResponse.setHeader("Location", checkoutUrl);
            httpServletResponse.setStatus(HttpServletResponse.SC_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
