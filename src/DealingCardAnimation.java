import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DealingCardAnimation extends JDialog{

    public DealingCardAnimation(JFrame frame, int dealingTime){

        super(frame, "Dealer Dealing Card", true);
        setSize(300, 200);
        setLocationRelativeTo(frame);

        // Create the panel to hold the image and message
        JPanel panel = new JPanel(new BorderLayout());

        ImageIcon originalIcon = new ImageIcon("dealingCard.jpg");
        // Resizing image
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);


        JLabel imageLabel = new JLabel(resizedIcon, JLabel.CENTER);
        panel.add(imageLabel, BorderLayout.CENTER);

        // Create the message label and add it to the panel
        JLabel messageLabel = new JLabel("Dealer dealing cards", JLabel.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(messageLabel, BorderLayout.SOUTH);

        // Add the panel to the dialog
        getContentPane().add(panel, BorderLayout.CENTER);

        // Create a timer to close the dialog after the specified time
        Timer timer = new Timer(dealingTime, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();

        setVisible(true);

    }

}
