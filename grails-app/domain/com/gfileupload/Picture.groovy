package com.gfileupload

class Picture {
    //Big picture
    String bpOriginalFilename
    String bpExtension
    String bpStringifiedSize
    String bpUUID
    long   bpSize

    //Thumb
    String tbUUID
    String description = " "


    static constraints = {
        bpOriginalFilename blank: false, nullable: false
        bpExtension        blank: false, nullable: false, maxSize: 20
        bpStringifiedSize  blank: false, nullable: false
        bpUUID             blank: false, nullable: false
        bpSize             blank: false, nullable: false
        tbUUID             blank: false, nullable: false
        description        blank: true,  nullable: false, maxSize: 500
    }

    def beforeValidate(){
        description = description ?: " "
    }


    String toString(){
        bpOriginalFilename
    }
}
