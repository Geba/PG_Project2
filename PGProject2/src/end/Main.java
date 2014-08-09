package end;

public class Main {
	//objetos
	static int Np;
	static int Nt;
	static double[][] pontos;
	static double[][] triangulos;
	//atributos
	static int[] Ia = new int [3];
	static double Ka;
	static int Od[] = new int [3];
    static double Kd;
	static double Ks;
    static double  h;
    static double[] Il = new double[3];
    static double[] Pl = new double [3];
    static Arquivo arq;
    //camera
    static Vetor C;
	static Vetor N;
	static Vetor V;
	static Vetor U;
	static Vetor V1;
	static double d;
    static double hx; //coloquei double p não ter surpresa
	static double hy; //coloquei double p não ter surpresa
    
    //camera
    
	static void LerObjeto(){
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
		System.out.println("Np: " + Np + "Nt: " + Np);
		
		for (int i = 0; i < Np; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.println(pontos[i][j]);
			}
		}
		
		for (int i = 0; i < Nt; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.println(triangulos[i][j]);
			}
		}
	}
	
    static void printAtributos() {
    	System.out.println("Ia: "+Ia[0]+" "+Ia[1]+" "+Ia[2]);
    	System.out.println("Ka: "+Ka);
    	System.out.println("Od: "+Od[0]+" "+Od[1]+" "+Od[2]);
    	System.out.println("Kd: "+Kd);
    	System.out.println("Ks: "+Ks);
    	System.out.println("h: "+h);
    	System.out.println("Il :"+Il[0]+" "+Il[1]+" "+Il[2]);
    	System.out.println("Pl :"+Pl[0]+" "+Pl[1]+" "+Pl[2]);    	
	}
    
    static void lerAtributos(){
    	arq  = new Arquivo("atributo.txt","lixoAtr.txt");
    	Ia[0] = arq.readInt();Ia[1] = arq.readInt();Ia[2] = arq.readInt();
    	Od[0] = arq.readInt();Od[1] = arq.readInt();Od[2] = arq.readInt();
    	Kd = arq.readInt();Ks= arq.readInt();h = arq.readInt();
    	Il[0] = arq.readDouble();Il[1] = arq.readDouble();Il[2] = arq.readDouble();
    	Pl[0] = arq.readDouble();Pl[1] = arq.readDouble();Pl[2] = arq.readDouble();
    	
    }
    
    static void lerCamera(){
    	arq = new Arquivo("camera.txt","lixoCam.txt");
    	C.coor[0] = arq.readInt(); C.coor[1] = arq.readInt();C.coor[2] = arq.readInt();
    	N.coor[0] = arq.readInt(); N.coor[1] = arq.readInt();N.coor[1] = arq.readInt();
    	V.coor[0] = arq.readInt(); V.coor[1] = arq.readInt(); V.coor[2] = arq.readInt();
    	d = arq.readDouble();
    	hx = arq.readInt();hy = arq.readInt();
    }
    
    
    static void printCamera(){
    	System.out.println("C: " + C.coor[0] + " " +C.coor[1] + " " + C.coor[2]); 
		System.out.println("N: " + N.coor[0] + " " +N.coor[1] + " " + N.coor[2]);
		System.out.println("V: " + V.coor[0] + " " + V.coor[1] + " " + V.coor[2]);
		System.out.println("d: " + d + "hx: " + hx + "hy: " + hy);
    }
     
    
    public static void main(String[] args) {
    	//PARA QUANDO ESTIVERMOS LENDO O ARQUIVO
    	/*
    	lerAtributos();
		printAtributos();
		lerCamera();
		printCamera();
		
		Vetor.normaliza(V);
		Vetor.normaliza(N);
		
		V1 = Algb.sub(V, Algb.projec(V, N));
    	
		U = Algb.prodVetorial(V, U);
		
		
		//Parte 3 - Mudança de coor - Mundiais -> Câmera 
		//é preciso fazer a inversa dessa matriz para que ela possa ser utilizada para a 
		//mudança de coor como requer a terceira parte.
		
		double [][] matrizRotInv = new double[V.size][V.size];
		for (int i = 0; i < V.size; i++) {
			matrizRotInv[i][0] = U.coor[i];
			matrizRotInv[i][1] = V.coor[i];
			matrizRotInv[i][2] = N.coor[i];    	
		}
			
			*/
    	
    	Vetor A = new Vetor(1, 2, 3);
    	Vetor B = new Vetor(4, 2, 1);
    	//
    	//Vetor C = Algb.prodEscalar(A, B);
    	//C.coor[0] + " " + C.coor[1] + " " + C.coor[2]
    	System.out.println(Algb.prodEscalar(A, B));
    	
    	
  
    }
}
