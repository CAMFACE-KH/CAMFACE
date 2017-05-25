package kh.com.camface.restcontrollers;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import kh.com.camface.filtering.PostFilter;
import kh.com.camface.models.Post;
import kh.com.camface.services.PostService;
import kh.com.camface.utilities.Pagination;
import kh.com.camface.utilities.ResponseList;
import kh.com.camface.utilities.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by HP1 on 4/10/2017.
 */
@RestController
public class PostRestController {

    @Autowired
    private PostService postService;

    @RequestMapping(value="/v1/api/popular-posts", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "1",
                    value = "Result page you want to retrieve (1..N)"),
            @ApiImplicitParam(name = "limit", dataType = "integer", paramType = "query", defaultValue = "15",
                    value = "Number of records per page."),
    })
    public ResponseList<Post> findAllPopularPosts(@ApiIgnore PostFilter filter, @ApiIgnore Pagination pagination){
        ResponseList<Post> response = new ResponseList<>();
        response.setCode(StatusCode.SUCCESS);
        List<Post> posts = postService.findAllPopularPosts(filter);
        int total_shared_story_count = 0;
        for(Post post: posts){
            total_shared_story_count += Integer.valueOf(post.getShareStoryCount());
        }

        for(Post post: posts){
            int shared_story_count = Integer.valueOf(post.getShareStoryCount());
            String created_time = post.getCreatedTime();
            // Add ':' before the last 2 digits in order to get "2017-04-10T01:53:26+00:00"
            String created_time_correct_format = created_time.substring(0, created_time.length()-2) + ":" + "00";

            ZonedDateTime shared_time = ZonedDateTime.parse(created_time_correct_format);
            ZonedDateTime current_time = ZonedDateTime.now();

            double var1 = (double) shared_story_count / total_shared_story_count;
            double var2 = (double) shared_time.toEpochSecond() / current_time.toEpochSecond();

            double ranking_score = (var1 + var2) / 2;
            post.setRankingScore(ranking_score);
        }
        Comparator<Post> comparator = new Comparator<Post>() {
            @Override
            public int compare(Post left, Post right) {
                return Double.compare(right.getRankingScore(), left.getRankingScore()); // use your logic
            }
        };
        Collections.sort(posts, comparator); // use the comparator as much as u want
        List<Post> top10 = new ArrayList<>(posts.subList(0, 10));
        response.setData(top10);
        return response;
    }
}
