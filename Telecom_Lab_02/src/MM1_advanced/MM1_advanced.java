package MM1_advanced;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MM1_advanced {
    private NextEvent nextEv;
    private ArrayList<Double> msgQueue;
    private boolean busy;
    private double tBecameBusy;                             //индикатор занятости системы
    private double tChangedQueue;                           //ближайший прошлый момент времени, когда длина очереди изменялась
    private double c;
    private double iQlen;
    private double iBusy;
    private double sDelay;                                    //накопленная сумма ожиданий в очереди по всем обработанным заявкам
    private int nMsgSer;
    private int nMsgArr;
    private boolean stop;                               //флаг останова
    private double clockMax;
    private int nMsgMax;
    private String stopType;
    private double timeInSys;
    private int numMsg;
    private int nServ;


    public MM1_advanced(double a, int b, String q){
        msgQueue = new ArrayList<Double>();                       //список времен сообщений в очереди
        nextEv = new NextEvent();

        clockMax = a;
        nMsgMax = b;
        stopType = q;

        tBecameBusy = 0.0D;                                 //ближайший прошлый момент времени, когда система была занята
        tChangedQueue = 0.0D;
        timeInSys = 0.0D;
        busy = false;
        stop = false;
        sDelay = 0.0D;
        c = 0.0D;                                           //модельное время
        numMsg = 0;
        nServ = 0;
    }

    public void mm1_adv(double lambda, double mu) {

        nextEv.settServeEnd(0.0);                           //инициализация времени завершения обслуживания
        nextEv.settArrive(df_exp(lambda));                  //инициализация первого времени прихода заявки в систему

        nMsgArr = 0;
        nMsgSer = 0;


        while (!stop){
            if(nMsgArr < nMsgMax || c < clockMax) {
                if (!this.busy || nextEv.gettArrive() < nextEv.gettServeEnd()) {
                    c = nextEv.gettArrive();
                    onNextArrive(lambda, mu);
                }else {
                    c = nextEv.gettServeEnd();
                    onServeEnd(mu);
                }
            }else {
                c = nextEv.gettServeEnd();
                onServeEnd(mu);
                nServ += 1;
            }

            stop = updateStopCondition(this.stopType);
        }

        double avgQlen = iQlen / c;                                 //среднее значение длины очереди (имитационно)
        double avgBusy = iBusy / c;                                 //среднее значение коэффициента занятости системы (имитационно)
        double avgDelay = sDelay / nMsgSer;                         //средняя задержка (имитационно)
        double avNcSys = numMsg/c;
        double avStSy = iBusy/numMsg + sDelay/numMsg;                          //среднее время пребывания заявки в системе (имитационно)
        double tInQ = sDelay/numMsg;

        double ro = lambda/mu;
        double tInQAn = ro/(mu*(1-ro));
        double avgQlenAn = ro*ro/(1-ro);
        double avStSyAn = 1/(mu*(1-ro));
        double avgDelayAn = avgQlenAn/lambda;
        double avNSysAn = ro/(1-ro);

        System.out.println("-----------------------Алгоритм MM1_Advanced------------------------------");

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
        System.out.println();

    }

    public static double df_exp(double lambda){
        return (-1/lambda)*Math.log(Math.random());
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
            if(nMsgArr == nMsgSer) {                                         //эта строчка кода следит, чтобы все поступившие заявки были обработаны.
                if (nMsgSer >= nMsgMax) {
                    this.stop = true;
                }
            }
        }
        return this.stop;
    }

    //Метод обработки события прибытия заявки либо в очередь, либо в прибор
    public void onNextArrive (double lambda, double mu){
        if (!this.busy){
            this.busy = true;
            this.tBecameBusy = c;
            nextEv.settServeEnd(c + df_exp(mu));
            numMsg = numMsg + 1;
        }
        else{
            iQlen = iQlen + msgQueue.size() * (c - tChangedQueue);
            tChangedQueue = c;
            msgQueue.add(c);


        }
        nMsgArr += 1;
        nextEv.settArrive(c + df_exp(lambda));

    }

    //Метод обработки события обслуживания заявки
    public void onServeEnd(double mu){
        if (msgQueue.size() == 0){
            iBusy = iBusy + (c - tBecameBusy);
            busy = false;
        }
        else{
            iQlen = iQlen + msgQueue.size() * (c - tChangedQueue);
            tChangedQueue = c;
            sDelay = sDelay + (c - msgQueue.get(0));
            msgQueue.remove(0);
            nextEv.settServeEnd(c + df_exp(mu));
            numMsg = numMsg + 1;
        }
        nMsgSer += 1;
    }


}
