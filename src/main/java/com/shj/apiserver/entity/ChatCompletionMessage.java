package com.shj.apiserver.entity;

import java.io.Serializable;

public class ChatCompletionMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String content;
    private String role;
    private String function_call;
    private String tool_calls;
}
