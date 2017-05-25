package kh.com.camface.services;

import kh.com.camface.filtering.PostFilter;
import kh.com.camface.models.Post;

import java.util.List;

/**
 * Created by HP1 on 4/10/2017.
 */
public interface PostService {

    public List<Post> findAllPopularPosts(PostFilter filter);
}
