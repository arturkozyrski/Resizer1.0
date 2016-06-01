import com.jtattoo.plaf.aero.AeroLookAndFeel;

import javax.swing.*;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        GUI gui = new GUI();

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }


        try {
            Properties props = new Properties();

            props.put("logoString", "");
            AeroLookAndFeel.setCurrentTheme(props);
            UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        gui.GUI();
    }
}
