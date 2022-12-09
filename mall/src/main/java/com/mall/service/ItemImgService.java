package com.mall.service;

import com.mall.entity.ItemImg;
import com.mall.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            imgUrl ="/images/item/" + imgName;
        }

        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }
        //상품 수정하기 1 - ItemService
        //상품 수정하기 2 - 변경 감지기능
        public void updateItemImg(Long itemImgId, MultipartFile itemImgFile)
                throws Exception{
                    if(!itemImgFile.isEmpty()) {
                        ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                                .orElseThrow(EntityNotFoundException::new);

                        //기존에 같은 이름으로 등록되었다면 이미지 파일 삭제
                        if (!StringUtils.isEmpty(savedItemImg.getImgName())){
                            fileService.deleteFile(itemImgLocation+"/"+savedItemImg.getImgName());
                        }
                        String oriImgName = itemImgFile.getOriginalFilename();
                        String imgName = fileService.uploadFile(itemImgLocation,
                                oriImgName,itemImgFile.getBytes());//업데이트 파일을 업로드한다.
                        String imgUrl = "/images/item/" + imgName;
                        savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);//save가 아닌 update를 써준다.
                    }
    }
}
