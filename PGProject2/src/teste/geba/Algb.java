package teste.geba;
//kokok
import end.Vetor;

public class Algb {

	
	public static Vetor sub(Vetor a, Vetor b){//return a - b;
		Vetor resp = new Vetor(a.getSize());
		for(int i =0;i<a.getSize();i++){
			resp.coordenadas[i] = a.coordenadas[i] - b.coordenadas[i];
		}
		return resp;
	}
	public static Vetor soma(Vetor a, Vetor b){//return a - b;
		Vetor resp = new Vetor(a.getSize());
		for(int i =0;i<a.getSize();i++){
			resp.coordenadas[i] = a.coordenadas[i] + b.coordenadas[i];
		}
		return resp;
	}
	
	
	static  double prodEscalar(Vetor a, Vetor b ){//produto escalar a.b
		double resp = 0;
		for(int i =0;i<a.getSize();i++){
			resp+= a.coordenadas[i]*b.coordenadas[i];
		}
		return resp;
	}
	
	
	
	
}
