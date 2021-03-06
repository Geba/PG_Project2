package end;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

public class Main {
	//objetos
	static int Np;
	static int Nt;
	static double[][] pontos;
	static int[][] triangulos;
	static double[][] NormaPontos;
	static double[][] NormaTriangulos;
	//atributos
	static int[] Ia = new int [3];
	static double Ka;
	static int Od[] = new int [3];
	static double Kd;
	static double Ks;
	static double  n;
	static double[] Il = new double[3];
	static double[] Pl = new double [3];
	static Arquivo arq;
	//camera
	static double[] C = new double[3];
	static double[] N = new double[3];
	static double[] V = new double[3];
	static double[] U = new double[3];
	static double[] V1 = new double[3];
	static double d;
	static double hx; //coloquei double p n�o ter surpresa
	static double hy; //coloquei double p n�o ter surpresa

	//auxiliares
	static double [][] matrizMudBase = new double[V.length][V.length];
	static double[][] pontosTrans = new double[Np][3];
	static double[][] pontos2d = new double[Np][2];

	static void lerObjetos(){
		Arquivo arq = new Arquivo("objeto.txt", "lixoObj.txt");
		Np = arq.readInt();
		Nt = arq.readInt();
		pontos = new double[Np][3];
		triangulos = new int[Nt][3]; 
		for (int i = 0; i < Np; i++) {
			for (int j = 0; j < 3; j++) {
				pontos[i][j] = arq.readDouble();
			}
		}
		for (int i = 0; i < Nt; i++) {
			for (int j = 0; j < 3; j++) {
				triangulos[i][j] = arq.readInt();
			}
		}
	}
	static void printObjetos(){
		System.out.println("Np: " + Np + " Nt: " + Np);
		for (int i = 0; i < Np; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(pontos[i][j] + " ");
			}
		}
		for (int i = 0; i < Nt; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.println(triangulos[i][j] + " ");
			}
		}
	}

	static void printAtributos() {
		System.out.println("Ia: "+Ia[0]+" "+Ia[1]+" "+Ia[2]);
		System.out.println("Ka: "+Ka);
		System.out.println("Od: "+Od[0]+" "+Od[1]+" "+Od[2]);
		System.out.println("Kd: "+Kd);
		System.out.println("Ks: "+Ks);
		System.out.println("n: "+n);
		System.out.println("Il :"+Il[0]+" "+Il[1]+" "+Il[2]);
		System.out.println("Pl :"+Pl[0]+" "+Pl[1]+" "+Pl[2]);    	
	}

	static void lerAtributos(){
		arq  = new Arquivo("atributos.txt","lixoAtr.txt");
		Ia[0] = arq.readInt();Ia[1] = arq.readInt();Ia[2] = arq.readInt();
		Ka = arq.readDouble();
		Od[0] = arq.readInt();Od[1] = arq.readInt();Od[2] = arq.readInt();
		Kd = arq.readInt();
		Ks= arq.readInt();
		n = arq.readInt();
		Il[0] = arq.readDouble();Il[1] = arq.readDouble();Il[2] = arq.readDouble();
		Pl[0] = arq.readDouble();Pl[1] = arq.readDouble();Pl[2] = arq.readDouble();
	}

	static void lerCamera(){
		arq = new Arquivo("camera.txt","lixoCam.txt");
		C[0] = arq.readDouble(); C[1] = arq.readDouble(); C[2] = arq.readDouble();
		N[0] = arq.readDouble(); N[1] = arq.readDouble();N[2] = arq.readDouble();
		V[0] = arq.readDouble(); V[1] = arq.readDouble(); V[2] = arq.readDouble();
		d = arq.readDouble();
		hx = arq.readDouble();hy = arq.readDouble();
	}


	static void printCamera(){
		System.out.println("C: " + C[0] + " " +C[1] + " " + C[2]); 
		System.out.println("N: " + N[0] + " " +N[1] + " " + N[2]);
		System.out.println("V: " + V[0] + " " + V[1] + " " + V[2]);
		System.out.println("d: " + d + "\nhx: " + hx + "\nhy: " + hy);
	}

	public static double[][] projetar2d(double[][] pontos){
		for (int i = 0; i < pontos.length; i++) {
			pontos2d[i][0] = (pontos[i][0]*d)/(pontos[i][2]*hx);
			pontos2d[i][1] = (pontos[i][1]*d)/(pontos[i][2]*hy);
		}
		return pontos2d;
	}

	static void calcularNormais() {
		double[] v1, v2, n;
		int pA, pB, pC;
		double[] zero = {0, 0, 0};
		for (int i = 0; i <= triangulos.length; i++) {
			pA = triangulos[i][0];
			pB = triangulos[i][1];
			pC = triangulos[i][2];

			// descobrir quais vetores pegar aqui
			v1 = Algbantigo.sub(pontos[pB], pontos[pA]);// calcula os dois vetores
			v2 = Algbantigo.sub(pontos[pC], pontos[pA]);// definidos pelos pontos do
			// triangulo
			n = Algbantigo.prodVetorial(v1, v2);
			if(Algbantigo.prodEscalar(C, n) > 0){
				n = Algbantigo.sub(zero, n);
			}
			
			for (int j = 0; j <= 3; j++) {
				NormaTriangulos[i] = n;//salva a normal no array d normais de triangulo
				//soma essa normal no array de normal de vertices
				NormaPontos[pA] = Algbantigo.soma(NormaPontos[pA], n);
				NormaPontos[pB] = Algbantigo.soma(NormaPontos[pA], n);
				NormaPontos[pC] = Algbantigo.soma(NormaPontos[pA], n);
			}

		}
		for(int i=0;i<NormaPontos.length;i++){
			NormaPontos[i] = Algbantigo.normalize(NormaPontos[i]);
		}

	}


	public void varredura(double[][] pontos2d){
		double px1,px2;
		double varicao;

		px1 = pontos2d[0][0];
		px2 = pontos2d[1][0];

		for(double y=pontos2d[0][1]; y<pontos2d[1][1]; y++){

		}

	}

	public void printaew (){
		int a, b, c;
		for (int i = 0; i < triangulos.length; i++) {
				a = triangulos[i][0]; 
				b = triangulos[i][1];
				c = triangulos[i][2];
			teste.render(pontos, a, b, c);
		}
	}
	
	public static void main(String[] args) {
		lerCamera();
		printCamera();
		lerAtributos();
		printAtributos();
		lerObjetos();
		printObjetos();


		//Parte 3 - Mudanca de coor - Mundiais -> Camera 
		//eh preciso fazer a inversa dessa matriz para que ela possa ser  
		//utilizada para a mudanca de coordenada como requer a terceira parte.
		//porém como as matrizes recebidas serao sempre ortornomais basta apenas 
		//transpor ele por sendo ortornomal M^-1 = M^t

		//descobrindo U que é o produto vetorial de N e V
		V = Algbantigo.sub(V, Algbantigo.projec(V, N));
		System.out.println("V = V-Proj(V,N): "+Algbantigo.VectorToString(V));
		U = Algbantigo.prodVetorial(N,  V);
		System.out.println("NxV: "+ Algbantigo.VectorToString(U));

		//normalizando
		U =Algbantigo.normalize(U);
		V = Algbantigo.normalize(V);
		N = Algbantigo.normalize(N);

		System.out.println("V: "+Algbantigo.VectorToString(V));
		System.out.println("U: "+Algbantigo.VectorToString(U));
		System.out.println("N: "+Algbantigo.VectorToString(N));

		//Matriz de mudança de coordenada de camera

		for (int i = 0; i < V.length; i++) {
			matrizMudBase[0][i] = U[i];
			matrizMudBase[1][i] = V[i];
			matrizMudBase[2][i] = N[i];
		}

		//Mudança de base de todos os pontos e da posicao da fonte de luz Pl
		pontosTrans = Algbantigo.mudancaDeCoordenada(pontos, matrizMudBase, C);
		Pl = Algbantigo.multMatrizVetor(matrizMudBase, Pl);

		//projetando os pontos na tela
		 GLProfile glprofile = GLProfile.getDefault();
         GLCapabilities glcapabilities = new GLCapabilities( glprofile );
         final GLCanvas glcanvas = new GLCanvas( glcapabilities );

         glcanvas.addGLEventListener( new GLEventListener() {
            
                
                 //essas são as funções que vc tem que dar override
                 //é tipo aquelas com c++ que a gente usa que chama outras funções
             @Override
             public void reshape( GLAutoDrawable glautodrawable, int x, int y, int width, int height ) {
                 Triangles.setup( glautodrawable.getGL().getGL2(), width, height );
             }
            
             @Override
             public void init( GLAutoDrawable glautodrawable ) {
             }
            
             @Override
             public void dispose( GLAutoDrawable glautodrawable ) {
             }
            
             @Override
             public void display( GLAutoDrawable glautodrawable ) {
                 Triangles.render( glautodrawable.getGL().getGL2(), ((Component) glautodrawable),((Component) glautodrawable), ((Component) glautodrawable));
             }
         });
         //Cria uma janelinha
         final JFrame frame = new JFrame( "Desenhar triângulo" );
         frame.add( glcanvas );
         //Quando fechar a janelinha destroi o desenho pra não ficar na memoria
         frame.addWindowListener( new WindowAdapter() {
             public void windowClosing(WindowEvent windowevent ) {
                 frame.remove( glcanvas );
                 frame.dispose();
                 System.exit( 0 );
             }
         });

         frame.setSize( 640, 480 );
         frame.setVisible( true );
		
		
	}
}
class Triangles {
    protected static void setup( GL2 gl2, int width, int height ) {
        gl2.glMatrixMode( GL2.GL_PROJECTION );
        gl2.glLoadIdentity();
 
        // as coordenadas começam no canto inferior esquerdo,e largura = tam janela
        GLU glu = new GLU();
        glu.gluOrtho2D( 0.0f, width, 0.0f, height );
 
        gl2.glMatrixMode( GL2.GL_MODELVIEW );
        gl2.glLoadIdentity();
 
        gl2.glViewport( 0, 0, width, height );
    }
 
    protected static void render( GL2 gl2, double[][]vertices, int a, int b, int c) {
 //       gl2.glClear( GL.GL_COLOR_BUFFER_BIT );
 
        // desenha o triângulo
        gl2.glLoadIdentity();
        gl2.glBegin( GL.GL_TRIANGLES ); //pra linhas tem que usar lines
        gl2.glColor3f(.5f, .5f, .5f);
        gl2.glVertex3d(vertices[a][0],vertices[a][1], vertices[a][2]);
        gl2.glVertex3d(vertices[b][0],vertices[b][1], vertices[b][2]);
        gl2.glVertex3d(vertices[c][0],vertices[c][1], vertices[c][2]);
        gl2.glColor3f(.5f, .5f, .5f);
        gl2.glEnd();
    }

	public static void render(GL2 gl2, Component component,
			Component component2, Component component3) {
		// TODO Auto-generated method stub
	}
}