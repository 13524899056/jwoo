package com.jwaoo.account.web.rest.dto;

import javax.validation.constraints.NotBlank;

/**
 * @author Jerry
 * @date 2017/12/15 18:31
 */
public class VerifyReqDto
{

    @NotBlank
    private String imgUrl;

    @NotBlank
    private String audioUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

}
