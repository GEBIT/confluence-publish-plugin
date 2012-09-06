AJS.toInit(function(){

    var popup;

    function createPopUp() {
        var popup = new AJS.Dialog(400, 400, "publish-asblog-dialog");
        var $dialog = popup.popup.element;
        var template = "<h1>Publish</h1>";// $(Confluence.Templates.UserStatus.dialogContent({maxChars: maxChars}));
        popup.addHeader(AJS.I18n.getText("publish.asblog.heading"));
        popup.addPanel(AJS.I18n.getText("publish.asblog.dialog.panel.title"), template);
        popup.addButton(AJS.I18n.getText("publish.asblog.dialog.button.publish"), publishAsBlog, "publish-asblog-button");
        popup.addCancel(AJS.I18n.getText("cancel.name"), function (dialog) {dialog.hide(); return false;});
        popup.setError = function(html) {
            $(".error-message", $dialog).html(html)
        };

        return popup;
    }

    function validate(text) {
//        var error;
//
//        if (!text) {
//            error = AJS.I18n.getText("status.message.error.blank");
//        } else if (!$.trim(text)) {
//            // The message was just whitespace
//            error = AJS.I18n.getText("status.message.error.onlywhitespace");
//        } else if (text.length > maxChars) {
//            error = AJS.I18n.getText("status.message.error.too.long", maxChars);
//        }
//
//        if (error) {
//            popup.setError(error);
//        }
//
//        return !error;
        return false;
    }

    var publishAsBlog = function() {
//        var $dialog = popup.popup.element,
//            $input = $("#status-text", $dialog),
//            $updateButton = $(".status-update-button", $dialog),
//            text = $input.val(),
//            reEnableForm,
//            updateTask;
//
//        function disableForm() {
//            $input.blur();
//            $input.prop("disabled", true).prop("readonly", true);
//            $updateButton.prop("disabled", true);
//
//            return function() {
//                $input.focus();
//                $input.prop("disabled", false).prop("readonly", false);
//                $updateButton.prop("disabled", false);
//            }
//        }
//
//        reEnableForm = disableForm();
//
//        if (!validate(text)) {
//            reEnableForm();
//            return false;
//        }
//
//        updateTask = AJS.safe.ajax({
//            url: Confluence.getContextPath() + "/status/update.action",
//            type: "POST",
//            dataType: "json",
//            data: {
//                "text": text
//            }
//        });
//
//        // Always re-enable the form.
//        updateTask.done(reEnableForm).fail(reEnableForm);
//
//        updateTask.done(function(data) {
//            if (data.errorMessage) {
//                popup.setError(data.errorMessage);
//            }
//            else {
//                setCurrentStatus(data);
//                $input.val("");
//                $dialog.fadeOut(200, function() {
//                    popup.hide();
//                });
//            }
//        });
//
//        updateTask.fail(function(xhr, text, error) {
//            AJS.log("Error updating status: " + text);
//            AJS.log(error);
//            popup.setError("There was an error - " + error);
//        });
//
//        return updateTask.promise();
    };

    alert($("#publish_asblog").length);

    $("#publish_asblog").click(function(e) {

        var dropDown = $(this).parents(".ajs-drop-down")[0];
        dropDown && dropDown.hide();

        if (typeof popup == "undefined") {
            popup = createPopUp();
        }
        popup.setError("");
        popup.show();
        return AJS.stopEvent(e);
    });

});

