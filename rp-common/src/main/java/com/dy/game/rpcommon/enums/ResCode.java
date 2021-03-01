package com.dy.game.rpcommon.enums;

public enum ResCode {


            ;

    private final Integer code;
    private final String message;

    ResCode(final Integer code, final String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
    }
