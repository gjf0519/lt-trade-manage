package com.lt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author gaijf
 * @description
 * @date 2020/3/10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LtUser {
    private int id;
    private String userName;
    private String passWord;
    private String email;
    private Date updateTime;
    private Date createTime;
}
