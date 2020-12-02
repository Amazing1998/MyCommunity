package com.chao.community.POJO;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class GitHubUser {
    private Long id;
    private String accountId;
    private String name;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;
}
