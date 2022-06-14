import java.text.DecimalFormat;
/**
 *  
 *  -En esta 'clase' se desarrollan todas las funciones para el desarrollo del algoritmo PSO basados en dicha presentacion.
 *  -El algoritmo se desarrolla en los siguientes pasos:
 *      1.- Inicialización
 *      2.- Evaluar la función de ajuste para cada particula
 *      3.- Para cada particula, calcular el valor de velocidad (v) y posición (x)
 *      4.- Evaluar la función de ajuste para encontrar el actual 'global best'
 *  -Función utilizada para la minimización: f(x) = 10*(x1 - 1)^2 + 20*(x2-2)^2 + 30*(x3-3)^2
 * 

 */
class Methods{
    DecimalFormat df = new DecimalFormat("#.###");
    DataSet ds = new DataSet();

    //Display array passed by parameter in console
    void showArray(double []array){
        for(int i=0;i<array.length;i++){
            System.out.println(df.format(array[i]));
        }
    }
    //Display matrix passed by parameter in console
    void showMatrix(double [][]matrix){
        for(int row=0;row<matrix.length;row++){
            for(int column=0;column<matrix[0].length;column++){
                System.out.print(df.format(matrix[row][column])+"\t");
            }
            System.out.println("");
        }
    }
    /*  ________________________________________________________________________________________________________________________
        PASO 1 - INICIALIZACIÓN
         * INICIALIZAR PARÁMETROS
             -Número de variables (x1, x2, x3 ... x^n-1)
             -Población = Cantidad de partículas (xN_i, xN_i+1, xN_i+1 ... xN_i-1)
             -Constantes (factor acelerador)
             -Valor de inercia (Valor aleatorio entre [0,1])
             -Máxima iteración (preferiblemente: iteración>1)
         ** INICIALIZAR POBLACIÓN (x_i) aleatoriamente para cada partícula
             -Inicializar velocidad (v_i) aleatoriamente para cada partícula
             -Inicializar posición
                 -Establecer mejor marca personal actual = Inicializar posición
     */

    // *INICIALIZAR PARÁMETROS
    //-Se declaró el número de variables y la población en la clase 'DataSet'.
            double c1=2, c2=2;          //-valores constantes (factor de aceleración)
            double w = Math.random();   //-Valor de inercia entre[0,1]
            int iterations = 100;       //Maximo de iteraciones

    // **INICIALIZAR POBLACION
            double [][] initPopulation(){
                    double[][] x_values = new double[ds.particulas][ds.x];
                   //DECLARACION DE POBLACION
                    double [][] dataSet = {{41.9,  43.4,  43.9,  44.5,  47.3,  47.5,  47.9,  50.2,  52.8,  53.2,  56.7,  57.0,  63.5,  65.3,  71.1,  77.0,  77.8 },  //x1
                                           {29.1,  29.3,  29.5,  29.7,  29.9,  30.3,  30.5,  30.7,  30.8,  30.9,  31.5,  31.7,  31.9,  32.0,  32.1,  32.5,  32.9 },  //x2
                                           {251.3, 251.3, 248.3, 267.5, 273.0, 276.5, 270.3, 274.9, 285.0, 290.0, 297.0, 302.5, 304.5, 309.3, 321.7, 330.7, 349.0}}; //x3

                   //Declaración "for" para transponer la matriz de ${x_values} y guardarla en la matriz ${matrix_aux}
                    for(int row=0;row<dataSet.length;row++){
                        for(int column=0;column<dataSet[0].length;column++){
                        x_values[column][row] = dataSet[row][column];
                        }
                    }
                    return x_values; //Población inicializada
                }
                //-Inicializar velocidad
                double [][] initVelocity(){
                    double [][]velocity = new double[ds.particulas][ds.x];
                    for(int row=0;row<velocity.length;row++){
                        for(int column=0;column<velocity[0].length;column++){
                            //Valor aleatorio entre [0,1]
                            double r = Math.random();
                                velocity[row][column]=r;
                        }
                    }
                    return velocity; //Velocidad inicializada
                }
    //-Inicializar posición (x_i) <--- Posición actual = Posición anterior (población en primera iteración) + velocidad
                double [][] initPosition(double [][]population, double [][]velocity){
                    for(int row=0;row<population.length;row++){
                        for(int column=0;column<population[0].length;column++){
                            population[row][column]+=velocity[row][column];
                        }
                    }
                    return population; //Posicion inicializada
                }
    //Inicialice "Personal Best" con los valores de la primera posición
                double [][] initPBest(double [][]position){
                    double [][]pBest = new double [ds.particulas][ds.x];
                    for(int row=0;row<position.length;row++){
                        for(int column=0;column<position[0].length;column++){
                            pBest[row][column]=position[row][column];
                        }
                    }
                    return pBest;
                }
    /*
     * FIN DEL PASO 1 - INICIACIÓN
     */

     /* ________________________________________________________________________________________________________________________
         PASO 2 - EVALUAR LA CONDICIÓN FÍSICA
         * CALCULAR EL VALOR DE APTITUD PARA CADA PARTICULA
             -Función objetivo utilizada para la minimización: f(x) = 10*(x1 - 1)^2 + 20*(x2-2)^2 + 30*(x3-3)^2
         ** ELIJA LA PARTÍCULA CON EL MEJOR VALOR DE CONDICIÓN FÍSICA COMO 'Global Best'
             -Establezca 'Mejor global' en x1, x2, x3 ... xN-1
     */

