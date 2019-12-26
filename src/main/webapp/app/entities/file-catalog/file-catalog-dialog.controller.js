(function() {
    'use strict';

    angular
        .module('ftpcountbuddyApp')
        .controller('FileCatalogDialogController', FileCatalogDialogController);

    FileCatalogDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FileCatalog'];

    function FileCatalogDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FileCatalog) {
        var vm = this;

        vm.fileCatalog = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.fileCatalog.id !== null) {
                FileCatalog.update(vm.fileCatalog, onSaveSuccess, onSaveError);
            } else {
                FileCatalog.save(vm.fileCatalog, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ftpcountbuddyApp:fileCatalogUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.insert = false;
        vm.datePickerOpenStatus.processFinishDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
