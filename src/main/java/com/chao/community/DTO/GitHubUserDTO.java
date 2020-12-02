package com.chao.community.DTO;

import lombok.Data;

@Data
public class GitHubUserDTO {
    private String login;
    private String id;
    private String email;
    private String avatarUrl;
}
