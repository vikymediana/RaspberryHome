package rbhbehaviour;

import com.hopding.jrpicam.RPiCamera;
import com.hopding.jrpicam.enums.AWB;
import com.hopding.jrpicam.enums.DRC;
import com.hopding.jrpicam.enums.Encoding;
import com.hopding.jrpicam.exceptions.FailedToRunRaspistillException;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ShootBehaviour extends SimpleBehaviour {

    private RPiCamera piCamera;

    @Override
    public void onStart() {
        // Attempt to create an instance of RPiCamera, will fail if raspistill is not properly installed
        try {
            String saveDir = "/home/pi/Desktop";
            piCamera = new RPiCamera(saveDir);
        } catch (FailedToRunRaspistillException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void action() {
        ACLMessage msg = getAgent().receive();
        if (msg != null) {
            if (msg.getPerformative() == ACLMessage.INFORM) {
                String content = msg.getContent();
                if ((content != null) && (content.indexOf("true") != -1)) {
                    piCamera.setAWB(AWB.AUTO)       // Change Automatic White Balance setting to automatic
                            .setDRC(DRC.OFF)            // Turn off Dynamic Range Compression
                            .setContrast(100)           // Set maximum contrast
                            .setSharpness(100)          // Set maximum sharpness
                            .setQuality(100)            // Set maximum quality
                            .setTimeout(1000)           // Wait 1 second to take the image
                            .turnOnPreview()            // Turn on image preview
                            .setEncoding(Encoding.PNG); // Change encoding of images to PNG
                    // Take a 650x650 still image, buffer it, and save it as "/home/pi/Desktop/A Cool Picture.png"
                    try {
                        BufferedImage buffImg = piCamera.takeBufferedStill(650, 650); // Take image and store in BufferedImage
                        File saveFile = new File("/home/pi/TFM/pruebecita.png"); // Create file to save image to
                        ImageIO.write(buffImg, "png", saveFile); // Save image to file
                        System.out.println("New PNG image saved to:\n\t" + saveFile.getAbsolutePath()); // Print out location of image
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            block();
        }

    }

    @Override
    public boolean done() {
        return false;
    }

}
