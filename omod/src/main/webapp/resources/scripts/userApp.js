var jq = jQuery;
var timeout;
var action;

jq(function(){
    jq('.required').on("keyup",function() {
        if(timeout){
            clearTimeout(timeout);
        }
        timeout = setTimeout(function(){
            validate();
        }, 300);
    });

    validate();
});

jq(document).ajaxError(function() {
    jq('#server-error-msg').show();
});

function setAction(a){
    action = a;
}

function requireValues(){
    var isAppIdValid = action == 'edit' || jq.trim(jq('#appId-field').val()) != '';
    if(isAppIdValid && jq.trim(jq('#json-field').val()) != ''){
        return true;
    }
    return false;
}

function toggleFields(isJsonValid){
    if(isJsonValid && requireValues()){
        jq('#save-button').removeAttr('disabled');
        jq('#errorMsg').hide();
    }else if(isJsonValid) {
        jq('#save-button').attr('disabled','disabled');
        jq('#errorMsg').hide();
    }else if(!isJsonValid) {
        jq('#save-button').attr('disabled','disabled');
        jq('#errorMsg').show();
    }
}

function validate(){
    var json = jq('#json-field').val();
    if(jq.trim(json) == ''){
        toggleFields(true);
        return;
    }

    jq.post("verifyJson.htm",
        {"json": json},
        function(data){
            jq('#server-error-msg').hide();
            toggleFields(data.isValid);
        },
        "json"
    );
}