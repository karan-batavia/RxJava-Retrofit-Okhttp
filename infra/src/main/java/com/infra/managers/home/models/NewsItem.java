
package com.infra.managers.home.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

import lombok.Data;
import lombok.NoArgsConstructor;

@Generated("org.jsonschema2pojo")
@Data
@NoArgsConstructor
public class NewsItem {

    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("text")
    @Expose
    public String text;
    @SerializedName("id")
    @Expose
    public long id;
    @SerializedName("user")
    @Expose
    public User user;
    @SerializedName("thread_id")
    @Expose
    public String threadId;
    @SerializedName("pagination_id")
    @Expose
    public String paginationId;

}
