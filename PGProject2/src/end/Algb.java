package end;


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
	public static Vetor prodVetorial(Vetor a, Vetor b){
		Vetor resp = new Vetor(3);
		resp.coor[0] = a.coor[1]*b.coor[2] - a.coor[2]*b.coor[1];
		resp.coor[1] = a.coor[2]*b.coor[0] - a.coor[0]*b.coor[2];
		resp.coor[2] = a.coor[0]*b.coor[1] - a.coor[1]*b.coor[0];
		return resp;
	}
	
	static Vetor projec(Vetor u, Vetor v){
		Vetor proj = new Vetor(u.size);
		double a=0,b=0,k;
		double size = u.getSize();
		//a = <v*u> |  b = <u*u>
		for(int i =0; i<size;i++){
		    a = a + u.coor[i] * v.coor[i];
		    b = b + v.coor[i]* v.coor[i];
		    proj.coor[i] = v.coor[i];
		}
		
		k = a/b;
		
		// proj.coor = v | k*proj.coor = k*v
		for (int i=0; i<size; i++){
			proj.coor[i] = k*proj.coor[i];
		}
		
    	return proj;    	
    }
	
}
