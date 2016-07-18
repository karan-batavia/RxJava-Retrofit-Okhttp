
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
    String createdAt;
    @SerializedName("text")
    @Expose
    String text;
    @SerializedName("id")
    @Expose
    long id;
    @SerializedName("user")
    @Expose
    User user;
    @SerializedName("thread_id")
    @Expose
    String threadId;
    @SerializedName("pagination_id")
    @Expose
    String paginationId;

    boolean animate;
}
