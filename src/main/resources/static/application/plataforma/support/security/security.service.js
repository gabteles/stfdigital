/**
 * @author Rodrigo Barreiros
 * 
 * @since 1.0.0
 * @since 17.07.2015
 */
(function() {
	'use strict';

	angular.module('plataforma').factory('SecurityService', function() {
		return {
			papeis : function() {
				return [
					{nome : 'peticionador'}, 
					{nome : 'recebedor'}, 
					{nome : 'autuador'}, 
					{nome : 'devolvedor'}, 
					{nome : 'distribuidor'} 
				]
			}
		};
	});
	
})();