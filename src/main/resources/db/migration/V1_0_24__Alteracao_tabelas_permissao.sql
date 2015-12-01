alter table corporativo.pessoa add column cod_cpf varchar2(11);
alter table corporativo.pessoa add column cod_oab varchar2(20);
alter table corporativo.pessoa add column dsc_email varchar2(100);
alter table corporativo.pessoa add column dsc_telefone varchar2(13);

alter table plataforma.usuario add column seq_pessoa bigint;
alter table plataforma.usuario drop column cod_cpf;
alter table plataforma.usuario drop column cod_oab;
alter table plataforma.usuario drop column dsc_email;
alter table plataforma.usuario drop column dsc_telefone;
alter table plataforma.usuario add constraint fk_pessoa_usua foreign key (seq_pessoa) references corporativo.pessoa;

create sequence corporativo.seq_associado increment by 1 start with 1 nomaxvalue minvalue 1 nocycle nocache;
create table corporativo.associado (seq_associado bigint not null, seq_pessoa bigint not null, seq_orgao bigint, tip_associado varchar2(20) not null, constraint pk_associado primary key (seq_associado));
alter table corporativo.associado add constraint uk_asso_seq_pessoa unique(seq_pessoa, seq_orgao);
alter table corporativo.associado add constraint FK_pessoa_asso foreign key (seq_pessoa) references corporativo.pessoa;
alter table corporativo.associado add constraint FK_orgao_asso foreign key (seq_orgao) references autuacao.orgao;
alter table corporativo.associado add constraint ck_asso_tip_associado check (tip_associado in ('GESTOR', 'ASSOCIADO', 'REPRESENTANTE'));
