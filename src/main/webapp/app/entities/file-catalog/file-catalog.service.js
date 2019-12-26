(function() {
    'use strict';
    angular
        .module('ftpcountbuddyApp')
        .factory('FileCatalog', FileCatalog);

    FileCatalog.$inject = ['$resource', 'DateUtils'];

    function FileCatalog ($resource, DateUtils) {
        var resourceUrl =  'api/file-catalogs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.insert = DateUtils.convertDateTimeFromServer(data.insert);
                        data.processFinishDate = DateUtils.convertDateTimeFromServer(data.processFinishDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
