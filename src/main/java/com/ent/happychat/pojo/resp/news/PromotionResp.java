package com.ent.happychat.pojo.resp.news;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PromotionResp implements Serializable {
    private static final long serialVersionUID = -6041331950582014459L;

    @ApiModelProperty("区域")
    private String area1;
    @ApiModelProperty("标题1")
    private String title1;
    @ApiModelProperty("视频链接1")
    private String videoPath1;
    @ApiModelProperty("视频封面1")
    private String videoCover1;
    @ApiModelProperty("图片路径1")
    private String imagePath1;

    @ApiModelProperty("区域")
    private String area2;
    @ApiModelProperty("标题2")
    private String title2;
    @ApiModelProperty("视频链接2")
    private String videoPath2;
    @ApiModelProperty("视频封面2")
    private String videoCover2;
    @ApiModelProperty("图片路径2")
    private String imagePath2;


}
