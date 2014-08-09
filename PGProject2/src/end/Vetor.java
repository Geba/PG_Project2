package end;

	
public class Vetor {
	public int size;
	public double[] coordenadas; //coordenadas do vetor
	
	public Vetor(int size){
		this.size = size;
		coordenadas = new double[size];
	}	
	
	public int getSize(){
		return size;
	}
	
	public static double getNorma(Vetor v) {
		double k = 0;
		for(int i = 0; i< v.size; i++){
		    k = k + v.coordenadas[i]* v.coordenadas[i];
    	}
    	k = Math.sqrt(k);
    	 
    	return k;
	}
	
	public static Vetor normaliza(Vetor v){
		double k = 0;
		
    	k = getNorma(v);
    	
    	for(int i = 0; i< v.size; i++){
    		v.coordenadas[i] = v.coordenadas[i]/k;
    	}
    	
    	return v;
    }
}
