package com.yusok7.springawswebservice.config.auth;

import com.yusok7.springawswebservice.config.auth.dto.OAuthAttributes;
import com.yusok7.springawswebservice.config.auth.dto.SessionUser;
import com.yusok7.springawswebservice.domain.user.User;
import com.yusok7.springawswebservice.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

/**
 * 해당 클래스에서는 구글 로그인 이후 가져온 사용자의 정보(email, name, picture등)들을 기반으로
 * 가입 및 정보수정, 세션 저장 등의 기능 지원한다.
 *
 * 즉!! 리소스 서버(즉, 소셜 서비스들)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시하는 곳이다!!!
 */

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 현재 로그인 진행 중인 서비스를 구분하는 코드
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // OAuth2 로그인 진행 시 키가 되는 필드값을 이야기한다. Primary Key와 같은 의미이다.
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스다.
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);

        /**
         * SessionUser
         * - 세션에 사용자 정보를 저장하기 위한 Dto 클래스
         * - 왜 User클래스를 쓰지 않고 새로 만들어서 써야만 할까?
         * - User 클래스에 직렬화를 구현하면 너무 많은 부수적인 부분을 생각해야한다.
         * - 책 188p
         */
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    /**
     * findByEmail을 통해 값이 있다면
     *      .map을 이용해 수정된 값을 반환한다.
     * 없다면
     *      .orElse를 통해 들어온 값을 그대로 반환한다.
     */
    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());
        return userRepository.save(user);
    }
}
