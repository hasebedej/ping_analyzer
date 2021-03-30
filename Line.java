public class Line {
    public String route;
    public int occurances;
    public int errorPacket;
    public double timeAvg;
    Line(String route, int occurances, double timeAvg, int errorPacket){
        this.route=route;
        this.occurances = occurances;
        this.errorPacket = errorPacket;
        this.timeAvg =timeAvg;
    }
}
