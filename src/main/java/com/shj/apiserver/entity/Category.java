package com.shj.apiserver.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("categories")
public class Category {
    @TableId
    private Integer categoryId;
    private Integer canCreateThread;
    private String description;
    private String icon;
    private String name;
    private Integer parentid;
    private Integer pid;
    private Integer property;
    private Integer sort;
    private Integer threadCount;
}
