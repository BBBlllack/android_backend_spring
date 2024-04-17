package com.shj.apiserver.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Thread {
    private Integer threadId;
    @TableId
    private Integer postId;
    private Integer topicId;
    private String title;
    private Integer categoryId;
    private String categoryName;
    private LocalDateTime issueAt;
    private Integer parentCategoryId;
    private String parentCategoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String content;
}
