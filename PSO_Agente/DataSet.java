/**
 *  
 */
public class DataSet {
    int particulas = 17;  //CANTIDAD DE PARTICULAS(population) (in xN_i, xN_i+1, xN_i+1 ... xN_i-1)
    int x=3;  // Cantidad de valores o X en el arreglo(x1, x2, x3 ... Xn-1)
    
    private double [][]x_valores=new double [particulas][x];
    private double [][] velocidad =new double [particulas][x];
    
    private double []fitnessFuntion = new double[particulas];   //Array to save the value result of the funtion using particles in x1, x2, x3 ... Xn-1
    private double [][]pBest = new double[particulas][x]; //Matrix to save 'Personal Best Values' values
    private double []gBest = new double[x];
    private double minFitnessValue;

    public double getMinFitnessValue() {
        return minFitnessValue;
    }

    public void setMinFitnessValue(double minFitnessValue) {
        this.minFitnessValue = minFitnessValue;
    }
    
    public double[] getgBest() {
        return gBest;
    }

    public void setgBest(double[] gBest) {
        this.gBest = gBest;
    }
    

    public double[][] getpBest() {
        return pBest;
    }

    public void setpBest(double[][] pBest) {
        this.pBest = pBest;
    }
    
    public double[][] getX_values() {
        return x_valores;
    }

    public void setX_values(double[][] x_values) {
        this.x_valores = x_values;
    }

    public double[][] getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(double[][] velocidad) {
        this.velocidad = velocidad;
    }

    public double[] getFitnessFuntion() {
        return fitnessFuntion;
    }

    public void setFitnessFuntion(double[] fitnessFuntion) {
        this.fitnessFuntion = fitnessFuntion;
    }
}
