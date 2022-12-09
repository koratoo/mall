package com.mall.service;

import com.mall.dto.ItemFormDto;
import com.mall.dto.ItemImgDto;
import com.mall.dto.ItemSearchDto;
import com.mall.dto.MainItemDto;
import com.mall.entity.Item;
import com.mall.entity.ItemImg;
import com.mall.repository.ItemImgRepository;
import com.mall.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        //상품등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i==0)//첫번째 이미지의 경우 대표 이미지 여부 값을 Y로 세팅한다.
                itemImg.setRepimgYn("Y");
            else
                itemImg.setRepimgYn("N");
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));//상품의 이미지 정보를 저장
        }

        return item.getId();
    }

        //상품 수정하기1 - 이미지 조회
        @Transactional(readOnly = true)
        public ItemFormDto getItemDtl(Long itemId){
            List<ItemImg> itemImgList =
                    itemImgRepository.findByItemIdOrderByIdAsc(itemId);//이미지 조회
            List<ItemImgDto> itemImgDtoList = new ArrayList<>();
            for(ItemImg itemImg : itemImgList) {
                ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
                itemImgDtoList.add(itemImgDto);
            }

            Item item = itemRepository.findById(itemId)
                    .orElseThrow(EntityNotFoundException::new); //상품 아이디를 통해 조회하고 없을시 예외처리
            ItemFormDto itemFormDto = ItemFormDto.of(item);
            itemFormDto.setItemImgDtoList(itemImgDtoList);
            return itemFormDto;

        }
        //상품 수정하기 4
        public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

            //상품수정
            Item item = itemRepository.findById(itemFormDto.getId())
                    .orElseThrow(EntityNotFoundException::new);
            item.updateItem(itemFormDto);

            List<Long> itemImgIds = itemFormDto.getItemImgIds();

            for(int i=0; i<itemImgFileList.size();i++){
                itemImgService.updateItemImg(itemImgIds.get(i),itemImgFileList.get(i));
            }
            return item.getId();
        }

        @Transactional(readOnly = true)
        public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
            return itemRepository.getAdminItemPage(itemSearchDto, pageable);
        }

        @Transactional(readOnly = true)
        public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){

            return itemRepository.getMainItemPage(itemSearchDto, pageable);
        }

}
