/**
 * @author Rodrigo Barreiros
 * 
 * @since 1.0.0
 * @since 10.07.2015
 */
(function() {
	'use strict';

	angular.plataforma.factory('messages', function() {
		return {
			success : function(message) {
				$('body').pgNotification({
					style: 'bar', 
					message: message, 
					position: 'top', 
					timeout: 6000, 
					type: 'success'
				}).show();
			},
			error : function(message) {
				$('body').pgNotification({
					style: 'bar', 
					message: message, 
					position: 'top', 
					timeout: 6000, 
					type: 'danger'
				}).show();
			},
			buildErrorMessage: function(errorMessageDto) {
				var errorMsg = '';
				for (var i in errorMessageDto.errors) {
					errorMsg += errorMessageDto.errors[i].message;
					if (i < (errorMessageDto.errors.length - 1)) {
						errorMsg += ' ';
					}
				}
				return errorMsg;
			}
		};
	});
	
})();
