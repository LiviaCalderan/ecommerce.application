package com.app.ecommerce.payload;

import com.app.ecommerce.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusUpdateDTO {

    private OrderStatus orderStatus;
}
