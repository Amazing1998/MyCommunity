package com.chao.community.controller;

import com.chao.community.POJO.GitHubUser;
import com.chao.community.mapper.GitHubUserMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class indexController {

    @Resource
    private GitHubUserMapper gitHubUserMapper;

    @GetMapping("/")
    public String index(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("myToken")){
                    String token = cookie.getValue();
                    GitHubUser gitHubUser = gitHubUserMapper.queryByToken(token);
                    if(gitHubUser!=null){
                        request.getSession().setAttribute("user",gitHubUser);
                    }
                    break;
                }
            }
        }
        return "index";
    }

}
