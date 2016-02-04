package gfileupload

import com.gfileupload.Picture
import help.FileUploadUtils
import org.imgscalr.Scalr
import org.springframework.web.multipart.MultipartFile

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

import org.springframework.context.i18n.LocaleContextHolder

public class ImageUploadService {
    static transactional =  false

    def grailsApplication
    def grailsLinkGenerator
    def messageSource


    public def getImages() {
        def results = []
        Picture.list().each {Picture pictureInstance ->
            results << [
                    pictureFileName : pictureInstance.bpOriginalFilename,
                    pictureSize: pictureInstance.bpStringifiedSize,
                    pictureUlr: grailsLinkGenerator.link(controller: 'picture', action: 'image', id: pictureInstance.id),
                    thumbUrl: grailsLinkGenerator.link(controller: 'picture', action: 'thumbnail', id: pictureInstance.id),
                    description: pictureInstance.description
            ]
        }

        return results
    }


    public def upload(MultipartFile uploadFile, String description){
        String baseUUID = UUID.randomUUID().toString()
        String extension = uploadFile.originalFilename.substring(uploadFile.originalFilename.lastIndexOf("."))
        String fileUUID = baseUUID + extension
        String fileUploadDir = grailsApplication.config.file.upload.image.directory?:'C:/temp'
        BufferedImage thumbnail;
        String thumbnailFilename = baseUUID + '-thumb.png'
        Map results = [:]

        File transferFile = new File("${fileUploadDir}/${fileUUID}")
        File thumbnailFile;

        try{
            uploadFile.transferTo(transferFile)
            thumbnail = Scalr.resize(ImageIO.read(transferFile), grailsApplication.config.file.upload.thumb.maxwidth ?: 290);
            thumbnailFile = new File("$fileUploadDir/$thumbnailFilename")
            ImageIO.write(thumbnail, 'png', thumbnailFile)
        }catch (Exception ex){
            log.error("No se pudo crear el thumb");
            results << [error: "No se pudieron subir el archivo."]
            results << [error: "No se pudo crear el thumb"]
            return results
        }

        Picture picture = new Picture(
                bpOriginalFilename: uploadFile.originalFilename,
                bpExtension: extension,
                bpStringifiedSize: FileUploadUtils.stringifyFileSize(uploadFile.size),
                bpUUID: fileUUID,
                bpSize: uploadFile.size,
                tbUUID: thumbnailFilename,
                description:description
        )

        if(!picture.save(flush: true)){
            try{
                transferFile.delete()
                thumbnailFile.delete()
            }catch (Exception ex){
                log.error("Could not delete files after upload Failed")
            }
            picture.errors.allErrors.each {
                results << ["errormsg":messageSource.getMessage(it,LocaleContextHolder.locale)]
            }
        }

        return results
    }

    public File retrieveThumb(Picture pictureInstance ){
        File file = null;
        String fileUploadDir =  grailsApplication.config.file.upload.image.directory?:'C:/temp'
        log.info("Trying to load thumb for ${pictureInstance}")
        if(pictureInstance){
            log.info("Trying to load thumb")
            File thumb = new File("${fileUploadDir}/${pictureInstance.tbUUID}")
            file = thumb ?: null
        }

        return file
    }

    public File retrievePicture(Picture pictureInstance){
        File file = null;
        String fileUploadDir =  grailsApplication.config.file.upload.image.directory?:'C:/temp'
        if(pictureInstance){
            File picture = new File("${fileUploadDir}/${pictureInstance.bpUUID}")
            file = picture?: null
        }
        return file;

    }
}
