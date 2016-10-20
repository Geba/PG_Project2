package end;

import java.awt.Component;
import java.awt.Frame;
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
 
 
//Clase que vai criar um triângulo
class Triangle {
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
 
public class teste {
        
            public static void main(String[] args ) {
                GLProfile glprofile = GLProfile.getDefault();
                GLCapabilities glcapabilities = new GLCapabilities( glprofile );
                final GLCanvas glcanvas = new GLCanvas( glcapabilities );
 
                glcanvas.addGLEventListener( new GLEventListener() {
                   
                       
                        //essas são as funções que vc tem que dar override
                        //é tipo aquelas com c++ que a gente usa que chama outras funções
                    @Override
                    public void reshape( GLAutoDrawable glautodrawable, int x, int y, int width, int height ) {
                        Triangle.setup( glautodrawable.getGL().getGL2(), width, height );
                    }
                   
                    @Override
                    public void init( GLAutoDrawable glautodrawable ) {
                    }
                   
                    @Override
                    public void dispose( GLAutoDrawable glautodrawable ) {
                    }
                   
                    @Override
                    public void display( GLAutoDrawable glautodrawable ) {
                        Triangle.render( glautodrawable.getGL().getGL2(), ((Component) glautodrawable),((Component) glautodrawable), ((Component) glautodrawable));
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

			public static void render(double[][] pontos, int a, int b, int c) {
				// TODO Auto-generated method stub
				
			}
}