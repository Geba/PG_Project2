package teste.geba;

import end.Vetor;

public class Algb {

	
	public static Vetor sub(Vetor a, Vetor b){//return a - b;
		Vetor resp = new Vetor(a.getSize());
		for(int i =0;i<a.getSize();i++){
			resp.coor[i] = a.coor[i] - b.coor[i];
		}
		return resp;
	}
	public static Vetor soma(Vetor a, Vetor b){//return a - b;
		Vetor resp = new Vetor(a.getSize());
		for(int i =0;i<a.getSize();i++){
			resp.coor[i] = a.coor[i] + b.coor[i];
		}
		return resp;
	}
	
	
	static  double prodEscalar(Vetor a, Vetor b ){//produto escalar a.b
		double resp = 0;
		for(int i =0;i<a.getSize();i++){
			resp+= a.coor[i]*b.coor[i];
		}
		return resp;
	}
	
	
	
	
}
