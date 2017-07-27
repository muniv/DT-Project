package com.muni.examples.aibrilcall;

import java.io.Serializable;

public class CallDto implements Serializable {
    private String id;
    private String title;

    public CallDto() {
    }

    public CallDto(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "CallDto{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
