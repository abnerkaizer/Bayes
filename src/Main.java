import java.util.List;
import java.util.ArrayList;
public class Main {
    private static double[][][] classe1 = new double[288][2][];
    private static double[][][] classe2 = new double [49][2][];
    private static double[][][] classe3 = new double[288][2][];
    private static List<double[][][]> classes = new ArrayList<>(3);
    private static List<ArrayList<Double>> medias = new ArrayList<>(3);
    private static List<ArrayList<Double>> desvios = new ArrayList<>(3);
    static double pc1 = 288/625;
    static double pc2 = 49/625;
    static double pc3 = 288/625;
    static double pc[] = new double[3];
    public static void main(String[] args) {
        setBases();
        pc[0] = pc1;
        pc[1] = pc2;
        pc[2] = pc3;
        double startTime = System.currentTimeMillis();
        classificadorBayes();
        double endTime = System.currentTimeMillis();
        double tempo = (endTime-startTime)/1000;
        System.out.printf("Tempo: %.2f s\n",tempo);
        //System.out.println(Math.PI);
    }
    private static void classificadorBayes(){
        double classe [][][];


        for (int j = 0; j < classes.size(); j++) {
            classe = classes.get(j);
            ArrayList<Double> media = new ArrayList<>(4);
            ArrayList<Double> desvio = new ArrayList<>(4);

            //Treino
            double entradas [];
            for (int i = 0; i < 4; i++) {
                double soma = 0;
                for (int k = 0; k <(int) classe.length*0.7; k++) {
                    entradas = classe[k][0];
                    soma += entradas[i];
                }
                media.add(soma/(int) classe.length*0.7);
            }
            medias.add(media);
            for (int i = 0; i < 4; i++) {
                double soma = 0;
                for (int k = 0; k < (int) classe.length*0.7; k++) {
                    entradas = classe[k][0];
                    soma += Math.pow(entradas[i]-media.get(i),2);
                }
                desvio.add(Math.sqrt(soma/(int) classe.length*0.7));
            }
            desvios.add(desvio);
        }

        //imprimeMatriz(medias);
        //imprimeMatriz(desvios);
        //Teste

        int erroClTeste = 0;
        int erroClTreino=0;
        for (int i = 0; i < 3; i++) {//Classes
            classe = classes.get(i);

//            int count = 0;
            for (int k = 0 ; k < (int) (classe.length*0.7); k++) {//amostras
                double x[] = classe[k][0];
                int cl = classificar(x,medias,desvios);
                int y=0;
                if (classe[k][1][0]==1&&classe[k][1][1]==0&&classe[k][1][2]==0){
                    y=1;
                } else if (classe[k][1][0]==0&&classe[k][1][1]==1&&classe[k][1][2]==0) {
                    y=2;
                }else{
                    y=3;
                }
                if (cl!= y){
                    erroClTreino++;
                }
//                count++;
            }
//            System.out.println(count);
//            count = 0;
            for (int k = (int) (classe.length*0.7); k < classe.length; k++) {//amostras
                double x[] = classe[k][0];
                int cl = classificar(x,medias,desvios);
                int y=0;
                if (classe[k][1][0]==1&&classe[k][1][1]==0&&classe[k][1][2]==0){
                    y=1;
                } else if (classe[k][1][0]==0&&classe[k][1][1]==1&&classe[k][1][2]==0) {
                    y=2;
                }else{
                    y=3;
                }
                if (cl!= y){
                    erroClTeste++;
                }
//                count++;
            }
//            System.out.println(count);
        }
        System.out.println("Erro classificação treino: "+erroClTreino+"/436"+" Erro classificação teste: "+erroClTeste+"/189");

    }
    private static int classificar(double x[],List<ArrayList<Double>> medias,List<ArrayList<Double>> desvios){
        int c = 1;
        for (int i = 2; i < pc.length; i++) {
            if (p(x,medias.get(i),desvios.get(i))*pc[i]>p(x,medias.get(c),desvios.get(c))*pc[c]){
                c = i;
            }
        }
        return c;
    }
    private static double p(double[] x,ArrayList<Double> medias,ArrayList<Double> desvios){
        double p=1;
        for (int i = 0; i < x.length; i++) {
            p*= g(x[i],medias.get(i),desvios.get(i));
        }
        return p;
    }
    private static double g(double x,double media,double desvio){
        double r = 1/Math.sqrt(2*Math.PI*desvio);
        r *= Math.exp(-(Math.pow(x-media,2))/(2*Math.pow(desvio,2)));
        return r;
    }
    private static void imprimeMatriz(List<ArrayList<Double>> matriz){
        //System.out.println(matriz.get(0).size());

        for (int i = 0; i < matriz.size(); i++) {
            System.out.println("Classe "+(i+1)+":");
            for (int j = 0; j < matriz.get(i).size(); j++) {
                System.out.print(matriz.get(i).get(j)+" ");
            }
            System.out.println();
            System.out.println();
        }
    }
    private static void setBases(){
        for (int i = 0; i < 288; i++) {
            classe1[i][0] = new double[4];
            classe1[i][1] = new double[3];
            classe3[i][0] = new double[4];
            classe3[i][1] = new double[3];
        }
        for (int i = 0; i < 49; i++) {
            classe2[i][0] = new double[4];
            classe2[i][1] = new double[3];
        }
        String lines[] = input.split("\n");
        int b1=0,b2=0,b3 =0;
        for (int i = 0; i < 625; i++) {
            String c[] = lines[i].split(",");
            if (c[0].equals("L")) {
                classe1[b1][1][0] = 1;
                classe1[b1][1][1] = 0;
                classe1[b1][1][2] = 0;
                for (int j = 1; j < c.length; j++) {
                    classe1[b1][0][j - 1] = Double.parseDouble(c[j]);
                }
                b1++;
            } else if (c[0].equals("B")) {
                classe2[b2][1][0] = 0;
                classe2[b2][1][1] = 1;
                classe2[b2][1][2] = 0;
                for (int j = 1; j < c.length; j++) {
                    classe2[b2][0][j - 1] = Double.parseDouble(c[j]);
                }
                b2++;
            } else if (c[0].equals("R")) {
                classe3[b3][1][0] = 0;
                classe3[b3][1][1] = 0;
                classe3[b3][1][2] = 1;
                for (int j = 1; j < c.length; j++) {
                    classe3[b3][0][j - 1] = Double.parseDouble(c[j]);
                }
                b3++;
            }
        }
        classes.add(classe1);
        classes.add(classe2);
        classes.add(classe3);
    }
    static String input = "B,1,1,1,1\n" +
            "R,1,1,1,2\n" +
            "R,1,1,1,3\n" +
            "R,1,1,1,4\n" +
            "R,1,1,1,5\n" +
            "R,1,1,2,1\n" +
            "R,1,1,2,2\n" +
            "R,1,1,2,3\n" +
            "R,1,1,2,4\n" +
            "R,1,1,2,5\n" +
            "R,1,1,3,1\n" +
            "R,1,1,3,2\n" +
            "R,1,1,3,3\n" +
            "R,1,1,3,4\n" +
            "R,1,1,3,5\n" +
            "R,1,1,4,1\n" +
            "R,1,1,4,2\n" +
            "R,1,1,4,3\n" +
            "R,1,1,4,4\n" +
            "R,1,1,4,5\n" +
            "R,1,1,5,1\n" +
            "R,1,1,5,2\n" +
            "R,1,1,5,3\n" +
            "R,1,1,5,4\n" +
            "R,1,1,5,5\n" +
            "L,1,2,1,1\n" +
            "B,1,2,1,2\n" +
            "R,1,2,1,3\n" +
            "R,1,2,1,4\n" +
            "R,1,2,1,5\n" +
            "B,1,2,2,1\n" +
            "R,1,2,2,2\n" +
            "R,1,2,2,3\n" +
            "R,1,2,2,4\n" +
            "R,1,2,2,5\n" +
            "R,1,2,3,1\n" +
            "R,1,2,3,2\n" +
            "R,1,2,3,3\n" +
            "R,1,2,3,4\n" +
            "R,1,2,3,5\n" +
            "R,1,2,4,1\n" +
            "R,1,2,4,2\n" +
            "R,1,2,4,3\n" +
            "R,1,2,4,4\n" +
            "R,1,2,4,5\n" +
            "R,1,2,5,1\n" +
            "R,1,2,5,2\n" +
            "R,1,2,5,3\n" +
            "R,1,2,5,4\n" +
            "R,1,2,5,5\n" +
            "L,1,3,1,1\n" +
            "L,1,3,1,2\n" +
            "B,1,3,1,3\n" +
            "R,1,3,1,4\n" +
            "R,1,3,1,5\n" +
            "L,1,3,2,1\n" +
            "R,1,3,2,2\n" +
            "R,1,3,2,3\n" +
            "R,1,3,2,4\n" +
            "R,1,3,2,5\n" +
            "B,1,3,3,1\n" +
            "R,1,3,3,2\n" +
            "R,1,3,3,3\n" +
            "R,1,3,3,4\n" +
            "R,1,3,3,5\n" +
            "R,1,3,4,1\n" +
            "R,1,3,4,2\n" +
            "R,1,3,4,3\n" +
            "R,1,3,4,4\n" +
            "R,1,3,4,5\n" +
            "R,1,3,5,1\n" +
            "R,1,3,5,2\n" +
            "R,1,3,5,3\n" +
            "R,1,3,5,4\n" +
            "R,1,3,5,5\n" +
            "L,1,4,1,1\n" +
            "L,1,4,1,2\n" +
            "L,1,4,1,3\n" +
            "B,1,4,1,4\n" +
            "R,1,4,1,5\n" +
            "L,1,4,2,1\n" +
            "B,1,4,2,2\n" +
            "R,1,4,2,3\n" +
            "R,1,4,2,4\n" +
            "R,1,4,2,5\n" +
            "L,1,4,3,1\n" +
            "R,1,4,3,2\n" +
            "R,1,4,3,3\n" +
            "R,1,4,3,4\n" +
            "R,1,4,3,5\n" +
            "B,1,4,4,1\n" +
            "R,1,4,4,2\n" +
            "R,1,4,4,3\n" +
            "R,1,4,4,4\n" +
            "R,1,4,4,5\n" +
            "R,1,4,5,1\n" +
            "R,1,4,5,2\n" +
            "R,1,4,5,3\n" +
            "R,1,4,5,4\n" +
            "R,1,4,5,5\n" +
            "L,1,5,1,1\n" +
            "L,1,5,1,2\n" +
            "L,1,5,1,3\n" +
            "L,1,5,1,4\n" +
            "B,1,5,1,5\n" +
            "L,1,5,2,1\n" +
            "L,1,5,2,2\n" +
            "R,1,5,2,3\n" +
            "R,1,5,2,4\n" +
            "R,1,5,2,5\n" +
            "L,1,5,3,1\n" +
            "R,1,5,3,2\n" +
            "R,1,5,3,3\n" +
            "R,1,5,3,4\n" +
            "R,1,5,3,5\n" +
            "L,1,5,4,1\n" +
            "R,1,5,4,2\n" +
            "R,1,5,4,3\n" +
            "R,1,5,4,4\n" +
            "R,1,5,4,5\n" +
            "B,1,5,5,1\n" +
            "R,1,5,5,2\n" +
            "R,1,5,5,3\n" +
            "R,1,5,5,4\n" +
            "R,1,5,5,5\n" +
            "L,2,1,1,1\n" +
            "B,2,1,1,2\n" +
            "R,2,1,1,3\n" +
            "R,2,1,1,4\n" +
            "R,2,1,1,5\n" +
            "B,2,1,2,1\n" +
            "R,2,1,2,2\n" +
            "R,2,1,2,3\n" +
            "R,2,1,2,4\n" +
            "R,2,1,2,5\n" +
            "R,2,1,3,1\n" +
            "R,2,1,3,2\n" +
            "R,2,1,3,3\n" +
            "R,2,1,3,4\n" +
            "R,2,1,3,5\n" +
            "R,2,1,4,1\n" +
            "R,2,1,4,2\n" +
            "R,2,1,4,3\n" +
            "R,2,1,4,4\n" +
            "R,2,1,4,5\n" +
            "R,2,1,5,1\n" +
            "R,2,1,5,2\n" +
            "R,2,1,5,3\n" +
            "R,2,1,5,4\n" +
            "R,2,1,5,5\n" +
            "L,2,2,1,1\n" +
            "L,2,2,1,2\n" +
            "L,2,2,1,3\n" +
            "B,2,2,1,4\n" +
            "R,2,2,1,5\n" +
            "L,2,2,2,1\n" +
            "B,2,2,2,2\n" +
            "R,2,2,2,3\n" +
            "R,2,2,2,4\n" +
            "R,2,2,2,5\n" +
            "L,2,2,3,1\n" +
            "R,2,2,3,2\n" +
            "R,2,2,3,3\n" +
            "R,2,2,3,4\n" +
            "R,2,2,3,5\n" +
            "B,2,2,4,1\n" +
            "R,2,2,4,2\n" +
            "R,2,2,4,3\n" +
            "R,2,2,4,4\n" +
            "R,2,2,4,5\n" +
            "R,2,2,5,1\n" +
            "R,2,2,5,2\n" +
            "R,2,2,5,3\n" +
            "R,2,2,5,4\n" +
            "R,2,2,5,5\n" +
            "L,2,3,1,1\n" +
            "L,2,3,1,2\n" +
            "L,2,3,1,3\n" +
            "L,2,3,1,4\n" +
            "L,2,3,1,5\n" +
            "L,2,3,2,1\n" +
            "L,2,3,2,2\n" +
            "B,2,3,2,3\n" +
            "R,2,3,2,4\n" +
            "R,2,3,2,5\n" +
            "L,2,3,3,1\n" +
            "B,2,3,3,2\n" +
            "R,2,3,3,3\n" +
            "R,2,3,3,4\n" +
            "R,2,3,3,5\n" +
            "L,2,3,4,1\n" +
            "R,2,3,4,2\n" +
            "R,2,3,4,3\n" +
            "R,2,3,4,4\n" +
            "R,2,3,4,5\n" +
            "L,2,3,5,1\n" +
            "R,2,3,5,2\n" +
            "R,2,3,5,3\n" +
            "R,2,3,5,4\n" +
            "R,2,3,5,5\n" +
            "L,2,4,1,1\n" +
            "L,2,4,1,2\n" +
            "L,2,4,1,3\n" +
            "L,2,4,1,4\n" +
            "L,2,4,1,5\n" +
            "L,2,4,2,1\n" +
            "L,2,4,2,2\n" +
            "L,2,4,2,3\n" +
            "B,2,4,2,4\n" +
            "R,2,4,2,5\n" +
            "L,2,4,3,1\n" +
            "L,2,4,3,2\n" +
            "R,2,4,3,3\n" +
            "R,2,4,3,4\n" +
            "R,2,4,3,5\n" +
            "L,2,4,4,1\n" +
            "B,2,4,4,2\n" +
            "R,2,4,4,3\n" +
            "R,2,4,4,4\n" +
            "R,2,4,4,5\n" +
            "L,2,4,5,1\n" +
            "R,2,4,5,2\n" +
            "R,2,4,5,3\n" +
            "R,2,4,5,4\n" +
            "R,2,4,5,5\n" +
            "L,2,5,1,1\n" +
            "L,2,5,1,2\n" +
            "L,2,5,1,3\n" +
            "L,2,5,1,4\n" +
            "L,2,5,1,5\n" +
            "L,2,5,2,1\n" +
            "L,2,5,2,2\n" +
            "L,2,5,2,3\n" +
            "L,2,5,2,4\n" +
            "B,2,5,2,5\n" +
            "L,2,5,3,1\n" +
            "L,2,5,3,2\n" +
            "L,2,5,3,3\n" +
            "R,2,5,3,4\n" +
            "R,2,5,3,5\n" +
            "L,2,5,4,1\n" +
            "L,2,5,4,2\n" +
            "R,2,5,4,3\n" +
            "R,2,5,4,4\n" +
            "R,2,5,4,5\n" +
            "L,2,5,5,1\n" +
            "B,2,5,5,2\n" +
            "R,2,5,5,3\n" +
            "R,2,5,5,4\n" +
            "R,2,5,5,5\n" +
            "L,3,1,1,1\n" +
            "L,3,1,1,2\n" +
            "B,3,1,1,3\n" +
            "R,3,1,1,4\n" +
            "R,3,1,1,5\n" +
            "L,3,1,2,1\n" +
            "R,3,1,2,2\n" +
            "R,3,1,2,3\n" +
            "R,3,1,2,4\n" +
            "R,3,1,2,5\n" +
            "B,3,1,3,1\n" +
            "R,3,1,3,2\n" +
            "R,3,1,3,3\n" +
            "R,3,1,3,4\n" +
            "R,3,1,3,5\n" +
            "R,3,1,4,1\n" +
            "R,3,1,4,2\n" +
            "R,3,1,4,3\n" +
            "R,3,1,4,4\n" +
            "R,3,1,4,5\n" +
            "R,3,1,5,1\n" +
            "R,3,1,5,2\n" +
            "R,3,1,5,3\n" +
            "R,3,1,5,4\n" +
            "R,3,1,5,5\n" +
            "L,3,2,1,1\n" +
            "L,3,2,1,2\n" +
            "L,3,2,1,3\n" +
            "L,3,2,1,4\n" +
            "L,3,2,1,5\n" +
            "L,3,2,2,1\n" +
            "L,3,2,2,2\n" +
            "B,3,2,2,3\n" +
            "R,3,2,2,4\n" +
            "R,3,2,2,5\n" +
            "L,3,2,3,1\n" +
            "B,3,2,3,2\n" +
            "R,3,2,3,3\n" +
            "R,3,2,3,4\n" +
            "R,3,2,3,5\n" +
            "L,3,2,4,1\n" +
            "R,3,2,4,2\n" +
            "R,3,2,4,3\n" +
            "R,3,2,4,4\n" +
            "R,3,2,4,5\n" +
            "L,3,2,5,1\n" +
            "R,3,2,5,2\n" +
            "R,3,2,5,3\n" +
            "R,3,2,5,4\n" +
            "R,3,2,5,5\n" +
            "L,3,3,1,1\n" +
            "L,3,3,1,2\n" +
            "L,3,3,1,3\n" +
            "L,3,3,1,4\n" +
            "L,3,3,1,5\n" +
            "L,3,3,2,1\n" +
            "L,3,3,2,2\n" +
            "L,3,3,2,3\n" +
            "L,3,3,2,4\n" +
            "R,3,3,2,5\n" +
            "L,3,3,3,1\n" +
            "L,3,3,3,2\n" +
            "B,3,3,3,3\n" +
            "R,3,3,3,4\n" +
            "R,3,3,3,5\n" +
            "L,3,3,4,1\n" +
            "L,3,3,4,2\n" +
            "R,3,3,4,3\n" +
            "R,3,3,4,4\n" +
            "R,3,3,4,5\n" +
            "L,3,3,5,1\n" +
            "R,3,3,5,2\n" +
            "R,3,3,5,3\n" +
            "R,3,3,5,4\n" +
            "R,3,3,5,5\n" +
            "L,3,4,1,1\n" +
            "L,3,4,1,2\n" +
            "L,3,4,1,3\n" +
            "L,3,4,1,4\n" +
            "L,3,4,1,5\n" +
            "L,3,4,2,1\n" +
            "L,3,4,2,2\n" +
            "L,3,4,2,3\n" +
            "L,3,4,2,4\n" +
            "L,3,4,2,5\n" +
            "L,3,4,3,1\n" +
            "L,3,4,3,2\n" +
            "L,3,4,3,3\n" +
            "B,3,4,3,4\n" +
            "R,3,4,3,5\n" +
            "L,3,4,4,1\n" +
            "L,3,4,4,2\n" +
            "B,3,4,4,3\n" +
            "R,3,4,4,4\n" +
            "R,3,4,4,5\n" +
            "L,3,4,5,1\n" +
            "L,3,4,5,2\n" +
            "R,3,4,5,3\n" +
            "R,3,4,5,4\n" +
            "R,3,4,5,5\n" +
            "L,3,5,1,1\n" +
            "L,3,5,1,2\n" +
            "L,3,5,1,3\n" +
            "L,3,5,1,4\n" +
            "L,3,5,1,5\n" +
            "L,3,5,2,1\n" +
            "L,3,5,2,2\n" +
            "L,3,5,2,3\n" +
            "L,3,5,2,4\n" +
            "L,3,5,2,5\n" +
            "L,3,5,3,1\n" +
            "L,3,5,3,2\n" +
            "L,3,5,3,3\n" +
            "L,3,5,3,4\n" +
            "B,3,5,3,5\n" +
            "L,3,5,4,1\n" +
            "L,3,5,4,2\n" +
            "L,3,5,4,3\n" +
            "R,3,5,4,4\n" +
            "R,3,5,4,5\n" +
            "L,3,5,5,1\n" +
            "L,3,5,5,2\n" +
            "B,3,5,5,3\n" +
            "R,3,5,5,4\n" +
            "R,3,5,5,5\n" +
            "L,4,1,1,1\n" +
            "L,4,1,1,2\n" +
            "L,4,1,1,3\n" +
            "B,4,1,1,4\n" +
            "R,4,1,1,5\n" +
            "L,4,1,2,1\n" +
            "B,4,1,2,2\n" +
            "R,4,1,2,3\n" +
            "R,4,1,2,4\n" +
            "R,4,1,2,5\n" +
            "L,4,1,3,1\n" +
            "R,4,1,3,2\n" +
            "R,4,1,3,3\n" +
            "R,4,1,3,4\n" +
            "R,4,1,3,5\n" +
            "B,4,1,4,1\n" +
            "R,4,1,4,2\n" +
            "R,4,1,4,3\n" +
            "R,4,1,4,4\n" +
            "R,4,1,4,5\n" +
            "R,4,1,5,1\n" +
            "R,4,1,5,2\n" +
            "R,4,1,5,3\n" +
            "R,4,1,5,4\n" +
            "R,4,1,5,5\n" +
            "L,4,2,1,1\n" +
            "L,4,2,1,2\n" +
            "L,4,2,1,3\n" +
            "L,4,2,1,4\n" +
            "L,4,2,1,5\n" +
            "L,4,2,2,1\n" +
            "L,4,2,2,2\n" +
            "L,4,2,2,3\n" +
            "B,4,2,2,4\n" +
            "R,4,2,2,5\n" +
            "L,4,2,3,1\n" +
            "L,4,2,3,2\n" +
            "R,4,2,3,3\n" +
            "R,4,2,3,4\n" +
            "R,4,2,3,5\n" +
            "L,4,2,4,1\n" +
            "B,4,2,4,2\n" +
            "R,4,2,4,3\n" +
            "R,4,2,4,4\n" +
            "R,4,2,4,5\n" +
            "L,4,2,5,1\n" +
            "R,4,2,5,2\n" +
            "R,4,2,5,3\n" +
            "R,4,2,5,4\n" +
            "R,4,2,5,5\n" +
            "L,4,3,1,1\n" +
            "L,4,3,1,2\n" +
            "L,4,3,1,3\n" +
            "L,4,3,1,4\n" +
            "L,4,3,1,5\n" +
            "L,4,3,2,1\n" +
            "L,4,3,2,2\n" +
            "L,4,3,2,3\n" +
            "L,4,3,2,4\n" +
            "L,4,3,2,5\n" +
            "L,4,3,3,1\n" +
            "L,4,3,3,2\n" +
            "L,4,3,3,3\n" +
            "B,4,3,3,4\n" +
            "R,4,3,3,5\n" +
            "L,4,3,4,1\n" +
            "L,4,3,4,2\n" +
            "B,4,3,4,3\n" +
            "R,4,3,4,4\n" +
            "R,4,3,4,5\n" +
            "L,4,3,5,1\n" +
            "L,4,3,5,2\n" +
            "R,4,3,5,3\n" +
            "R,4,3,5,4\n" +
            "R,4,3,5,5\n" +
            "L,4,4,1,1\n" +
            "L,4,4,1,2\n" +
            "L,4,4,1,3\n" +
            "L,4,4,1,4\n" +
            "L,4,4,1,5\n" +
            "L,4,4,2,1\n" +
            "L,4,4,2,2\n" +
            "L,4,4,2,3\n" +
            "L,4,4,2,4\n" +
            "L,4,4,2,5\n" +
            "L,4,4,3,1\n" +
            "L,4,4,3,2\n" +
            "L,4,4,3,3\n" +
            "L,4,4,3,4\n" +
            "L,4,4,3,5\n" +
            "L,4,4,4,1\n" +
            "L,4,4,4,2\n" +
            "L,4,4,4,3\n" +
            "B,4,4,4,4\n" +
            "R,4,4,4,5\n" +
            "L,4,4,5,1\n" +
            "L,4,4,5,2\n" +
            "L,4,4,5,3\n" +
            "R,4,4,5,4\n" +
            "R,4,4,5,5\n" +
            "L,4,5,1,1\n" +
            "L,4,5,1,2\n" +
            "L,4,5,1,3\n" +
            "L,4,5,1,4\n" +
            "L,4,5,1,5\n" +
            "L,4,5,2,1\n" +
            "L,4,5,2,2\n" +
            "L,4,5,2,3\n" +
            "L,4,5,2,4\n" +
            "L,4,5,2,5\n" +
            "L,4,5,3,1\n" +
            "L,4,5,3,2\n" +
            "L,4,5,3,3\n" +
            "L,4,5,3,4\n" +
            "L,4,5,3,5\n" +
            "L,4,5,4,1\n" +
            "L,4,5,4,2\n" +
            "L,4,5,4,3\n" +
            "L,4,5,4,4\n" +
            "B,4,5,4,5\n" +
            "L,4,5,5,1\n" +
            "L,4,5,5,2\n" +
            "L,4,5,5,3\n" +
            "B,4,5,5,4\n" +
            "R,4,5,5,5\n" +
            "L,5,1,1,1\n" +
            "L,5,1,1,2\n" +
            "L,5,1,1,3\n" +
            "L,5,1,1,4\n" +
            "B,5,1,1,5\n" +
            "L,5,1,2,1\n" +
            "L,5,1,2,2\n" +
            "R,5,1,2,3\n" +
            "R,5,1,2,4\n" +
            "R,5,1,2,5\n" +
            "L,5,1,3,1\n" +
            "R,5,1,3,2\n" +
            "R,5,1,3,3\n" +
            "R,5,1,3,4\n" +
            "R,5,1,3,5\n" +
            "L,5,1,4,1\n" +
            "R,5,1,4,2\n" +
            "R,5,1,4,3\n" +
            "R,5,1,4,4\n" +
            "R,5,1,4,5\n" +
            "B,5,1,5,1\n" +
            "R,5,1,5,2\n" +
            "R,5,1,5,3\n" +
            "R,5,1,5,4\n" +
            "R,5,1,5,5\n" +
            "L,5,2,1,1\n" +
            "L,5,2,1,2\n" +
            "L,5,2,1,3\n" +
            "L,5,2,1,4\n" +
            "L,5,2,1,5\n" +
            "L,5,2,2,1\n" +
            "L,5,2,2,2\n" +
            "L,5,2,2,3\n" +
            "L,5,2,2,4\n" +
            "B,5,2,2,5\n" +
            "L,5,2,3,1\n" +
            "L,5,2,3,2\n" +
            "L,5,2,3,3\n" +
            "R,5,2,3,4\n" +
            "R,5,2,3,5\n" +
            "L,5,2,4,1\n" +
            "L,5,2,4,2\n" +
            "R,5,2,4,3\n" +
            "R,5,2,4,4\n" +
            "R,5,2,4,5\n" +
            "L,5,2,5,1\n" +
            "B,5,2,5,2\n" +
            "R,5,2,5,3\n" +
            "R,5,2,5,4\n" +
            "R,5,2,5,5\n" +
            "L,5,3,1,1\n" +
            "L,5,3,1,2\n" +
            "L,5,3,1,3\n" +
            "L,5,3,1,4\n" +
            "L,5,3,1,5\n" +
            "L,5,3,2,1\n" +
            "L,5,3,2,2\n" +
            "L,5,3,2,3\n" +
            "L,5,3,2,4\n" +
            "L,5,3,2,5\n" +
            "L,5,3,3,1\n" +
            "L,5,3,3,2\n" +
            "L,5,3,3,3\n" +
            "L,5,3,3,4\n" +
            "B,5,3,3,5\n" +
            "L,5,3,4,1\n" +
            "L,5,3,4,2\n" +
            "L,5,3,4,3\n" +
            "R,5,3,4,4\n" +
            "R,5,3,4,5\n" +
            "L,5,3,5,1\n" +
            "L,5,3,5,2\n" +
            "B,5,3,5,3\n" +
            "R,5,3,5,4\n" +
            "R,5,3,5,5\n" +
            "L,5,4,1,1\n" +
            "L,5,4,1,2\n" +
            "L,5,4,1,3\n" +
            "L,5,4,1,4\n" +
            "L,5,4,1,5\n" +
            "L,5,4,2,1\n" +
            "L,5,4,2,2\n" +
            "L,5,4,2,3\n" +
            "L,5,4,2,4\n" +
            "L,5,4,2,5\n" +
            "L,5,4,3,1\n" +
            "L,5,4,3,2\n" +
            "L,5,4,3,3\n" +
            "L,5,4,3,4\n" +
            "L,5,4,3,5\n" +
            "L,5,4,4,1\n" +
            "L,5,4,4,2\n" +
            "L,5,4,4,3\n" +
            "L,5,4,4,4\n" +
            "B,5,4,4,5\n" +
            "L,5,4,5,1\n" +
            "L,5,4,5,2\n" +
            "L,5,4,5,3\n" +
            "B,5,4,5,4\n" +
            "R,5,4,5,5\n" +
            "L,5,5,1,1\n" +
            "L,5,5,1,2\n" +
            "L,5,5,1,3\n" +
            "L,5,5,1,4\n" +
            "L,5,5,1,5\n" +
            "L,5,5,2,1\n" +
            "L,5,5,2,2\n" +
            "L,5,5,2,3\n" +
            "L,5,5,2,4\n" +
            "L,5,5,2,5\n" +
            "L,5,5,3,1\n" +
            "L,5,5,3,2\n" +
            "L,5,5,3,3\n" +
            "L,5,5,3,4\n" +
            "L,5,5,3,5\n" +
            "L,5,5,4,1\n" +
            "L,5,5,4,2\n" +
            "L,5,5,4,3\n" +
            "L,5,5,4,4\n" +
            "L,5,5,4,5\n" +
            "L,5,5,5,1\n" +
            "L,5,5,5,2\n" +
            "L,5,5,5,3\n" +
            "L,5,5,5,4\n" +
            "B,5,5,5,5\n";
}