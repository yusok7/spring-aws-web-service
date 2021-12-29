package com.yusok7.springawswebservice.web;

import com.yusok7.springawswebservice.config.auth.LoginUser;
import com.yusok7.springawswebservice.config.auth.dto.SessionUser;
import com.yusok7.springawswebservice.service.posts.PostsService;
import com.yusok7.springawswebservice.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {

        model.addAttribute("posts", postsService.findAllDesc());

        if (user != null) {
            /**
             * 세션에 저장된 값이 있을 때만 model에 userName으로 등록한다.
             * 즉 해당 부분 덕분에 index.mustache에서 userName을 사용할 수 있다.
             * 세션에 저장된 값이 없으면 model엔 아무런 값이 없는 상태이니 로그인 버튼이 보이게 된다.
             */
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }

}
