package com.gfileupload

import grails.converters.JSON
import org.springframework.http.HttpStatus
import org.springframework.web.multipart.MultipartHttpServletRequest

class PictureController {
    def imageUploadService


    def upload(){
        switch (request.method){
            case 'GET':
                println("Getting files from GET")
                render imageUploadService.images as JSON
            break;
            case 'POST':
                Map uploadResults = [:]
                if(request instanceof  MultipartHttpServletRequest){
                    println("uploading file")
                    uploadResults = imageUploadService.upload(request.getFile('image'), (String)params.get('description'))
                }
                render uploadResults as JSON
            break;
            default:
                println("Method not Allowed")
                render status: HttpStatus.METHOD_NOT_ALLOWED.value()
            break;
        }

    }

    def fileBrowser(){}

    def image(long id){
        def pictureInstance = Picture.read(id)

        if(pictureInstance){
            File picture = imageUploadService.retrievePicture(pictureInstance)
            try{
                if(picture){
                    response.setHeader("Content-disposition","inline;filename=${pictureInstance.bpOriginalFilename}")
                    render file: picture.newInputStream(), contentType: "image/${pictureInstance.bpExtension}".toString()
                }else{
                    render status: HttpStatus.NOT_FOUND.value()
                }
            }catch (Exception ex){}
        }else{
            render status: HttpStatus.NOT_FOUND.value()
        }
    }

    def thumbnail(long id){
        def pictureInstance = Picture.read(id)
        log.info("serving thumbs")

        if(pictureInstance){
            File thumb = imageUploadService.retrieveThumb(pictureInstance);
            try{
                if(thumb){
                    log.info("Sending back thumb")
                    response.setHeader("Content-disposition","inline;filename=${pictureInstance.tbUUID}")
                    render file: thumb.newInputStream() , contentType: 'image/png'
                }else{
                    log.error("No se encontro la imagen")
                    render status: HttpStatus.NOT_FOUND.value()
                }
            }catch (Exception ex){

            }
        }else{
            log.error("No se encontro la instancia de picture")
            render status: HttpStatus.NOT_FOUND.value()
        }

    }




}
