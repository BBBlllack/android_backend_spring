package com.shj.apiserver.controller;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private OkHttpClient client;

    private static final String CLINT_SECRET = "dd42836a172121bb9e48f39eb80639ce7f8c53398f3f7e7b6f836f0f571ec5fe";
    private static final String CLINT_ID = "9525505dca256e3d45533fa95c28a86c00f219121dcf2fdd5a5da1aceb17888d";

    /**
     * Oauth2.0
     * @param code
     * @param state
     * @return
     * @throws IOException
     */
    @GetMapping("/auth/success")
    public Object authSuccess(String code, String state) throws IOException {
        log.info("user auth code: {}", code);
        log.info("user auth state: {}", state);
        // 向gitee认证服务器发起请求
        // https://gitee.com/oauth/token?grant_type=authorization_code&code={code}&client_id={client_id}&redirect_uri={redirect_uri}&client_secret={client_secret}
        String url = "https://gitee.com/oauth/token?grant_type=authorization_code&code=%s&client_id=%s&redirect_uri=%s&client_secret=%s";
        String auth_url = String.format(url, code, CLINT_ID, "http://localhost:8080/user/auth/success", CLINT_SECRET);
        log.info("auth url: {}", auth_url);
        RequestBody body = new FormBody.Builder()
                .add("client_secret", CLINT_SECRET)
                .build();
        Request request = new Request.Builder()
                .url(auth_url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        try {
            String token = response.body().string();
            log.info("认证服务器返回: {}", token);
            return token;
        }finally {
            response.close();
        }
    }

    /**
     * {"access_token":"4346fda566317e048f418a980e90a88d","token_type":"bearer","expires_in":86400,
     * "refresh_token":"0241c831f05ca4da0ad69fe1c1445500cce0c86a35f56512f5d94bde39cbd2c7",
     * "scope":"user_info emails","created_at":1710158702}
     */
    // 10.202.252.117

    @GetMapping("/auth/info")
    public Object authInfo(String token) throws IOException {
        String url = "https://gitee.com/api/v5/user?access_token=" + token;
        return client.newCall(new Request.Builder().url(url).get().build()).execute().body().string();
    }
}
