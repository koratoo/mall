package com.mall.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    //Audit는 감시하다
    //JPA가 업데이트 사항을 감시한다는 내용
    @Override
    public Optional<String> getCurrentAuditor(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";
        if(authentication != null){
            userId = authentication.getName();
        }

        return Optional.of(userId);
    }
}
