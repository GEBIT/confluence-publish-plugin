AJS.toInit(function(){

    var dialog;

    function createDialog() {
        dialog = new AJS.Dialog(860, 530);
        dialog.addHeader(AJS.I18n.getText("publish.dialog.asblog.header"), "");
        dialog.addPanel(AJS.I18n.getText("publish.dialog.asblog.title"), "", "", 1);
        var template = Confluence.Templates.Publish.publishAsBlog();
        dialog.getCurrentPanel().html(template);
        dialog.addCancel(AJS.I18n.getText("cancel.name"), function() {
            dialog.hide();
        });
        dialog.addSubmit(AJS.I18n.getText("publish.dialog.asblog.button"), function() {
        });
    }

    AJS.$("#publish_asblog").click(function(e) {
        if(typeof dialog == "undefined") {
            createDialog()
        }
        dialog.show();
    });

});

