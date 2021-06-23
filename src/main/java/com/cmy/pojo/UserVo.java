package com.cmy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo{

    private String id;
    private String username;
    private String password;
    private String applyRole;
    private Integer roleId;
    private Integer point;
    private String menuStr;
}
