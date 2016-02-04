<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <g:set var="pageTitle" value="${message(code: 'default.upload.label', args: ['Imagenes'], default: 'Subir imagenes')}" />
    <title>${pageTitle}</title>
    <meta name="layout" content="main" >
</head>
<body>
    <h1>${pageTitle}</h1>
    <section class="panel panel-default" id="file-form">
        <article class="panel-body">
            <g:uploadForm action="upload" name="uploadForm">
                <fieldset>
                    <article class="form-group">
                        <label>Imagen</label>
                        <g:field type="file" name="image" class="form-control" required=""/>
                    </article>
                    <article class="form-group">
                        <label>Descripción</label>
                        <g:textArea name="description" class="form-control" />
                    </article>
                </fieldset>
                <fieldset>
                    <div class="progress" id="upload-progress" style="display: none">
                        <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 60%;">
                            <span class="sr-only">60% Complete</span>
                        </div>
                    </div>
                </fieldset>
                <fieldset>
                    <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-upload"></span> Subir</button>
                </fieldset>
                <br>
                <fieldset id="errors"></fieldset>

            </g:uploadForm>
        </article>

    </section>
    <section class="panel panel-default" >
        <article class="panel-body" id="file-list"></article>
    </section>

    <div id="img-preview" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog  modal-lg ">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                </div>
                <div class="modal-content">
                    <img id="loaded-image" src="" alt="IMAGE" width="100%" >
                    <p class="img-description"></p>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

    <content tag="javascript">
        <script type="text/javascript">
            var uploadUrl = "${createLink(action: 'upload')}";
        </script>
        <asset:javascript src="views/picture.js" />
    </content>
</body>
</html>