package com.sparta.javajyojo.enums;

public enum ContentTypeEnum {
    ORDER("order"),
    REVIEW("review");

    private final String contentType;

    ContentTypeEnum(String value) {
        this.contentType = value;
    }
}
