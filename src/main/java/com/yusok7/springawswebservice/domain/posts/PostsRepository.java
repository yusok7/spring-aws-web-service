package com.yusok7.springawswebservice.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Entity 클래스와 기본 Entity Repository는 함께 위치해야 한다. 즉 하나의 도메인 패키지에서 함께 관리한다.
 */
public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();
}
