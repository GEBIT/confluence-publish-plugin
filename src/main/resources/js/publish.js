AJS.toInit(function(){

    var popup;

    function createDialog() {
        var contextPath = AJS.Confluence.getContextPath();
        var pageId = AJS.params.pageId;
        var newsUrl;

        popup = new AJS.Dialog({width: 480, height: 330, id: "publish-asblog-dialog"});

        popup.addPage("form-page");
        popup.addPage("creating-page");
        popup.addPage("loading-page");
        popup.addPage("error-page");
        popup.addPage("delete-page");

        popup.gotoPage(1);
        popup.addPanel("Publish as Blog");
        popup.addHeader("Publish");
        popup.getCurrentPanel().html("<div><p>You are going to publish this page as a blog post.  This will create a blog post in the same space with the same title as your original page.</p><p>All content and attachments will be copied and your original page will be left entirely alone.</p></div>");
        popup.addCancel("Cancel", function() {
            popup.hide();
        });
        popup.addSubmit("Publish", function() {
            popup.gotoPage(2);
            AJS.$.ajax({
                type:"PUT",
                url: contextPath + "/rest/publish/1/publish/"+pageId,
                success: function(data, status, jqXHR) {
                	newsUrl = jqXHR.getResponseHeader("location");
                    popup.gotoPage(6);
                },
                error: function(jqXHR, exception) {
                    if (jqXHR.status == 401) {
                        popup.gotoPage(4);
                    } else if (jqXHR.status == 404) {
                        popup.gotoPage(5);
                    }
                    setTimeout(function(){
                        popup.hide();
                    }, 2000);
                }
            })
        });

        popup.gotoPage(2);
        popup.addPanel("Creating","#creating");
        popup.addHeader("Creating");
        popup.getCurrentPanel().html("<div id='creating'>Creating blog...</div>");

        popup.gotoPage(3);
        popup.addPanel("Loading","#loading");
        popup.addHeader("Loading");
        popup.getCurrentPanel().html("<div id='loading'>Loading blog...</div>");

        popup.gotoPage(4);
        popup.addPanel("Error 401","#error401");
        popup.addHeader("Error");
        popup.getCurrentPanel().html("<div id='error401'>Unauthorized</div>");

        popup.gotoPage(5);
        popup.addPanel("Error 404","#error404");
        popup.addHeader("Error");
        popup.getCurrentPanel().html("<div id='error404'>Requested page not found</div>");

        popup.gotoPage(6);
        popup.addPanel("Delete Page?","#deletePage");
        popup.addHeader("Delete");
        popup.getCurrentPanel().html("<div id='deletePage'>Delete the page that is now published as news?</div>");
        popup.addCancel("Cancel", function() {
            window.location = newsUrl;
            popup.hide();
        });
        popup.addSubmit("Delete Page", function() {
            window.location = contextPath + '/pages/removepage.action?pageId=' + pageId;
        });


        popup.gotoPage(1);
    }

    AJS.$("#publish-asblog").click(function(e) {
        if(typeof popup == "undefined") {
            createDialog()
        }
        popup.show();
    });

});

