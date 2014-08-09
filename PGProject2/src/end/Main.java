package end;
//oi
public class Main {
	//objetos
	static int Np;//Numero de Pontos
	static int Nt;//Numero de triangulos
	static double[][] pontos;
	static double[][] triangulos;
	//atributos
	static int[] Ia = new int [3];
	static double Ka;
	static int Od[] = new int [3];
    static double Kd;
	static double Ks;
    static double  n;
    static double[] Il = new double[3];
    static double[] Pl = new double [3];
    static Arquivo arq;
    //camera
    static Vetor C = new Vetor(3);
	static Vetor N = new Vetor(3);
	static Vetor V = new Vetor(3);
	static Vetor U = new Vetor(3);
	static Vetor V1 = new Vetor(3);
	static double d;
    static double hx; //coloquei double p n�o ter surpresa
	static double hy; //coloquei double p n�o ter surpresa
    
    //auxiliares
	static Vetor pmenosc = new Vetor(3); //[P - C] que vai multiplicar pela matriz I (Parte 3)
    
	static void lerObjetos(){
		Arquivo arq = new Arquivo("objeto.txt", "lixoObj.txt");
		Np = arq.readInt();
		Nt = arq.readInt();
		
		pontos = new double[Np][3];
		triangulos = new double[Nt][3]; 
		
		for (int i = 0; i < Np; i++) {
			for (int j = 0; j < 3; j++) {
				pontos[i][j] = arq.readDouble();
			}
		}
		
		for (int i = 0; i < Nt; i++) {
			for (int j = 0; j < 3; j++) {
				triangulos[i][j] = arq.readDouble();
			}
		}
	}
	
	static void printObjetos(){
		System.out.println("Np: " + Np + " Nt: " + Np);
		
		for (int i = 0; i < Np; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(pontos[i][j] + " ");
			}
		}
		
		for (int i = 0; i < Nt; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.println(triangulos[i][j] + " ");
			}
		}
	}
	
    static void printAtributos() {
    	System.out.println("Ia: "+Ia[0]+" "+Ia[1]+" "+Ia[2]);
    	System.out.println("Ka: "+Ka);
    	System.out.println("Od: "+Od[0]+" "+Od[1]+" "+Od[2]);
    	System.out.println("Kd: "+Kd);
    	System.out.println("Ks: "+Ks);
    	System.out.println("n: "+n);
    	System.out.println("Il :"+Il[0]+" "+Il[1]+" "+Il[2]);
    	System.out.println("Pl :"+Pl[0]+" "+Pl[1]+" "+Pl[2]);    	
	}
    
    static void lerAtributos(){
    	arq  = new Arquivo("atributo.txt","lixoAtr.txt");
    	Ia[0] = arq.readInt();Ia[1] = arq.readInt();Ia[2] = arq.readInt();
    	Ka = arq.readDouble();
    	Od[0] = arq.readInt();Od[1] = arq.readInt();Od[2] = arq.readInt();
    	Kd = arq.readInt();
    	Ks= arq.readInt();
    	n = arq.readInt();
    	Il[0] = arq.readDouble();Il[1] = arq.readDouble();Il[2] = arq.readDouble();
    	Pl[0] = arq.readDouble();Pl[1] = arq.readDouble();Pl[2] = arq.readDouble();
    	
    }
    
    static void lerCamera(){
    	arq = new Arquivo("camera.txt","lixoCam.txt");
    	C.coor[0] = arq.readDouble(); C.coor[1] = arq.readDouble(); C.coor[2] = arq.readDouble();
    	N.coor[0] = arq.readDouble(); N.coor[1] = arq.readDouble();N.coor[2] = arq.readDouble();
    	V.coor[0] = arq.readDouble(); V.coor[1] = arq.readDouble(); V.coor[2] = arq.readDouble();
    	d = arq.readDouble();
    	hx = arq.readDouble();hy = arq.readDouble();
    }
    
    
    static void printCamera(){
    	System.out.println("C: " + C.coor[0] + " " +C.coor[1] + " " + C.coor[2]); 
		System.out.println("N: " + N.coor[0] + " " +N.coor[1] + " " + N.coor[2]);
		System.out.println("V: " + V.coor[0] + " " + V.coor[1] + " " + V.coor[2]);
		System.out.println("d: " + d + "\nhx: " + hx + "\nhy: " + hy);
    }
     
	
    
    public static void main(String[] args) {
    	lerCamera();
    	printCamera();
    	System.out.println();
 	
    	
    	//Parte 3 - Mudan�a de coor - Mundiais -> C�mera 
    	//� preciso fazer a inversa dessa matriz para que ela possa ser  
    	//utilizada para a mudan�a de coor como requer a terceira parte.
    	V1 = Algb.sub(V, Algb.projec(V, N));
    	
    	U = Algb.prodVetorial(N, V);
    	
    	Vetor.normaliza(U);
    	Vetor.normaliza(V1);
    	Vetor.normaliza(N);
    	
    	Vetor P = new Vetor(3);
    	
    	for (int k = 0; k < 3; k++) {
    		pmenosc.coor[k] = P.coor[k] - C.coor[k]; 
    	}
    	
    	double [][] I = new double[3][3];
		for (int i = 0; i < V.size; i++) {
			I[i][0] = U.coor[i];
			I[i][1] = V.coor[i];
			I[i][2] = N.coor[i];    	
		}
		Vetor aus= Algb.sub(P, C);
		Algb.multMatrizVetor(I, aus);
		
    	
    	//PARA QUANDO ESTIVERMOS LENDO O ARQUIVO
    	/*
    	lerAtributos();
		printAtributos();
    	lerObjetos();
    	printObjetos();
		
		double [][] matrizRotInv = new double[V.size][V.size];
		for (int i = 0; i < V.size; i++) {
			matrizRotInv[i][0] = U.coor[i];
			matrizRotInv[i][1] = V.coor[i];
			matrizRotInv[i][2] = N.coor[i];    	
		}
			
			*/
    	
    	//Vetor A = new Vetor(1, 2, 3);
    	//Vetor B = new Vetor(4, 2, 1);
    	//
    	//Vetor C = Algb.prodEscalar(A, B);
    	//C.coor[0] + " " + C.coor[1] + " " + C.coor[2]
    	//System.out.println(Algb.prodEscalar(A, B));
    	
    	
  
    }
}
