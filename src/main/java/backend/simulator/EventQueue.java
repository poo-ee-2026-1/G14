package backend.simulator;

// Importa fila de prioridade do Java
import java.util.PriorityQueue;

// Classe responsável por armazenar e organizar os eventos futuros da simulação.

public class EventQueue {

// ATRIBUTOS

    // Fila de prioridade dos eventos
    private final PriorityQueue<Event> events;

//CONSTRUTOR


    // Construtor padrão da fila de eventos
    public EventQueue() {

        // Cria a fila vazia
        this.events = new PriorityQueue<>();
    }

// MÉTODOS PRINCIPAIS


    // Adiciona um novo evento à fila
    public void addEvent(Event event) {

        // Insere evento na fila
        this.events.add(event);
    }

    // Remove e retorna o próximo evento
    public Event nextEvent() {

        // retorna e remove o menor elemento
        return this.events.poll();
    }

    // Retorna o próximo evento sem remover
    public Event peekNextEvent() {

        // consulta o próximo elemento
        return this.events.peek();
    }

    // Verifica se a fila está vazia
    public boolean isEmpty() {

        return this.events.isEmpty();
    }

    // Retorna quantidade de eventos
    public int size() {

        return this.events.size();
    }

    // Remove todos os eventos da fila
    public void clear() {

        this.events.clear();
    }
}