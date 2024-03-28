package com.s001.shoppingmall.affiliate.dto;

import com.s001.shoppingmall.order.entity.DeliveryStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderStatusPatchApiRequest {

	private String orderStatus;

	public static OrderStatusPatchApiRequest of(DeliveryStatus deliveryStatus) {
		// 주문 완료, 배송중, 배송 완료, 주문 취소
		String value = switch (deliveryStatus) {
			case ORDER_CANCELED -> "주문 취소";
			case ORDER_COMPLETED -> "주문 완료";
			case DELIVERY_COMPLETED -> "배송 완료";
			case DELIVERY_IN_PROGRESS -> "배송중";
		};
		return new OrderStatusPatchApiRequest(value);
	}
}
