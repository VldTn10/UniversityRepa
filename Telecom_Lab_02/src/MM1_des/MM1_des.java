package MM1_des;

import MM1_advanced.NextEvent;

import java.util.ArrayList;

public class MM1_des {

    private Event event;
    private PriorityQueue evQueue;
    private ArrayList<Double> msgQueue;
    private boolean busy;
    private double tBecameBusy;                                     //индикатор занятости системы
    private double tChangedQueue;                                   //ближайший прошлый момент времени, когда длина очереди изменялась
    private double c;
    private double iQlen;
    private double iBusy;
    private double sDelay;                                          //накопленная сумма ожиданий в очереди по всем обработанным заявкам
    private int nMsgSer;
    private int nMsgArr;
    private boolean stop;                                           //флаг останова
    private double clockMax;
    private int nMsgMax;
    private String stopType;
    private int EV_ARRIVE;
    private int EV_SERVE_END;
    private int numMsg;
    private int nServ;

    public MM1_des(double a, int b, String q){

        msgQueue = new ArrayList<Double>();  //список времен сообщений в очереди
        evQueue = new PriorityQueue(); // хз, так ли это
        event = new Event();

        clockMax = a;
        nMsgMax = b;
        stopType = q;

        tBecameBusy = 0.0D;       //ближайший прошлый момент времени, когда система была занята
        tChangedQueue = 0.0D;
        busy = false;
        stop = false;
        sDelay = 0.0D;
        c = 0.0D;                //модельное время
        EV_ARRIVE = 1; //это константа какая-то
        EV_SERVE_END = 3; //тоже какая-то константа
        numMsg = 0;
        nServ = 0;

    }

    public void mm1_des(double lambda, double mu){

        event.setType(EV_ARRIVE);
        event.setFireTime(df_exp(lambda));
        evQueue.push(event);

        while (!stop && evQueue.length() > 0){

            event = evQueue.head(); //event = evQueue.get(0);
            evQueue.pop();
            c = event.getFireTime();
            if(nMsgArr < nMsgMax || c < clockMax) {
                if (event.getType() == EV_ARRIVE) {
                    onNextArriveDes(lambda, mu);
                } else {
                    if (event.getType() == EV_SERVE_END) {
                        onServeEndDes(mu);
                    }
                }
            }else{
                    onServeEndDes(mu);
                nServ += 1;
            }

            stop = updateStopCondition(this.stopType);
        }

        double avgQlen = iQlen / c;                                 //среднее значение длины очереди (имитационно)
        double avgBusy = iBusy / c;                                 //среднее значение коэффициента занятости системы (имитационно)
        double avgDelay = sDelay / nMsgSer;                         //средняя задержка (имитационно)
        double avNcSys = numMsg/c;
        double avStSy = c/numMsg + sDelay/numMsg;                          //среднее время пребывания заявки в системе (имитационно)
        double tInQ = sDelay/numMsg;

        double ro = lambda/mu;
        double tInQAn = ro/(mu*(1-ro));
        double avgQlenAn = ro*ro/(1-ro);
        double avStSyAn = 1/(mu*(1-ro));
        double avgDelayAn = avgQlenAn/lambda;
        double avNSysAn = ro/(1-ro);

        System.out.println("-------------------------Алгоритм MM1_des---------------------------------");
        System.out.println("среднее значение длины очереди (имитационно): " + avgQlen);
        System.out.println("среднее значение длины очереди (аналитически): " + avgQlenAn);
        System.out.println("среднее время пребывания заявки в системе (имитационно): " + avStSy);
        System.out.println("среднее время пребывания заявки в системе (аналитически): " + avStSyAn);
        System.out.println("среднее время пербывания заявки в очереди (имитационно): " + tInQ);
        System.out.println("среднее время пребывания заявки в очереди (аналитически): " + tInQAn);
        System.out.println("среднее значение коэффициента занятости системы (имитационно): " + avgBusy);
        System.out.println("среднее значение коэффициента занятости системы (аналитически): " + ro);
        System.out.println("средняя задержка(Формула Литла)(имитационно): " + avgDelay);
        System.out.println("средняя задержка(Формула Литла)(аналитически): " + avgDelayAn);
        System.out.println("среднее число заявок в системе (имитационно): " + avNcSys);
        System.out.println("среднее число заявок в системе (аналитически): " + avNSysAn);

        System.out.println();
        System.out.println("количество элементов в очереди: " + msgQueue.size());
        System.out.println("количество принятых заявок: " + nMsgArr);
        System.out.println("количество обслуженных заявок: " + nMsgSer);
        System.out.println("количество дообслуженных заявок: " + nServ);
        //System.out.println("------------------------------------------------------------------");
        //System.out.println(c);

    }

    //Метод проверки и обновления условия и индикатора остановки (взяли из предыдущей лабы)
    public boolean updateStopCondition(String a){
        String foo = "t";
        String foo1 = "m";

        if(a.equals(foo)){
            if(c >= clockMax) {
                this.stop = true;
                System.out.println(a);
            }
        }

        if(a.equals(foo1)) {
            if(nMsgArr == nMsgSer) {      //эта строчка кода следит, чтобы все поступившие заявки были обработаны.
                if (nMsgSer >= nMsgMax) {
                    this.stop = true;
                }
            }
        }
        return this.stop;
    }

    public static double df_exp(double lambda){
        return (-1/lambda)*Math.log(Math.random());
    }

    //Метод обработки события прибытия заявки либо в очередь, либо в прибор
    public void onNextArriveDes(double lambda, double mu){
        if (!this.busy){
            this.busy = true;
            this.tBecameBusy = c;
            event.setType(EV_SERVE_END);
            event.setFireTime(c + df_exp(mu));
            evQueue.push(event);
            numMsg += 1;
        }
        else{
            iQlen = iQlen + msgQueue.size() * (c - tChangedQueue);
            tChangedQueue = c;
            msgQueue.add(c);
        }
        event = new Event();
        event.setType(EV_ARRIVE);
        event.setFireTime(c + df_exp(lambda));
        evQueue.push(event);
        nMsgArr += 1;
    }

    //Метод обработки события обслуживания заявки
    public void onServeEndDes(double mu){
        if (msgQueue.size() == 0){
            iBusy = iBusy + (c - tBecameBusy);
            busy = false;
        }
        else{
            iQlen = iQlen + msgQueue.size() * (c - tChangedQueue);
            tChangedQueue = c;
            sDelay = sDelay + (c - msgQueue.get(0));
            msgQueue.remove(0);
            event.setType(EV_SERVE_END);
            event.setFireTime(c + df_exp(mu));
            evQueue.push(event);
            numMsg += 1;
        }
        nMsgSer += 1;
    }
}


