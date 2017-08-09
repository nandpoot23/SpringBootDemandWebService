/**
 * Created by shekhargulati on 10/06/14.
 */

var app = angular.module('todoapp', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute'
]);


/*
app.run(function($rootScope) {

	  $rootScope.buildUrl(url, params) {
		    var serializedParams = $httpParamSerializer(params);

	        if (serializedParams.length > 0) {
	            url += ((url.indexOf('?') === -1) ? '?' : '&') + serializedParams;
	        }

	        return url;
	  }

});
*/

app.config(function ($routeProvider) {
    $routeProvider.when('/reports/metrics/', {
        templateUrl: 'views/metrics-view.html',
        controller: 'MetricsViewCtrl'
    }).when('/reports/caches/', {
        templateUrl: 'views/caches-view.html',
        controller: 'CachesViewCtrl'
    }).when('/reports/health/', {
        templateUrl: 'views/health-view.html',
        controller: 'HealthViewCtrl'
    }).when('/', {
        templateUrl: 'views/main-view.html',
        controller: 'MainViewCtrl'
    /*})
    /*})
    .when('/jmxsearch', {
        templateUrl: 'views/jmxsearch.html',
        controller: 'JmxSearchCtrl'
    })
    .when('/weblogic-consoles/:id', {
        templateUrl: 'views/weblogic-console-detail.html',
        controller: 'JmxWlConsoleCtrl'
    })
    .when('/numeritrack/block-states', {
        templateUrl: 'views/numeritrack-block-states.html',
        controller: 'NumeritrackBlockStatesCtrl'
    })
    .when('/numeritrack/rc-lookup', {
        templateUrl: 'views/numeritrack-rc-lookup.html',
        controller: 'NumeritrackRcLookupCtrl'*/
    })
    .otherwise({
        redirectTo: '/'
    })
});




app.controller('CachesViewCtrl', function ($scope, $http, $location, $routeParams) {
	$scope.loading = true;
	
	var id = $routeParams.id;
	
    $http.get('/maint/caches/metrics/').success(function (json) {
        console.log('Success' + json)
    	$scope.loading = false;
	    $scope.responseList = json;
	    //$scope.details = details
	}).error(function (json, status) {
	    console.log('Error ' + json)
    	$scope.loading = false;
    	$scope.responseList = json;
	})
});



app.controller('MainViewCtrl', function ($scope, $http, $location, $routeParams) {
	$scope.loading = true;

	$http.get('/env/').success(function (json) {
        console.log('Success' + json)
        
        var response = {}
        if(json) {
        	var defaultProps = json["defaultProperties"]
        	if(defaultProps) {
        		response['springappname'] = defaultProps['spring.application.name']
        	}
        	
        	var cloudVars = json["cloud"]
        	if(cloudVars) {
	        	response['cfappname'] =   cloudVars['cloud.application.name']
	        	response['cfspacename'] = cloudVars['cloud.application.space_name']
	        	response['cfappindex'] =  cloudVars['cloud.application.instance_index']
	        	response['cfappuris'] =   cloudVars['cloud.application.application_uris']
        	}
        }
        
    	$scope.loading = false;
	    $scope.response = response;
	    //$scope.details = details
	}).error(function (json, status) {
	    console.log('Error ' + json)
    	$scope.loading = false;
    	$scope.response = json;
	})
});





app.controller('HealthViewCtrl', function ($scope, $http, $location, $routeParams, HealthService) {
	$scope.loading = true;
    $http.get('/health/').success(function (json) {
        console.log('Success' + json)
	    var health = {}
	    if(json) {
		    var services = []
		    for (key in json) {
		    	var service = {}
		    	if(key === 'status') {
		    		health['status'] = json[key]
		    		health['overallstatus'] = json[key]
		    		health[key] = json[key]
		    	} else {
			    	services.push(HealthService.findService(key, json[key]));
		    	}

		    }
	    	health['services'] = services
	    }
	    
        $scope.loading = false;
    	$scope.response = health;
	}).error(function (json, status) {
	    console.log('status ' + status)
	    
	    var health = {}
	    if(json) {
		    var services = []
		    for (key in json) {
		    	var service = {}
		    	if(key === 'status') {
		    		//console.log('key ' + key)
		    		//console.log('json[key] ' + json[key])

		    		health['status'] = json[key]
		    		health['overallstatus'] = json[key]
		    		health[key] = json[key]
		    	} else {
			    	services.push(HealthService.findService(key, json[key]));
		    	}

		    }
	    	health['services'] = services
	    }
	    
    	console.log('Error ' + json)
    	$scope.loading = false;
    	$scope.response = health;
	})
});




