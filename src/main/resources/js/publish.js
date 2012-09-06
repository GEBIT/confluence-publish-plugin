AJS.toInit(function(){

    var dialog;

    function createDialog() {
        dialog = new AJS.Dialog(860, 530);
        dialog.addHeader("Publish To Blog", "");
        dialog.addPanel("Publish To Blog", "", "", 1);
        var template = Confluence.Templates.Publish.publishAsBlog();
        dialog.getCurrentPanel().html(template);
        dialog.addCancel("Cancel", function() {
            dialog.hide();
        });
        dialog.addSubmit("Publish", function() {
        });
    }

    AJS.$("#publish_asblog").click(function(e) {
        if(typeof dialog == "undefined") {
            createDialog()
        }
        dialog.show();
    });

});

