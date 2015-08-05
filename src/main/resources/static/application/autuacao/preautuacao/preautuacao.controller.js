/**
 * @author Rodrigo Barreiros
 * 
 * @since 1.0.0
 * @since 10.07.2015
 */ 
(function() {
	'use strict';
	
	angular.autuacao.controller('PreautuacaoController', function ($log, $http, $state, $stateParams, properties) {
		var preautuacao = this;
		
		preautuacao.idPeticao = $stateParams.idTarefa;
		
		preautuacao.finalizar = function() {
			$http.post(properties.apiUrl + '/peticao/' + preautuacao.idPeticao + '/preautuacao').success(function(data, status, headers, config) {
				$log.debug('Sucesso');
				$state.go('dashboard');
			}).error(function(data, status, headers, config) {
				$log.debug('Erro');
			});
		};
	});

})();

