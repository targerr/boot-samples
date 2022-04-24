package com.example.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author wanggs
 * @since 2022-04-22
 */
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String title;

    private String content;

    private LocalDateTime createDateTime;

    private LocalDateTime modifyDateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }
    public LocalDateTime getModifyDateTime() {
        return modifyDateTime;
    }

    public void setModifyDateTime(LocalDateTime modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

    @Override
    public String toString() {
        return "Blog{" +
            "id=" + id +
            ", title=" + title +
            ", content=" + content +
            ", createDateTime=" + createDateTime +
            ", modifyDateTime=" + modifyDateTime +
        "}";
    }
}
