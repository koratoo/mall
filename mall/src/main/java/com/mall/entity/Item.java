package com.mall.entity;

import com.mall.constant.ItemSellStatus;
import com.mall.dto.ItemFormDto;
import com.mall.exception.OutOfStockException;
import javassist.tools.rmi.ObjectNotFoundException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {

    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //상품코드

    @Column(name="item_name", nullable = false, length = 50)
    private String itemNm;//상품명

    @Column(name="item_price", nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockNumber;//재고수량

    @Lob//CLOB,BLOB이 있다. 사이즈가 큰 데이터를 외부에 저장하기 위함
    @Column(name="item_detail", nullable = false)
    private String itemDetail;

    @Enumerated(EnumType.STRING)//enum타입 매핑
    private ItemSellStatus itemSellStatus;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

    //상품 수정하기3 - 상품을 업데이트하는 로직 구현
    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber){
        //재고 수량에서 주문 후 남은 재고 수량을 구함
        int restStock = this.stockNumber -stockNumber;
        if(restStock<0){
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량 "+this.stockNumber+")");
        }
        this.stockNumber = restStock;
    }

    public void addStock(int stockNumber){
        this.stockNumber += stockNumber;
    }
}
