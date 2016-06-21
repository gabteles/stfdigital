#!/usr/bin/env bash


repositorios=( "plataforma/core" \
    "plataforma/discovery" \
    "plataforma/gateway" \
    "plataforma/services" \
    "plataforma/ui" \
    "plataforma/documents" \
    "plataforma/userauthentication" \
    "autuacao/autuacao" \
    "autuacao/distribuicao" \
    "autuacao/peticionamento" \
    "autuacao/recebimento" \
)

for identificador in "${repositorios[@]}"
do
    cd $identificador
    echo "#$identificador"
    git "$@"
    echo "#"
    cd - > /dev/null
done