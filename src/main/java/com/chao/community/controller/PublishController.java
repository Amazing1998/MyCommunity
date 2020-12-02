package com.chao.community.controller;

import com.chao.community.POJO.GitHubUser;
import com.chao.community.POJO.Question;
import com.chao.community.mapper.UserMapper;
import com.chao.community.mapper.QuestionMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Resource
    private QuestionMapper questionMapper;
    @Resource
    private UserMapper userMapper;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            HttpServletRequest request,
            Model model
    ){
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        if (title == null || title == ""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }

        if(description == null || description == ""){
            model.addAttribute("error","问题补充不能为空");
            return "publish";
        }

        if (tag == null || tag == ""){
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }

        GitHubUser gitHubUser = null;
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("myToken")){
                    String token = cookie.getValue();
                    gitHubUser = userMapper.queryByToken(token);
                    if(gitHubUser!=null){
                        request.getSession().setAttribute("user",gitHubUser);
                    }
                    break;
                }
            }
        }
        if(gitHubUser == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(gitHubUser.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionMapper.create(question);
        return "redirect:/";
    }
}
