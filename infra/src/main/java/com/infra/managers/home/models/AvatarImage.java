
package com.infra.managers.home.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

import lombok.Data;
import lombok.NoArgsConstructor;

@Generated("org.jsonschema2pojo")
@Data
@NoArgsConstructor
public class AvatarImage {

    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("width")
    @Expose
    public Integer width;
    @SerializedName("is_default")
    @Expose
    public Boolean isDefault;
    @SerializedName("height")
    @Expose
    public Integer height;

}
