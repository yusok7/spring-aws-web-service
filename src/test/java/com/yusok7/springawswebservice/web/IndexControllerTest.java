package com.yusok7.springawswebservice.web;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class IndexControllerTest {

    /**
     * TestRestTemplate : SpringBoot에서 컨트롤러를 테스트 하기 위해서 사용한다.
     * cf. MockMvc 또한 컨트롤러를 테스트하는 라이브러리이다.
     *
     * 둘의 차이는 서블릿 컨테이너의 실행여부
     *
     * MockMvc는 컨테이너를 실행하지 않는다.
     * TestRestTemplate은 컨테이너를 직접 실행시킨다.
     *
     * !! 테스트 관점의 차이가 있다.
     * MockMvc는 서버의 입장에서 구현한 API를 통해 비지니스 로직에 문제가 없는지 테스트하고
     * TestRestTemplate은 클라이언트 입장에서 사용할 때 문제가 없는지 테스트한다.
     *
     * 출처 : https://youngssse.tistory.com/entry/Java-Spring-TestRestTemplate
     */

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void 메인페이지_로딩() {

        // when
        String body = restTemplate.getForObject("/", String.class);

        // then
        Assertions.assertThat(body).contains("스프링 부트로 시작하는 웹 서비스");
    }

}