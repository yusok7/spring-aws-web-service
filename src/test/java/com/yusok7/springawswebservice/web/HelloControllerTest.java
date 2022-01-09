package com.yusok7.springawswebservice.web;

import com.yusok7.springawswebservice.config.auth.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ExtendWith
 * 테스트를 진행할 대 JUnit에 내장된 실행자 외에 다른 실행자를 실행시킨다.
 * 여기서는 SpringRunner라는 스프링 실행자를 사용한다.
 */
@ExtendWith(SpringExtension.class)
/**
 * WebMvcTest
 * 여러 스프링 어노테이션 중, Web(Spring MVC)에 집중할 수 있는 어노테이션이다.
 * 선언할 경우 @Controller, @ControllerAdvice 등을 사용할 수 있다.
 * 단, @Service, @Component, @Repository등은 사용할 수 없다.
 * 여기서는 Controller만 사용하기 때문에 선언한다.
 */
@WebMvcTest(controllers = HelloController.class,
        excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = SecurityConfig.class)
        })
class HelloControllerTest {

    // @Autowired : 스프링이 관리하는 빈(Bean)을 주입 받는다.
    // 웹 API를 테스트할 때 사용한다.
    // 스프링 MVC의 시작점. 이 클래스를 통해 HTTP GET, POST등에 대한 API를 테스트 할 수 있다.
    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(roles = "USER")
    public void hello_리턴() throws Exception {
        String hello = "hello";

        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void helloDto_리턴() throws Exception {
        String name = "hello";
        int amount = 1000;

        /**
         * ▪ param
         *      ▫ API 테스트할 때 사용될 요청 파라미터를 설정
         *      ▫ 단, String 값만 허용
         *      ▫ 그래서 숫자/날짜 등의 데이터도 등록할 때는 문자열로 변경해야만 가능하다.
         * ▪ jsonPath
         *      ▫ JSON응답값을 필드별로 검증할 수 있는 메소드
         *      ▫ $를 기준으로 필드명을 명시한다.
         *      ▫ 여기서는 name과 amount를 검증하니 $.name, $.amount로 검증한다.
         */

        mvc.perform(get("/hello/dto")
                .param("name", name)
                .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));
    }
}