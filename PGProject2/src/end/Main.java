package end;

public class Main {
	//atributos
	static int[] Ia = new int [3];
	static double Ka;
	static int Od[] = new int [3];
    static double Kd;
	static double Ks;
    static double  h;
    static double[] Il = new double[3];
    static double[] Pl = new double [3];
    static Arquivo arquivo;
    //camera
    static Vetor C;
	static Vetor N;
	static Vetor V;
	static Vetor U;
	static Vetor V1;
	static double d;
    static int hx;
	static int hy;
    
    //camera
    
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
    	arquivo  = new Arquivo("atributo.txt","lixo.txt");
    	Ia[0] = arquivo.readInt();Ia[1] = arquivo.readInt();Ia[2] = arquivo.readInt();
    	Od[0] = arquivo.readInt();Od[1] = arquivo.readInt();Od[2] = arquivo.readInt();
    	Kd = arquivo.readInt();Ks= arquivo.readInt();h = arquivo.readInt();
    	Il[0] = arquivo.readDouble();Il[1] = arquivo.readDouble();Il[2] = arquivo.readDouble();
    	Pl[0] = arquivo.readDouble();Pl[1] = arquivo.readDouble();Pl[2] = arquivo.readDouble();
    	
    }
    
    static void lerCamera(){
    	arquivo = new Arquivo("camera.txt","lixo.txt");
    	C.coor[0] = arquivo.readInt(); C.coor[1] = arquivo.readInt();C.coor[2] = arquivo.readInt();
    	N.coor[0] = arquivo.readInt(); N.coor[1] = arquivo.readInt();N.coor[1] = arquivo.readInt();
    	V.coor[0] = arquivo.readInt(); V.coor[1] = arquivo.readInt(); V.coor[2] = arquivo.readInt();
    	d = arquivo.readDouble();
    	hx = arquivo.readInt();hy = arquivo.readInt();
    }
    
    
    static void printCamera(){
    	
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
