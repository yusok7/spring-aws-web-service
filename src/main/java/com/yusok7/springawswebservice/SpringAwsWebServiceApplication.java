package com.yusok7.springawswebservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 앞으로 만들 프로젝트의 메인 클래스
 * @SpringBootApplication으로 인해 스프링 부트의 자동 설정, 스프링 Bean 읽기와 생성을 모두 자동으로 설정된다.
 * @SpringBootApplication이 있는 위치부터 설정을 읽어가기 때문에 이 클래스는 항상 프로젝트의 최상단에 위치해야만 한다.
 */

@EnableJpaAuditing
@SpringBootApplication
public class SpringAwsWebServiceApplication {

	public static void main(String[] args) {
		// SpringApplication.run으로 인해 내장 WAS를 실행한다.
		SpringApplication.run(SpringAwsWebServiceApplication.class, args);
	}

}
