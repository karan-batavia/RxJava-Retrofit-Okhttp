
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
    String url;
}
