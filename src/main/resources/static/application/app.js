/**
 * @author Lucas Mariano
 * @author Rodrigo Barreiros
 * 
 * @since 1.0.0
 * @since 06.07.2015
 */ 
(function() {
	'use strict';

	angular.element(document).ready(function() {
		angular.bootstrap(document, ['app']);
	});

	angular.module('app', ['ui.router', 'ct.ui.router.extras.sticky', 'ct.ui.router.extras.previous', 'plataforma', 'autuacao', 'properties', 'ui.select2', 'ngSanitize', 'ngCookies', 'angularFileUpload'])
	
	.config(function($stateProvider, $urlRouterProvider, $logProvider, $httpProvider, $locationProvider) {
		$httpProvider.interceptors.push('security-interceptor');
		$httpProvider.interceptors.push('error-handler');
		$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
		$urlRouterProvider.otherwise('/dashboard');
		$locationProvider.html5Mode(true);
		$logProvider.debugEnabled(true);
		
		$stateProvider
			.state('root', {
				views: {
					'main@': {
						templateUrl: 'application/plataforma/template/default.tpl.html',
						controller: 'TemplateController',
						controllerAs: 'vm'
					}
				}
			})
			.state('cadastro', {
				url: '/cadastro',
				views: {
					'main@': {
						templateUrl: 'application/plataforma/cadastro/cadastro.tpl.html',
						controller: 'CadastroController',
						controllerAs: 'vm'
					}
				}
			})
			.state('login', {
				url: '/login',
				views: {
					'main@': {
						templateUrl: 'application/plataforma/login/login.tpl.html'
					}
				}
			});
	})
	.value('version', '0.1.0');
	
})();
