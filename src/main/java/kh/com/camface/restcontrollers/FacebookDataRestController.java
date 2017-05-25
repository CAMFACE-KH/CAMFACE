package kh.com.camface.restcontrollers;

import com.fasterxml.jackson.databind.JsonNode;
import com.googlecode.batchfb.Batcher;
import com.googlecode.batchfb.FacebookBatcher;
import com.googlecode.batchfb.Later;
import com.googlecode.batchfb.PagedLater;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import kh.com.camface.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by HP1 on 4/24/2017.
 */
@RestController
public class FacebookDataRestController {

    @Autowired
    private PostRepository postRepository;

    @RequestMapping(value = "/v1/api/facebook/{accessToken}", method = RequestMethod.POST)
    public String getFacebookDataByAccessToken(@PathVariable("accessToken") String strAccessToken) {

        //String strAccessToken = "EAALCDAomCzwBAPwp0bwS6KdCcbiep6yXsgo3KZBPqRxllZAb9C5C0LL5rOu8ZAc2Gg2XWe0PpJNWBKcOPb7Q1tZBUr7nAJjZCXnXl2kkwgw3BuicaIGTEl6KwWsLpU8LehZBXrhf9GXCKmivoVJbglt5lTsJA0BEXT06hPw7uhwZCWFIrkRnBt5uwQ7ZA1JlWtcZD";
        FacebookClient.AccessToken accessToken = null;
        try {
            accessToken = new DefaultFacebookClient().obtainExtendedAccessToken("776306919082812", "b2e74d95f3f04769aba7e94c9cc3096d", strAccessToken);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Access Token has been expired.");
        }
        while (true) {
            try {
                System.out.println(accessToken.getAccessToken());
                System.out.println(accessToken.getExpires());
                System.out.println(accessToken.getTokenType());

                Batcher batcher = new FacebookBatcher(accessToken.getAccessToken());

                Later<JsonNode> me = batcher.graph("me");

                PagedLater<JsonNode> feeds = batcher.paged("/me/home?fields=id,application,call_to_action,caption,created_time,description,from,icon,is_hidden,is_published,message,message_tags,name,object_id,parent_id,picture,place,privacy,properties,source,status_type,story,story_tags,targeting,to,type,updated_time,with_tags,shares,link,permalink_url,likes.limit(0).summary(true),comments.limit(0).summary(true)&summary(true)", JsonNode.class);
                do {
                    for (JsonNode jsonNode : feeds.get()) {
                        System.out.println(jsonNode);
                        System.out.println("===================================================================");
                        kh.com.camface.models.Post post = new kh.com.camface.models.Post();
                        String story = jsonNode.get("story") == null ? "" : jsonNode.get("story").textValue();

                        String message = jsonNode.get("message") == null ? null : jsonNode.get("message").textValue();
                        String strStory = "";
                        String postId = "";
                        if (story != "") {
                            //TODO: TO GET THE SHARED WITH OTHER ONLY
                            if (story.contains("and") && story.contains("shared")) {
                                postId = jsonNode.get("id") == null ? null : jsonNode.get("id").textValue();
                                post.setId(postId);
                                post.setName(jsonNode.get("name") == null ? null : jsonNode.get("name").textValue());
                                post.setMessage(message);
                                post.setStory(story);
                                post.setCaption(jsonNode.get("caption") == null ? null : jsonNode.get("caption").textValue());
                                post.setCreatedTime(jsonNode.get("created_time") == null ? null : jsonNode.get("created_time").textValue());
                                post.setDescription(jsonNode.get("description") == null ? null : jsonNode.get("description").textValue());
                                post.setPicture(jsonNode.get("picture") == null ? "" : jsonNode.get("picture").textValue());
                                post.setIsPublished(jsonNode.get("is_published") == null ? null : jsonNode.get("is_published").textValue());
                                post.setIsHidden(jsonNode.get("is_hidden") == null ? null : jsonNode.get("is_hidden").textValue());
                                post.setPermalinkUrl(jsonNode.get("permalink_url") == null ? null : jsonNode.get("permalink_url").textValue());
                                post.setLink(jsonNode.get("link") == null ? null : jsonNode.get("link").textValue());
                                post.setParentId(jsonNode.get("parent_id") == null ? null : jsonNode.get("parent_id").textValue());

                                post.setStatus(jsonNode.get("status") == null ? null : jsonNode.get("status").textValue());
                                post.setStatusType(jsonNode.get("status_type") == null ? null : jsonNode.get("status_type").textValue());
                                try {
                                    post.setFromId(jsonNode.get("from") == null ? null : jsonNode.get("from").get("id").textValue());
                                    post.setFromName(jsonNode.get("from") == null ? null : jsonNode.get("from").get("name").textValue());
                                } catch (Exception ex) {

                                }

                                try {
                                    post.setLikeCount(jsonNode.get("likes").get("summary").get("total_count").asInt() + "");
                                } catch (Exception ex) {
                                    post.setLikeCount("0");
                                }
                                try {
                                    post.setCommentCount(jsonNode.get("comments").get("summary").get("total_count").asInt() + "");
                                } catch (Exception ex) {
                                    post.setLikeCount("0");
                                }
                                try {
                                    System.out.println("====> " + jsonNode);
                                    post.setShareCount((jsonNode.get("shares") == null ? "0" : jsonNode.get("shares").get("count") + ""));


                                    String str = post.getStory();
                                    if (str.contains("shared")) {
                                        str = str.substring(0, str.indexOf(" shared"));
                                        System.out.println(str);
                                    }
                                    Pattern pp = Pattern.compile("and -?\\d+ others");
                                    Matcher mm = pp.matcher(str);
                                    int totalShares = 2;
                                    mm.find();
                                    try {
                                        System.out.println("GROUP ==> " + mm.group());
                                        totalShares += Integer.valueOf(mm.group().replaceAll(" others", "").replaceAll("and ", "")) - 1;
                                    } catch (IllegalStateException ex) {
                                        ex.printStackTrace();
                                        ;
                                    }
                                    post.setShareStoryCount(totalShares + "");
                                } catch (NullPointerException ex) {

                                }

                                postRepository.save(post);

                                //TODO: TO SAVE PARENT INFORMATION
                                try {
                                    if (post.getParentId() == null) {
                                        Later<JsonNode> originalPost = batcher.graph(post.getLink());
                                        JsonNode original = originalPost.get();
                                        post = new kh.com.camface.models.Post();
                                        post.setId(original.get("og_object") == null ? null : original.get("og_object").get("id").textValue());
                                        post.setTitle(original.get("og_object") == null ? null : original.get("og_object").get("title").textValue());
                                        post.setDescription(original.get("og_object") == null ? null : original.get("og_object").get("description").textValue());
                                        post.setType(original.get("og_object") == null ? null : original.get("og_object").get("type").textValue());
                                        post.setUpdatedTime(original.get("og_object") == null ? null : original.get("og_object").get("updated_time").textValue());
                                        System.out.println("ORIGINAL POST ==> " + original);
                                        System.out.println("SHARE COUNT ==> " + original.get("share").get("share_count"));
                                        System.out.println("COMMENT COUNT ==> " + original.get("share").get("comment_count"));
                                        post.setShareCount(original.get("share").get("share_count").asInt() + "");
                                        post.setCommentCount(original.get("share").get("comment_count").asInt() + "");
                                    } else {
                                        Later<JsonNode> postByParentId = batcher.graph(post.getParentId() + "?fields=id,application,call_to_action,caption,created_time,description,from,icon,is_hidden,is_published,message,message_tags,name,object_id,parent_id,picture.width(249).height(476),place,privacy,properties,source,status_type,story,story_tags,targeting,to,type,updated_time,with_tags,shares,link,permalink_url,likes.limit(0).summary(true),comments.limit(0).summary(true)&summary(true)");
                                        JsonNode original = postByParentId.get();
                                        post = new kh.com.camface.models.Post();
                                        post.setId(original.get("id") == null ? null : original.get("id").textValue());
                                        post.setName(original.get("name") == null ? null : original.get("name").textValue());
                                        post.setCaption(original.get("caption") == null ? null : original.get("caption").textValue());
                                        post.setDescription(original.get("description") == null ? null : original.get("description").textValue());
                                        post.setCreatedTime(original.get("created_time") == null ? null : original.get("created_time").textValue());
                                        post.setPicture(original.get("picture") == null ? "" : original.get("picture").textValue());
                                        post.setIsPublished(original.get("is_published") == null ? null : original.get("is_published").textValue());
                                        post.setIsHidden(original.get("is_hidden") == null ? null : original.get("is_hidden").textValue());
                                        post.setType(original.get("type") == null ? null : original.get("type").textValue());
                                        try {
                                            post.setMessage(original.get("message") == null ? null : original.get("message").textValue());
                                        } catch (Exception ex) {

                                        }
                                        post.setPermalinkUrl(original.get("permalink_url") == null ? null : original.get("permalink_url").textValue());
                                        post.setLink(original.get("link") == null ? null : original.get("link").textValue());
                                        try {
                                            post.setFromId(original.get("from") == null ? null : original.get("from").get("id").textValue());
                                            post.setFromName(original.get("from") == null ? null : original.get("from").get("name").textValue());
                                        } catch (Exception ex) {

                                        }
                                        System.out.println("PARENT POST ==> " + original);
                                        System.out.println("SHARE COUNT ==> " + original.get("shares").get("count").asInt());
                                        System.out.println("COMMENT COUNT ==> " + original.get("comments").get("summary").get("total_count").asInt());
                                        post.setShareCount(original.get("shares").get("count").asInt() + "");
                                        post.setCommentCount(original.get("comments").get("summary").get("total_count").asInt() + "");
                                        post.setLikeCount(original.get("likes").get("summary").get("total_count").asInt() + "");
                                    }
                                    //TODO: SAVE PARENT INFORMATION INTO DATABASE
                                    System.out.println("SAVE PARENT " + post.getId());
                                    post.setParentId(postId);
                                    postRepository.saveOrignal(post);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                } while ((feeds = feeds.next()) != null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
