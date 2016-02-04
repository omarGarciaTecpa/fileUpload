class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		//"/"(view:"/index")
        "/"(controller: 'picture', action: 'fileBrowser')
        "/status"(view: '/appStatus')
		"500"(view:'/error')
	}
}
