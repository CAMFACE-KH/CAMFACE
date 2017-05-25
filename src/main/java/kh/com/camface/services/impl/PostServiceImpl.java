package kh.com.camface.services.impl;

import kh.com.camface.filtering.PostFilter;
import kh.com.camface.models.Post;
import kh.com.camface.repositories.PostRepository;
import kh.com.camface.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by HP1 on 4/10/2017.
 */
@Service
public class PostServiceImpl implements PostService{

    @Autowired
    private PostRepository postRepository;

    @Override
    public List<Post> findAllPopularPosts(PostFilter filter) {
        try{
            return postRepository.findAllPopularPost(filter);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
