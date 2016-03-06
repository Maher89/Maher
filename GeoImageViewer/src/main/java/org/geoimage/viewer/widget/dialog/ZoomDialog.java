/*
 * ZoomDialog.java
 *
 * Created on April 8, 2008, 1:08 PM
 */
package org.geoimage.viewer.widget.dialog;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import org.geoimage.viewer.core.SumoPlatform;
import org.geoimage.viewer.core.layers.visualization.ZoomWindowLayer;
import org.slf4j.LoggerFactory;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;

/**
 *
 * @author  thoorfr
 */
public class ZoomDialog extends javax.swing.JDialog {
	private static org.slf4j.Logger logger=LoggerFactory.getLogger(ZoomDialog.class);

    private ZoomWindowLayer layer;
    private Texture texture;

    /** Creates new form ZoomDialog */
    public ZoomDialog(java.awt.Frame parent, boolean modal, ZoomWindowLayer zlayer) {
        super(parent, modal);
        initComponents();
        setTitle("Zoom "+zlayer.getName());
        jCheckBox1.setSelected(zlayer.isAutomaticConstrast());
        gLCanvas1.addGLEventListener(new GLEventListener() {
        	
        	@Override
            public void init(GLAutoDrawable arg0) {

            }
            
            @Override
            public void display(GLAutoDrawable arg0) {
                if (texture == null) {
                    return;
                }
                texture.enable(arg0.getGL());
                GL2 gl = gLCanvas1.getGL().getGL2();
                texture.bind(gl);
                TextureCoords coords = texture.getImageTexCoords();
                gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
                gl.glBegin(GL2.GL_QUADS);
                gl.glTexCoord2f(coords.left(), coords.bottom());
                gl.glVertex2f(0, 0);
                gl.glTexCoord2f(coords.right(), coords.bottom());
                gl.glVertex2f(1, 0);
                gl.glTexCoord2f(coords.right(), coords.top());
                gl.glVertex2f(1, 1);
                gl.glTexCoord2f(coords.left(), coords.top());
                gl.glVertex2f(0, 1);
                gl.glEnd();
                texture.disable(gl);
            }

            @Override
            public void reshape(GLAutoDrawable drawable, int arg1, int arg2, int arg3, int arg4) {
                GL2 gl = drawable.getGL().getGL2();
                gl.glMatrixMode(GL2.GL_PROJECTION);
                gl.glLoadIdentity();
                new GLU().gluOrtho2D(0, 1, 0, 1);
                gl.glMatrixMode(GL2.GL_MODELVIEW);
                gl.glLoadIdentity();
            }
            

			@Override
			public void dispose(GLAutoDrawable arg0) {

			}
        });
        Thread th = new Thread(new Runnable() {

            public void run() {
                while (true) {
                    try {
                        Thread.sleep(25);
                        gLCanvas1.repaint();
                    } catch (InterruptedException ex) {
                    	logger.error(ex.getMessage(),ex);
                    }
                }
            }
            });
        th.start();
        this.layer = zlayer;
        this.addWindowListener(new WindowListener() {

            public void windowOpened(WindowEvent e) {

            }

            public void windowClosing(WindowEvent e) {
                layer.setActive(false);
                SumoPlatform.getApplication().refresh();
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }
        });
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

    	//TODO:check how to repleace this deprecated method
        //gLCanvas1 = new GLCanvas(null, null, org.geoimage.viewer.core.SumoPlatform.getApplication().getGeoContext().getGLContext(), null);
        gLCanvas1 = new GLCanvas();//null, null, org.geoimage.viewer.core.SumoPlatform.getApplication().getMainCanvas().getContext(), null);
        jCheckBox1 = new javax.swing.JCheckBox();

        setTitle("Zoom");
        setAlwaysOnTop(true);
        setBackground(java.awt.Color.white);
        setBounds(new java.awt.Rectangle(0, 0, 256, 256));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        getContentPane().add(gLCanvas1, java.awt.BorderLayout.CENTER);

        jCheckBox1.setText("Automatic Contrast");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        getContentPane().add(jCheckBox1, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        layer.setAutomaticConstrast(jCheckBox1.isSelected());
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.media.opengl.awt.GLCanvas gLCanvas1;
    private javax.swing.JCheckBox jCheckBox1;
    // End of variables declaration//GEN-END:variables
}
