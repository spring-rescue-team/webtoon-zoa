package com.project.webtoonzoa.entity.Enum;

public enum Day {
    MON("mon"),
    THE("the"),
    WED("wed"),
    THU("thu"),
    FRI("fri"),
    SAT("sat"),
    SUN("sun");

    private final String value;

    Day(String value) {
        this.value = value;
    }

    public String getDay() {
        return this.value;
    }
}
