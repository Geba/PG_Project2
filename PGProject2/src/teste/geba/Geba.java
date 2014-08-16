package teste.geba;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL4;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.FPSAnimator;

import end.Algb;
import end.Arquivo;

public class Geba implements GLEventListener {

	private double theta = 0;
	private double s = 0;
	private double c = 0;

	// objetos
	static int Np;
	static int Nt;
	static double[][] pontos;
	static int[][] triangulos;
	static double[][] NormPontos;
	static double[][] NormTriangulos;
	// atributos
	static int[] Ia = new int[3];
	static double Ka;
	static int Od[] = new int[3];
	static double Kd;
	static double Ks;
	static double n;
	static double[] Il = new double[3];
	static double[] Pl = new double[3];
	static Arquivo arq;
	// camera
	static double[] C = new double[3];
	static double[] N = new double[3];
	static double[] V = new double[3];
	static double[] U = new double[3];
	static double[] V1 = new double[3];
	static double d;
	static double hx; // coloquei double p n�o ter surpresa
	static double hy; // coloquei double p n�o ter surpresa

	// auxiliares
	static double[][] matrizMudBase = new double[V.length][V.length];
	static double[][] pontosTrans, pontos2d;

	static void lerObjetos() {
		Arquivo arq = new Arquivo("objeto.txt", "lixoObj.txt");
		Np = arq.readInt();
		Nt = arq.readInt();
		pontos = new double[Np][3];
		triangulos = new int[Nt][3];
		pontosTrans = new double[Np][3];
		pontos2d = new double[Np][2];
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

	static void printObjetos() {
		// System.out.println("Np: " + Np + " Nt: " + Np);
		for (int i = 0; i < Np; i++) {
			for (int j = 0; j < 3; j++) {
				// System.out.print(pontos[i][j] + " ");
			}
		}
		for (int i = 0; i < Nt; i++) {
			for (int j = 0; j < 3; j++) {
				// System.out.println(triangulos[i][j] + " ");
			}
		}
	}

	static void printAtributos() {
		// System.out.println("Ia: " + Ia[0] + " " + Ia[1] + " " + Ia[2]);
		// System.out.println("Ka: " + Ka);
		// System.out.println("Od: " + Od[0] + " " + Od[1] + " " + Od[2]);
		// System.out.println("Kd: " + Kd);
		// System.out.println("Ks: " + Ks);
		// System.out.println("n: " + n);
		// System.out.println("Il :" + Il[0] + " " + Il[1] + " " + Il[2]);
		// System.out.println("Pl :" + Pl[0] + " " + Pl[1] + " " + Pl[2]);
	}

	static void lerAtributos() {
		arq = new Arquivo("atributos.txt", "lixoAtr.txt");
		Ia[0] = arq.readInt();
		Ia[1] = arq.readInt();
		Ia[2] = arq.readInt();
		Ka = arq.readDouble();
		Od[0] = arq.readInt();
		Od[1] = arq.readInt();
		Od[2] = arq.readInt();
		Kd = arq.readInt();
		Ks = arq.readInt();
		n = arq.readInt();
		Il[0] = arq.readDouble();
		Il[1] = arq.readDouble();
		Il[2] = arq.readDouble();
		Pl[0] = arq.readDouble();
		Pl[1] = arq.readDouble();
		Pl[2] = arq.readDouble();

	}

	static void lerCamera() {
		arq = new Arquivo("camera.txt", "lixoCam.txt");
		C[0] = arq.readDouble();
		C[1] = arq.readDouble();
		C[2] = arq.readDouble();
		N[0] = arq.readDouble();
		N[1] = arq.readDouble();
		N[2] = arq.readDouble();
		V[0] = arq.readDouble();
		V[1] = arq.readDouble();
		V[2] = arq.readDouble();
		d = arq.readDouble();
		hx = arq.readDouble();
		hy = arq.readDouble();
	}

	static void printCamera() {
		// System.out.println("C: " + C[0] + " " + C[1] + " " + C[2]);
		// System.out.println("N: " + N[0] + " " + N[1] + " " + N[2]);
		// System.out.println("V: " + V[0] + " " + V[1] + " " + V[2]);
		// System.out.println("d: " + d + "\nhx: " + hx + "\nhy: " + hy);
	}

	public static double[][] projetar2d(double[][] pontos) {

		for (int i = 0; i < pontos.length; i++) {
			pontos2d[i][0] = (pontos[i][0] * d) / (pontos[i][2] * hx);
			pontos2d[i][1] = (pontos[i][1] * d) / (pontos[i][2] * hy);
			// pontos2d[i][0] = ((pontos2d[i][0]+1)/2)* (800-1);
			// pontos2d[i][1] = ((1-pontos2d[i][1])/2)* (600-1);
		}
		return pontos2d;
	}

	static void calcularNormais() {
		for (int i = 0; i <= triangulos.length; i++) {
			double[] v1, v2, n;
			int pA, pB, pC;
			pA = triangulos[i][0];
			pB = triangulos[i][1];
			pC = triangulos[i][2];

			// descobrir quais vetores pegar aqui
			v1 = Algb.sub(pontos[pB], pontos[pA]);// calcula os dois vetores
			v2 = Algb.sub(pontos[pC], pontos[pA]);// definidos pelos pontos do
													// triangulo
			n = Algb.prodVetorial(v1, v2);
			for (int j = 0; j <= 3; j++) {
				NormTriangulos[i] = n;// salva a normal no array d normais de
										// triangulo
				// soma essa normal no array de normal de vertices
				NormPontos[pA] = Algb.soma(NormPontos[pA], n);
				NormPontos[pB] = Algb.soma(NormPontos[pA], n);
				NormPontos[pC] = Algb.soma(NormPontos[pA], n);
			}

		}
		for (int i = 0; i < NormPontos.length; i++) {
			NormPontos[i] = Algb.normalize(NormPontos[i]);
		}

	}

	public void varredura(double[][] pontos2d) {
		double px1, px2;
		double varicao;

		px1 = pontos2d[0][0];
		px2 = pontos2d[1][0];

		for (double y = pontos2d[0][1]; y < pontos2d[1][1]; y++) {

		}

	}

	public static void main(String[] args) {

		lerCamera();
		printCamera();
		lerAtributos();
		printAtributos();
		lerObjetos();
		printObjetos();
		pontos2d = projetar2d(pontos);
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		GLCanvas canvas = new GLCanvas(caps);
		pontosTrans = Algb.mudancaDeCoordenada(pontos, matrizMudBase, C);
		pontos2d = projetar2d(pontosTrans);

		Frame frame = new Frame("AWT Window Test");
		frame.setSize(300, 300);
		frame.add(canvas);
		frame.setVisible(true);

		// by default, an AWT Frame doesn't do anything when you click
		// the close button; this bit of code will terminate the program when
		// the window is asked to close
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		canvas.addGLEventListener(new Geba());
		FPSAnimator animator = new FPSAnimator(canvas, 60);
		animator.add(canvas);
		animator.start();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		update();
		render(drawable);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void init(GLAutoDrawable drawable) {
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
	}

	private void update() {
		theta += 0.01;
		s = Math.sin(theta);
		c = Math.cos(theta);

	}

	private void render(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		// draw a triangle filling the window
		// gl.glBegin(GL.GL_LINE_LOOP);
		// gl.glColor3f(1, 0, 0);
		// gl.glVertex2d(-c, -c);
		// gl.glColor3f(0, 1, 0);
		// gl.glVertex2d(0, c);
		// gl.glColor3f(0, 0, 1);
		// gl.glVertex2d(s, -s);
		// gl.glEnd();
		// gl.glBegin(GL.GL_LINE_STRIP);
		// gl.glColor3f(1, 0, 0);
		// gl.glVertex2d(c, -c);
		// gl.glColor3f(0, 0, 1);
		// gl.glVertex2d(0, -c);
		// gl.glColor3f(0, 1, 0);
		// gl.glVertex2d(-s, s);
		// gl.glEnd();
		for (int t = 0; t < Nt - 1; t++) {
			gl.glBegin(GL.GL_LINE_LOOP);
			for (int k = 0; k < 3; k++) {
				System.out.println(" " + pontosTrans[0][1] + " "
						+ pontosTrans[0][2] + " " + pontosTrans[0][0]);
				System.out.println(" " + pontos2d[0][1] + " "
						+ pontos2d[0][0]);
				double[] a = pontosTrans[triangulos[t][k] - 1];
				gl.glColor3f(0, 1, 0);
				gl.glVertex2d(a[0] / 10, a[1] / 10);
			}
			gl.glEnd();

		}

	}
}
