package com.x.wcrm.core.entity;

import com.x.base.core.entity.JsonProperties;
import com.x.base.core.project.annotation.FieldDescribe;

public class RecordProperties extends JsonProperties {
    @FieldDescribe("跟进内容")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
