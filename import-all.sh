#!/usr/bin/env bash

cd plataforma/core;               gradle eclipse; cd -
cd plataforma/discovery;          gradle eclipse; cd -
cd plataforma/documents;          gradle eclipse; cd -
cd plataforma/gateway;            gradle eclipse; cd -
cd plataforma/processos;          gradle eclipse; cd -
cd plataforma/test;               gradle eclipse; cd -
cd plataforma/ui;                 gradle eclipse; cd -
cd plataforma/identidades;        gradle eclipse; cd -

cd autuacao/autuacao;             gradle eclipse; cd -
cd autuacao/distribuicao;         gradle eclipse; cd -
cd autuacao/peticionamento;       gradle eclipse; cd -
cd autuacao/recebimento;          gradle eclipse; cd -