package com.chao.community.controller;

import com.chao.community.DTO.GitHubDTO;
import com.chao.community.DTO.GitHubUserDTO;
import com.chao.community.POJO.GitHubUser;
import com.chao.community.mapper.UserMapper;
import com.chao.community.provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class loginController {
    @Resource
    private GitHubProvider gitHubProvider;
    @Resource
    private UserMapper userMapper;
    @Value("${github.client.id}")
    private String client_id;
    @Value("${github.client.secret}")
    private String client_secret;

    @GetMapping("/login")
    public String login(){
        return "login";
    }


    @GetMapping("/gitHubLogin")
    public String gitHubLogin(){
        return "redirect:https://github.com/login/oauth/authorize?client_id=c636a7b9d2e60afb6caf&redirect_uri=http://localhost:8080/oauth/redirect&state=Amazing";
    }

    @GetMapping("/oauth/redirect")
    public String redirect(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response){
        GitHubDTO gitHubDTO = new GitHubDTO();
        gitHubDTO.setClient_id(client_id);
        gitHubDTO.setClient_secret(client_secret);
        gitHubDTO.setRedirect_uri("http://localhost:8080/oauth/redirect");
        gitHubDTO.setCode(code);
        gitHubDTO.setState(state);
        String accessToken = gitHubProvider.getAccessToken(gitHubDTO);
        GitHubUserDTO gitHubUserDTO = gitHubProvider.getGitHubUser(accessToken);
        if(gitHubUserDTO != null){
            GitHubUser gitHubUser = userMapper.queryById(gitHubUserDTO.getId());
            if(gitHubUser == null){
                gitHubUser = new GitHubUser();
                gitHubUser.setGmtCreate(System.currentTimeMillis());
                gitHubUser.setGmtModified(gitHubUser.getGmtCreate());
                gitHubUser.setAccountId(gitHubUserDTO.getId());
                gitHubUser.setToken(UUID.randomUUID().toString());
                gitHubUser.setName(gitHubUserDTO.getLogin());
                gitHubUser.setAvatarUrl(gitHubUserDTO.getAvatarUrl());
                userMapper.insertUser(gitHubUser);
            }
            Cookie cookie = new Cookie("myToken", gitHubUser.getToken());
            cookie.setMaxAge(Integer.MAX_VALUE);
            cookie.setDomain("localhost");
            cookie.setPath("/");
            response.addCookie(cookie);
            return "redirect:/";
        }else {
            return "login";
        }

    }


}
