(function($){
    $(document).ready(function(){
        loadPreview('#img-preview', "#loaded-image", "a.linkPreview", "#file-list");
         uploadFileForm('form[name="uploadForm"]', $("#upload-progress"));
         ajaxLoadFileList();

    });

    function ajaxLoadFileList(){
        $.get(uploadUrl, function(data){loadFileList(data)});
    }

    function loadFileList(data){
        var targetDiv = $("#file-list");
        targetDiv.empty();
        for(var i = 0; i< data.length ; i++){
            targetDiv.append($('<article class="img-thumbnail"><a href="'+data[i].pictureUlr+'" class="linkPreview"><img src="'+data[i].thumbUrl+'" height="100"></a><article class="description hide">'+data[i].description+'</article></article>'));
        }
    }

    function loadPreview(modalElement,targetImgElement, linkElement, anchorElement){
        var anchor = $(anchorElement);
        var modal = $(modalElement);
        var targetImg = $(targetImgElement);

        if(anchor.length > 0){
            anchor.on('click', linkElement , function(event){
                event.preventDefault();
                var thisLink = $(this);
                targetImg.prop("src", thisLink.prop('href'));
                targetImg.siblings('p').empty();
                targetImg.siblings('p').html(thisLink.siblings('.description').text());
                modal.modal('show');
            });
        }else{console.error("anchor element does not exist");}
    }

    function uploadFileForm(form, progressbar){
        if($(form).length > 0){
            $(form).submit(function(event){
                var thisForm = $(this);
                var uri = thisForm.prop("action");
                var formData = new FormData(thisForm[0]);
                var submitButton = thisForm.find(":submit");
                var progressbarInner = progressbar.find('div.progress-bar');

                $.ajax({
                    url: uri,
                    xhr: function (){
                        // Custom XMLHttpRequest
                        var myXhr = $.ajaxSettings.xhr();
                        if(myXhr.upload){ // Check if upload property exists
                            myXhr.upload.addEventListener('progress',function(progress){progressHandler(progress, progressbar, progressbarInner);}, false);
                            myXhr.upload.addEventListener('load',function(progress){progressHandler(progress, progressbar, progressbarInner);}, false);
                        }
                        return myXhr;
                    },
                    beforeSend: function(){beforeSend(submitButton, progressbar, progressbarInner);},
                    success: function(data){successHandler(data,submitButton, progressbar, progressbarInner, thisForm);},
                    method: "POST",
                    error: errorHandler,
                    // Form data
                    data: formData,
                    //Options to tell jQuery not to process data or worry about content-type.
                    cache: false,
                    contentType: false,
                    processData: false

                });


                event.preventDefault();
            });
        }else{
            console.error("The form doesn´t exist");
        }
    }

    function beforeSend(submitBtn, progressbar, progressbarInner){
        submitBtn.prop('disabled', true);
        progressbarInner.width('1%');
        progressbarInner.prop('aria-valuenow', 1);
        progressbar.find('span').html('0%');
        progressbar.show();
    }

    function successHandler(data,submitBtn, progressbar, progressbarInner, form){
        progressbar.hide(1000);
        $(form).trigger("reset");
        errorReporter(data);
        ajaxLoadFileList();
        submitBtn.prop('disabled', false);
    }

    function errorReporter(data){
        if(data.errormsg){
        $("#errors").append($('<div class="alert alert-info alert-dismissible fade in" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>'+data.errormsg+'</div>'));
        }
    }

    function progressHandler(progress, progressbar, progressbarInner){
        if(progress.lengthComputable){
            if(progress.loaded === progress.total){
                progressbarInner.addClass('progress-bar-success');
            }
            var percentComplete = Math.round((progress.loaded / progress.total) * 100).toFixed(2);
            progressbarInner.width(percentComplete+'%');
            progressbarInner.prop('aria-valuenow', percentComplete);
            progressbar.find('span').html(percentComplete+'%');
        }
    }

    function errorHandler(){
        console.error("Hubo un error al subir el archivo");
    }

})(jQuery);