package com.example.links_orgenizer;

import java.util.ArrayList;

public class Link {
    private String type, name, description, tags, link;

    public Link(String type, String name, String description, String tags, String link) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.tags = tags;
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
