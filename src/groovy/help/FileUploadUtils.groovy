package help

import java.math.RoundingMode

class FileUploadUtils {
    static transactional = false

    static final int KB = 1024
    static final int MB =  1024*KB
    static final int GB = 1024*MB

    /**Transforma el tamaño(en bytes) en un string*/
    public static String stringifyFileSize(long size){
        BigDecimal result = 0
        String appendScale = 'bytes'

        if(size < KB){
            result = size
            appendScale = 'bytes'
        }
        else if(size/KB < KB){
            result = size/KB
            appendScale = 'Kb'
        }
        else if(size/MB < MB){
            result = size/MB
            appendScale = 'Mb'
        }else{
            result = size/GB
            appendScale = 'Bb'
        }

        return "${result.setScale(2,RoundingMode.HALF_UP)} $appendScale"
    }
}
