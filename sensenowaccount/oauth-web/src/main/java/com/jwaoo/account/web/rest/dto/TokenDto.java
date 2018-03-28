package com.jwaoo.account.web.rest.dto;

public class TokenDto {

	private String token;
    private Integer locked = 0;
    private Integer perfect = 0;

	public TokenDto() {
	}

	public TokenDto(String token, Integer perfect) {
		this.token = token;
		this.perfect = perfect;
	}

	public TokenDto(String token, Integer perfect, Integer locked) {
		this.token = token;
		this.perfect = perfect;
        this.locked = locked;
	}

	public TokenDto(String token) {
		this.token = token;
	}

    public Integer getPerfect() {
        return perfect;
    }

    public String getToken() {
		return token;
	}

    public Integer getLocked() {
        return locked;
    }
}
