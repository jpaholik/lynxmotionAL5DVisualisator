package lynxmotional5dvisualisator;

import java.util.Arrays;

public class LynxmotionAL5DVisualisator {

    public static void main(String[] args) throws Exception {
        RobotLocation loc = new RobotLocation("0, 280, 0");
        System.out.println(Arrays.toString(loc.coordinates));
        System.out.println(Arrays.toString(loc.angles));
        
        RobotVisualisatorWindow window = new RobotVisualisatorWindow(
                RobotLocation.RAMENO, RobotLocation.PREDLAKTIE, RobotLocation.ZAPASTIE,
                loc.angles[1], loc.angles[2], loc.angles[3]
        );
        
        
    }
    
}
