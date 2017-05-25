package kh.com.camface.repositories;

import kh.com.camface.filtering.PostFilter;
import kh.com.camface.models.Post;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by HP1 on 4/10/2017.
 */
@Repository
public interface PostRepository {

   //TODO: TO FIND ALL POPULAR POSTS
   public List<Post> findAllPopularPost(PostFilter filter);

   //TODO: TO SAVE A NEW POST
   public int save(Post post);

   //TODO: TO SAVE A NEW ORIGINAL POST
   public int saveOrignal(Post post);
}
