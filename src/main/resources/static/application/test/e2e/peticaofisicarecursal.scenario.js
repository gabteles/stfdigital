/**
 * @author Anderson.Araujo
 * 
 * @since 25.01.2016
 */
/*jshint undef:false */
(function() {
	'use strict';
	
	var PrincipalPage = require('./pages/principal.page');
	
	var PeticionamentoPage = require('./pages/peticionamento.page');
	
	var RegistroPage = require('./pages/registro.page');
	
	var AutuacaoPage = require('./pages/autuacao.page');
	
	var DistribuicaoPage = require('./pages/distribuicao.page');
	
	var PreautuacaoPage = require('./pages/preautuacao.page');
	
	var AnalisePressupostoPage = require('./pages/analise.page');
	
	var RevisaoPage = require('./pages/revisao.page');
	
	var AnaliseRGPage = require('./pages/analiseRG.page');
	
	var LoginPage = require('./pages/login.page');
	
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
	
	describe('Autuação de Petições Físicas Recursais:', function() {
		
		beforeEach(function() {
			console.info('\nrodando:', jasmine.getEnv().currentSpec.description);
		});
		
		it('Deveria logar como recebedor', function() {
			login('recebedor');
		});

		
		it('Deveria registrar uma peticao física recursal', function(){
			
			var registroPage = new RegistroPage();
			
			registroPage.registrarVariasPeticoes(3);
			
			expect(browser.getCurrentUrl()).toMatch(/\/dashboard/);
			
			loginPage.logout();
		    
		});
		
		it('Deveria logar como preautuador recursal', function() {
			login('preautuador-recursal');
		});

		it('Deveria pré-atuar como válida a petição recebida como eleitoral ou criminal', function() {
			
			principalPage = new PrincipalPage();
			
		    expect(principalPage.tarefas().count()).toBeGreaterThan(0);
		    
		    principalPage.tarefas().get(0).getText().then(function(text) {
		    	pos = text.search("#");
		    	pos = pos + 1;
		    	peticaoId = text.substr(pos, text.length);
		    	expect(principalPage.tarefas().get(0).getText()).toEqual('Pré-Autuar Processo Recursal #' + peticaoId);
		    });
		    
		    preautuarRecursal('Criminal');
		    
		});
		
		it('Deveria logar como autuador recursal', function() {
			login('autuador-recursal-ce');
		});
		
		it('Deveria autuar um processo recursal com a preferência criminal ou eleitoral', function(){
			
			principalPage.tarefas().get(0).getText().then(function(text) {
		    	pos = text.search("#");
		    	pos = pos + 1;
		    	peticaoId = text.substr(pos, text.length);
		    	expect(principalPage.tarefas().get(0).getText()).toEqual('Autuar Processo Recursal Criminal / Eleitoral #' + peticaoId);
		    });
		    
		    principalPage.executarTarefa();
		    
		    expect(browser.getCurrentUrl()).toMatch(/\/processo\/autuacao-criminal/);
		    
		    var autuacaoPage = new AutuacaoPage();
		    
			autuacaoPage.partePoloAtivo('João da Silva');
		    
			autuacaoPage.partePoloPassivo('Maria da Silva');
			
			autuacaoPage.selecionarAssunto('287');
		    
			autuacaoPage.finalizarRecursal();
			
			expect(browser.getCurrentUrl()).toMatch(/\/dashboard/);
			
			loginPage.logout();
		});
		
		it('Deveria logar como preautuador recursal', function() {
			login('preautuador-recursal');
		});
		
		it('Deveria pré-atuar como válida a petição recebida com tipo preferencia diferente de criminal e eleitoral', function() {
			
			principalPage = new PrincipalPage();
			
		    expect(principalPage.tarefas().count()).toBeGreaterThan(0);
		    
		    principalPage.tarefas().get(0).getText().then(function(text) {
		    	pos = text.search("#");
		    	pos = pos + 1;
		    	peticaoId = text.substr(pos, text.length);
		    	expect(principalPage.tarefas().get(0).getText()).toEqual('Pré-Autuar Processo Recursal #' + peticaoId);
		    });
		    
		    preautuarRecursal('Medida Liminar');
		    
		});
		
		it('Deveria logar como analista-pressupostos', function() {
			login('analista-pressupostos');
		});
		
		it('Deveria analisar os pressupostos do processo recursal informando como inapto', function() {
			
		    expect(principalPage.tarefas().count()).toBeGreaterThan(0);
		    
		    principalPage.tarefas().get(0).getText().then(function(text) {
		    	pos = text.search("#");
		    	pos = pos + 1;
		    	peticaoId = text.substr(pos, text.length);
		    	expect(principalPage.tarefas().get(0).getText()).toEqual('Analisar Pressupostos Formais #' + peticaoId);
		    });
		    
		    principalPage.executarTarefa();
		    
		    expect(browser.getCurrentUrl()).toMatch(/\/processo\/analise/);
		    
		    var analisePage = new AnalisePressupostoPage();
		    
		    analisePage.classificarInapto('Inapto');
		    
		    analisePage.selecionarMotivo('Outro');
		    
		    analisePage.preencherObsMotivo();
		    
		    analisePage.preencherObsAnalise();
		    
		    analisePage.finalizarAnalise();

			expect(browser.getCurrentUrl()).toMatch(/\/dashboard/);
			
			loginPage.logout();
		});
		
		it('Deveria logar como revisor de processo recursal', function() {
			login('revisor-processo-ri');
		});
		
		it ('Deveria revisar processo recursal como apto ', function(){
			
		    expect(principalPage.tarefas().count()).toBeGreaterThan(0);
		    
		    principalPage.tarefas().get(0).getText().then(function(text) {
		    	pos = text.search("#");
		    	pos = pos + 1;
		    	peticaoId = text.substr(pos, text.length);
		    	expect(principalPage.tarefas().get(0).getText()).toEqual('Revisar Processo Inapto #' + peticaoId);
		    });
		    
		    principalPage.executarTarefa();
		    
		    expect(browser.getCurrentUrl()).toMatch(/\/processo\/revisao/);
		    
		    var revisaoPage = new RevisaoPage();
		    
		    revisaoPage.preencherObsAnalise();
		    
		    revisaoPage.finalizarRevisao();

			expect(browser.getCurrentUrl()).toMatch(/\/dashboard/);
			
			loginPage.logout();
		    
		});
		
		it('Deveria logar como analista de repercussão geral', function() {
			login('analista-repercussao-g');
		});
		
		it ('Deveria analisar o processo com Repercussão Geral ', function(){
			
		    expect(principalPage.tarefas().count()).toBeGreaterThan(0);
		    
		    principalPage.tarefas().get(0).getText().then(function(text) {
		    	pos = text.search("#");
		    	pos = pos + 1;
		    	peticaoId = text.substr(pos, text.length);
		    	expect(principalPage.tarefas().get(0).getText()).toEqual('Analisar Assunto / RG #' + peticaoId);
		    });
		    
		    principalPage.executarTarefa();
		    
		    expect(browser.getCurrentUrl()).toMatch(/\/processo\/repercussao/);
		    
		    var analiseRGPage = new AnaliseRGPage();
		    
		    analiseRGPage.selecionarTese();
		    
		    analiseRGPage.preencherNumeroTese();
		    
		    analiseRGPage.selecionarAssunto(287);
		    
		    analiseRGPage.preencherObsAnalise();
		    
		    analiseRGPage.finalizar();

			expect(browser.getCurrentUrl()).toMatch(/\/dashboard/);
			
			loginPage.logout();
		    
		});
		
		
		var preautuarRecursal = function(tipoPreferencia){
			
		  	principalPage.executarTarefa();

			expect(browser.getCurrentUrl()).toMatch(/\/peticao\/preautuacao\/recursal/);
		    
			var preautuacaoPage = new PreautuacaoPage();
			
			preautuacaoPage.classificar('AI');
			
			preautuacaoPage.selecionarPreferencias(tipoPreferencia);
			
			preautuacaoPage.preencherAnalise();
			
			preautuacaoPage.finalizar();
			
			expect(browser.getCurrentUrl()).toMatch(/\/dashboard/);
			
			loginPage.logout();
		}
		
		
	});
})();
