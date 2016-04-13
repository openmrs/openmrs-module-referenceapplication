var jq = jQuery;

jq(function(){
    jq('.referenceapplication-action').click(function(){
        jq(this).parent().submit();
    });
});

function showDeleteUserAppDialog(appId){
    var deleteUserAppDialog = emr.setupConfirmationDialog({
        selector: "#referenceapplication-delete-userApp-dialog-" + appId.replace(/\./g, '\\.'),
        actions: {
            cancel: function() {
                deleteUserAppDialog.close();
            }
        }
    });

    deleteUserAppDialog.show();

}
