(function() {
    'use strict';

    angular
        .module('ftpcountbuddyApp')
        .controller('FileCatalogDeleteController',FileCatalogDeleteController);

    FileCatalogDeleteController.$inject = ['$uibModalInstance', 'entity', 'FileCatalog'];

    function FileCatalogDeleteController($uibModalInstance, entity, FileCatalog) {
        var vm = this;

        vm.fileCatalog = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FileCatalog.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
