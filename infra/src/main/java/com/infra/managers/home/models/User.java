
package com.infra.managers.home.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

import lombok.Data;
import lombok.NoArgsConstructor;

@Generated("org.jsonschema2pojo")
@Data
@NoArgsConstructor
public class User {

    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("avatar_image")
    @Expose
    public AvatarImage avatarImage;
    @SerializedName("cover_image")
    @Expose
    public CoverImage coverImage;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;

}
