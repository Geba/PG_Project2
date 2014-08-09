package end;

	
public class Vetor {
	public int size;
	public double[] coor; //coor do vetor
	
	public Vetor(int size){
		this.size = size;
		coor = new double[size];
	}	
	
	public Vetor(double i, double j, double k) {
		coor = new double[3];
		coor[0] =i;
		coor[1] =j;
		coor[2] =k;
	}

	public int getSize(){
		return size;
	}
	
	public static double getNorma(Vetor v) {
		double k = 0;
		for(int i = 0; i< v.size; i++){
		    k = k + v.coor[i]* v.coor[i];
    	}
    	k = Math.sqrt(k);
    	 
    	return k;
	}
	
	public static Vetor normaliza(Vetor v){
		double k = 0;
		
    	k = getNorma(v);
    	
    	for(int i = 0; i< v.size; i++){
    		v.coor[i] = v.coor[i]/k;
    	}
    	
    	return v;
    }
}
