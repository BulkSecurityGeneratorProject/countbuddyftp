(function () {
    'use strict';

    angular
        .module('ftpcountbuddyApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
