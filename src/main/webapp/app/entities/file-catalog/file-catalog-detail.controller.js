(function() {
    'use strict';

    angular
        .module('ftpcountbuddyApp')
        .controller('FileCatalogDetailController', FileCatalogDetailController);

    FileCatalogDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FileCatalog'];

    function FileCatalogDetailController($scope, $rootScope, $stateParams, previousState, entity, FileCatalog) {
        var vm = this;

        vm.fileCatalog = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ftpcountbuddyApp:fileCatalogUpdate', function(event, result) {
            vm.fileCatalog = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
