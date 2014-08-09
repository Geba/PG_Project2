package end;


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
	public static Vetor prodVetorial(Vetor a, Vetor b){
		Vetor resp = new Vetor(3);
		resp.coordenadas[0] = a.coordenadas[1]*b.coordenadas[2] - a.coordenadas[2]*b.coordenadas[1];
		resp.coordenadas[1] = a.coordenadas[2]*b.coordenadas[0] - a.coordenadas[0]*b.coordenadas[2];
		resp.coordenadas[2] = a.coordenadas[0]*b.coordenadas[1] - a.coordenadas[1]*b.coordenadas[0];
		return resp;
	}
	
	
}
