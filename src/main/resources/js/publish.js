AJS.toInit(function(){

    var popup;

    function createDialog() {
        var contextPath = AJS.Confluence.getContextPath();
        var pageId = AJS.params.pageId;

        popup = new AJS.Dialog(480, 330);

        popup.addPage("form-page");
        popup.addPage("creating-page");
        popup.addPage("loading-page");

        popup.gotoPage(1);
        popup.addPanel(AJS.I18n.getText("publish.dialog.asblog.title"));
        popup.addHeader(AJS.I18n.getText("publish.dialog.asblog.header"));
        popup.getCurrentPanel().html(Confluence.Templates.Publish.publishAsBlog());
        popup.addCancel(AJS.I18n.getText("cancel.name"), function() {
            popup.hide();
        });
        popup.addSubmit(AJS.I18n.getText("publish.dialog.asblog.button"), function() {
            popup.gotoPage(2);
            AJS.$.ajax({
                type:"PUT",
                url: contextPath + "/rest/publish/1/publish/"+pageId,
                success: function(data, status, jqXHR) {
                    popup.gotoPage(3);
                    setTimeout(function(){
                        window.location = jqXHR.getResponseHeader("location");
                        popup.hide();
                    }, 1000);
                }
            })
        });

        popup.gotoPage(2);
        popup.addPanel("Creating","#creating");
        popup.addHeader(AJS.I18n.getText("publish.dialog.creating"));
        popup.getCurrentPanel().html(Confluence.Templates.Publish.creating());

        popup.gotoPage(3);
        popup.addPanel("Loading","#loading");
        popup.addHeader(AJS.I18n.getText("publish.dialog.loading"));
        popup.getCurrentPanel().html(Confluence.Templates.Publish.loading());

        popup.gotoPage(1);
    }

    AJS.$("#publish_asblog").click(function(e) {
        if(typeof popup == "undefined") {
            createDialog()
        }
        popup.show();
    });

});

