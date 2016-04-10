
public class MarcovRrocess {
    public static void main(String[] args) {
        //Первый блок переменных, пока оставим
        int M = 0, U = 500;
        double p = 0.8, q = 1-p;
        double sigm = 0;

        //Второй блок переменных
        double T = 0.02, Tslot = 0.02; // T - Среднее время поступления утройств, Tslot - длина слота (мс)

        sigm = 1 - Math.pow(2.71828182846, -(1/T)*Tslot); //Вероятность активации уст-ва

        //M - какие-то каналы
        System.out.println("M1 ----------------------------------------------------");
        for(M=1; M<=10; M++){
            //i - устройства задержек
            for(int i=0; i<U; i++){
                System.out.printf("%11.10f", S(0.5, 1, sigm, M, U, i));
                System.out.println();
            }
            M = M+1;
            System.out.println("M" + M + " ----------------------------------------------------");
            M = M-1;
        }
    }

    //Ожидаемое количество устройств успешно передавших данные среди i
    protected static double S(double p, double q, double sigm, int M, double U, int i){
        return Math.pow((1-(q*sigm)/M), U-i) * i * p * Math.pow((1-p/M), i-1) + (U-i) * q * sigm * Math.pow((1-(q*sigm)/M), U-i-1) * Math.pow((1 - p/M), i);
    }
}
