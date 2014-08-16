package end;

public class Varredura {
	public Varredura(){
		
	}
	
	public void Varrer(double[][] ponto){
		//yMedia: Valor do y que tem o valor intermediário dos três pontos
	
		double inclinacaoP1P2, inclinacaoP1P3, inclinacaoP2P3;
		inclinacaoP1P2 = (ponto[0][1]-ponto[1][1])/ponto[0][0]-ponto[1][0];
		inclinacaoP1P3 = (ponto[1][1]-ponto[2][1])/ponto[1][0]-ponto[2][0];
		inclinacaoP2P3 = (ponto[2][1]-ponto[2][1])/ponto[2][0]-ponto[3][0];
		//dividir o triangulo em dois triangulos
		double posicaoInicio = ponto[0][0],posicaoAtualY=ponto[0][1],posicaoFim;
		posicaoInicio = ponto[0][0];
		posicaoFim = ponto[0][0];
		
		
		
		for(double i=ponto[0][1]; i==ponto[1][1];i++){
			posicaoInicio=posicaoInicio+inclinacaoP1P2;
			posicaoFim=posicaoFim+inclinacaoP1P3;
			for(double j = posicaoInicio; j==posicaoFim; j++){
								
			}
		}
		for(double i=ponto[1][1]; i<ponto[2][1];i++){
			
		}
		//acha o ponto que intersecta as retas com maior e menor y
		
	}
}
