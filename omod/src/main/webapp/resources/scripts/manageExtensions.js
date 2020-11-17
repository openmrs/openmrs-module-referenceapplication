var jq = jQuery;

jq(function(){
    jq('.referenceapplication-action').click(function(){
        jq(this).parent().submit();
    });
});

