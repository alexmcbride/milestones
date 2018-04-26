function message(msg, success) {
    var successClass = success ? 'success' : 'warning';
    $('#shareProjectMessage').html('<br><div class="alert alert-'+successClass+'" role="alert">' +
        '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + msg + '</div>');
}

function unshare(id, projectId, csrf) {
    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: urls.unshare,
        data: { userId: id, projectId: projectId, antiForgeryToken: csrf },
        success: function(data, status, xhr) {
            if (data.success) {
                $('#sharedProjectItem-' + data.id).remove();
            }
            message(data.message, data.success);
        }
    });
}

function initProjectSharing(projectId, csrf) {
    $('#shareProjectSearch').autocomplete({
        source: urls.autocomplete,
        minLength: 2,
        select: function (evt, ui) {
            $('#userId').val(ui.item.id);
            $('#shareButton').removeAttr('disabled');
        }
    });

    $('#shareProjectForm').on('submit', function (evt) {
        evt.preventDefault();

        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: urls.share,
            data: $(this).serialize(),
            success: function (data, status, xhr) {
                if (data.success) {
                    $('#shareProjectSearch').val('');
                    $('#shareButton').attr('disabled', 'disabled');
                    $('#notSharedMessage').css({display: 'none'});
                    $('#sharedProjectsList').append('<div class="row shared-row" id="sharedProjectItem-' + data.id + '">' +
                        '<div class="col-sm-7"><strong>' + data.username + '</strong></div>' +
                        '<div class="col-sm-5"><a href="#" onclick="unshare(' + data.id + ', '+projectId+', \'' + csrf + '\')">' +
                        '<i class="fas fa-eye-slash"></i> ' +
                        'Unshare</a></div>' +
                        '</div>');
                }
                message(data.message, data.success);
            }
        });
    });

    $('#copy').click(function(evt){
        var sharedUrl = $('#sharedUrl');
        sharedUrl.select();
        document.execCommand('copy');
        sharedUrl.blur();
    });

    $('#publicCheckBox').change(function(){
        $.ajax({
            url: '/api/make-public',
            method: 'POST',
            data: $('#isPublicForm').serialize(),
            dataType: 'json',
            success: function(data, status, xhr) {
                message(data.message, data.success);

                if (data.open) {
                    $('#copySharedUrl').css({display: 'block'});
                }
                else {
                    $('#copySharedUrl').css({display: 'none'});
                }
            }
        });
    });

    $('#copyUrl').click(function() {
        var copyText = document.getElementById("sharedUrl");

        /* Select the text field */
        copyText.select();

        /* Copy the text inside the text field */
        document.execCommand("Copy");
    });
};
