package end2;

public class Varredura {
	public Varredura(){
		
	}
	
	public void Varrer(int[][] ponto){
		//yMedia: Valor do y que tem o valor intermediário dos três pontos
	
		
		//tratar casos de divisao por zero
		int inclinacaoP1P2, inclinacaoP1P3, inclinacaoP2P3;
		inclinacaoP1P2 = (ponto[0][1]-ponto[1][1])/ponto[0][0]-ponto[1][0];
		inclinacaoP1P3 = (ponto[0][1]-ponto[2][1])/ponto[0][0]-ponto[2][0];
		inclinacaoP2P3 = (ponto[1][1]-ponto[2][1])/ponto[1][0]-ponto[2][0];
		//dividir o triangulo em dois triangulos
		int posicaoInicio = ponto[0][0],posicaoAtualY=ponto[0][1],posicaoFim;
		posicaoInicio = ponto[0][0];
		posicaoFim = ponto[0][0];
		
				for(int i=ponto[0][1]; i<=ponto[1][1];i++){
			posicaoInicio=posicaoInicio+inclinacaoP1P2;
			posicaoFim=posicaoFim+inclinacaoP1P3;
			int incremento = 1;
			if(posicaoFim<posicaoInicio) incremento*=-1;
			for(int j = posicaoInicio; j<=posicaoFim; j+=incremento){
								
			}
		}
		for(int i=ponto[1][1]; i<ponto[2][1];i++){
			posicaoInicio=posicaoInicio+inclinacaoP2P3;
			posicaoFim=posicaoFim+inclinacaoP1P3;
			int incremento = 1;
			if(posicaoFim<posicaoInicio) incremento*=-1;
			for(int j = posicaoInicio; j<=posicaoFim; j+=incremento){
								
			}
		}
		//acha o ponto que intersecta as retas com maior e menor y
		
	}
}
