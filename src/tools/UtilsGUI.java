package tools;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.BalloonTipStyle;
import net.java.balloontip.styles.RoundedBalloonStyle;

/**
 * Clase que contiene utilerias para intercaces graficas.
 * @author carlos
 */
public class UtilsGUI {    
    public static String LOOK = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";    
    public static String UB_LOOK = "Error al cargar el LookAndFeel.";    
    static BalloonTip myBalloonTipTxt;
       
    /**
     * Aplica el LookAndFeel a un JFrame.
     * @param frame Frame al cual aplicarle el aspecto grafico.
     */
    public static void setLookAndFeel(JFrame frame) {
        
        // Evaluar el OS utilizado para aplicar el aspecto grafico correcto.
        String OS = System.getProperty("os.name").toLowerCase();
        try {            
            if(OS.contains("nix") || OS.contains("nux")) {
                LOOK = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
            }
            UIManager.setLookAndFeel(LOOK);
            SwingUtilities.updateComponentTreeUI((Component)frame);
            frame.pack();
        } catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.out.println("Error " + UB_LOOK + "\n\n" + ex.getMessage()  + "\n\nUbicación: " + "setLookAndFeel");
        }
    }
    
    /**
     * Aplica el LookAndFeel a un JDialog.
     * @param dialog Ventana de dialogo a la cual aplicarle el aspecto grafico.
     */
    public static void setLookAndFeel (JDialog dialog) {
        
        // Evaluar el OS utilizado para aplicar el aspecto grafico correcto.
        String OS = System.getProperty("os.name").toLowerCase();
        try {            
            if(OS.contains("nix") || OS.contains("nux"))
            {
                LOOK = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
            }
            UIManager.setLookAndFeel(LOOK);
            SwingUtilities.updateComponentTreeUI((Component)dialog);
            dialog.pack();
        } catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.out.println("Error " + UB_LOOK + "\n\n" + ex.getMessage()  + "\n\nUbicación: " + "setLookAndFeel");
        }
    }
    
    /**
     * Muestra un globo de texto de aviso.
     * @param componente Componente sobre el cual mostrar el globo.
     * @param texto Texto a mostrar en el globo.
     * @param isBeep 
     */
    public static void showBallonAviso(JComponent componente, String texto, boolean isBeep) {
        hideBallonTxt();
        BalloonTipStyle style = new RoundedBalloonStyle(5,5,Color.YELLOW, Color.BLACK);
        myBalloonTipTxt = new BalloonTip(componente, texto, style, false);

        if(isBeep) {
            java.awt.Toolkit.getDefaultToolkit().beep();
        }            
    }
    
    /**
     * Esconde un globo de texto.
     */
    public static void hideBallonTxt() {
        if(myBalloonTipTxt != null) {
            if(myBalloonTipTxt.isVisible()) {
                myBalloonTipTxt.setVisible(false);
            }                
        }            
    }    
}