package de.embl.cba.bdp2.open;

import de.embl.cba.bdp2.BigDataProcessor2;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import javax.swing.*;

import static de.embl.cba.bdp2.read.NamingScheme.LUXENDO_REGEXP;
import static de.embl.cba.bdp2.utils.Utils.COMMAND_BDP_PREFIX;

@Plugin(type = Command.class, menuPath = "Plugins>BigDataProcessor2 " + AbstractOpenCommand.COMMAND_OPEN_PATH + OpenLuxendoMuViCommand.COMMAND_FULL_NAME )
public class  OpenLuxendoMuViCommand< R extends RealType< R > & NativeType< R > > extends AbstractOpenCommand< R >
{
    public static final String COMMAND_NAME = "Open Luxendo MuVi...";
    public static final String COMMAND_FULL_NAME = COMMAND_BDP_PREFIX + COMMAND_NAME;
    public static final String LEFT = "Left";
    public static final String RIGHT = "Right";
    public static final String BOTH = "Both";

    @Parameter(label = "Stack index"  )
    protected int stackIndex = 0;

    @Parameter(label = "Open Camera", choices = {
            LEFT,
            RIGHT,
            BOTH
    })
    protected String camera = BOTH;

    public void run()
    {
        SwingUtilities.invokeLater( () ->  {

            String regExp = LUXENDO_REGEXP.replace( "STACK", "" + stackIndex );

            if ( camera.equals( LEFT ) ) regExp = regExp.replace( "/Cam_(.*)", "/Cam_Left" );
            if ( camera.equals( RIGHT ) ) regExp = regExp.replace( "/Cam_(.*)", "/Cam_Right" );


            outputImage =
                        BigDataProcessor2.openImage(
                                directory.toString(),
                                regExp,
                                regExp,
                                "Data" );

            handleOutputImage( true, false );

        });
    }
}