/**
 * Created by PENHCHET on 4/10/2017.
 */
$(function(){
    var posts = {};
    posts.findAllPopularPosts = function(){
        $.ajax({
            url: "/v1/api/popular-posts",
            type: 'POST',
            dataType: 'JSON',
            success: function (response) {
                if (response.CODE == "0000") {
                    $("#POSTS").html("");
                    $.each(response.DATA, function (key, value) {
                        //moment($("#selectDate").val(), 'DD-MM-YYYY').format('YYYYMMDD')
                        response.DATA[key]["month"] = moment(response.DATA[key]["createdDate"]).format("MMMM");
                        response.DATA[key]["day"] = moment(response.DATA[key]["createdDate"]).format("DD");
                    });
                    if (response.DATA.length > 0) {
                        $("#POSTS_TEMPLATE").tmpl(response.DATA).appendTo("#POSTS");
                    } else {
                        $("#POSTS").html("គ្មានទិន្នន័យទេ");
                    }
                }
            },
            error: function (data, status, err) {
                console.log("error: " + data + " status: " + status + " err:" + err);
            }
        }).complete(function () {

        });
    };

    posts.findAllPopularPosts();

    $(document).on("click", ".post", function(){
       if($(this).data("link")!="") {
           window.open($(this).data("link"), "_blank");
       }
    });




});