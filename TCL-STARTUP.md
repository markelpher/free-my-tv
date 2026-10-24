# Abertura TCL

A tela com o logo TCL aparece por aproximadamente 1,2 segundo na primeira abertura da tela inicial em cada processo do launcher, somente quando `Build.MANUFACTURER` ou `Build.BRAND` é exatamente TCL (sem diferenciar maiúsculas ou espaços externos).

- Não reaparece ao apertar Home ou ao voltar das configurações.
- Pode reaparecer se o Android encerrar o processo e o launcher iniciar novamente.
- Qualquer seta, OK ou Voltar dispensa a abertura; teclas de volume e sistema mantêm o comportamento normal.
- Não altera o firmware, a animação de boot, o launcher padrão ou os pacotes instalados.
- O logo é um vetor local; não há download, áudio nem animação contínua durante a execução.

O desenho do logo foi obtido da navegação do site brasileiro da TCL:
https://aws-obg-image-lb-1.tcl.com/content/dam/brandsite/it-resources/common/navigation/tcl-logo.svg

TCL é marca da respectiva fabricante. A tela serve apenas à identificação do aparelho; este fork não é firmware oficial ou certificado pela TCL. Os paths foram convertidos para VectorDrawable preservando as proporções e a cor do SVG.

Teste: `bash tools/check.sh tests` cobre o filtro por fabricante e a exibição única por processo. A presença do logo, sua duração e o retorno à home devem ser verificados no aparelho real.
