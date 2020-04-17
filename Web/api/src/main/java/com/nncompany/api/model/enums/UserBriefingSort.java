package com.nncompany.api.model.enums;

public enum UserBriefingSort {
    id("id"),
    user("user.name"),
    briefing("briefing.name"),
    date("lastDate");

    private final String title;

    UserBriefingSort(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
