(function() {
    'use strict';

    angular
        .module('ftpcountbuddyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('file-catalog', {
            parent: 'entity',
            url: '/file-catalog?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ftpcountbuddyApp.fileCatalog.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/file-catalog/file-catalogs.html',
                    controller: 'FileCatalogController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fileCatalog');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('file-catalog-detail', {
            parent: 'file-catalog',
            url: '/file-catalog/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ftpcountbuddyApp.fileCatalog.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/file-catalog/file-catalog-detail.html',
                    controller: 'FileCatalogDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fileCatalog');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FileCatalog', function($stateParams, FileCatalog) {
                    return FileCatalog.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'file-catalog',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('file-catalog-detail.edit', {
            parent: 'file-catalog-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/file-catalog/file-catalog-dialog.html',
                    controller: 'FileCatalogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FileCatalog', function(FileCatalog) {
                            return FileCatalog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('file-catalog.new', {
            parent: 'file-catalog',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/file-catalog/file-catalog-dialog.html',
                    controller: 'FileCatalogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                path: null,
                                processed: null,
                                deleted: null,
                                insert: null,
                                processFinishDate: null,
                                deviceId: null,
                                uuid: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('file-catalog', null, { reload: 'file-catalog' });
                }, function() {
                    $state.go('file-catalog');
                });
            }]
        })
        .state('file-catalog.edit', {
            parent: 'file-catalog',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/file-catalog/file-catalog-dialog.html',
                    controller: 'FileCatalogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FileCatalog', function(FileCatalog) {
                            return FileCatalog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('file-catalog', null, { reload: 'file-catalog' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('file-catalog.delete', {
            parent: 'file-catalog',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/file-catalog/file-catalog-delete-dialog.html',
                    controller: 'FileCatalogDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FileCatalog', function(FileCatalog) {
                            return FileCatalog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('file-catalog', null, { reload: 'file-catalog' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