app.controller('MetricsViewCtrl', function ($scope, $http, $location, $routeParams) {
	$scope.loading = true;
    $http.get('/metrics/').success(function (json) {
        console.log('Success' + json)
    	$scope.loading = false;
	    $scope.responseList = json;
	    

	    //console.log('Success' + json['mem'])
	    //console.log('Success' + json['mem.free'])
	    var details = {};
	    
	    
	    details.mem = json['mem']
	    details.memfree = json['mem.free']
	    details.uptime = json['uptime']
	    details.instanceuptime = json['instance.uptime']
	    
	    
	    details.heap = json['heap']
	    details.heapcommitted = json['heap.committed']
	    details.heapinit = json['heap.init']
	    details.heapused = json['heap.used']
	    
	    details.threadspeak = json['threads.peak']
	    details.threadsdaemon = json['threads.daemon']
	    details.threads = json['threads']
	    
	    details.classes = json['classes']
	    details.classesloaded = json['classes.loaded']
	    details.classesunloaded = json['classes.unloaded']
	    
	    
	    // timer
	    var timers = []
	    for (key in json) {
	    	
	    	//console.log('Key: ' + key)
	    	index = key.indexOf('snapshot.75thPercentile')
	        if (index > 0) { 
	        	//console.log('KeyMatch: ' + key)
	        	prefix = key.substring(0, index)
	        	var timer = {}
	        	
	        	var postFixes = ["count","oneMinuteRate", "fiveMinuteRate", "fifteenMinuteRate", "meanRate",
	        	                 "snapshot.75thPercentile", "snapshot.95thPercentile", 
	        	                 "snapshot.98thPercentile", "snapshot.99thPercentile",
	        	                 "snapshot.999thPercentile",
	        	                 "snapshot.max", "snapshot.mean", "snapshot.median", 
	        	                 "snapshot.min", "snapshot.stdDev"]
	        	
	        	timer['prefix'] = prefix
	        	 
	        	var packageParts = prefix.split(".");
	        	
	        	timer['method'] =  packageParts[packageParts.length-1]
	        	timer['method1'] =  packageParts[packageParts.length-2]
	        	timer['method2'] =  packageParts[packageParts.length-3]
	        	
	        	//console.log('prefix to look for.: ' + prefix)
        		for(i = 0; i < postFixes.length; i++) {
	        		//if (keyprefix == prefix + postfix) { 
	        			//console.log('KeyPrefix: ' + ( prefix + postFixes[i] ))
        			    var postFix =  postFixes[i];
	        			var postFixes_san = postFix.replace(".", "_");
	        			var value = json[( prefix + postFixes[i] )]
	        			/*
	        			if(value.indexOf('e') > 0 && value.length > 7) {
	        				value = Number(value).toPrecision(4)
	        			}
	        			*/
	        			//console.log('value: ' + value)
	        			//console.log('values: ' + value.length)
        		    	if(!isNaN(parseFloat(value)) && value < 1) {
        		    		value = Math.round(Number(value) * 10000) / 10000;
    	        			//console.log('if value: ' + value)
        		    	}
	        			
	        			
	        			//console.log('KeyPrefix: ' + ( prefix + postFixes[i] ) + ' ' + value)
	        			timer[postFixes_san] = value
	        		//}
        		}
	        	timers.push(timer)
	        
	        }
	        
	    }
	    details.timers = timers
	    
	    /*
	    "instance.uptime": 10299616,
	    "uptime": 10325362,
	    "systemload.average": -1.0,
	    "heap.committed": 1754624,
	    "heap.init": 262144,
	    "heap.used": 1333191,
	    "heap": 3702784,
	    "threads.peak": 32,
	    "threads.daemon": 24,
	    "threads": 32,
	    "classes": 15303,
	    "classes.loaded": 15303,
	    "classes.unloaded": 0,
	    "gc.ps_scavenge.count": 20,
	    "gc.ps_scavenge.time": 836,
	    "gc.ps_marksweep.count": 4,
	    "gc.ps_marksweep.time": 1805,
	    "counter.status.206.star-star": 2,
	    "counter.status.200.metrics": 1,
	    "gauge.response.star-star": 12.0,
	    "counter.status.200.star-star": 124,
	    "counter.status.404.star-star": 4,
	    "gauge.response.metrics.root": 5.0,
	    "counter.status.200.metrics.root": 4,
	    "gauge.response.metrics": 10.0,
	    "httpsessions.max": -1,
	    "httpsessions.active": 0,
	    "datasource.primary.active": 0,
	    "datasource.primary.usage": 0.0,
	    "datasource.core.commons.rdm.active": 0,
	    "datasource.core.commons.rdm.usage": 0.0
	    */
	    $scope.details = details
	}).error(function (json, status) {
	    console.log('Error ' + json)
    	$scope.loading = false;
    	$scope.responseList = json;
	})
});




app.factory('HealthService', function () {
	return {
		findService: function(servicename, json) {
			var service = {}
			service['servicename'] = servicename
			
			var childservices = []
			var otherparams = []
			for(key in json) {
				if(key === 'status') {
					service[key] = json[key]
		    	} else if(key === 'error') {
					service[key] = json[key]
		    	} else {
		    		var jsonVal = json[key]
		    		
		    		// If it has a child status, then
		    		// it must be serivce itself
		    		if(jsonVal['status']) {
		    			var childservice = {}
		    			childservice['servicename'] = jsonVal
		    			childservice['status'] = jsonVal['status']
		    			childservices.push(childservice)
		    		} else {
		    			var otherparam = {}
		    			otherparam['name'] = key
		    			otherparam['value'] = jsonVal
		    			otherparams.push(otherparam)
		    		}
		    	}
				service['params'] = otherparams
			}
			return service
		}
	};
});
