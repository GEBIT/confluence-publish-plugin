AJS.toInit(function(){

    var dialog;

    function createDialog() {
        var contextPath = AJS.Confluence.getContextPath();
        var pageId = AJS.params.pageId;

        dialog = new AJS.Dialog(480, 330);
        dialog.addHeader(AJS.I18n.getText("publish.dialog.asblog.header"), "");
        dialog.addPanel(AJS.I18n.getText("publish.dialog.asblog.title"), "", "", 1);
        var template = Confluence.Templates.Publish.publishAsBlog();
        dialog.getCurrentPanel().html(template);
        dialog.addCancel(AJS.I18n.getText("cancel.name"), function() {
            dialog.hide();
        });
        dialog.addSubmit(AJS.I18n.getText("publish.dialog.asblog.button"), function() {
            AJS.$.ajax({
                type:"PUT",
                url: contextPath + "/rest/publish/1/publish/"+pageId,
                success: function() {
                    // TODO: redirect to blog
                    dialog.hide();
                }
            })
        });
    }

    AJS.$("#publish_asblog").click(function(e) {
        if(typeof dialog == "undefined") {
            createDialog()
        }
        dialog.show();
    });

});

