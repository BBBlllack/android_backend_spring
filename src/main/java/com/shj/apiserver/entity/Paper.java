package com.shj.apiserver.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Paper implements Serializable {
    /**
     alter table paper
     add column pname varchar(255);
     alter table paper
     add column fid int;
     alter table paper
     add column fname varchar(255);

     update paper, thread set paper.pname = thread.categoryName where paper.postid = thread.postId;
     update paper, thread set paper.fid = thread.parentCategoryId where paper.postid = thread.postId;
     update paper, thread set paper.fname = thread.parentCategoryName where paper.postid = thread.postId;
     */
    private Integer id;

    private Integer postId;

    private Integer pid; // 当前分类 id
    private String pname; // 当前分类名
    private Integer fid; // 父类id
    private String fname; // 父类名

    @TableField("title_zh")
    private String titleZh;

    @TableField("title_en")
    private String titleEn;

    private String author;

    private String link;

    private String comment;

    @TableField("detail_en")
    private String detailEn;

    @TableField("detail_zh")
    private String detailZh;

    private LocalDateTime createdAt;
    private Integer favor;

}
