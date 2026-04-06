# G14

# Smart-Charge-Station-
Smart Charge Station é um simulador de uma estação de totens de carregamento para carros elétricos. 

# Cronograma
06/04 - Fundamentos

13/04 - Core + Simulador

27/04 - Backend Base

12/05 - Logica (Implementar Algoritmos)

26/05 - Frontend

01/06 - Apresentacao

![imagem](https://github.com/poo-ee-2026-1/G14/blob/main/imagem.jpeg)

# Descrição do Problema
# Motivação para solucionar o problema (dados que demonstrem que é relevante) 
# Objetivo Geral (o produto final do projeto)
  Desenvolver um simulador de uma estação de totens de carregamento para carros elétricos com capacidade para oito totens (exibição em um grid 4x2) que gere aleatoriamente uma frota de carros de modelos diferentes (como se fossem clientes chegando para abastecer) e posicione os carros nos totens disponíveis. O simulador então executa um algoritmo de prioridade respeitando a potência máxima da estação e as especificações de cada modelo de carro, levando em conta o nível de bateria atual do carro. além disso, monitora o abastecimento e exibe um log de eventos, gerando um relatório no final do ciclo, quando enfim exibe o valor, em reais, do abastecimento. 
# Objetivos Específicos (cada etapa/funcionalidade do projeto individualmente) 
# Estratégias de Implementação 
    - Levantamento de Requisitos 
    - Modelagem do Sistema (Diagrama UML)
    - Desenvolvimento Modular
      - Modulo 1 
      - Modulo 2
    - Testes e Validação 
      - Sobrecarga 
      - Estresse 
# Tabela de Membros e Divisão de Tarefas 
    - Pedro Di Francescantonio Oliveira
    - Fernanda Eleuterio Borges 
# Tecnologias e Ferramentas Utilizadas
    - Linguagem: Java
    - Interface: QT Creator
# Conceitos Acadêmicos Utilizados
# Modelagem Inicial
CORE 
CLASSES 
Aplication 
GameLoop 
TimeSystem 
Appcontroler 
	OBJETOS
aplication (objeto principal da aplicação) 
gameLoop (objeto responsável pelo loop principal) 
timeSystem (objeto responsável pelo tempo da simulação)
appControler (objeto intermediador entre frontend e backend) 
	ENCAPSULAMENTO 
Atributos
running 
targetFPS 
simulationTime
timeScale
deltaSimulationTime
simulator
mainWindow 
Métodos 
start()
inicialize()
run()
stop()
update()
render()
setTimeScale()
getSimulationTime
processInput()
startSimulation()
resetSimulation()
	ABSTRAÇÃO
Aplication abstraída como inicialização do sistema 
GameLoop abstraído como ciclo contínuo de execução
TimeSystem abstraído como controlar do tempo real e simulado
Appcontroler abstraído como a ponte entre a interface e o simulador
	HERANÇA
Herança não obrigatória 
	POLIMORFISMO 
Polimorfismo não obrigatório
# Arquivos
  - Código Fonte 
  - Diagramas UML 
# Relatórios Individuais da Equipe 
# Considerações Finais 
