package lynxmotional5dvisualisator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import static java.lang.Math.sin;
import javax.swing.JFrame;

public class RobotVisualisatorWindow extends JFrame implements Runnable {
    
    enum ArmPart {RAMENO, PREDLAKTIE, ZAPASTIE, NONE};
    
    private static final char RAMENO_SELECTOR = 'r';
    private static final char PREDLAKTIE_SELECTOR = 'p';
    private static final char ZAPASTIE_SELECTOR = 'z';
    private static final char NONE_SELECTOR = 'n';
    
    double armAngle;
    double forearmAngle;
    double wristAngle;
    double armLength;
    double forearmLength;
    double wristLength;
    
    /** current key detected */
    private char key;
    private int keyCode;
    
    /** current robot arm position */
    double[] position;
    
    /** shall the window be closed? */
    private boolean terminate;
    
    private ArmPart selectedPart = ArmPart.NONE;
    
    int startX = 500;
    int startY = 500;
    
    public RobotVisualisatorWindow(double armLength, double forearmLength, double wristLength) {
        this.armLength = armLength;
        this.forearmLength = forearmLength;
        this.wristLength = wristLength;
        armAngle = 0;
        forearmAngle = 0;
        wristAngle = 0;
        
        setTitle("Direct Robot Control");     
        position = new double[5];
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                key = evt.getKeyChar(); 
                keyCode = evt.getKeyCode();
                //System.out.println("listener: " + key);
                if (key == 'Q') terminate = true;
            }
        });
        setVisible(true);
        setFocusable(true);
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        key = ' ';
        new Thread(this).start();
    }
    
    public RobotVisualisatorWindow(double armLength, double forearmLength, double wristLength, 
            double armAngle, double forearmAngle, double wristAngle) {
        this.armLength = armLength;
        this.forearmLength = forearmLength;
        this.wristLength = wristLength;
        this.armAngle = armAngle;
        this.forearmAngle = forearmAngle;
        this.wristAngle = wristAngle;
        
        setTitle("Virtual Robot Control");     
        position = new double[5];
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                key = evt.getKeyChar(); 
                keyCode = evt.getKeyCode();
                //System.out.println("listener: " + key);
                if (key == 'Q') terminate = true;
            }
        });
        setVisible(true);
        setFocusable(true);
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        key = ' ';
        new Thread(this).start();
    }
    
    @Override 
    public void run() {
        while(!terminate)
        {
            if (key != ' ') 
            {
                respondToKey();
                key = ' ';
                try { Thread.sleep(250); } catch (Exception e) {}
            }
            
            try { Thread.sleep(250); } catch (Exception e) {}
        }        
        dispose(); 
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        Color colorArm = 
                (selectedPart == ArmPart.RAMENO) ? Color.RED : Color.BLACK;
        Color colorForearm = 
                (selectedPart == ArmPart.PREDLAKTIE) ? Color.RED : Color.BLACK;
        Color colorWrist = 
                (selectedPart == ArmPart.ZAPASTIE) ? Color.RED : Color.BLACK;
        
        double paintArmAngle = armAngleToPaintAngle(armAngle);
        double paintForearmAngle = forearmAngleToPaintAngle(forearmAngle, paintArmAngle);
        double paintWristAngle = wristAngleToPaintAngle(wristAngle, paintForearmAngle);
        
        double[] forearmCoord = 
                drawLine(g, startX, startY, paintArmAngle, armLength, colorArm);
        double[] wristCoord = 
                drawLine(g, forearmCoord[0], forearmCoord[1], paintForearmAngle, forearmLength, colorForearm);
        double[] finalPoint = drawLine(g, wristCoord[0], wristCoord[1], paintWristAngle, wristLength, colorWrist);
        
        System.out.println(
                "Reached position: [ " + (startX - finalPoint[0]) + ", " + (startY - finalPoint[1]) + "]");
    }
    
    public void respondToKey() {
        switch(key) {
            case RAMENO_SELECTOR: 
                selectedPart = ArmPart.RAMENO; 
                break;
            case PREDLAKTIE_SELECTOR: 
                selectedPart = ArmPart.PREDLAKTIE; 
                break;
            case ZAPASTIE_SELECTOR: 
                selectedPart = ArmPart.ZAPASTIE; 
                break;
            case NONE_SELECTOR:
                selectedPart = ArmPart.NONE;
                break;
        }
        
        switch(keyCode) {
            case KeyEvent.VK_LEFT:
                moveSelectedPartLeft();
                break;
            case KeyEvent.VK_RIGHT:
                moveSelectedPartRight();
                break;
        }
        
        repaint();
    }
    
    private void moveSelectedPartLeft() {
        switch(selectedPart) {
            case RAMENO:
                armAngle--;
                break;
            case PREDLAKTIE:
                forearmAngle--;
                break;
            case ZAPASTIE:
                wristAngle--;
                break;
            default: break;
        }
    }
    
    private void moveSelectedPartRight() {
        switch(selectedPart) {
            case RAMENO:
                armAngle++;
                break;
            case PREDLAKTIE:
                forearmAngle++;
                break;
            case ZAPASTIE:
                wristAngle++;
                break;
            default: break;
        }
    }
    
    public double[] drawLine(Graphics g, double x1, double y1, double angle, double length) {
        g.setColor(Color.BLACK);
        
        double x2 = x1 + Math.cos(angle * (Math.PI / 180.0)) * length;
        double y2 = y1 + Math.sin(angle * (Math.PI / 180.0)) * length;
        g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
        
        double[] toReturn = {x2, y2};
        return toReturn;
    }
    
    public double[] drawLine(Graphics g, double x1, double y1, double angle, double length, Color color) {
        g.setColor(color);
        
        double x2 = x1 + Math.cos(angle * (Math.PI / 180.0)) * length;
        double y2 = y1 + Math.sin(angle * (Math.PI / 180.0)) * length;
        g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
        
        double[] toReturn = {x2, y2};
        return toReturn;
    }
    
    /** return angle of arm that can be used to be painted with drawLine method
     * @param angle arm angle from angles[] array
     */
    private double armAngleToPaintAngle(double angle) {
        return (270 + angle) % 360;
    }
    
    /** return angle of forearm that can be used to be painted with drawLine method
     * @param angle forearm angle from angles[] array
     * @param paintArmAngle angle returned by armAngleToPaintAngle method
     */
    private double forearmAngleToPaintAngle(double angle, double paintArmAngle) {
        return (((paintArmAngle + 180) % 360) + 90 + angle) % 360;
    }
    
    /** return angle of wrist that can be used to be painted with drawLine method
     * @param angle wrist angle from angles[] array
     * @param paintForearmAngle angle returned by forearmAngleToPaintAngle method
     */
    private double wristAngleToPaintAngle(double angle, double paintForearmAngle) {
        return (paintForearmAngle + angle) % 360;
    }
}
