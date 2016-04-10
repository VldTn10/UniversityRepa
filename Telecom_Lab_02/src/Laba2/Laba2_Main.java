package Laba2;
import MM1_advanced.*;
import MM1_des.*;

import java.util.Scanner;


public class Laba2_Main {


    public static void main(String[] args){
        double lambda = 7.0D;
        double mu = 8.0D;
        double clockMax = 0.0D;
        int nMsgMax = 0;
        String foo = "t";
        Scanner s = new Scanner(System.in);

        System.out.print("Выберите тип ограничения, t - по времени, m - по количеству сообщений: ");
        String a = s.nextLine();

        if(a.equals(foo)){
            System.out.print("Введите ограничение по времени: ");
            clockMax = s.nextDouble();                                      //ограничение на максимальное количество времени
        }else  {
            System.out.print("Введите ограничение по сообщениям: ");
            nMsgMax = s.nextInt();                                          //ограничение на максимальное количество заявок
        }

        System.out.println();
        MM1_advanced alg1 = new MM1_advanced(clockMax, nMsgMax, a);
        alg1.mm1_adv(lambda, mu);
        MM1_des alg2 = new MM1_des(clockMax, nMsgMax, a);
        alg2.mm1_des(lambda, mu);

    }
}
