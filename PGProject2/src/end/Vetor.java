package end;

public class Vetor {
	int size;
	double[] coordenadas; //coordenadas do vetor
	
	public Vetor(int size){
		this.size = size;
		coordenadas = new double[size];
	}
	
	int getSize(){
		return size;
	}
}
