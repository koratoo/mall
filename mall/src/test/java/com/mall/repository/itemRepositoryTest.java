package com.mall.repository;

import com.mall.constant.ItemSellStatus;
import com.mall.entity.Item;
import com.mall.entity.QItem;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
    @TestPropertySource(locations="classpath:application-test.properties")
    class ItemRepositoryTest{

        @PersistenceContext
        EntityManager   em;

        @Autowired
        ItemRepository itemRepository;

        @Test
        @DisplayName("상품저장 테스트")
        public void createItemList(){
            for(int i=1;i<10;i++){
                Item item = new Item();
                item.setItemNm("필름레드 샹크스" + i);
                item.setPrice(23000 + i);
                item.setItemDetail("필름 레드 개봉으로 인한 한정 이벤트 상품"+ i);
                item.setItemSellStatus(ItemSellStatus.SELL);
                item.setStockNumber(20);
                item.setRegTime(LocalDateTime.now());
                item.setUpdateTime(LocalDateTime.now());
                Item savedItem = itemRepository.save(item);
            }
        }

        @Test
        @DisplayName("상품명 조회 테스트")
        public void findByItemNameTest(){
            this.createItemList();
            List<Item> itemList = itemRepository.findByItemNm("필름레드 샹크스");
                    for(Item item : itemList){
                        System.out.println(item.toString());
                    }
        }

        @Test
        @DisplayName("상품명, 상품상세설명 or 테스트")
        public void findByItemNameOrItemDetailTest(){
            this.createItemList();
            List<Item> itemList =
                    itemRepository.findByItemNmOrItemDetail("필름레드 샹크스","필름 레드 개봉으로 인한 한정 이벤트 상품");
                    for(Item item : itemList){
                        System.out.println(item.toString());
                    }
        }

        @Test
        @DisplayName("가격 LessThan 테스트")
        public void findByPriceLessThanTest(){
            this.createItemList();
            List<Item> itemList = itemRepository.findByPriceLessThan(24000);
            for(Item item : itemList){
                System.out.println(item.toString());
            }
        }
        @Test
        @DisplayName("가격 내림차순 테스트")
        public void findByPriceLessThanOrderByPriceDesc(){
            this.createItemList();
            List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(24000);
            for(Item item : itemList){
                System.out.println(item.toString());
            }
        }

        @Test
        @DisplayName("@Query를 이용한 상품조회 테스트")
        public void findByItemDetailTest(){
            this.createItemList();
            List<Item> itemList = itemRepository.findByItemDetail("필름 레드");
            for(Item item : itemList){
                System.out.println(item.toString());
            }

        }

        @Test
        @DisplayName("Querydsl 조회테스트1")
        public void queryDslTest(){
            this.createItemList();
            JPAQueryFactory queryFactory = new JPAQueryFactory(em);
            QItem qItem = QItem.item;
            JPAQuery<Item>  query = queryFactory.selectFrom(qItem)
                    .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                    .where(qItem.itemDetail.like("%"+"테스트 상품 상세설명"+"%"))
                    .orderBy(qItem.price.desc());

            List<Item> itemList = query.fetch();

            for(Item item : itemList){
                System.out.println(item.toString());
            }
        }


}