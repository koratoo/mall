package com.mall.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CartItemDto {

    @NotNull(message = "상품 아이디를 입력해 주세요!")
    private Long itemId;

    @Min(value=1, message="최소 1개 이상을 담아주세요!")
    private int count;
}
