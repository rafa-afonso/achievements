# Achievements App — resumo do design (Android UX v1)

Fonte: artefato de design "Achievements App — Android UX v1" (https://claude.ai/artifact/8a3GwYGLyAfYfNVPyyT1uW), 8 telas de celular (390×844), interativas e ligadas entre si.
Observação: a conversa do link compartilhado (claude.ai/share/bfe42a46…) não pôde ser lida daqui (bloqueio do Cloudflare). Este resumo vem só do artefato; decisões que estavam apenas no texto da conversa não estão aqui.

## Conceito

Um app Android pessoal para **registrar conquistas** (coisas que você já fez) e **acompanhar metas em andamento**, organizadas por **categorias**, com uma **linha do tempo** e uma tela de **retrospectiva** ("Look back"). A ideia central é ver o caminho percorrido: conquistas podem ser encadeadas ("follow-up"), formando uma trilha (ex.: 5K → 10K → meia maratona).

## Navegação

- Barra inferior com 3 abas: **Timeline**, **Categories**, **Look back**.
- Botão flutuante (FAB) "New" na Timeline e "+" no detalhe da categoria → criar conquista.
- Detalhes abrem em tela cheia com botão de voltar; "Log progress" é uma folha (bottom sheet).

## Telas

1. **Timeline (home)** — data de hoje e "Your achievements"; busca; cartão escuro com o total do ano (47, "4 this month") e mini gráfico de barras por mês, que leva ao Look back; chips de filtro por categoria (All, Career, Health, Learning, Personal); seção "In progress" com barras de progresso (ex.: "Run a half marathon 14 / 21.1 km", "Read 12 books 9 / 12"); lista do mês agrupada ("October 2026") em linha do tempo vertical com ponto colorido da categoria, chip, data e indicação "Follows up: …".
2. **Categories** — grade 2 colunas de cartões (ícone colorido, nome, nº de conquistas, meta anual opcional com barra "16 of 20", ou "1 in progress · last 2 Sep"); cartão tracejado "New category"; botão de ordenar.
3. **Category detail** — cabeçalho com ícone, nome, total e "since March 2025"; barra da meta anual; filtro segmentado All / In progress / Done (com contagens); lista agrupada por ano, com item em progresso mostrando fração (2/3) e concluídos com check; indicação de follow-ups.
4. **Achievement detail** — chips de categoria e status; título; data de início e alvo; cartão de progresso (valor grande, %, barra) com ações "Log progress" e "Mark done"; seção **"The road here"** mostrando a cadeia de conquistas anteriores até "You are here" e link "Plan a follow-up"; **Progress log** com histórico de registros (valor, data, nota, delta "+2 km"). Editar e mais opções no topo.
5. **New achievement** — "What did you accomplish?"; escolha de categoria (chips + "New"); status **Already done** ou **Track progress**; quando em progresso: alvo (número), unidade (texto livre, ex. "modules", "km"), data de início; **Follows up on** (opcional, vincula a uma conquista anterior); "Why it matters" (opcional); salvar.
6. **Log progress (sheet)** — valor atual com botões −/+ (passo 0,5), % do alvo e delta; atalhos rápidos; data (Today); nota; interruptor "Mark done when I hit the target" (ligado por padrão); salvar.
7. **New category** — prévia do chip; nome; cor (8 opções: teal, orange, violet, rose, amber, blue, green, slate); ícone (12 opções: briefcase, activity, book, heart, growth, code, trophy, music, home, camera, star, flag); meta anual opcional ("Adds a goal bar to the category page").
8. **Look back** — período Month / 2026 / All time; total do ano com comparação ("More than double all of 2025 (20)"); gráfico de barras por mês com melhor mês; "Where it went" (distribuição por categoria); "One year ago today" (memória de um ano atrás).

## Modelo de dados implícito

- **Category**: nome, cor, ícone, meta anual opcional (nº de conquistas no ano), data de criação.
- **Achievement**: título, categoria, status (done / in progress), data de conclusão ou início, alvo numérico + unidade (só quando acompanhado), valor atual, "why it matters" (nota), `followsUp` → outra conquista (forma cadeias), auto-concluir ao atingir o alvo.
- **ProgressEntry**: conquista, valor, data, nota (o delta é calculado).
- Derivados: totais por mês/ano/categoria, melhor mês, "um ano atrás hoje", progresso da meta da categoria.

## Decisões visuais

- Plataforma alvo: **Android** (padrões Material 3: barra de navegação inferior com indicador em pílula, FAB estendido, bottom sheet, botões segmentados), alvos de toque ≥ 44–48 px.
- Tipografia: **Space Grotesk** para títulos e números, **DM Sans** para texto.
- Cores: fundo #F2F3F5, cartões brancos, texto #15171C, secundário #5B6170; cor de destaque padrão azul #2346D1 (ajustável). Cada categoria tem sua cor (Career #0E6E61, Health #B23C0A, Learning #6337C9, Personal #A3204F, Finance #7A5600), usada em chips, pontos da timeline e barras.
- Cantos arredondados generosos (16–20 px nos cartões), ícones de traço, sem emoji.
- Textos do protótipo em inglês.

## Escopo funcional da v1 (inferido das telas)

Criar/editar categorias; criar conquistas já feitas ou com progresso; registrar progresso; marcar como concluída (manual ou automática); encadear follow-ups; timeline com filtro e busca; detalhe por categoria com metas anuais; estatísticas de retrospectiva.
