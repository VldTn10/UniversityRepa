package MM1_des;

import java.util.ArrayList;
import java.util.List;

public class PriorityQueue {
    private List<Event> queue;

    //создаем список из времен очереди
    public PriorityQueue(){
        queue = new ArrayList<Event>();
    }

    //длина очереди
    public int length(){
        return queue.size();
    }

    //функция выбора события с наивысшим приоритетом (ближайшего по времени наступления)
    public Event head(){
        double time = queue.get(0).getFireTime();
        int i = 0;
        int count = 0;
        for (Event e : queue) { //цикл, в котором вычисляется событие, ближайшее по времени наступления
            if (e.getFireTime() < time ){
                time = e.getFireTime();
                count = i;
            }
            i++;
        }
        return queue.get(count); //возвращаем событие с наивысшим приоритетом
    }

    //удаление события с наивысшим приоритетом (ближайшего по времени наступления)
    public void pop(){
        double time = queue.get(0).getFireTime();
        int i = 0;
        int count = 0;
        for (Event e : queue) { //цикл, в котором вычисляется событие, ближайшее по времени наступления
            if (e.getFireTime() < time ){
                time = e.getFireTime();
                count = i;
            }
            i++;
        }
        queue.remove(count);
    }

    //помещение элемента в конец очереди
    public void push(Event e){
        queue.add(e);
    }

    //очистка очереди
    public void clear(){
        queue.clear();
    }

    public List getList(){
        return queue;
    }
}