    //* CALCULAR EL VALOR DE APTITUD PARA CADA PARTICULA
    //-Función objetivo utilizada para la minimización: f(x) = 10*(x1 - 1)^2 + 20*(x2-2)^2 + 30*(x3-3)^2

                double[] calculateFitnessFuntion(double [][]x_values){
                    double fitnessValue[]=new double[ds.particulas];
                        for(int row = 0; row<fitnessValue.length;row++){
                            fitnessValue[row]= 10*(Math.pow(x_values[row][0] - 1, 2)) + (20*(Math.pow(x_values[row][1] - 2, 2))) + (30*(Math.pow(x_values[row][2]-3,2)));
                        }
                 return fitnessValue;    
                }
    //** ELEGIR LA PARTICULA CON MEJOR VALOR DE APTITUD COMO 'Global Best'
            double fitnessValue(double []fitnessValue){
                    double bestFitnessValue=fitnessValue[0];
                    for(int i = 1; i<fitnessValue.length;i++){
                        if(fitnessValue[i]<bestFitnessValue){
                            bestFitnessValue=fitnessValue[i];
                        }
                    }
                    return bestFitnessValue;
                }
    //-Establecer 'Mejor global' en x1, x2, x3 ... xN-1
                double[] calculateGlobalBest(double []fitnessFuntion, double fitnessValue, double [][]x_positions){
                    int position=0; 
                    double []gBest= new double[ds.x];
                        for(int i=0;i<fitnessFuntion.length;i++){
                            if(fitnessValue==fitnessFuntion[i]){
                                position=i;
                            }
                        }
                        for(int row=0;row<gBest.length;row++){
                            gBest[row]=x_positions[position][row];
                        }
                    return gBest;
                }
    /*
     * FIN DEL PASO 2 - EVALUAR LA CONDICIÓN FÍSICA
     */

     /* ____________________________________________________________________________________________________________________
         PASO 3 - PARA CADA PARTICULA CALCULE LA VELOCIDAD Y LA POSICION
         * CALCULAR LA VELOCIDAD POR: Vi^t+1 = W*V^t + C1*R1(pBest - Xi^t) + C2R2(gBest - Xi^t)
         ** CALCULAR LAS POSICIONES DE LAS PARTÍCULAS POR: X^t+1 = X^t + V^t
     */

    //* ACTUALIZAR VELOCIDAD POR: Vi^t+1 = W*V^t + C1*R1(pBest - Xi^t) + C2R2(gBest - Xi^t)
            double[][] updateVelocity(double [][]velocity, double [][]x_position, double [][]pBest, double []gBest){
                double r=Math.random();
                    for(int row = 0; row<velocity.length;row++){
                        for(int column=0;column<velocity[0].length;column++){
                            velocity[row][column] = (w*velocity[row][column]) + 
                                                    ((c1*r)*((pBest[row][column]) - (x_position[row][column]))) + 
                                                    ((c2*r)*((gBest[column])      - (x_position[row][column])));
                        }
                    }
                return velocity;
            }
    //** CALCULAR LAS POSICIONES DE LAS PARTÍCULAS POR: X^t+1 = X^t + V^t
            double[][] updatePosition(double[][]velocity,double[][]x_values){
                for(int row = 0; row<velocity.length;row++){
                        for(int column=0;column<velocity[0].length;column++){
                            x_values[row][column]+=velocity[row][column];
                        }
                }
                return x_values;
            }

            /*
     * FIN DEL PASO 3 - PARA CADA PARTICULA CALCULAR VELOCIDAD Y POSICION
     /* ______________________________________________________________________________________________________________________
         PASO 4 - ACTUALIZAR 'GLOBAL BEST' Y 'PERSONAL BEST'
         * COMPARE EL VALOR MÍNIMO DE APTITUD ACTUAL CON EL NUEVO VALOR DE APTITUD MÍNIMA Y ELIJA EL MEJOR
         ** ACTUALIZAR MEJOR GLOBAL
         *** ACTUALIZAR LO MEJOR PERSONAL
      */

    //* COMPARE EL VALOR MÍNIMO DE APTITUD ACTUAL CON EL NUEVO VALOR DE APTITUD MÍNIMA Y ELIJA EL MEJOR
            double updateFitnessValue(double minFitnessValue, double[] fitnessFuntion){
                for(int i=0;i<fitnessFuntion.length;i++){
                    if(fitnessFuntion[i] < minFitnessValue){
                        minFitnessValue=fitnessFuntion[i];
                    }
                }
                return minFitnessValue;
            }
        //***ACTUALIZAMOS EL PERSONAL BEST
            double[][] updatePBest(double [][]x_position, double[][]pBest, double []fitnessFuntion, double []currentFitnessFuntion){
                for(int row=0;row<fitnessFuntion.length;row++){
                        if(currentFitnessFuntion[row]<fitnessFuntion[row]){
                            for(int column=0;column<pBest[0].length;column++){
                                pBest[row][column]=x_position[row][column];
                            }
                        }
                }
                return pBest;
            }
    /*
     * FIN DEL PASO 4 - ACTUALIZAR 'GLOBAL BEST' Y 'PERSONAL BEST'
     */
}