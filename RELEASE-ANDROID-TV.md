# Releases Android TV

A versão Android deste fork é **1.3**, `versionCode=5`, pacote `io.freemytv`.
O dashboard Moi vem do PR upstream #222 (head importado `9207974`). A identidade visual Android TV é uma personalização do fork, não um firmware oficial.

## Publicação automática

1. Atualize `android:versionName` e incremente `android:versionCode` no manifesto.
2. Rode `bash tools/check.sh all` e teste na TCL: HOME, controle, aplicativos, configurações, temas e entradas. Os testes de fonte/JVM não comprovam uso real pela TV.
3. Faça commit e envie a branch. Crie uma tag correspondente ao manifesto (nesta versão, `v1.3`) e envie a tag ao GitHub.
4. `.github/workflows/release.yml` executa todos os checks em runner hospedado e só então recompila com a chave de assinatura persistente. O próprio Actions cria a release com APK e SHA256SUMS.txt. Não publique a release manualmente.

O Secret `FREEMYTV_KEYSTORE_BASE64` contém a chave original codificada e foi autorizado pelo proprietário. Nunca coloque a chave, seu conteúdo ou `config.properties` no Git. Restrinja alterações nos workflows e acesso de escrita ao repositório: quem controla o workflow pode usar o Secret. Guarde backup seguro da chave local.

O APK público usa configuração genérica, sem chaves de API pessoais. Um APK local pode usar `config.properties`, mas não deve ser anexado à release pública.

## Instalação na TCL

A release não abre acesso à rede local e não depende de runner self-hosted. O GitHub não consegue alcançar diretamente o IP privado da TCL. Instale o APK assinado por ADB na rede local com `adb -s <serial> install -r <apk>`, sem desinstalar a versão anterior. Confirme versão instalada, assinatura e HOME depois da instalação.

Publicar uma release não implementa atualizações OTA no app: um cliente de atualizações na TV ainda é um recurso separado. Nenhum workflow deve alterar o modo HOME, desativar pacotes ou fazer debloat. A opção Original permanece o caminho de recuperação.

O fluxo de release é Android; as compilações LG/Samsung continuam nos checks, mas seus pacotes não são publicados por este workflow.
