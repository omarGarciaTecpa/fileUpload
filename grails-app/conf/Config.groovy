// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

//==================================================     EXTERNALIZANDO CONFIGURACION     ================================================================

if(!grails.config.locations || !(grails.config.locations instanceof List)) { grails.config.locations = []  }
grails.config.locations << ["classpath:${appName}-config.properties", "classpath:${appName}-config.groovy"]


def EXTERNAL_CONFIG_FILES = ["${appName}-datasource-config.groovy", "${appName}-config.groovy"]
File externalDirectory
File importFile

String absExternalPath =  "C:/grails-apps/config-files/${appName}"
String userExternalPath = "${userHome}/grails-apps/config-files/${appName}"



println "======================================================================================="
println "LOADING CONFIGURATION"
println "======================================================================================="
println "... SEARCHING IN ABSOLUTE PATH ${absExternalPath}"

externalDirectory = new File(absExternalPath)
if(externalDirectory && externalDirectory.isDirectory()){
    println (".... CONFIGURATION FOLDER FOUND AT ${absExternalPath}")
}else{
    println(".... SEARCHING IN USER FOLDER ${userExternalPath}")
    externalDirectory = new File(userExternalPath)
    if(externalDirectory && externalDirectory.isDirectory()){
        println ("..... CONFIGURATION FOLDER FOUND AT ${userExternalPath}")
    }else{
        println ".... NO EXTERNAL CONFIGURATION FILE FOUND. USING IN CODE DEFAULTS"
        externalDirectory = null
    }
}


if(externalDirectory){
    println("\n\n------- IMPORTING FILES FROM CONFIGURATION FOLDER   ----------")
    for(String configFileName in EXTERNAL_CONFIG_FILES){
        println("*TRYING TO IMPORT CONFIGURATION FILE: ${configFileName}")
        importFile =  new File("${externalDirectory.path}\\${configFileName}")
        if(importFile.exists()){
            grails.config.locations << "file:${importFile.path}"
            println("***Importing [${importFile.path}] ")
        }else{
            println ("COULD NOT FIND ${configFileName} IN THE CONFIGURATION FOLDER: ${externalDirectory}")
        }
    }
}


grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [
    all:           '*/*',
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
        // TODO: grails.serverURL = "http://www.changeme.com"
    }
}

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails.web.servlet',        // controllers
           'org.codehaus.groovy.grails.web.pages',          // GSP
           'org.codehaus.groovy.grails.web.sitemesh',       // layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping',        // URL mapping
           'org.codehaus.groovy.grails.commons',            // core / classloading
           'org.codehaus.groovy.grails.plugins',            // plugins
           'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
}
