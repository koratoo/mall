package com.mall.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberFormDto {

        @NotBlank(message = "이름을 입력해 주세요")
        private String name;

        @NotEmpty(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "이메일 형식을 입력해 주세요!")
        private String email;

        @NotEmpty(message = "비밀번호를 입력해 주세요")
        @Length(min=8, max=16, message="비밀번호는 8자 이상 16자 이하로 입력부탁드립니다.")
        private String password;

        @NotEmpty(message = "주소를 입력해 주세요!")
        private String address;
}
