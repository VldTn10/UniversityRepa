package MM1_advanced;


public class NextEvent {
    private double tArrive;     //время прихода следующей заявки
    private double tServeEnd;   //время завершения обслуживания

    public double gettArrive()
    {
        return tArrive;
    }

    public void settArrive(double tArrive) {
        this.tArrive = tArrive;
    }

    public double gettServeEnd() {
        return tServeEnd;
    }

    public void settServeEnd(double tServeEnd) {
        this.tServeEnd = tServeEnd;
    }
}
