package com.jwaoo.account.web.rest.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

/**
 * @author Jerry
 * @date 2017/8/15 10:31
 */
public class NearByReqDto
{

//    @NotEmpty
//    @Size(min = -180, max = 180)
//    private Double[] loc;

    @NotNull
    @DecimalMin("-180")
    @DecimalMax("180")
    private Double longitude;

    @NotNull
    @DecimalMin("-90")
    @DecimalMax("90")
    private Double latitude;

    @Max(100)
    private Integer distance;

    @Max(1000)
    private Integer num;

    @Length(max = 24)
    private String start;


//    public Double[] getLoc() {
//        return loc;
//    }
//
//    public void setLoc(Double[] loc) {
//        this.loc = loc;
//    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

}
