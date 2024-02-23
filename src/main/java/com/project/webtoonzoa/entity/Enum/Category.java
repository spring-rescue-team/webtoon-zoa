package com.project.webtoonzoa.entity.Enum;

public enum Category {
    SF("SF"),
    HORROR("HORROR"),
    COMEDY("COMEDY"),
    FANTASY("FANTASY"),
    LOVECOMEDY("LOVECOMEDY");

    private final String value;

    Category(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
