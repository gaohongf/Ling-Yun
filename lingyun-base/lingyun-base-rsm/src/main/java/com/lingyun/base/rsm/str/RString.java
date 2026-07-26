package com.lingyun.base.rsm.str;

public record RString(String str) {
    public static RString warp(String str) {
        return new RString(str);
    }
}
