package end;

	
public class Vetor {
	public int size;
	public double[] coor; //coor do vetor
	
	public Vetor(int size){
		this.size = size;
		coor = new double[size];
		for (int i = 0; i < size; i++) {
			coor[i] = 0;
		}
		
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
    		//if(v.coor[i]/k == -0){
    		//	v.coor[i] = (-1)*v.coor[i]/k;
    		//} else{
    		v.coor[i] = v.coor[i]/k;
    		//}
    	}
    	
    	return v;
    }
	
	public static String printVetor(int size, Vetor v){
		String out = "";
		for (int i = 0; i < size; i++) {
			if(i == size - 1){
				out = out +v.coor[i]+"";	
			} else {
				out = out+ v.coor[i] + " " ;
			}
		}
		return out;
	}
}
