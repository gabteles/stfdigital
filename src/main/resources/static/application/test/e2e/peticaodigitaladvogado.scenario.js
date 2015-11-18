/**
 * @author Rodrigo Barreiros
 * 
 * @since 1.0.0
 * @since 07.07.2015
 */
/*jshint undef:false */
(function() {
	'use strict';
	
	var LoginPage = require('./pages/login.page');
	
	var PrincipalPage = require('./pages/principal.page');
	
	var PeticionamentoPage = require('./pages/peticionamento.page');
	
	var RegistroPage = require('./pages/registro.page');
	
	var AutuacaoPage = require('./pages/autuacao.page');
	
	var DistribuicaoPage = require('./pages/distribuicao.page');
	
	var PreautuacaoPage = require('./pages/preautuacao.page');
	
	var principalPage;
	
	var loginPage;
	
	var pos;
	
	var peticaoId;
	
	var login = function(user) {
		browser.ignoreSynchronization = true;
		if (!loginPage) loginPage = new LoginPage();
		loginPage.login(user);
		expect(browser.getCurrentUrl()).toMatch(/\/dashboard/);
		browser.ignoreSynchronization = false;
	};
		
	describe('Autuação de Petições Digitais Originárias:', function() { 
		
		beforeEach(function() {
			console.info('\nrodando:', jasmine.getEnv().currentSpec.description);
		});
		
		it('Deveria logar como peticionador', function() {
			login('peticionador');
		});

		it('Deveria navegar para a página de envio de petições digitais', function() {
			// Ao instanciar a Home Page, o browser já deve navega para a home page ("/")
			principalPage = new PrincipalPage();
			
			// Verificando se a Home Page tem conteúdo...
			expect(browser.isElementPresent(principalPage.conteudo)).toBe(true);
			
			// Iniciando o Processo de Autuação...
			principalPage.iniciarProcesso('link_registrar_peticao_eletronica');
			
			// Verificando se, após iniciar o processo, o browser está na página de registro de petições físicas
			expect(browser.getCurrentUrl()).toMatch(/\/peticao/);
		});

		it('Deveria enviar uma nova petição digital', function() {
			
			peticionar('RE');
			
			principalPage = new PrincipalPage();
			
			principalPage.iniciarProcesso('link_registrar_peticao_eletronica');
			
			peticionar('AP');
			
			loginPage.logout();
		});
		
		it('Deveria logar como autuador', function() {
			login('autuador');
		});
		
		it('Deveria atuar como válida a petição recebida', function() {
			
		    expect(principalPage.tarefas().count()).toEqual(2);
		    		    
		    principalPage.tarefas().get(0).getText().then(function(text) {
		    	pos = text.search("#");
		    	pos = pos + 1;
		    	peticaoId = text.substr(pos, text.length);
		    	expect(principalPage.tarefas().get(0).getText()).toEqual('Autuar Processo #' + peticaoId);
		    });
		    
			autuar();
			
			autuar();
			
			expect(browser.getCurrentUrl()).toMatch(/\/dashboard/);
			
			loginPage.logout();
		});
		
		it('Deveria logar como distribuidor', function() {
			login('distribuidor');
		});

		it('Deveria distribuir a petição autuada', function() {
					    
		    expect(principalPage.tarefas().count()).toEqual(1);
		    
		    principalPage.tarefas().get(0).getText().then(function(text) {
		    	pos = text.search("#");
		    	pos = pos + 1;
		    	peticaoId = text.substr(pos, text.length);
		    	expect(principalPage.tarefas().get(0).getText()).toEqual('Distribuir Processo #' + peticaoId);
		    });
			
		    principalPage.executarTarefa();

			expect(browser.getCurrentUrl()).toMatch(/\/peticao\/\d+\/distribuicao/);

			var distribuicaoPage = new DistribuicaoPage();
			
			distribuicaoPage.selecionar('Min. Roberto Barroso');
			
			distribuicaoPage.selecionarPrimeiraParte();
			
			distribuicaoPage.finalizar();
		    
			expect(browser.getCurrentUrl()).toMatch(/\/dashboard/);
			
			loginPage.logout();
		}); 
		
		it('Deveria logar como gestor-autuacao', function() {
			login('gestor-autuacao');
		});
		
		it('Deveria exibir os dashlets do papel gestor-autuacao', function(){			
			expect(browser.isElementPresent(principalPage.titleGestaoAutuacao)).toBe(true)
			loginPage.logout();
		});
		
		it('Deveria logar como cartoraria', function() {
			login('cartoraria');
		});
		
		it ('Deveria exibir a dashlet do papel cartorária', function(){			
			expect(principalPage.dashletMinhasTarefas.count()).toEqual(1);
		});
		
		
		var peticionar = function(siglaClasse){
			
			var peticionamentoPage = new PeticionamentoPage();
			
			peticionamentoPage.classificarClasse(siglaClasse);
			
			peticionamentoPage.partePoloAtivo('João da Silva');
		    
			peticionamentoPage.partePoloPassivo('Maria da Silva');
			
			peticionamentoPage.uploadPecas();
			
			peticionamentoPage.removePecas();
			
			peticionamentoPage.uploadPecas();
			
			peticionamentoPage.selecionarTipoPeca('Ato coator');
		    
			peticionamentoPage.registrar();
			
			expect(browser.getCurrentUrl()).toMatch(/\/dashboard/);
			
			expect(principalPage.dashletMinhasTarefas.count()).toEqual(1);
			
			expect(principalPage.dashletMinhasPeticoes.count()).toEqual(1);
		}
		
		var autuar = function(){

			principalPage.executarTarefa();
		    
			expect(browser.getCurrentUrl()).toMatch(/\/peticao\/\d+\/autuacao/);
			
			var autuacaoPage = new AutuacaoPage();
			
			autuacaoPage.classificar('AP');
			
			autuacaoPage.finalizar();
		    
		}
		
	});
})();