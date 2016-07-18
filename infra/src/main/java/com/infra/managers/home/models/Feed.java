package com.infra.managers.home.models;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by hetashah on 7/17/16.
 */
@Data
@NoArgsConstructor
public class Feed {
    List<NewsItem> data;
}
