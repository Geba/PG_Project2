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
    	C.coordenadas[0] = arquivo.readInt(); C.coordenadas[1] = arquivo.readInt();C.coordenadas[2] = arquivo.readInt();
    	N.coordenadas[0] = arquivo.readInt(); N.coordenadas[1] = arquivo.readInt();N.coordenadas[1] = arquivo.readInt();
    	V.coordenadas[0] = arquivo.readInt(); V.coordenadas[1] = arquivo.readInt(); V.coordenadas[2] = arquivo.readInt();
    	d = arquivo.readDouble();
    	hx = arquivo.readInt();hy = arquivo.readInt();
    }
    
    
    static void printCamera(){
    	
    }
    
    Vetor projec(Vetor u, Vetor v){
		Vetor proj = new Vetor(u.size);
		double a=0,b=0,k;
		double size = u.getSize();
		//a = <v*u> |  b = <u*u>
		for(int i =0; i<size;i++){
		    a = a + u.coordenadas[i] * v.coordenadas[i];
		    b = b + v.coordenadas[i]* v.coordenadas[i];
		    proj.coordenadas[i] = v.coordenadas[i];
		}
		
		k = a/b;
		
		// proj.coordenadas = v | k*proj.coordenadas = k*v
		for (int i=0; i<size; i++){
			proj.coordenadas[i] = k*proj.coordenadas[i];
		}
		
    	return proj;    	
    }
    
  
    public static void main(String[] args) {
	
		lerAtributos();
		printAtributos();
		lerCamera();
		printCamera();
		
		Vetor.normaliza(V);
		Vetor.normaliza(N);
		
		U = Algeb.prodEscalar(V, U);
		
	}

}
