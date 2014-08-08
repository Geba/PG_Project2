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
}
