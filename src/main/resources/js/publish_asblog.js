AJS.toInit(function(){

    var dialog = new AJS.Dialog(860, 530);
    dialog.addHeader("Publish To Blog", "");

    dialog.addPanel("Publish To Blog", "", "", 1);
    dialog.getCurrentPanel().html('<p>INSERT CONTENT HERE</p>');

    dialog.addCancel("Cancel", function() {
        dialog.hide();
    });

    dialog.addSubmit("Publish", function() {

    });

    AJS.$("#publish_asblog").click(function(e) {
        dialog.show();
    });

});

