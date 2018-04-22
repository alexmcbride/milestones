function message(msg, success) {
    var successClass = success ? 'success' : 'warning';
    $('#shareProjectMessage').html('<br><div class="alert alert-'+successClass+'" role="alert">' +
        '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + msg + '</div>');
}

function unshare(id, projectId, csrf) {
    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: '/api/unshare-project',
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
        source: '/api/autocomplete',
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
            url: '/api/share-project',
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
};
